package it.polimi.ingsw.client;

import it.polimi.ingsw.model.stripped.*;

import java.util.ArrayList;

public class LocalModel {
    ArrayList<StrippedBoard> boards;
    ArrayList<StrippedCharacter> characters;
    StrippedClouds clouds;
    StrippedIslands islands;

    public ArrayList<StrippedBoard> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<StrippedBoard> boards) {
        this.boards = boards;
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
