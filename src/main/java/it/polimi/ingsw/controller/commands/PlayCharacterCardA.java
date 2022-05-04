package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardA implements Command {

    int characterCardID;

    public PlayCharacterCardA( int ID) {
        this.characterCardID = ID;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID) ;
    }
}
