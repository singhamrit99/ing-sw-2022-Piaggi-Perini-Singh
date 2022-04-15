package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;

public class StudentCharacter extends CharacterCard {
    EnumMap<Colors, Integer> students;
    Bag bag;

    public StudentCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) throws IncorrectArgumentException {
        super(imageName, startingPrice, description, type, ability, requirements);
        bag = Bag.getInstance();

        students = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            students.put(color, 0);
        }

        try {
            students = bag.drawStudents(this.getType().getValue());
        } catch (IncorrectArgumentException e) {
            throw new IncorrectArgumentException();
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

    @Override
    public void activate() {

    }

    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }
}
