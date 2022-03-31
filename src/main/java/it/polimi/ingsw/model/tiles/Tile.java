package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;

public interface Tile {

    void addStudents(HashMap<Colors, Integer> students) throws IncorrectArgumentException;
}
