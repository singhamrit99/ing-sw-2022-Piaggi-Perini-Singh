package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;

import java.util.EnumMap;

public class StrippedCloud {

    public String name;
    private EnumMap<Colors, Integer> students;

    public StrippedCloud(String name, EnumMap<Colors, Integer> students) {
        this.name = name;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
