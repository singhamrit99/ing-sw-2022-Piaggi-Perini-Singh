package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumerations.Colors;

import java.util.EnumMap;

public class SetupCharacterCard extends CharacterCard{

    EnumMap<Colors,Integer> studentsOnCard;
    public SetupCharacterCard(int characterID, int startingPrice, String powerDescription) {
        super(characterID, startingPrice, powerDescription);
        this.studentsOnCard= new EnumMap<>(Colors.class);
        for(Colors color: Colors.values())
        {
            studentsOnCard.put(color, 0);
        }

    }
}
