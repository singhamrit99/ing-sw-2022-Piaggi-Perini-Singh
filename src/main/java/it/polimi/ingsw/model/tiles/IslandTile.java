package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

public class IslandTile implements Tile {
    private String name;
    private HashMap<Colors, Integer> students;
    private String towerOwner;
    private int numberOfTowers;
    private boolean hasMotherNature;
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

    public String getOwner() {
        return towerOwner;
    }
    public void setOwner(String nicknamePlayer) {
        towerOwner = nicknamePlayer;
    }

    public HashMap<Colors, Integer> getStudents() {
        return students;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public void addStudents(HashMap<Colors, Integer> summedStudents) throws  IncorrectArgumentException{
        HashMap<Colors, Integer> tmp = getStudents();
        for (Map.Entry<Colors, Integer> studentsNewHashMap : summedStudents.entrySet()){
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

    public void sumTowersUnification(int towers) {
        numberOfTowers += towers;
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