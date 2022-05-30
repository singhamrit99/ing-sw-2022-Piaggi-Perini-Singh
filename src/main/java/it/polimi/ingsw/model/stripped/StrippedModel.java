package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Optional;

public class StrippedModel implements Serializable {
    final private ArrayList<StrippedBoard> boards;
    final private ArrayList<StrippedCharacter> characters;
    final private ArrayList<StrippedCloud> clouds;
    final private ArrayList<StrippedIsland> islands;
    private String currentPlayer;
    private String winnerTeam;
    public ArrayList<AssistantCardDeck> getAssistantDecks() {
        return assistantDecks;
    }
    final private ArrayList<AssistantCardDeck> assistantDecks;

    public StrippedModel(ArrayList<StrippedBoard> boards, ArrayList<StrippedCharacter> characters,
                         ArrayList<StrippedCloud> clouds, ArrayList<StrippedIsland> islands, ArrayList<AssistantCardDeck> assistantDecks) {
        this.boards = boards;
        this.characters = characters;
        this.clouds = clouds;
        this.islands = islands;
        this.assistantDecks = assistantDecks;
    }

    public void updateModel(PropertyChangeEvent evt) {
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
            case "game-over":
                winnerTeam= (String)evt.getNewValue();
                break;
            default:
                System.out.println("scrivere una exception sensata"); //TODO
                break;
        }
    }

    private void changeAssistantDeck(PropertyChangeEvent evt){
        String ownerDeck = currentPlayer;
        Optional<AssistantCardDeck> deckToModify = assistantDecks.stream().filter(d ->  d.getOwner().equals(ownerDeck)).findFirst();
        if(deckToModify.isPresent()){
            assistantDecks.remove(deckToModify);
            assistantDecks.add((AssistantCardDeck) evt.getNewValue());
        }
    }

    private void setBoard(PropertyChangeEvent evt) {
        String ownerBoard = (String) evt.getOldValue();
        Optional<StrippedBoard> boardToModify = boards.stream().filter(b -> ownerBoard.equals(b.getOwner())).findFirst();
        if (boardToModify.isPresent()) {
            switch (evt.getPropertyName()) {
                case "entrance":
                    boardToModify.get().setEntrance((EnumMap<Colors, Integer>) evt.getNewValue());
                    break;
                case "dining":
                    boardToModify.get().setDining((EnumMap<Colors, Integer>) evt.getNewValue());
                    break;
                case "towers":
                    boardToModify.get().setNumberOfTowers((int)evt.getNewValue());
                    break;
                case "coins":
                    boardToModify.get().setCoins((int) evt.getNewValue());
                    break;
                case "professorTable":
                    boardToModify.get().setProfessorsTable((ArrayList<Colors>) evt.getNewValue());
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
        StrippedCharacter changedCard = (StrippedCharacter) evt.getOldValue();
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
        Optional<StrippedIsland> islandFound = islands.stream().filter(x -> x.getName().equals(changedIsland.getName())).findFirst();
        if (islandFound.isPresent()) {
            islands.remove(islandFound); //IslandEvent Deletion
            if (evt.getNewValue() != null) {
                islands.add((StrippedIsland) evt.getNewValue());
            }
        } else {
            System.out.println("Exception changeIsland , strippedModel"); //todo
        }
    }

    private void changeCloud(PropertyChangeEvent evt) {
        StrippedCloud changedCloud;
        if (evt.getOldValue()!=null) {
             changedCloud = (StrippedCloud) evt.getOldValue();
        }
        else
        {
             changedCloud = (StrippedCloud) evt.getNewValue();
        }
        Optional<StrippedCloud> cloudFound = clouds.stream().filter(x -> x.getName().equals(changedCloud.getName())).findFirst();
        if (cloudFound.isPresent()) {
            clouds.remove(cloudFound);
            clouds.add(changedCloud);
        } else {
            System.out.println("Exception changeCloud , strippedModel"); //todo
        }
    }

    private void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer(){
        return currentPlayer;
    }

    public ArrayList<StrippedCharacter> getCharacters() {
        return characters;
    }

    public ArrayList<StrippedCloud> getClouds() {
        return clouds;
    }

    public ArrayList<StrippedIsland> getIslands() {return islands;}

    public ArrayList<StrippedBoard> getBoards() {
        return boards;
    }

}
