package it.polimi.ingsw.client;

import it.polimi.ingsw.model.stripped.*;

import java.util.ArrayList;

public class LocalModel {
    ArrayList<StrippedBoard> boards;
    StrippedCharacter character;
    StrippedClouds clouds;
    StrippedIslands islands;

    public ArrayList<StrippedBoard> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<StrippedBoard> boards) {
        this.boards = boards;
    }

    public StrippedCharacter getCharacter() {
        return character;
    }

    public void setCharacter(StrippedCharacter character) {
        this.character = character;
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
