package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.DrawFromBagCommand;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.network.server.stripped.StrippedIsland;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameViewController extends InitialStage implements Controller {
    protected static AtomicBoolean opened = new AtomicBoolean(false);
    private String currentBoardView; //the owner of the board current visible on the screen
    ArrayList<StackPane> islandsPanes;

    public GameViewController(GUI gui) {
        super(gui);
        opened.set(false);
        //importing all assets from resource folder
        try {
            blackTowerImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.BLACK_TOWER)));
            whiteTowerImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.WHITE_TOWER)));
            greyTowerImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.GREY_TOWER)));
            blueStudentImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.BLUE_STUDENT)));
            greenStudentImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.GREEN_STUDENT)));
            pinkStudentImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.PINK_STUDENT)));
            redStudentImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.RED_STUDENT)));
            yellowStudentImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.YELLOW_STUDENT)));
            island0 = new Image(Files.newInputStream(Paths.get(ResourcesPath.ISLAND_0)));
            island1 = new Image(Files.newInputStream(Paths.get(ResourcesPath.ISLAND_1)));
            island2 = new Image(Files.newInputStream(Paths.get(ResourcesPath.ISLAND_2)));
            motherNature = new Image(Files.newInputStream(Paths.get(ResourcesPath.MN)));
        } catch (IOException io) {
            System.out.println("Error importing img assets in GameViewController");
        }
    }

    @FXML
    public void initialize() {
        if (!opened.get()) { //first opening
            opened.set(true);
            System.out.println(GUI.client.getLocalPlayerList());
            initializePlayersViewMenu(GUI.client.getLocalPlayerList());
            firstRefreshBoard();
        }

        if (itemBoardViewArray.size() > 0) {
            for (MenuItem boardView : itemBoardViewArray) {
                boardView.setOnAction((event) -> changeViewBoard(boardView.getText()));
            }
        }

        leaveGame.setOnAction((event) -> { //leaving game
            try {
                GUI.client.leaveRoom();
                Platform.exit();
                System.exit(0);
            } catch (RemoteException | UserNotInRoomException | UserNotRegisteredException ignored) {
            }
        });

        //animations
        if (islandsPanes.size() != 0) {
            for (Pane island : islandsPanes) {
                TranslateTransition floatingTitle = new TranslateTransition();
                floatingTitle.setNode(island);
                floatingTitle.setDuration(Duration.millis(4000));
                floatingTitle.setCycleCount(TranslateTransition.INDEFINITE);
                floatingTitle.setByX(10);
                floatingTitle.setAutoReverse(true);
                floatingTitle.setInterpolator(Interpolator.EASE_BOTH);
                floatingTitle.play();
            }
        }
    }

    public void changeViewBoard(String viewOwnerTarget) {
        if (GUI.client.getLocalPlayerList().contains(viewOwnerTarget)) {
            currentBoardView = viewOwnerTarget;
            reloadEntrance();
            reloadDining();
            reloadProfs();
            reloadTowers();
        }
    }

    private Image studentImgFromColor(Colors c) {
        if (c.equals(Colors.BLUE)) return blueStudentImg;
        else if (c.equals(Colors.YELLOW)) return yellowStudentImg;
        else if (c.equals(Colors.PINK)) return pinkStudentImg;
        else if (c.equals(Colors.GREEN)) return greenStudentImg;
        else return redStudentImg;
    }


    public boolean isOpened() {
        return opened.get();
    }

    private void initializePlayersViewMenu(ArrayList<String> players) {
        List<MenuItem> items = changeViewBoard.getItems();
        int indexItem = 0;
        while (indexItem < 4) {
            if (indexItem > players.size() - 1) {
                items.get(indexItem).setVisible(false);
            } else {
                items.get(indexItem).setText(players.get(indexItem));
            }
            indexItem++;
        }
        itemBoardViewArray = items;

    }

    private void firstRefreshBoard() {
        initializeImagesEntrance();
        currentBoardView = GUI.client.getNickname();
        reloadEntrance();
        initializeImagesTowers();
        reloadTowers();
        reloadProfs();
        initializeImagesDining();
        reloadDining();
        initializeClouds();
        reloadClouds();
        reloadIslands();
        reloadBag();
        loadAssistantDeck();
    }

    private void reloadBag() {
        bag.setOnMouseClicked(mouseEvent -> {
            DrawFromBagCommand drawFromBagOrder = new DrawFromBagCommand(GUI.client.getNickname());
            try {
                GUI.client.performGameAction(drawFromBagOrder);
            } catch (NotEnoughCoinsException e) {
                Controller.showErrorDialogBox(StringNames.NOT_ENOUGH_COINS);
            } catch (AssistantCardNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_NOT_FOUND);
            } catch (NegativeValueException e) {
                Controller.showErrorDialogBox(StringNames.NEGATIVE_VALUE);
            } catch (IncorrectStateException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_STATE);
            } catch (MotherNatureLostException e) {
                Controller.showErrorDialogBox(StringNames.MOTHER_NATURE_LOST);
            } catch (ProfessorNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.PROFESSOR_NOT_FOUND);
            } catch (IncorrectPlayerException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_PLAYER);
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            }
        });
    }

    private ArrayList<ImageView> spawnStudents(StrippedIsland island) {
        ArrayList<ImageView> imagesToReturn = new ArrayList<>();
        EnumMap<Colors, Integer> students = island.getStudents();
        for (Colors c : students.keySet()) {
            if (students.get(c) != 0) {
                int i = 0;
                Image rightColor = studentImgFromColor(c);
                while (i < students.get(c)) {
                    ImageView student = new ImageView(rightColor);
                    imagesToReturn.add(student);
                    i++;
                }
            }
        }
        return imagesToReturn;
    }

    private ArrayList<ImageView> spawnTowers(StrippedIsland island) {
        ArrayList<ImageView> towersToReturn = new ArrayList<>();
        EnumMap<Colors, Integer> students = island.getStudents();
        Image rightColor;
        switch (island.getTowersColor()) {
            case WHITE:
                rightColor = whiteTowerImg;
                break;
            case GREY:
                rightColor = greyTowerImg;
                break;
            default:
                rightColor = blackTowerImg;
                break;
        }
        ;

        for (int i = 0; i < island.getNumOfTowers(); i++) {
            ImageView towerImage = new ImageView(rightColor);
            towersToReturn.add(towerImage);
        }

        return towersToReturn;
    }

    ArrayList<Image> islandsImgs;

    public void reloadIslands() {
        islandsPanes = new ArrayList<>();
        IslandsPane.getChildren().clear();
        IslandsPane.maxHeight(450);
        IslandsPane.maxWidth(1000);
        IslandsPane.setAlignment(Pos.TOP_CENTER);
        GridPane Islands = new GridPane();
        Islands.setGridLinesVisible(false);
        IslandsPane.getChildren().add(Islands);
        RowConstraints row = new RowConstraints();
        row.setPrefHeight(150);
        row.setMaxHeight(150);
        ColumnConstraints column = new ColumnConstraints();
        column.setPrefWidth(200);
        column.setMaxWidth(200);
        Islands.getRowConstraints().add(row);
        Islands.getColumnConstraints().add(column);
        Islands.setAlignment(Pos.TOP_CENTER);

        islandsImgs = new ArrayList<>();
        islandsImgs.add(island0);
        islandsImgs.add(island1);
        islandsImgs.add(island2);

        ArrayList<StrippedIsland> islandsBackEnd = GUI.client.getLocalModel().getIslands();
        int indexIsland = 0;

        //FIRST LINE OF ISLANDS
        for (int i = 0; i < 6; i++) {
            if (i == 0 || i == 5) {
                Islands.addRow(0, new Text("")); //empty cell alignment
            } else {
                if (islandsBackEnd.get(indexIsland).getName().equals("EMPTY")) {
                    Islands.addRow(0, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(i % 3);
                    HBox islandHbox = new HBox();
                    GridPane towersPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> towers = spawnTowers(islandsBackEnd.get(indexIsland));
                    int j = 0;
                    for (ImageView tower : towers) {
                        tower.setFitHeight(25);
                        tower.setFitWidth(25);
                        towersPane.addRow(j / 2, tower);
                        j++;
                    }
                    GridPane studentsPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> imgStudents = spawnStudents(islandsBackEnd.get(indexIsland));
                    for (j = 0; j < imgStudents.size(); j++) {
                        ImageView img = imgStudents.get(j);
                        img.setFitWidth(20);
                        img.setFitHeight(20);
                        studentsPane.addRow(j / 3, img);
                    }
                    if (islandsBackEnd.get(indexIsland).hasMotherNature()) spawnMN(studentsPane);
                    Islands.addRow(0, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            }
            indexIsland++;
        }

        //SECOND LINE
        for (int i = 0; i < 6; i++) {
            if (i != 0 && i != 5) {
                Islands.addRow(1, new Text("")); //empty cell
            } else if (i == 0) {
                if (islandsBackEnd.get(11).getName().equals("EMPTY")) {
                    Islands.addRow(1, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(1 + i % 2);
                    HBox islandHbox = new HBox();
                    GridPane towersPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> towers = spawnTowers(islandsBackEnd.get(11));
                    for (int x = 0; x < towers.size(); x++) {
                        towers.get(x).setFitHeight(25);
                        towers.get(x).setFitWidth(25);
                        towersPane.addRow(x / 2, towers.get(x));
                    }
                    GridPane studentsPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> imgStudents = spawnStudents(islandsBackEnd.get(11));
                    for (int j = 0; j < imgStudents.size(); j++) {
                        ImageView img = imgStudents.get(j);
                        img.setFitWidth(20);
                        img.setFitHeight(20);
                        studentsPane.addRow(j / 2, img);
                    }
                    if (islandsBackEnd.get(11).hasMotherNature()) spawnMN(studentsPane);
                    Islands.addRow(1, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            } else if (i == 5) { //second island of the FIRST line with the HOLE of the circle inside
                if (islandsBackEnd.get(4).getName().equals("EMPTY")) {
                    Islands.addRow(1, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(1 + i % 2);
                    HBox islandHbox = new HBox();
                    GridPane towersPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> towers = spawnTowers(islandsBackEnd.get(4));
                    int y = 0;
                    for (ImageView tower : towers) {
                        tower.setFitHeight(25);
                        tower.setFitWidth(25);
                        towersPane.addRow(y / 2, tower);
                        y++;
                    }
                    GridPane studentsPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> imgStudents = spawnStudents(islandsBackEnd.get(4));
                    int im = 0;
                    for (ImageView img : imgStudents) {
                        img.setFitWidth(20);
                        img.setFitHeight(20);
                        studentsPane.addRow(im / 2, img);
                        im++;
                    }
                    if (islandsBackEnd.get(4).hasMotherNature()) spawnMN(studentsPane);
                    Islands.addRow(1, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            }
        }

        //THIRD LINE
        for (int i = 0; i < 6; i++) {
            if (i != 0 && i != 5) {
                Islands.addRow(2, new Text("")); //empty cell
            } else if (i == 0) {
                if (islandsBackEnd.get(10).getName().equals("EMPTY")) {
                    Islands.addRow(2, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(1 + i % 2);
                    HBox islandHbox = new HBox();
                    GridPane towersPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> towers = spawnTowers(islandsBackEnd.get(10));
                    int t = 0;
                    for (ImageView tower : towers) {
                        tower.setFitHeight(25);
                        tower.setFitWidth(25);
                        towersPane.addRow(t / 2, tower);
                        t++;
                    }
                    GridPane studentsPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> imgStudents = spawnStudents(islandsBackEnd.get(10));
                    for (int j = 0; j < imgStudents.size(); j++) {
                        ImageView img = imgStudents.get(j);
                        img.setFitWidth(20);
                        img.setFitHeight(20);
                        studentsPane.addRow(j / 2, img);
                    }
                    if (islandsBackEnd.get(10).hasMotherNature()) spawnMN(towersPane);
                    Islands.addRow(2, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            } else if (i == 5) { //second island of the SECOND line with the HOLE of the circle inside
                if (islandsBackEnd.get(5).getName().equals("EMPTY")) {
                    Islands.addRow(2, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(1 + i % 2);
                    HBox islandHbox = new HBox();
                    GridPane towersPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> towers = spawnTowers(islandsBackEnd.get(5));
                    int x = 0;
                    for (ImageView tower : towers) {
                        tower.setFitHeight(25);
                        tower.setFitWidth(25);
                        towersPane.addRow(x / 2, tower);
                        x++;
                    }
                    GridPane studentsPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> imgStudents = spawnStudents(islandsBackEnd.get(5));
                    for (int j = 0; j < imgStudents.size(); j++) {
                        ImageView img = imgStudents.get(j);
                        img.setFitWidth(20);
                        img.setFitHeight(20);
                        studentsPane.addRow(j / 2, img);
                    }
                    if (islandsBackEnd.get(5).hasMotherNature()) spawnMN(studentsPane);
                    Islands.addRow(2, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            }
        }

        //LAST ISLANDS
        indexIsland = 9;
        for (int i = 0; i < 6; i++) {
            if (i == 0 || i == 5) {
                Islands.addRow(3, new Text("")); //empty cell
            } else {
                if (islandsBackEnd.get(indexIsland).getName().equals("EMPTY")) {
                    Islands.addRow(0, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(i % 3);
                    HBox islandHbox = new HBox();
                    GridPane towersPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> towers = spawnTowers(islandsBackEnd.get(indexIsland));
                    int o = 0;
                    for (ImageView tower : towers) {
                        tower.setFitHeight(25);
                        tower.setFitWidth(25);
                        towersPane.addRow(o / 2, tower);
                        o++;
                    }
                    GridPane studentsPane = initGridPaneIsland(island, islandHbox);
                    ArrayList<ImageView> imgStudents = spawnStudents(islandsBackEnd.get(indexIsland));
                    for (int j = 0; j < imgStudents.size(); j++) {
                        ImageView img = imgStudents.get(j);
                        img.setFitWidth(20);
                        img.setFitHeight(20);
                        studentsPane.addRow(j / 2, img);
                    }
                    if (islandsBackEnd.get(indexIsland).hasMotherNature()) spawnMN(studentsPane);
                    Islands.addRow(3, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
                indexIsland--; //out from the if !!
            }
        }
    }

    private StackPane initIsland(int index) {
        StackPane island = new StackPane();
        island.maxHeight(100);
        island.maxWidth(100);
        islandsPanes.add(island);
        ImageView islandImg = new ImageView(islandsImgs.get(index));
        islandImg.setFitWidth(200);
        islandImg.setFitHeight(200);
        island.getChildren().add(islandImg);
        island.setAlignment(Pos.CENTER);
        return island;
    }

    private GridPane initGridPaneIsland(StackPane island, HBox islandHBox) {
        islandHBox.maxWidth(150);
        islandHBox.maxHeight(150);
        if (!island.getChildren().contains(islandHBox))
            island.getChildren().add(islandHBox);
        GridPane grid = new GridPane();
        RowConstraints row = new RowConstraints();
        row.setPrefHeight(55);
        row.setMaxHeight(55);
        ColumnConstraints column = new ColumnConstraints();
        column.setPrefWidth(65);
        column.setMaxWidth(65);
        islandHBox.getChildren().add(grid);
        island.setAlignment(Pos.CENTER);
        islandHBox.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    private void spawnMN(GridPane pane) {
        ImageView mn = new ImageView(motherNature);
        mn.setFitHeight(40);
        mn.setFitWidth(40);
        mn.maxWidth(40);
        mn.maxHeight(40);
        pane.addRow(0, mn);

        mn.setOnMouseClicked((event) -> {
            Scene scene;
            try {
                String filePath = ResourcesPath.FXML_FILE_PATH + "MoveMotherNatureView" + ResourcesPath.FILE_EXTENSION;
                FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                loader.setController(new MoveMotherNatureController(gui));
                scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle(StringNames.TITLE);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void reloadEntrance() {
        board.setOnMouseClicked((event) -> {
            Scene scene;
            try {
                String filePath = ResourcesPath.FXML_FILE_PATH + "MoveStudentsView" + ResourcesPath.FILE_EXTENSION;
                FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                loader.setController(new MoveStudentsController(gui));
                scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle(StringNames.TITLE);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            EnumMap<Colors, Integer> entrance = GUI.client.getLocalModel().getBoardOf(currentBoardView).getEntrance();
            int indexEntranceAssets = 0;
            for (Colors c : entrance.keySet()) {
                if (entrance.get(c) != 0) {
                    int i = 0;
                    Image rightColor = studentImgFromColor(c);
                    while (i < entrance.get(c)) {
                        entranceStudentsImgs.get(indexEntranceAssets).setVisible(true);
                        entranceStudentsImgs.get(indexEntranceAssets).setImage(rightColor);
                        i++;
                        indexEntranceAssets++;
                    }
                }
            }
            //hiding others position if there aren't
            while (indexEntranceAssets < entranceStudentsImgs.size()) {
                entranceStudentsImgs.get(indexEntranceAssets).setVisible(false);
                indexEntranceAssets++;
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.ERROR_LOCALMODEL);
        }
    }

    public void reloadClouds() {
        int numPlayers = GUI.client.getLocalPlayerList().size();
        ArrayList<StrippedCloud> clouds = GUI.client.getLocalModel().getClouds();

        for (int cloudIndex = 0; cloudIndex < numPlayers; cloudIndex++) {
            int indexStudentsAssets = 0;
            EnumMap<Colors, Integer> students = clouds.get(cloudIndex).getStudents();
            for (Colors c : students.keySet()) {
                Image rightColor = studentImgFromColor(c);
                if (students.get(c) != 0) {
                    int numSameColor = 0;
                    while (numSameColor < students.get(c)) { //this in case of more students with same color
                        switch (cloudIndex) {
                            case 0:
                                if (numPlayers == 3) {
                                    studentsCloud1v3.get(indexStudentsAssets).setImage(rightColor);
                                    studentsCloud1v3.get(indexStudentsAssets).setVisible(true);
                                } else {
                                    studentsCloud1v4.get(indexStudentsAssets).setImage(rightColor);
                                    studentsCloud1v4.get(indexStudentsAssets).setVisible(true);
                                }
                                break;
                            case 1:
                                if (numPlayers == 3) {
                                    studentsCloud2v3.get(indexStudentsAssets).setImage(rightColor);
                                    studentsCloud2v3.get(indexStudentsAssets).setVisible(true);
                                } else {
                                    studentsCloud2v4.get(indexStudentsAssets).setImage(rightColor);
                                    studentsCloud2v4.get(indexStudentsAssets).setVisible(true);
                                }
                                break;
                            case 2:
                                if (numPlayers == 3) {
                                    studentsCloud3v3.get(indexStudentsAssets).setImage(rightColor);
                                    studentsCloud3v3.get(indexStudentsAssets).setVisible(true);
                                } else {
                                    studentsCloud3v4.get(indexStudentsAssets).setImage(rightColor);
                                    studentsCloud3v4.get(indexStudentsAssets).setVisible(true);
                                }
                                break;
                            case 3:
                                studentsCloud4v4.get(indexStudentsAssets).setImage(rightColor);
                                studentsCloud4v4.get(indexStudentsAssets).setVisible(true);
                                break;
                        }
                        numSameColor++;
                        indexStudentsAssets++;
                    }
                }
            }
            //hides remaining students
            int studentsForEachCloud = 3;
            if (numPlayers == 3) studentsForEachCloud = 4;
            while (indexStudentsAssets < studentsForEachCloud) {
                switch (cloudIndex) {
                    case 0:
                        if (numPlayers == 3) studentsCloud1v3.get(indexStudentsAssets).setVisible(false);
                        else studentsCloud1v4.get(indexStudentsAssets).setVisible(false);
                        break;
                    case 1:
                        if (numPlayers == 3) studentsCloud2v3.get(indexStudentsAssets).setVisible(false);
                        else studentsCloud2v4.get(indexStudentsAssets).setVisible(false);
                        break;
                    case 2:
                        if (numPlayers == 3) studentsCloud3v3.get(indexStudentsAssets).setVisible(false);
                        else studentsCloud3v4.get(indexStudentsAssets).setVisible(false);
                        break;
                    case 3:
                        studentsCloud4v4.get(indexStudentsAssets).setVisible(false);
                        break;
                }
                indexStudentsAssets++;
            }
        }
    }

    private void reloadTowers() {
        try {
            StrippedBoard b = GUI.client.getLocalModel().getBoardOf(currentBoardView);
            int numberOfTowers = b.getNumberOfTowers();
            int i = 0;
            Image towerColor;
            switch (b.getColorsTowers()) {
                case WHITE:
                    towerColor = whiteTowerImg;
                    break;
                case GREY:
                    towerColor = greyTowerImg;
                    break;
                default:
                    towerColor = blackTowerImg;
                    break;
            }
            for (ImageView t : towersImgs) {
                if (i < numberOfTowers) {
                    t.setVisible(true);
                    t.setImage(towerColor);
                } else {
                    t.setVisible(false);
                }
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.ERROR_LOCALMODEL);
        }
    }

    private void reloadProfs() {
        try {
            ArrayList<Colors> table = GUI.client.getLocalModel().getBoardOf(currentBoardView).getProfessorsTable();
            blueProf.setVisible(false);
            redProf.setVisible(false);
            pinkProf.setVisible(false);
            yellowProf.setVisible(false);
            greenProf.setVisible(false);
            for (Colors c : table) {
                if (c.equals(Colors.BLUE)) blueProf.setVisible(true);
                if (c.equals(Colors.RED)) redProf.setVisible(true);
                if (c.equals(Colors.PINK)) pinkProf.setVisible(true);
                if (c.equals(Colors.YELLOW)) yellowProf.setVisible(true);
                if (c.equals(Colors.GREEN)) greenProf.setVisible(true);
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.ERROR_LOCALMODEL);
        }
    }

    public void reloadDining() {
        try {
            EnumMap<Colors, Integer> dining = GUI.client.getLocalModel().getBoardOf(currentBoardView).getDining();
            for (Colors c : dining.keySet()) {
                int i = 0;
                while (i < 10) {
                    boolean visible = i < dining.get(c);
                    switch (c) {
                        case BLUE:
                            blueDiningImgs.get(i).setVisible(visible);
                            break;
                        case PINK:
                            pinkDiningImgs.get(i).setVisible(visible);
                            break;
                        case YELLOW:
                            yellowDiningImgs.get(i).setVisible(visible);
                            break;
                        case GREEN:
                            greenDiningImgs.get(i).setVisible(visible);
                            break;
                        case RED:
                            redDiningImgs.get(i).setVisible(visible);
                            break;
                    }
                    i++;
                }
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.ERROR_LOCALMODEL);
        }
    }

    private void initializeImagesEntrance() {
        entranceStudentsImgs = new ArrayList<>();
        for (Node student : entrance.getChildren()) {
            entranceStudentsImgs.add((ImageView) student);
        }
    }

    private void initializeClouds() {
        int numOfPlayers = GUI.client.getLocalPlayerList().size();
        studentsCloud1v3 = new ArrayList<>();
        studentsCloud1v4 = new ArrayList<>();
        studentsCloud2v3 = new ArrayList<>();
        studentsCloud2v4 = new ArrayList<>();
        studentsCloud3v3 = new ArrayList<>();
        studentsCloud3v4 = new ArrayList<>();

        if (numOfPlayers == 3) {
            try {
                cloud1.setImage(new Image(Files.newInputStream(Paths.get(ResourcesPath.CLOUD_1))));
                cloud2.setImage(new Image(Files.newInputStream(Paths.get(ResourcesPath.CLOUD_2))));
                cloud3.setImage(new Image(Files.newInputStream(Paths.get(ResourcesPath.CLOUD_3))));
            } catch (IOException e) {
                throw new RuntimeException(e); //todo
            }
            cloud4.setVisible(false);
        } else if (numOfPlayers == 2) {
            cloud3.setVisible(false);
            cloud4.setVisible(false);
        }
        //cloud 1 students
        for (Node s : cloud1v3.getChildren()) {
            studentsCloud1v3.add((ImageView) s);
        }
        for (Node s : cloud1v4.getChildren()) {
            studentsCloud1v4.add((ImageView) s);
        }

        //cloud 2 students
        for (Node s : cloud2v3.getChildren()) {
            studentsCloud2v3.add((ImageView) s);
        }
        for (Node s : cloud2v4.getChildren()) {
            studentsCloud2v4.add((ImageView) s);
        }

        //cloud 3 students
        for (Node s : cloud3v3.getChildren()) {
            studentsCloud3v3.add((ImageView) s);
        }
        for (Node s : cloud3v4.getChildren()) {
            studentsCloud3v4.add((ImageView) s);
        }

        //cloud 4 students
        studentsCloud4v4 = new ArrayList<>();
        for (Node s : cloud4v4.getChildren()) {
            studentsCloud4v4.add((ImageView) s);
        }

        hideStudentsClouds(numOfPlayers);
    }

    private void hideStudentsClouds(int numOfPlayers) {
        ArrayList<ArrayList<ImageView>> studentsToHide = new ArrayList<>();
        switch (numOfPlayers) {
            case 3:
                studentsToHide.add(studentsCloud1v4);
                studentsToHide.add(studentsCloud2v4);
                studentsToHide.add(studentsCloud3v4);
                studentsToHide.add(studentsCloud4v4);
                break;
            case 4:
                studentsToHide.add(studentsCloud1v3);
                studentsToHide.add(studentsCloud2v3);
                studentsToHide.add(studentsCloud3v3);
                break;
            case 2:
                studentsToHide.add(studentsCloud1v3);
                studentsToHide.add(studentsCloud2v3);
                studentsToHide.add(studentsCloud3v3);
                studentsToHide.add(studentsCloud3v4);
                studentsToHide.add(studentsCloud4v4);
                break;
        }
        for (ArrayList<ImageView> array : studentsToHide) {
            for (ImageView student : array) {
                student.setVisible(false);
            }
        }
    }

    private void initializeImagesTowers() {
        towersImgs = new ArrayList<>();
        for (Node tower : towers.getChildren()) {
            towersImgs.add((ImageView) tower);
        }
    }

    private void initializeImagesDining() {
        blueDiningImgs = new ArrayList<>();
        for (Node s : blueDining.getChildren()) {
            blueDiningImgs.add((ImageView) s);
        }

        pinkDiningImgs = new ArrayList<>();
        for (Node s : pinkDining.getChildren()) {
            pinkDiningImgs.add((ImageView) s);
        }

        greenDiningImgs = new ArrayList<>();
        for (Node s : greenDining.getChildren()) {
            greenDiningImgs.add((ImageView) s);
        }

        yellowDiningImgs = new ArrayList<>();
        for (Node s : yellowDining.getChildren()) {
            yellowDiningImgs.add((ImageView) s);
        }

        redDiningImgs = new ArrayList<>();
        for (Node s : redDining.getChildren()) {
            redDiningImgs.add((ImageView) s);
        }
    }

    private void loadAssistantDeck() {
        assistantDeck.setOnMouseClicked(mouseEvent -> {
            Scene scene;
            try {
                String filePath = ResourcesPath.FXML_FILE_PATH + "SelectAssistantView" + ResourcesPath.FILE_EXTENSION;
                FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                loader.setController(new AssistantCardController(gui));

                scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle(StringNames.TITLE);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<MenuItem> itemBoardViewArray; //the menu items necessary to change the view

    private ArrayList<ImageView> entranceStudentsImgs;

    private ArrayList<ImageView> towersImgs;

    //image assets import
    private Image blackTowerImg, whiteTowerImg, greyTowerImg;

    private Image blueStudentImg, greenStudentImg, pinkStudentImg,
            redStudentImg, yellowStudentImg;

    private ArrayList<ImageView> blueDiningImgs, redDiningImgs, yellowDiningImgs,
            greenDiningImgs, pinkDiningImgs;

    private ArrayList<ImageView> studentsCloud1v3, studentsCloud1v4,
            studentsCloud2v3, studentsCloud2v4, studentsCloud3v3,
            studentsCloud3v4, studentsCloud4v4;

    private Image island0, island1, island2;

    private Image motherNature;

    // assets on screen
    @FXML
    private Menu changeViewBoard;

    @FXML
    private MenuItem leaveGame;
    @FXML
    private ImageView redProf, yellowProf,
            greenProf, pinkProf, blueProf;

    @FXML
    private ImageView cloud1, cloud2, cloud3, cloud4;

    @FXML
    private StackPane cloud1v3, cloud1v4, cloud2v3,
            cloud2v4, cloud3v3, cloud3v4, cloud4v4;

    @FXML
    private StackPane board;

    @FXML
    private StackPane entrance;

    @FXML
    private StackPane towers;

    @FXML
    private StackPane yellowDining, redDining, greenDining,
            blueDining, pinkDining;

    @FXML
    private StackPane IslandsPane;

    @FXML
    private StackPane bag;

    @FXML
    private ImageView assistantDeck;
}
