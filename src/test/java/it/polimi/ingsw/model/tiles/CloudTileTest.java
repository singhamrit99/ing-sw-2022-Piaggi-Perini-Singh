package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CloudTileTest {

    @Test
    void removeStudents() {
         EnumMap<Colors,Integer> students=new EnumMap<Colors, Integer>(Colors.class);
        students.put(Colors.GREEN, 2);
        students.put(Colors.RED, 1);
        CloudTile tile= new CloudTile("TestCloud");
        try {
            tile.addStudents(students);
        }
        catch(IncorrectArgumentException e)
        {
            e.printStackTrace();
        }
        for (Map.Entry<Colors, Integer> studentType : students.entrySet()) {
            if (studentType.getValue() > 0) {
                studentType.setValue(0);
            }
            assertEquals(0,students.get(Colors.GREEN));
            assertEquals(0,students.get(Colors.RED));
            assertEquals(0,students.get(Colors.BLUE));
            assertEquals(0,students.get(Colors.YELLOW));
            assertEquals(0,students.get(Colors.PINK));


    }}

    @Test
    void addStudents() {



    }
}