package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

//This class of cards has a resource counter on it, thus needing to be initialized at the beginning of the game. CharacterIDs range from 1 to 3
public class StudentSetupCard extends CharacterCard {

    private EnumMap<Students, Integer> studentsonCard = new EnumMap(Students.class);
    int id = this.getCharacterID();

    @Override
    public void callPower(Player playercaller, int id) {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

        if (id == 1) {
            Students student = Students.GREEN;
            System.out.println("Choose the color to ignore in the calculation");
            ExchangeStudents( playercaller);        }
        else if (id == 2) {
            CardStudentToDining(playercaller);
        } else if (id == 3) {

            CardStudentToIsle(playercaller);
        }
        else
        {
            throw new java.lang.Error("Card Mismatch");
        }


    }

    //Every power call starts with a brief description of the power itself, the same the player saw when choosing it
    public void ExchangeStudents(Player playercaller) {

        System.out.println((this.getPowerDescription()));
    }

    public void CardStudentToDining(Player playercaller) {

        System.out.println((this.getPowerDescription()));
    }


    public void CardStudentToIsle(Player playercaller) {

        System.out.println((this.getPowerDescription()));

        //Player picks student

    }

    public void addStudentstoCard(EnumMap<Students, Integer> summedStudents) throws IncorrectArgumentException {
        EnumMap<Students, Integer> tmp = studentsonCard;
        for (Map.Entry<Students, Integer> studentsNewHashMap : summedStudents.entrySet()) {
            if (studentsNewHashMap.getValue() >= 0) {
                if (tmp.containsKey(studentsNewHashMap.getKey())) {
                    tmp.put(studentsNewHashMap.getKey(), studentsNewHashMap.getValue() + tmp.get(studentsNewHashMap.getKey()));
                } else {
                    tmp.put(studentsNewHashMap.getKey(), studentsNewHashMap.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        studentsonCard = tmp;
    }

}
