package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.EnumMap;

public class PlayCharacterCardC implements Command {
    Controller controller;
    int characterCardID;
    EnumMap<Colors, Integer> students1;
    EnumMap<Colors, Integer> students2;

    public PlayCharacterCardC(Controller controller, int characterCardID,
                              EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
        this.controller = controller;
        this.characterCardID = characterCardID;
        this.students1 = students1;
        this.students2 = students2;
    }
    @Override
    public void execute() {
        controller.callPlayCharacterCard(characterCardID, students1,  students2);
    }
}
