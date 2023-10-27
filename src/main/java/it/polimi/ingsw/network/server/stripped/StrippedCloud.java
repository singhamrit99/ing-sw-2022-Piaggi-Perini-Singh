package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.tiles.Cloud;

import java.io.Serializable;
import java.util.EnumMap;

public class StrippedCloud implements Serializable {

    public String name;
    private EnumMap<Colors, Integer> students;

    /**
     * StrippedCloud constructor, used in room.
     * @param cloud the full Cloud the StrippedCloud is built from.
     */
    public StrippedCloud(Cloud cloud) {
        this.name = cloud.getName();
        this.students = cloud.getStudents();
    }

    /**
     * Getter for the Name field.
     * @return the Stripped Cloud's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the students field.
     * @return the students EnumMap
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    /**
     * Setter for students field.
     * @param students updated students.
     */
    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
