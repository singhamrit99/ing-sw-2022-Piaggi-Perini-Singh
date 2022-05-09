package it.polimi.ingsw.client;

import it.polimi.ingsw.model.stripped.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class LocalModel {
    ArrayList<StrippedBoard> boards;
    ArrayList<StrippedCharacter> characters;
    StrippedClouds clouds;
    StrippedIslands islands;

    public LocalModel(ArrayList<StrippedBoard> boards, ArrayList<StrippedCharacter> characters, StrippedClouds clouds, StrippedIslands islands) {
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

    public  ArrayList<StrippedCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<StrippedCharacter> characters) {
        this.characters = characters;
    }

    public StrippedClouds getClouds() {
        return clouds;
    }

    public void setClouds(StrippedClouds clouds) {
        this.clouds = clouds;
    }

    public StrippedIslands getIslands() {
        return islands;
    }

    public void setIslands(StrippedIslands islands) {
        this.islands = islands;
    }
}
