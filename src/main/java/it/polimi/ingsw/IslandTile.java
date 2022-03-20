package it.polimi.ingsw;

import java.util.HashMap;

public class IslandTile implements Tile{
    public String name;
    private boolean hasMotherNature;
    private HashMap<StudentDisc,Integer> students;
    private String towerOwner;
    private int numberOfTowers;
    private boolean hasNoEntryTile;

    public IslandTile(String islandname){
        name = islandname;
        hasMotherNature = false;
        towerOwner = "nobody";
        numberOfTowers = 0;
        hasNoEntryTile = false;
    }
    public void addStudents(HashMap<StudentDisc,Integer> students){}
}
