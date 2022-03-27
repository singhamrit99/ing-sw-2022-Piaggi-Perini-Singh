package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
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
    private ArrayList<ProfessorPawn> professorsTable;
    private int numberOfTowers;

    public SchoolBoard(int numberOfPlayers) {
        this.entrance = new HashMap<>();
        this.dining = new HashMap<>();
        this.professorsTable = new ArrayList<>();

        if (numberOfPlayers == 3) {
            numberOfTowers = 6;
        } else {
            numberOfTowers = 8;
        }
    }

    public void addStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = getEntrance();

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

    public void removeStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
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
        this.removeStudents(studentsDiscs);
        this.setDining(studentsDiscs);
    }

    public void moveProfessors(ArrayList<ProfessorPawn> professors) {
        for (ProfessorPawn professor : professors) {
            if (professorsTable.contains(professor)) {
                professor.changeStatus();
            }
        }
    }

    public boolean hasEnoughStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IncorrectArgumentException("HashMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        return true;
    }

    public void moveTowers(int towers) {
        this.numberOfTowers += towers;
    }

    private void setEntrance(HashMap<StudentDisc, Integer> studentsDiscs) {
        this.entrance = studentsDiscs;
    }

    private HashMap<StudentDisc, Integer> getEntrance() {
        return entrance;
    }

    private void setDining(HashMap<StudentDisc, Integer> studentsDiscs) {
        this.dining = studentsDiscs;
    }

    private HashMap<StudentDisc, Integer> getDining() {
        return this.dining;
    }

    public ArrayList<ProfessorPawn> getProfessorsTable() {
        return professorsTable;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public int getStudentsByColor(Colors color) throws IncorrectArgumentException {
        StudentDisc studentDisc = new StudentDisc(color);

        if (dining.containsKey(studentDisc)) {
            return dining.get(studentDisc);
        } else {
            throw new IncorrectArgumentException();
        }
    }
}
