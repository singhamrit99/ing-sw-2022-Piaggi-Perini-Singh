package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardD implements Command {

    int characterCardID;
    int choice;

    public PlayCharacterCardD(int characterCardID,int choice) {
        this.characterCardID = characterCardID;
        this.choice = choice;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID, choice);
    }
}
