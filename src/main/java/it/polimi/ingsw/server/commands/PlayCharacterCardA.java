package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardA implements Command {
    int characterCardID;
    String  playerCaller;
    public PlayCharacterCardA(String playerCaller, int ID) {
        this.playerCaller =playerCaller;
        this.characterCardID = ID;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID);
    }
}
