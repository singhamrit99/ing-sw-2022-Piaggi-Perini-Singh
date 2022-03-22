package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentDisc;

import java.util.HashMap;

public class CloudTile implements Tile {
    public String name;
    private HashMap<StudentDisc, Integer> students;

    public CloudTile(String cloudname) {
        name = cloudname;
    }

    public HashMap<StudentDisc, Integer> removeStudents() {
        return new HashMap<StudentDisc, Integer>();
    }

    public void addStudents(HashMap<StudentDisc, Integer> students) {
    }
}
