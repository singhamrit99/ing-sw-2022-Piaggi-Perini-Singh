package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;

public class SetupCharacterCard implements Tile {

    EnumMap<Colors, Integer> studentsOnCard;

    public SetupCharacterCard(int characterID, int startingPrice, String powerDescription) {
        this.studentsOnCard = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            studentsOnCard.put(color, 0);
        }

    }



    @Override
    public void addStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {

    }
}

