package it.polimi.ingsw.model;

import it.polimi.ingsw.view.View;

public class PlayerMove {

    private final Player player;
    private final View view;
    private final int move;

    public PlayerMove(Player player, View view, int move) {
        this.player = player;
        this.view = view;
        this.move = move;
    }

    public Player getPlayer() {
        return player;
    }

    public View getView() {
        return view;
    }

    public int getMove() {
        return move;
    }
}
