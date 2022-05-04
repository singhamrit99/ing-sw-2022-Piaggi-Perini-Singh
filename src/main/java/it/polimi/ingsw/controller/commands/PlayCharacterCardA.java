package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardA implements Command {

    Controller controller;
    int characterCardID;

    public PlayCharacterCardA(Controller controller, int ID) {
        this.controller = controller;
        this.characterCardID = ID;
    }

    @Override
    public void execute() {
        controller.callPlayCharacterCard(characterCardID) ;
    }
}
