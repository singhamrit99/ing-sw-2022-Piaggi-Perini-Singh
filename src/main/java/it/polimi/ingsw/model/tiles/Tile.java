package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;

public interface Tile {

    void addStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException;
}
