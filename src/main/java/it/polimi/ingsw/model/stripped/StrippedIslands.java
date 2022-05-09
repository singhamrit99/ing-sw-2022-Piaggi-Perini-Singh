package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.tiles.Island;

import java.util.EnumMap;
import java.util.LinkedList;

public class StrippedIslands {
    private LinkedList<StrippedIsland> strippedIslands;

    public StrippedIslands(LinkedList<Island> islands){
        strippedIslands = new LinkedList<>();
        int i = 0;
        for (Island s: islands) {
            StrippedIsland newStrippedIsland = new StrippedIsland(s.getName(),s.getTowersColor(),
                    s.getNumOfTowers(),s.getStudents(),s.hasMotherNature(), s.hasNoEntryTile());
            strippedIslands.add(newStrippedIsland);
        }
    }

    public LinkedList<StrippedIsland> getStrippedIslands(){
        return strippedIslands;
    }

}

