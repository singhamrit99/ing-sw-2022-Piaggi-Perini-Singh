package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.exceptions.BadFormattedLocalModelException;
import it.polimi.ingsw.exceptions.LocalModelNotLoadedException;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.view.UI;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class StrippedModel implements Serializable {
    final private ArrayList<StrippedBoard> boards;
    final private ArrayList<StrippedCharacter> characters;
    final private ArrayList<StrippedCloud> clouds;
    final private ArrayList<StrippedIsland> islands;
    private String currentPlayer;
    private UI ui;
    private boolean canPlayMN;
    private State state;
    private String firstPlayer;
    final private ArrayList<AssistantCardDeck> assistantDecks;

    public ArrayList<AssistantCardDeck> getAssistantDecks() {
        return assistantDecks;
    }

    public StrippedCharacter selectedCharacter;

    /**
     * StrippedModel constructor, called from Room class on game startup. Build the whole StrippedModel.
     *
     * @param boards         Every player's board in ArrayList form.
     * @param characters     Every character in the game, in ArrayList form.
     * @param clouds         Every Cloud in the game, in ArrayList form.
     * @param islands        Every Island in the game, in ArrayList form.
     * @param assistantDecks Every Assistant Card Deck in the game, in ArrayList form.
     */
    public StrippedModel(ArrayList<StrippedBoard> boards, ArrayList<StrippedCharacter> characters,
                         ArrayList<StrippedCloud> clouds, ArrayList<StrippedIsland> islands, ArrayList<AssistantCardDeck> assistantDecks) {
        this.boards = boards;
        this.characters = characters;
        this.clouds = clouds;
        this.islands = islands;
        this.assistantDecks = assistantDecks;
        this.state = State.PLANNINGPHASE;
        firstPlayer = "";
        selectedCharacter = null;
    }

    /**
     * The method that updates the LocalModel following the arrival of an event.
     *
     * @param evt The event that is received. Its name field allows for handling.
     * @throws LocalModelNotLoadedException    Thrown if the LocalModel isn't loaded.
     * @throws BadFormattedLocalModelException Thrown if the local model has been built incorrectly.
     */
    public void updateModel(PropertyChangeEvent evt) throws LocalModelNotLoadedException, BadFormattedLocalModelException {
        switch (evt.getPropertyName()) {
            case "entrance":
            case "dining":
            case "coins":
            case "professorTable":
            case "towers":
                setBoard(evt);
                break;
            case "character":
                changePriceCharacterCard(evt);
                break;
            case "island":
            case "island-conquest":
            case "island-merged":
                changeIsland(evt);
                break;
            case "cloud":
                changeCloud(evt);
                break;
            case "assistant":
                changeAssistantDeck(evt);
                break;
            case "current-player":
                setCurrentPlayer((String) evt.getNewValue());
                ui.currentPlayer((String) evt.getNewValue());
                break;
            case "change-phase":
                setState(evt);
                break;
            case "first-player-change":
                setFirstPlayer(evt);
                break;
            default:
                throw new BadFormattedLocalModelException();
        }
    }

    /**
     * Setter method for the first player.
     *
     * @param evt First player event.
     */
    private void setFirstPlayer(PropertyChangeEvent evt) {
        firstPlayer = (String) evt.getNewValue();
    }

    /**
     * Setter method for changing the state.
     *
     * @param evt Change-state event.
     */
    private void setState(PropertyChangeEvent evt) {
        state = (State) evt.getOldValue();
    }

    /**
     * Method called when a change in the assistant card deck is notified.
     *
     * @param evt Change assistant deck event (play card)
     * @throws LocalModelNotLoadedException Thrown if the local model is not loaded.
     */
    private void changeAssistantDeck(PropertyChangeEvent evt) throws LocalModelNotLoadedException {
        String ownerDeck = currentPlayer;
        StrippedBoard board = getBoardOf(ownerDeck);
        board.setDeck((AssistantCardDeck) evt.getNewValue());
        String playedCard = (String) evt.getOldValue();
        ui.deckChange(playedCard);
    }

    /**
     * Method used to set the board after a related event.
     *
     * @param evt Any board event (change dining, change entrance, professors...)
     */
    private void setBoard(PropertyChangeEvent evt) throws BadFormattedLocalModelException {
        String ownerBoard = (String) evt.getOldValue();
        Optional<StrippedBoard> boardToModify = boards.stream().filter(b -> ownerBoard.equals(b.getOwner())).findFirst();
        if (boardToModify.isPresent()) {
            switch (evt.getPropertyName()) {
                case "entrance":
                    boardToModify.get().setEntrance((EnumMap<Colors, Integer>) evt.getNewValue());
                    ui.entranceChanged(evt);
                    break;
                case "dining":
                    boardToModify.get().setDining((EnumMap<Colors, Integer>) evt.getNewValue());
                    ui.diningChange(evt);
                    break;
                case "towers":
                    boardToModify.get().setNumberOfTowers((int) evt.getNewValue());
                    ui.towersEvent(evt);
                    break;
                case "coins":
                    boardToModify.get().setCoins((int) evt.getNewValue());
                    ui.coinsChanged(evt);
                    break;
                case "professorTable":
                    boardToModify.get().setProfessorsTable((ArrayList<Colors>) evt.getNewValue());
                    ui.professorChanged();
                    break;
                default:
                    throw new BadFormattedLocalModelException();
            }
        } else {
            throw new BadFormattedLocalModelException();
        }
    }

    /**
     * Changes the price of a character card after successful activation.
     *
     * @param evt the Change-price event.
     */
    private void changePriceCharacterCard(PropertyChangeEvent evt) throws BadFormattedLocalModelException {
        StrippedCharacter changedCard = (StrippedCharacter) evt.getNewValue();
        StrippedCharacter cardToUpdate = null;
        int i = 0;
        for (StrippedCharacter card : characters) {
            if (card.sameCard(changedCard)) {
                characters.get(i).setStudents(changedCard.getStudents());
                characters.get(i).setNoEntryTiles(changedCard.getNoEntryTiles());
                cardToUpdate = card;
            }
            i++;
        }

        if (changedCard != null && cardToUpdate != null) {
            if (cardToUpdate.getPrice() != changedCard.getPrice()) {
                cardToUpdate.setPrice(changedCard.getPrice()); //update
                ui.characterChanged(evt);
            }
        } else {
            throw new BadFormattedLocalModelException();
        }
    }

    /**
     * Handler of change-island event.
     *
     * @param evt change-island event(such as students or towers being placed or conquests happening)
     */
    private void changeIsland(PropertyChangeEvent evt) throws BadFormattedLocalModelException {
        StrippedIsland islandToFound = (StrippedIsland) evt.getOldValue();
        Optional<StrippedIsland> optionalIslandFound = islands.stream().filter(x -> x.getName().equals(islandToFound.getName())).findFirst();
        String eventName = evt.getPropertyName();
        if (optionalIslandFound.isPresent()) {
            StrippedIsland islandToChange = optionalIslandFound.get();
            if (evt.getNewValue() != null) {
                if (eventName.equals("island") || eventName.equals("island-conquest")) {
                    StrippedIsland newProperties = (StrippedIsland) evt.getNewValue();
                    islandToChange.setNumberOfTowers(newProperties.getNumOfTowers());
                    islandToChange.setTowersColor(newProperties.getTowersColor());
                    islandToChange.setStudents(newProperties.getStudents());
                    islandToChange.setHasMotherNature(newProperties.hasMotherNature());
                    islandToChange.setHasNoEnterTile(newProperties.hasNoEnterTile());
                    if (eventName.equals("island")) ui.islandChange(evt);
                    else ui.islandConquest(evt);
                }
            }else {
                if(eventName.equals("island-merged")){
                    islandToChange.setDestroyed();
                    ui.islandMerged(evt);
                }else{
                    throw new BadFormattedLocalModelException();
                }
            }
        } else {
            throw new BadFormattedLocalModelException();
        }
    }

    /**
     * Method used to handle changes in clouds.
     *
     * @param evt change-cloud event.
     */
    private void changeCloud(PropertyChangeEvent evt) {
        StrippedCloud changedCloud;
        if (evt.getOldValue() != null) {
            changedCloud = (StrippedCloud) evt.getOldValue();
        } else {
            changedCloud = (StrippedCloud) evt.getNewValue();
        }
        Optional<StrippedCloud> optionalCloudFound = clouds.stream().filter(x -> x.getName().equals(changedCloud.getName())).findFirst();
        if (optionalCloudFound.isPresent()) {
            StrippedCloud cloudFound = optionalCloudFound.get();
            int indexOfCloudsToReplace = clouds.indexOf(cloudFound);
            clouds.remove(cloudFound);
            clouds.add(indexOfCloudsToReplace, changedCloud);
        } else {
            clouds.add(changedCloud);
        }
        ui.notifyCloud(evt);
    }

    /**
     * Current player setter.
     *
     * @param currentPlayer the nickname of the current player.
     */
    private void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Getter for the Current player field.
     *
     * @return Current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Getter for the characters field.
     *
     * @return characters
     */
    public ArrayList<StrippedCharacter> getCharacters() {
        return characters;
    }

    /**
     * Getter for the clouds field.
     *
     * @return clouds
     */
    public ArrayList<StrippedCloud> getClouds() {
        return clouds;
    }

    /**
     * Getter for the islands field.
     *
     * @return islands
     */
    public ArrayList<StrippedIsland> getIslands() {
        return islands;
    }

    /**
     * Getter for the boards field.
     *
     * @return boards
     */
    public ArrayList<StrippedBoard> getBoards() {
        return boards;
    }

    /**
     * Returns a specific player's board. Utility method.
     *
     * @param owner The nickname of the desired board.
     * @return the board where the owner field is the same as "owner" parameter.
     * @throws LocalModelNotLoadedException Thrown when there's an issue with the localmodel.
     */
    public StrippedBoard getBoardOf(String owner) throws LocalModelNotLoadedException {
        for (StrippedBoard b : boards) {
            if (b.getOwner().equals(owner)) return b;
        }
        throw new LocalModelNotLoadedException();
    }

    /**
     * Binds this localModel to a UI(GUI or CLI)
     *
     * @param ui the UI of choice
     */
    public void setUi(UI ui) {
        this.ui = ui;
    }

    /**
     * Getter for the can play MN field.
     *
     * @return can play MN
     */
    public boolean isCanPlayMN() {
        return canPlayMN;
    }

    /**
     * Setter for the can play MN field.
     *
     * @param canPlayMN true or false depending on the state of the game.
     */
    public void setCanPlayMN(boolean canPlayMN) {
        this.canPlayMN = canPlayMN;
    }

    /**
     * Getter for the state field.
     *
     * @return state
     */
    public State getState() {
        return state;
    }

    /**
     * Getter for the firstplayer field.
     *
     * @return firstplayer
     */
    public String getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * Returns a cloud, selected by its name.
     *
     * @param selectedItem the Name the Cloud is selected by.
     * @return the StrippedCloud itself.
     */
    public StrippedCloud getCloudByName(AtomicReference<String> selectedItem) {
        for (StrippedCloud cloud : getClouds()) {
            if (cloud.getName().equals(selectedItem.get())) {
                return cloud;
            }
        }
        return null;
    }
}
