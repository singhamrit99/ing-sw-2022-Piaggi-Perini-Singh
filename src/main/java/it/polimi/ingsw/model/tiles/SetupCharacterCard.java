package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

public class SetupCharacterCard implements Tile {

    EnumMap<Colors, Integer> studentsOnCard;

    public SetupCharacterCard(int characterID, int startingPrice, String powerDescription) {
        this.studentsOnCard = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            studentsOnCard.put(color, 0);
        }

    }
    public EnumMap<Colors, Integer> getStudents() {
        return studentsOnCard;
    }



    @Override
    public void addStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {

        EnumMap<Colors, Integer> tmp = getStudents();
        for (Map.Entry<Colors, Integer> studentsNewEnumMap : students.entrySet()){
            if (studentsNewEnumMap.getValue() >= 0) {
                //if (tmp.containsKey(studentsNewEnumMap.getKey())) {
                tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue() + tmp.get(studentsNewEnumMap.getKey()));
               /* } else {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue());
                }*/
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }
        }
        studentsOnCard = tmp;
    }
}

