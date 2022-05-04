package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardD implements Command {

    Controller controller;
    int characterCardID;
    int choice;

    public PlayCharacterCardD(Controller controller, int characterCardID,int choice) {
        this.controller = controller;
        this.characterCardID = characterCardID;
        this.choice = choice;
    }

    @Override
    public void execute() {
        controller.callPlayCharacterCard(characterCardID, choice);
    }
}
