package it.polimi.ingsw.view.GUI.controllerFX;

public interface ResourcesPath {
    String FXML_FILE_PATH = "/fxml/";
    String FILE_EXTENSION = ".fxml";
    String LAUNCHER = "Launcher";
    String LOBBY = "Lobby";
    String ROOM = "Room";
    String NEW_ROOM = "NewRoom";
    String GAME_VIEW = "GameView";

    String GAME_OVER = "GameOver";

    // ----   GRAPHICS IMG IN GAME -----
    //towers
    String BLACK_TOWER = "/img/towers/black_tower.png";
    String GREY_TOWER = "/img/towers/grey_tower.png";
    String WHITE_TOWER = "/img/towers/white_tower.png";

    //students
    String BLUE_STUDENT = "/img/students/student_blue.png";
    String GREEN_STUDENT = "/img/students/student_green.png";
    String PINK_STUDENT = "/img/students/student_pink.png";
    String RED_STUDENT = "/img/students/student_red.png";
    String YELLOW_STUDENT = "/img/students/student_yellow.png";

    //clouds only for 3players
    String CLOUD_1= "/img/clouds/3players/cloud_3p_v1.png";
    String CLOUD_2= "/img/clouds/3players/cloud_3p_v2.png";
    String CLOUD_3= "/img/clouds/3players/cloud_3p_v3.png";

    //islands
    String ISLAND_0= "/img/islands/v0.png";
    String ISLAND_1= "/img/islands/v1.png";
    String ISLAND_2= "/img/islands/v2.png";

    //MotherNature
    String MN = "/img/islands/mn.png";

    //Assistants
    String ASSISTANT_CARDS = "/img/assistants/";
    String IMAGE_EXTENSION_ASS = ".png";

    //Characters
    String CHARACTERS = "/img/characters/character";
    String IMAGE_EXTENSION_CHAR = ".jpg";
    String NOENTRYTILE = "/img/characters/effects/noEntry.png";
}
