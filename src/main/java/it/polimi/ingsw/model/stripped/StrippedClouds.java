package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.tiles.Cloud;

import java.util.ArrayList;
import java.util.EnumMap;

public class StrippedClouds {

    final private ArrayList<StrippedCloud> clouds;

    public StrippedClouds(ArrayList<Cloud> clouds){
        this.clouds = new ArrayList<>();
        for (Cloud c:clouds) {
            StrippedCloud strippedCloud = new StrippedCloud(c.getName(),c.getStudents());
            this.clouds.add(strippedCloud);
        }
    }

    public ArrayList<StrippedCloud> getClouds() {
        return clouds;
    }
}
