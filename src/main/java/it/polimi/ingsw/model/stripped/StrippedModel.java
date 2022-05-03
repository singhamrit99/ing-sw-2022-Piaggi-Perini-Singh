package it.polimi.ingsw.model.stripped;

public class StrippedModel {
    StrippedBoard board;
    StrippedCharacter character;
    StrippedClouds clouds;
    StrippedPlayer player;
    StrippedIslands islands;

    public StrippedBoard getBoard() {
        return board;
    }

    public void setBoard(StrippedBoard board) {
        this.board = board;
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

    public StrippedPlayer getPlayer() {
        return player;
    }

    public void setPlayer(StrippedPlayer player) {
        this.player = player;
    }

    public StrippedIslands getIslands() {
        return islands;
    }

    public void setIslands(StrippedIslands islands) {
        this.islands = islands;
    }
}
