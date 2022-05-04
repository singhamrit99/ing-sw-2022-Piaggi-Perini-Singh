package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;
import it.polimi.ingsw.model.exceptions.ProfessorNotFoundException;

import java.util.EnumMap;
import java.util.Map;

public class StudentCharacter extends CharacterCard {
    private EnumMap<Colors, Integer> students;
    private Bag bag;

    private int studentIndex;
    private int islandIndex;
    private EnumMap<Colors, Integer> students1;
    private EnumMap<Colors, Integer> students2;

    public StudentCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) throws NegativeValueException {
        super(imageName, startingPrice, description, type, ability, requirements);
        bag = Bag.getInstance();
        studentIndex = -1;
        islandIndex = -1;
        students1 = new EnumMap<>(Colors.class);
        students2 = new EnumMap<>(Colors.class);

        students = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            students.put(color, 0);
        }

        try {
            students = bag.drawStudents(this.getType().getValue());
        } catch (NegativeValueException e) {
            throw new NegativeValueException();
        }
    }

    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getStudents(), studentsToAdd);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(students, studentsToRemove);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public boolean hasEnoughStudents(EnumMap<Colors, Integer> students) throws IllegalArgumentException, NegativeValueException {
        EnumMap<Colors, Integer> studentsDiscs = getStudents();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IllegalArgumentException("EnumMap is not correct");
                }
            } else {
                throw new NegativeValueException("EnumMap is not correct");
            }
        }
        return true;
    }

    @Override
    public void activate(Game game) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException {
        Ability.Actions action = this.getAbility().getAction();
        switch (action) {
            case ADD_ISLAND:
                EnumMap<Colors, Integer> studentsToAddIsland = new EnumMap<>(Colors.class);
                studentsToAddIsland.put(Colors.getStudent(studentIndex), 1);

                if (hasEnoughStudents(studentsToAddIsland)) {
                    game.getIsland(islandIndex).addStudents(studentsToAddIsland);
                    removeStudents(studentsToAddIsland);
                    addStudents(bag.drawStudents(getAbility().getValue()));
                    setStatus(2);
                }
                break;
            case SWAP_ENTRANCE:
                int count = 0;
                int count1 = 0;

                for (Map.Entry<Colors, Integer> set : students1.entrySet()) count += set.getValue();
                if (getAbility().getValue() <= count) throw new IllegalArgumentException("Too many students are given");

                for (Map.Entry<Colors, Integer> set : students2.entrySet()) count1 += set.getValue();
                if (count != count1) throw new IllegalArgumentException("The given students number do not match");

                game.getCurrentPlayer().getSchoolBoard().removeStudents(students2);
                game.getCurrentPlayer().getSchoolBoard().addStudents(students1);
                setStatus(2);
                break;
            case SWAP_ENTRANCE_DINING:
                count = 0;
                count1 = 0;

                for (Map.Entry<Colors, Integer> set : students1.entrySet()) count += set.getValue();
                if (getAbility().getValue() <= count) throw new IllegalArgumentException("Too many students are given");

                for (Map.Entry<Colors, Integer> set : students2.entrySet()) count1 += set.getValue();
                if (count != count1) throw new IllegalArgumentException("The given students number do not match");

                game.getCurrentPlayer().getSchoolBoard().moveStudents(students1);
                game.getCurrentPlayer().getSchoolBoard().removeDiningStudents(students2);
                game.getCurrentPlayer().getSchoolBoard().addStudents(students2);
            case ADD_DINING:
                EnumMap<Colors, Integer> studentsToAddDining = new EnumMap<>(Colors.class);
                studentsToAddDining.put(Colors.getStudent(studentIndex), 1);

                if (hasEnoughStudents(studentsToAddDining)) {
                    game.getCurrentPlayer().getSchoolBoard().addStudentsDining(studentsToAddDining);
                    game.checkAndPlaceProfessor();
                    removeStudents(studentsToAddDining);
                    addStudents(bag.drawStudents(getAbility().getValue()));
                    setStatus(2);
                }
                break;
            default:
                break;
        }
    }

    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    public void setChoices(int student, int island) {
        studentIndex = student;
        islandIndex = island;
    }

    public void setEnums(EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
        this.students1 = students1;
        this.students2 = students2;
    }
}
