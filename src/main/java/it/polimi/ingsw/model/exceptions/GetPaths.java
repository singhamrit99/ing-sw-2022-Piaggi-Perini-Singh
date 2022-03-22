package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.FilePaths;

public class GetPaths implements FilePaths {

    public String getAssistantLocation() {
        return ASSISTANT_CARDS_LOCATION;
    }

    public String getCharactersLocation() {

        return CHARACTER_CARDS_LOCATION;
    }

    public String getTilesLocation() {

        return CHARACTER_CARDS_LOCATION;
    }


}
