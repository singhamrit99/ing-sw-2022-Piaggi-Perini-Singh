package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;

import java.util.EnumMap;
import java.util.Map;


public class Cloud {
    final private String name;
    private EnumMap<Colors, Integer> students;

    /**
     * Cloud tile constructor. Since it's filled when the Game starts its only parameter is its name from the related json.
     * @param name The name of the cloud.
     */
    public Cloud(String name) {
        this.name = name;
        students = new EnumMap<>(Colors.class);
        students = StudentManager.createEmptyStudentsEnum();
    }

    /**
     * Returns cloud name.
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Returns students on cloud .
     * @return name
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    /**
     * This method sets up the students enums so that they're ready to be drawn to.
     * @return Students enum ready for action
     */
    public EnumMap<Colors, Integer> drawStudents() {
        EnumMap<Colors, Integer> returnedStudents = StudentManager.createEmptyStudentsEnum();

        for (Colors color: Colors.values()){
            returnedStudents.put(color, students.get(color));
        }

        setStudents(StudentManager.createEmptyStudentsEnum());
        return returnedStudents;
    }

    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
