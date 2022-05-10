package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.stripped.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Optional;

public class StrippedModel {
    private ArrayList<StrippedBoard> boards;
    private ArrayList<StrippedCharacter> characters;
    private ArrayList<StrippedCloud> clouds;
    private ArrayList<StrippedIsland> islands;

    public StrippedModel(ArrayList<StrippedBoard> boards, ArrayList<StrippedCharacter> characters,
                         ArrayList<StrippedCloud> clouds, ArrayList<StrippedIsland> islands) {
        this.boards = boards;
        this.characters = characters;
        this.clouds = clouds;
        this.islands = islands;
    }

    public ArrayList<StrippedBoard> getBoards() {
        return boards;
    }

    public void setBoard(PropertyChangeEvent evt, String propertyName){
        StrippedBoard boardSource = (StrippedBoard) evt.getSource();
        Optional<StrippedBoard> boardToModify = boards.stream().filter(b -> boardSource.getOwner().equals(b.getOwner())).findFirst();
        if(boardToModify.isPresent()){
            switch(propertyName) {
                case "entrance":
                    boardToModify.get().setEntrance(boardSource.getEntrance());
                    break;
                case "dining":
                    boardToModify.get().setDining(boardSource.getDining());
                    break;
                case "coins":
                    boardToModify.get().setCoins(boardSource.getCoins());
                    break;
                case "professorTable":
                    boardToModify.get().setProfessorsTable(boardSource.getProfessorsTable());
                    break;
                default:
                    System.out.println("exception da fare setBoard");
                    break;
            }
        }
        else { //todo
            System.out.println("exception da fare setBoard");
        }
    }

    public void changePriceCharacterCard(PropertyChangeEvent evt, String propertyName) {
        StrippedCharacter changedCard = (StrippedCharacter) evt.getSource();
        StrippedCharacter cardToUpdate = null;
        for(StrippedCharacter card: characters){
            if(card.sameCard(changedCard)){
                cardToUpdate = card;
            }
        }

        if(cardToUpdate!= null){
            if(cardToUpdate.getPrice()!= (int)evt.getOldValue())
                cardToUpdate.setPrice((int)evt.getNewValue()); //update
            else{
                System.out.println("buttare fuori una exception sensata"); //todo
            }
        }else{
            System.out.println("buttare fuori una exception sensata"); //todo
        }
    }

    public void changeIsland(PropertyChangeEvent evt, String propertyName){
                StrippedIsland newIsland = (StrippedIsland) evt.getSource();
                Optional<StrippedIsland> islandFound = islands.stream().filter(x -> x.getName().equals(newIsland.getName())).findFirst();
                if(islandFound.isPresent()){
                    islands.remove(islandFound); //Island Deletion
                    if(evt.getNewValue() != null){
                        islands.add((StrippedIsland) evt.getNewValue());
                    }
                }
                else{
                    System.out.println("Exception changeIsland , strippedModel"); //todo
                }
    }

    public void changeCloud(PropertyChangeEvent evt, String propertyName){
        StrippedCloud newCloud = (StrippedCloud ) evt.getSource();
        Optional<StrippedCloud > cloudFound = clouds.stream().filter(x -> x.getName().equals(newCloud.getName())).findFirst();
        if(cloudFound.isPresent()){
            clouds.remove(cloudFound);
            clouds.add((StrippedCloud) evt.getNewValue());
        }
        else{
            System.out.println("Exception changeCloud , strippedModel"); //todo
        }
    }

    public  ArrayList<StrippedCharacter> getCharacters() {
        return characters;
    }

    public ArrayList<StrippedCloud> getClouds() {
        return clouds;
    }

    public void setCloud(StrippedCloud newCloud) {
        clouds.add(newCloud);
    }

    public StrippedIsland getIsland() {
        return islands.get(0);
    }

    public void setIslands(StrippedIsland island) {
        islands.add(island);
    }
}
