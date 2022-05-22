package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardD implements Command {
    int characterCardID;
    int choice;
    String playerCaller;
    public PlayCharacterCardD(String playerCaller, int characterCardID, int choice) {
        this.playerCaller = playerCaller;
        this.characterCardID = characterCardID;
        this.choice = choice;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID, choice);
    }
}
