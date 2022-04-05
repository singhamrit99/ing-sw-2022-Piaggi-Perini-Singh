package it.polimi.ingsw.model;


//Interface that contains file paths for JSON files.
public final class FilePaths {
    static String ASSISTANT_CARDS_LOCATION = "/Cards.json";
    static String ISLAND_TILES_LOCATION = "/IslandTiles.json";
    static String CLOUD_TILES_LOCATION = "/Clouds.json";
    static String CHARACTER_CARDS_LOCATION = "/Characters.json";

    public static String getAssistantCardLocation() {
        return ASSISTANT_CARDS_LOCATION;
    }

    public static String getCharacterCardsLocation() {
        return CHARACTER_CARDS_LOCATION;
    }

    public static String getIslandTilesLocation() {
        return ISLAND_TILES_LOCATION;
    }

    public static String getCloudTilesLocation() {
        return CLOUD_TILES_LOCATION;
    }
}
