package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class SchoolBoard {
    private HashMap<StudentDisc, Integer> entrance;
    private HashMap<StudentDisc, Integer> dining;
    private ArrayList<ProfessorPawn> professorTable;
    private int numberOfTowers;

    public SchoolBoard() {
        this.entrance = new HashMap<>();
        this.dining = new HashMap<>();
        this.professorTable = new ArrayList<>();
        numberOfTowers = 0;
    }

    public void addStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), set.getValue() + studentsDiscs.get(set.getKey()));
                } else {
                    studentsDiscs.put(set.getKey(), set.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }

        }
        this.setEntrance(studentsDiscs);
    }

    private void setEntrance(HashMap<StudentDisc, Integer> studentsDiscs) {
        this.entrance = studentsDiscs;
    }

    private HashMap<StudentDisc, Integer> getEntrance() {
        return this.entrance;
    }

    public void removeStudents(HashMap<StudentDisc, Integer> studentsToRemove) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<StudentDisc, Integer> set : studentsToRemove.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), studentsDiscs.get(set.getKey()) - set.getValue());
                } else {
                    throw new IncorrectArgumentException("HashMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        this.setEntrance(studentsDiscs);
    }

    public void moveStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getDining();

        for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), set.getValue() + studentsDiscs.get(set.getKey()));
                } else {
                    studentsDiscs.put(set.getKey(), set.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }

        }
        this.setDining(studentsDiscs);
    }

    private void setDining(HashMap<StudentDisc, Integer> studentsDiscs) {
        this.dining = studentsDiscs;
    }

    private HashMap<StudentDisc, Integer> getDining() {
        return this.dining;
    }

    public void moveProfessors(ArrayList<ProfessorPawn> professors) {
        for (ProfessorPawn professor : professors) {
            if (this.professorTable.contains(professor)) {
                professor.changeStatus();
            }
        }
    }

    public void moveTowers(int towers) {
        this.numberOfTowers += towers;
    }

}
