package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardB implements Command {
    int characterCardID;
    int student;
    int island;

    public PlayCharacterCardB(int ID, int student, int island) {
        this.characterCardID = ID;
        this.student = student;
        this.island = island;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID, student,  island);
    }
}
