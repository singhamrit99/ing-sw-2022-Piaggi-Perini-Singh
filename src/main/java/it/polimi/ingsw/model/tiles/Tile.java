package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;

public interface Tile {

    void addStudents(EnumMap<Students, Integer> students) throws IncorrectArgumentException;
}
