package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;

import java.util.EnumMap;
import java.util.LinkedList;

public class StrippedIslands {
    private LinkedList<StrippedIsland> strippedIslands;
    private String name;
    private EnumMap<Colors, Integer> returnStudents;
    private int numberOfTowers;
    private Towers towersColor;
    private int motherNaturePos;

    public StrippedIslands(LinkedList<StrippedIsland> strippedIslands, int motherNaturePos){
        this.motherNaturePos = motherNaturePos;
        this.strippedIslands = strippedIslands;
    }

    public LinkedList<StrippedIsland> getStrippedIslands(){
        return strippedIslands;
    }

    public void replaceStrippedIslandTile(){

    }

}

