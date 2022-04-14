package it.polimi.ingsw.model;

public class MoveMessage {
    private final Player player;
    private final Game game;

    public MoveMessage(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }
}
