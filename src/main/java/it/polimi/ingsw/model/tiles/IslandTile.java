package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

public class IslandTile implements Tile {
    private String name;
    private EnumMap<Students, Integer> students;
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



    public EnumMap<Students, Integer> getStudents() {
        return students;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public void addStudents(EnumMap<Students, Integer> summedStudents) throws  IncorrectArgumentException{
        EnumMap<Students, Integer> tmp = getStudents();
        for (Map.Entry<Students, Integer> studentsNewEnumMap : summedStudents.entrySet()){
            if (studentsNewEnumMap.getValue() >= 0) {
                if (tmp.containsKey(studentsNewEnumMap.getKey())) {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue() + tmp.get(studentsNewEnumMap.getKey()));
                } else {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue());
                }
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
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