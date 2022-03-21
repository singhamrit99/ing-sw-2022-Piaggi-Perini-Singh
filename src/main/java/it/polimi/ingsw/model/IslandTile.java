package it.polimi.ingsw.model;

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
    //I need to add these to the UML
    public String getOwner(){
        return towerOwner;
    }

    public void sumStudentsUnification(HashMap<StudentDisc,Integer> s){
        // SUM OF HASHMAP ?? students += s;
    }

    public HashMap<StudentDisc,Integer> getStudents(){
        return students;
    }

    public void sumTowersUnification(int towers){
        numberOfTowers += towers;
    }

    public int getTowers(){
        return numberOfTowers;
    }

    public void addStudents(HashMap<StudentDisc,Integer> students){}
}
