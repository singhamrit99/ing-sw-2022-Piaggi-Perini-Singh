package it.polimi.ingsw.client;

import it.polimi.ingsw.model.stripped.*;

import java.util.ArrayList;

public class LocalModel {
    ArrayList<StrippedBoard> boards;
    StrippedCharacter character;
    StrippedClouds clouds;
    ArrayList<StrippedPlayer> players;
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

    public ArrayList<StrippedPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<StrippedPlayer> players) {
        this.players = players;
    }

    public StrippedIslands getIslands() {
        return islands;
    }

    public void setIslands(StrippedIslands islands) {
        this.islands = islands;
    }
}
