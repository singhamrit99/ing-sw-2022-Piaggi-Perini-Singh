package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudentDisc;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

//This class of cards has a resource counter on it, thus needing to be initialized at the beginning of the game. CharacterIDs range from 1 to 4
public class StudentSetupCard extends CharacterCard {

    private HashMap<StudentDisc, Integer> studentsonCard = new HashMap<>();
    int id = this.getCharacterID();

    @Override
    public void callPower(Player playercaller, int id) {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

        switch (id) {

            case 1:
                CardStudentToIsle(playercaller);
                break;
            case 2:
                ExchangeStudents(playercaller);
                break;
            case 3:
                CardStudentToDining(playercaller);
                break;
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

    public void addStudentstoCard(HashMap<StudentDisc, Integer> summedStudents) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> tmp = studentsonCard;
        for (Map.Entry<StudentDisc, Integer> studentsNewHashMap : summedStudents.entrySet()) {
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

