package it.polimi.ingsw;

import java.util.HashMap;

public class CloudTile implements Tile{
    public String name;
    private HashMap<StudentDisc,Integer> students;

    public HashMap<StudentDisc,Integer> removeStudents(){
        return new HashMap<StudentDisc,Integer>();
    }
    public void addStudents(HashMap<StudentDisc,Integer> students){}
}
