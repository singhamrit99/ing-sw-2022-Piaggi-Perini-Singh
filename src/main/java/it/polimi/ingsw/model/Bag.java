package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class Bag {
    private HashMap<StudentDisc, Integer> students;
    private final int studentType = 5;

    public Bag(HashMap<StudentDisc, Integer> students){
        this.students = students;
    }

    public void addStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getStudents();

        for(Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0){
                if(studentsDiscs.containsKey(set.getKey())){
                    studentsDiscs.put(set.getKey(), set.getValue() + studentsDiscs.get(set.getKey()));
                } else {
                    studentsDiscs.put(set.getKey(), set.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }

        }
        this.setStudents(studentsDiscs);
    }

    public HashMap<StudentDisc, Integer> drawStudents(int n) throws IncorrectArgumentException {
        //int n = 3; //modify by Tino
        int type, quantity;
        HashMap<StudentDisc, Integer> studentsDrawn = new HashMap<>();

        for (int i = 0; i < n;){
            type = (int)Math.floor(Math.random()*(studentType + 1));
            quantity = (int)Math.floor(Math.random()*(n-i)+1);
            studentsDrawn.put(new StudentDisc(Colors.getColor(type)), quantity);
            i += quantity;
        }

        this.removeStudents(studentsDrawn);
        return studentsDrawn;

    }

    private void removeStudents(HashMap<StudentDisc, Integer> studentsToRemove) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getStudents();

        for(Map.Entry<StudentDisc, Integer> set : studentsToRemove.entrySet()) {
            if (set.getValue() >= 0){
                if(studentsDiscs.containsKey(set.getKey())){
                    studentsDiscs.put(set.getKey(), studentsDiscs.get(set.getKey()) - set.getValue());
                } else {
                    throw new IncorrectArgumentException("HashMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        this.setStudents(studentsDiscs);
    }

    public HashMap<StudentDisc, Integer> getStudents() {
        return students;
    }

    public void setStudents(HashMap<StudentDisc, Integer> students) {
        this.students = students;
    }
}
