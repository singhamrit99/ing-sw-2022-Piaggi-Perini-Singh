package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.tiles.Cloud;

import java.io.Serializable;
import java.util.EnumMap;

public class StrippedCloud implements Serializable {

    public String name;
    private EnumMap<Colors, Integer> students;

    public StrippedCloud(Cloud cloud) {
        this.name = cloud.getName();
        this.students = cloud.getStudents();
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
