package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentDisc;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

public class IslandTile implements Tile {
    public String name;
    private boolean hasMotherNature;
    private HashMap<StudentDisc, Integer> students;
    private String towerOwner;
    private int numberOfTowers;
    private boolean hasNoEntryTile;

    public IslandTile(String islandname) {
        name = islandname;
        hasMotherNature = false;
        towerOwner = "nobody";
        numberOfTowers = 0;
        hasNoEntryTile = false;
    }

    public String getName(){
        return name;
    }
    //I need to add these to the UML
    public String getOwner() {
        return towerOwner;
    }

    public void addStudents(HashMap<StudentDisc, Integer> summedStudents) throws  IncorrectArgumentException{
        HashMap<StudentDisc, Integer> tmp = getStudents();
        for (Map.Entry<StudentDisc, Integer> studentsNewHashMap : summedStudents.entrySet()){
            if (studentsNewHashMap.getValue() >= 0) {
                if (tmp.containsKey(studentsNewHashMap.getKey())) {
                    tmp.put(studentsNewHashMap.getKey(), studentsNewHashMap.getValue() + tmp.get(studentsNewHashMap.getKey()));
                } else {
                    tmp.put(studentsNewHashMap.getKey(), studentsNewHashMap.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        students = tmp;
    }

    public HashMap<StudentDisc, Integer> getStudents() {
        return students;
    }

    public void sumTowersUnification(int towers) {
        numberOfTowers += towers;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    public void moveMotherNature() {
        hasMotherNature = true;
    }

    public void removeMotherNature() {
        hasMotherNature = false;
    }

}