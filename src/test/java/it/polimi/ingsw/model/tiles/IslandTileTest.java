package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Towers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTileTest {

    @Test
    void testgetName() {
        String test= "Test";
        IslandTile island = new IslandTile("Test");

        assertEquals(test, island.getName());
    }

    @Test
    void testgetTowersColor() {
        Towers color=Towers.WHITE;
        IslandTile island = new IslandTile("Test");

        island.setTowersColor(color);
        assertEquals(color, island.getTowersColor());
    }

    @Test
    void testsetTowersColor() {
        Towers color= Towers.GREY;
        IslandTile island = new IslandTile("Test");

        island.setTowersColor(color);
        assertEquals(island.getTowersColor(),color);
    }

    @Test
    void testgetStudents() {
    }

    @Test
    void testgetNumOfTowers() {
    }

    @Test
    void testaddStudents() {
    }

    @Test
    void testsumTowers() {
    }

    @Test
    void testhasMotherNature() {
    }

    @Test
    void testmoveMotherNature() {
    }

    @Test
    void testremoveMotherNature() {
    }
}