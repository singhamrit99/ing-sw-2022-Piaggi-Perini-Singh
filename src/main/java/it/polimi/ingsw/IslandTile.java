package it.polimi.ingsw;

import java.util.HashMap;

public class IslandTile implements Tile{
    public String name;
    private boolean hasMotherNature;
    private HashMap<StudentDisc,Integer> students;
    private String towerOwner;
    private int numberOfTowers;
    private boolean hasNoEntryTile;
    public void addStudents(HashMap<StudentDisc,Integer> students){}
}
