package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardB implements Command {
    Controller controller;
    int characterCardID;
    int student;
    int island;

    public PlayCharacterCardB(Controller controller, int ID, int student, int island) {
        this.controller = controller;
        this.characterCardID = ID;
        this.student = student;
        this.island = island;
    }

    @Override
    public void execute() {
        controller.callPlayCharacterCard(characterCardID, student,  island);
    }
}
