package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.EnumMap;

public class PlayCharacterCardC implements Command {
    int characterCardID;
    EnumMap<Colors, Integer> students1;
    EnumMap<Colors, Integer> students2;

    public PlayCharacterCardC(int characterCardID,
                              EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
        this.characterCardID = characterCardID;
        this.students1 = students1;
        this.students2 = students2;
    }
    @Override
    public void execute(Controller controller) {
        controller.callPlayCharacterCard(characterCardID, students1,  students2);
    }
}
