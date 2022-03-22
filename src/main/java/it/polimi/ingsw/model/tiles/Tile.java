package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentDisc;

import java.util.HashMap;

public interface Tile {

    void addStudents(HashMap<StudentDisc,Integer> students);
}
