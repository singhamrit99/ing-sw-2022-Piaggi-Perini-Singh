package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.exceptions.BadFormattedLocalModelEvent;
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
    private String winnerTeam;
    private UI ui;
    private boolean canPlayMN;
    private State state;
    private String firstPlayer;
    final private ArrayList<AssistantCardDeck> assistantDecks;

    public ArrayList<AssistantCardDeck> getAssistantDecks() {
        return assistantDecks;
    }

    public StrippedCharacter selectedCharacter;

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

    public void updateModel(PropertyChangeEvent evt) throws LocalModelNotLoadedException, BadFormattedLocalModelEvent {
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
            case "game-over":
                winnerTeam = (String) evt.getNewValue();
                ui.gameOver(winnerTeam);
                break;
            case "change-phase":
                setState(evt);
                break;
            case "first-player-change":
                setFirstPlayer(evt);
                ui.currentPlayer((String) evt.getNewValue());
                break;
            default:
                throw new BadFormattedLocalModelEvent();
        }
    }

    private void setFirstPlayer(PropertyChangeEvent evt) {
        firstPlayer = (String) evt.getNewValue();
    }

    private void setState(PropertyChangeEvent evt) {
        state = (State) evt.getOldValue();
    }

    private void changeAssistantDeck(PropertyChangeEvent evt) throws LocalModelNotLoadedException {
        String ownerDeck = currentPlayer;
        StrippedBoard board = getBoardOf(ownerDeck);
        board.setDeck((AssistantCardDeck) evt.getNewValue());
        String playedCard = (String) evt.getOldValue();
        ui.deckChange(playedCard);
    }

    private void setBoard(PropertyChangeEvent evt) {
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
                    System.out.println("exception da fare setBoard"); //todo
                    break;
            }
        } else { //todo
            System.out.println("exception da fare setBoard");
        }
    }

    private void changePriceCharacterCard(PropertyChangeEvent evt) {
        StrippedCharacter changedCard = (StrippedCharacter) evt.getNewValue();
        StrippedCharacter cardToUpdate = null;
        for (StrippedCharacter card : characters) {
            if (card.sameCard(changedCard)) {
                cardToUpdate = card;
            }
        }

        if (cardToUpdate != null) {
            if (cardToUpdate.getPrice() == changedCard.getPrice()) {
                int newPriceCard = (int) evt.getNewValue();
                cardToUpdate.setPrice(newPriceCard); //update
            } else {
                System.out.println("throws an exception because the old price of character is not the same"); //todo
            }
        } else {
            System.out.println("throws an exception not found card to update"); //todo
        }
    }

    private void changeIsland(PropertyChangeEvent evt) {
        StrippedIsland changedIsland = (StrippedIsland) evt.getOldValue();
        Optional<StrippedIsland> optionalIslandFound = islands.stream().filter(x -> x.getName().equals(changedIsland.getName())).findFirst();
        if (optionalIslandFound.isPresent()) {
            StrippedIsland islandToChange = optionalIslandFound.get();
            if (evt.getNewValue()!= null) {
                if (evt.getPropertyName().equals("island") ||
                        evt.getPropertyName().equals("island-conquest")) {
                    StrippedIsland newProperties = (StrippedIsland) evt.getNewValue();
                    islandToChange.setNumberOfTowers(newProperties.getNumOfTowers());
                    islandToChange.setTowersColor(newProperties.getTowersColor());
                    islandToChange.setStudents(newProperties.getStudents());
                    islandToChange.setHasMotherNature(newProperties.hasMotherNature());
                    islandToChange.setHasNoEnterTile(newProperties.hasNoEnterTile());
                    if (evt.getPropertyName().equals("island")) ui.islandChange(evt);
                    else ui.islandConquest(evt);
            }
            else if (evt.getPropertyName().equals("island-merged"))
                 {
                    islandToChange.setDestroyed();
                    ui.islandMerged(evt);
                }
                else{
                    System.out.println("Exception changeIsland , strippedModel due"); //TODO
                }
            }
        } else {
            System.out.println("Exception changeIsland , strippedModel tre"); //TODO
        }
    }


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

    private void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<StrippedCharacter> getCharacters() {
        return characters;
    }

    public ArrayList<StrippedCloud> getClouds() {
        return clouds;
    }

    public ArrayList<StrippedIsland> getIslands() {
        return islands;
    }

    public ArrayList<StrippedBoard> getBoards() {
        return boards;
    }

    public StrippedBoard getBoardOf(String owner) throws LocalModelNotLoadedException {
        for (StrippedBoard b : boards) {
            if (b.getOwner().equals(owner)) return b;
        }
        throw new LocalModelNotLoadedException(); //todo change the name of this exception with something more specific
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public boolean isCanPlayMN() {
        return canPlayMN;
    }

    public void setCanPlayMN(boolean canPlayMN) {
        this.canPlayMN = canPlayMN;
    }

    public State getState() {
        return state;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public StrippedCloud getCloudByName(AtomicReference<String> selectedItem) {
        for (StrippedCloud cloud : getClouds()) {
            if (cloud.getName().equals(selectedItem.get())) {
                return cloud;
            }
        }
        return null;
    }
}
