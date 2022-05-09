package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.tiles.Cloud;

import java.util.EnumMap;

public class StrippedClouds {
    public String name;
    private EnumMap<Colors, Integer> students;

    public StrippedClouds(Cloud[] clouds){

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
