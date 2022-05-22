package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardB implements Command {
    int characterCardID;
    int student;
    int island;

    String playerCaller;
    public PlayCharacterCardB(String playerCaller, int ID, int student, int island) {
        this.playerCaller = playerCaller;
        this.characterCardID = ID;
        this.student = student;
        this.island = island;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID, student, island);
    }
}
