package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.network.server.commands.DrawFromBagCommand;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.network.server.stripped.StrippedIsland;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameViewController extends InitialStage implements Controller {
    protected static AtomicBoolean opened = new AtomicBoolean(false);
    private String currentBoardView; //the owner of the board current visible on the screen
    ArrayList<StackPane> islandsPanes = new ArrayList<>();

    private final DropShadow dropShadowIslandStuff;
    private List<MenuItem> itemBoardViewArray; //the menu items necessary to change the view

    private ArrayList<ImageView> entranceStudentsImgs, towersImgs, charactersCards;

    //image assets import
    private Image blackTowerImg, whiteTowerImg, greyTowerImg, blueStudentImg, greenStudentImg, pinkStudentImg, redStudentImg, yellowStudentImg;

    private ArrayList<ImageView> blueDiningImgs, redDiningImgs, yellowDiningImgs, greenDiningImgs, pinkDiningImgs;

    private ArrayList<ImageView> studentsCloud1v3, studentsCloud1v4, studentsCloud2v3, studentsCloud2v4, studentsCloud3v3, studentsCloud3v4, studentsCloud4v4;

    private Image island0, island1, island2, motherNature, noEntry;

    private ArrayList<Image> islandsImgs;

    // assets on screen
    @FXML
    private Menu changeViewBoard, currentPlayer;

    @FXML
    private MenuItem leaveGame;

    @FXML
    private ImageView redProf, yellowProf, greenProf, pinkProf, blueProf, cloud1, cloud2, cloud3, cloud4,
            assistantDeck, character1, character2, character3;

    @FXML
    private StackPane cloud1v3, cloud1v4, cloud2v3, cloud2v4, cloud3v3, cloud3v4, cloud4v4,
            yellowDining, redDining, greenDining, blueDining, pinkDining,
            towers, islandsPane, bag, entrance, board;

    @FXML
    private VBox cloudv1, cloudv2, character1Vbox, character2Vbox, character3Vbox;

    @FXML
    private HBox characters;

    @FXML
    Label coinsText;

    @FXML
    StackPane coinsIndicator;

    /**
     * The constructor class for GameVieController, which imports all assets from resource folder.
     *
     * @param gui The GUI that needs to display all of this information.
     */
    public GameViewController(GUI gui) {
        super(gui);
        opened.set(false);

        //DropShadow effect for islands students, MN, towers
        dropShadowIslandStuff = new DropShadow();
        dropShadowIslandStuff.setBlurType(BlurType.ONE_PASS_BOX);
        dropShadowIslandStuff.setHeight(40);
        dropShadowIslandStuff.setWidth(40);
        dropShadowIslandStuff.setSpread(0.3);
        dropShadowIslandStuff.setColor(Color.rgb(0, 0, 0, 0.6f));
    }

    /**
     * Setter method to tell whether the view is open or not
     *
     * @param b boolean value
     */
    public static void setOpened(boolean b) {
        opened.set(b);
    }

    @FXML
    public void initialize() {
        //importing all assets from resource folder
        blackTowerImg = new Image(ResourcesPath.BLACK_TOWER);
        whiteTowerImg = new Image(ResourcesPath.WHITE_TOWER);
        greyTowerImg = new Image(ResourcesPath.GREY_TOWER);
        blueStudentImg = new Image(ResourcesPath.BLUE_STUDENT);
        greenStudentImg = new Image(ResourcesPath.GREEN_STUDENT);
        pinkStudentImg = new Image(ResourcesPath.PINK_STUDENT);
        redStudentImg = new Image(ResourcesPath.RED_STUDENT);
        yellowStudentImg = new Image(ResourcesPath.YELLOW_STUDENT);
        island0 = new Image(ResourcesPath.ISLAND_0);
        island1 = new Image(ResourcesPath.ISLAND_1);
        island2 = new Image(ResourcesPath.ISLAND_2);
        motherNature = new Image(ResourcesPath.MN);
        noEntry = new Image(ResourcesPath.NOENTRYTILE);

        if (!opened.get()) { //first opening
            opened.set(true);
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
                GUI.client.leaveGame();
                opened.set(false);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_IN_ROOM);
            }
        });
    }

    public void setCurrentPlayer(String currentPlayer) {
        int maxLength = 28;
        int panning = maxLength - currentPlayer.length();
        String panningString = "";
        for (int i = 0; i < panning / 2; i++) {
            panningString += ' ';
        }
        this.currentPlayer.setText("[ Current Player:" + panningString + currentPlayer + panningString + "]");
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

        //initialize name current player
        setCurrentPlayer(GUI.client.getLocalPlayerList().get(0));
    }

    private void firstRefreshBoard() {
        currentBoardView = GUI.client.getNickname();
        initializeImagesEntrance();
        initializeImagesTowers();
        initializeImagesDining();
        initializeClouds();
        loadAssistantDeck();
        reloadCoins();
        reloadCharacters();
        reloadEntrance();
        reloadTowers();
        reloadProfs();
        reloadDining();
        reloadClouds();
        reloadIslands();
        reloadBag();
    }

    private void reloadBag() {
        bag.setOnMouseClicked(mouseEvent -> {
            if (GUI.client.getLocalModel().getState().equals(State.PLANNINGPHASE) && GUI.client.getLocalModel().getCurrentPlayer().equals(GUI.client.getNickname())) {
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
                    Controller.showErrorDialogBox(StringNames.REMOTE);
                } catch (UserNotInRoomException e) {
                    Controller.showErrorDialogBox(StringNames.USER_NOT_IN_ROOM);
                } catch (IncorrectArgumentException e) {
                    Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
                } catch (UserNotRegisteredException e) {
                    Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                } catch (FullDiningException e) {
                    Controller.showErrorDialogBox(StringNames.FULL_DINING);
                } catch (CardPlayedInTurnException e) {
                    Controller.showErrorDialogBox(StringNames.CARD_PLAYED_IN_TURN);
                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                    Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                }
            }
        });
    }

    public void reloadCharacters() {
        charactersCards = new ArrayList<>();
        charactersCards.add(character1);
        charactersCards.add(character2);
        charactersCards.add(character3);

        ArrayList<StrippedCharacter> characterCardsStripped = GUI.client.getLocalModel().getCharacters();

        boolean expert = false;
        try {
            expert = GUI.client.getExpertMode();
        } catch (RemoteException | RoomNotExistsException e) {
            Controller.showErrorDialogBox(StringNames.REMOTE);
        }

        //default state is with students hidden
        ArrayList<VBox> vBoxesCharacters = new ArrayList<>();
        vBoxesCharacters.add(character1Vbox);
        vBoxesCharacters.add(character2Vbox);
        vBoxesCharacters.add(character3Vbox);
        for (VBox box : vBoxesCharacters) {
            for (Node student : box.getChildren()) {
                student.setVisible(false);
            }
        }

        //hide characters card in case of not expert mode
        if (!expert) for (int i = 0; i < 3; i++) charactersCards.get(i).setVisible(false);

        if (expert) {
            int indexCharacter = 0;
            for (StrippedCharacter c : characterCardsStripped) {
                Image character = new Image(getClass().getResourceAsStream(ResourcesPath.CHARACTERS
                        + c.getCharacterID() + ResourcesPath.IMAGE_EXTENSION_CHAR));
                charactersCards.get(indexCharacter).setImage(character);

                //if there are students ..
                if (c.getStudents() != null) {
                    int i = 0;
                    for (Colors color : c.getStudents().keySet()) {
                        if (c.getStudents().get(color) > 0) {
                            int y = c.getStudents().get(color);
                            while (y > 0) {
                                ImageView student = (ImageView) vBoxesCharacters.get(indexCharacter).getChildren().get(i);
                                student.setImage(studentImgFromColor(color));
                                student.setVisible(true);
                                i++;
                                y--;
                            }
                        }
                    }
                }

                //if there are no entry tiles, I replace the students IMG with NoEntry tiles
                if (c.getNoEntryTiles() > 0) {
                    int i = 0;
                    while (i < c.getNoEntryTiles()) {
                        ImageView NoEntryTile = (ImageView) vBoxesCharacters.get(indexCharacter).getChildren().get(i);
                        NoEntryTile.setImage(noEntry);
                        NoEntryTile.setVisible(true);
                        i++;
                    }
                }
                indexCharacter++;
            }
        }

        characters.setOnMouseClicked(mouseEvent -> {
            if ((GUI.client.getLocalModel().getState().equals(State.ACTIONPHASE_1) || GUI.client.getLocalModel().getState().equals(State.ACTIONPHASE_2) || GUI.client.getLocalModel().getState().equals(State.ACTIONPHASE_3)) && GUI.client.getLocalModel().getCurrentPlayer().equals(GUI.client.getNickname())) {
                String filePath = ResourcesPath.FXML_FILE_PATH + "SelectCharacterView" + ResourcesPath.FILE_EXTENSION;
                FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                loader.setController(new CharacterCardController(gui));
                try {
                    Controller.loadScene(loader);
                } catch (IOException e) {
                    Controller.showErrorDialogBox(StringNames.ERROR_IO);
                }
            }
        });
    }

    public void reloadCoins() {
        boolean expert = false;
        try {
            expert = GUI.client.getExpertMode();
        } catch (RoomNotExistsException ignored) {
        } //impossible in this case
        catch (RemoteException e) {
            Controller.showErrorDialogBox(StringNames.REMOTE);
        }

        if (expert) {
            coinsIndicator.setVisible(true);
            try {
                int coins = GUI.client.getLocalModel().getBoardOf(currentBoardView).getCoins();

                coinsText.setText(Integer.toString(coins));
            } catch (LocalModelNotLoadedException e) {
                Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
            }
        } else {
            coinsIndicator.setVisible(false);
        }
    }

    public void reloadIslands() {
        islandsPanes = new ArrayList<>();
        islandsPane.getChildren().clear();
        islandsPane.maxHeight(450);
        islandsPane.maxWidth(1000);
        islandsPane.setAlignment(Pos.TOP_CENTER);
        GridPane Islands = new GridPane();
        Islands.setGridLinesVisible(false);
        islandsPane.getChildren().add(Islands);
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

        //clickEvent
        islandsPane.setOnMouseClicked((event) -> {
            if (GUI.client.getLocalModel().getState().equals(State.ACTIONPHASE_2) && GUI.client.getLocalModel().getCurrentPlayer().equals(GUI.client.getNickname())) {
                try {
                    String filePath = ResourcesPath.FXML_FILE_PATH + "MoveMotherNatureView" + ResourcesPath.FILE_EXTENSION;
                    FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new MoveMotherNatureController(gui));
                    Controller.loadScene(loader);
                } catch (IOException e) {
                    Controller.showErrorDialogBox(StringNames.ERROR_IO);
                }
            }
        });

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
                    initTowersPane(island, islandHbox, islandsBackEnd.get(indexIsland));
                    GridPane studentsPane = initStudentsPane(island, islandHbox, islandsBackEnd.get(indexIsland));
                    if (islandsBackEnd.get(indexIsland).hasNoEnterTile()) {
                        spawnNoEntryTile(island);
                        if (islandsBackEnd.get(indexIsland).hasMotherNature()) spawnMNIsland(island);
                    } else if (islandsBackEnd.get(indexIsland).hasMotherNature()) spawnMNIsland(studentsPane);
                    Islands.addRow(0, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
                indexIsland++;
            }
        }

        //SECOND LINE
        for (int i = 0; i < 6; i++) {
            if (i != 0 && i != 5) {
                Islands.addRow(1, new Text("")); //empty cell
            } else { //for island 11 and 4
                int realIndex = 11;  //in case of i =0;
                if (i != 0) realIndex = 4;
                if (islandsBackEnd.get(realIndex).getName().equals("EMPTY")) {
                    Islands.addRow(1, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(1);
                    HBox islandHbox = new HBox();
                    initTowersPane(island, islandHbox, islandsBackEnd.get(realIndex));
                    GridPane studentsPane = initStudentsPane(island, islandHbox, islandsBackEnd.get(realIndex));
                    if (islandsBackEnd.get(realIndex).hasNoEnterTile()) {
                        spawnNoEntryTile(island);
                        if (islandsBackEnd.get(realIndex).hasMotherNature()) spawnMNIsland(island);
                    } else if (islandsBackEnd.get(realIndex).hasMotherNature()) spawnMNIsland(studentsPane);
                    Islands.addRow(1, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            }
        }


        for (int i = 0; i < 6; i++) {
            if (i != 0 && i != 5) {
                Islands.addRow(2, new Text("")); //empty cell
            } else { //for island 10 and 5
                int realIndex = i;  //in case of i=5 -> 5;
                if (i != 5) realIndex = 10; //in case of i=0 -> 10
                if (islandsBackEnd.get(realIndex).getName().equals("EMPTY")) {
                    Islands.addRow(2, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(1);
                    HBox islandHbox = new HBox();
                    initTowersPane(island, islandHbox, islandsBackEnd.get(realIndex));
                    GridPane studentsPane = initStudentsPane(island, islandHbox, islandsBackEnd.get(realIndex));
                    if (islandsBackEnd.get(realIndex).hasNoEnterTile()) {
                        spawnNoEntryTile(island);
                        if (islandsBackEnd.get(realIndex).hasMotherNature()) spawnMNIsland(island);
                    } else if (islandsBackEnd.get(realIndex).hasMotherNature()) spawnMNIsland(studentsPane);
                    Islands.addRow(2, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
            }
        }

        //LAST LINE OF ISLANDS IN THE GRID
        indexIsland = 9;
        for (int i = 0; i < 6; i++) {
            if (i == 0 || i == 5) {
                Islands.addRow(3, new Text("")); //empty cell
            } else {
                if (islandsBackEnd.get(indexIsland).getName().equals("EMPTY")) {
                    Islands.addRow(3, new Text("")); //empty cell alignment
                } else {
                    StackPane island = initIsland(i % 3);
                    HBox islandHbox = new HBox();
                    initTowersPane(island, islandHbox, islandsBackEnd.get(indexIsland));
                    GridPane studentsPane = initStudentsPane(island, islandHbox, islandsBackEnd.get(indexIsland));
                    if (islandsBackEnd.get(indexIsland).hasNoEnterTile()) {
                        spawnNoEntryTile(island);
                        if (islandsBackEnd.get(indexIsland).hasMotherNature()) spawnMNIsland(island);
                    } else if (islandsBackEnd.get(indexIsland).hasMotherNature()) spawnMNIsland(studentsPane);
                    Islands.addRow(3, island);
                    Islands.getRowConstraints().add(row);
                    Islands.getColumnConstraints().add(column);
                }
                indexIsland--; //out from the if !!
            }
        }
    }


    private GridPane initStudentsPane(StackPane island, HBox islandHbox, StrippedIsland backendIsland) {
        GridPane studentsPane = initGridPaneIsland(island, islandHbox);
        ArrayList<ImageView> imgStudents = spawnStudentsIsland(backendIsland);
        for (int j = 0; j < imgStudents.size(); j++) {
            ImageView img = imgStudents.get(j);
            img.setFitWidth(20);
            img.setFitHeight(20);
            studentsPane.addRow(j / 2, img);
        }
        return studentsPane;
    }

    private void initTowersPane(StackPane island, HBox islandHbox, StrippedIsland backendIsland) {
        GridPane towersPane = initGridPaneIsland(island, islandHbox);
        ArrayList<ImageView> towers = spawnTowersIsland(backendIsland);
        for (int x = 0; x < towers.size(); x++) {
            towers.get(x).setFitHeight(25);
            towers.get(x).setFitWidth(25);
            towersPane.addRow(x / 2, towers.get(x));
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

    private void spawnMNIsland(GridPane pane) {
        ImageView mn = new ImageView(motherNature);
        mn.setEffect(dropShadowIslandStuff);
        mn.setFitHeight(40);
        mn.setFitWidth(40);
        mn.maxWidth(40);
        mn.maxHeight(40);
        pane.addRow(0, mn);
    }

    private void spawnMNIsland(Pane pane) {
        ImageView mn = new ImageView(motherNature);
        mn.setEffect(dropShadowIslandStuff);
        mn.setFitHeight(40);
        mn.setFitWidth(40);
        mn.maxWidth(40);
        mn.maxHeight(40);
        pane.getChildren().add(mn);
    }

    private void spawnNoEntryTile(StackPane pane) {
        ImageView noEntryView = new ImageView(noEntry);
        noEntryView.setFitHeight(150);
        noEntryView.setFitWidth(150);
        noEntryView.maxWidth(150);
        noEntryView.maxHeight(150);
        noEntryView.setEffect(dropShadowIslandStuff);
        pane.getChildren().add(noEntryView);
    }

    private ArrayList<ImageView> spawnStudentsIsland(StrippedIsland island) {
        ArrayList<ImageView> imagesToReturn = new ArrayList<>();
        EnumMap<Colors, Integer> students = island.getStudents();
        for (Colors c : students.keySet()) {
            if (students.get(c) != 0) {
                int i = 0;
                Image rightColor = studentImgFromColor(c);
                while (i < students.get(c)) {
                    ImageView student = new ImageView(rightColor);
                    student.setEffect(dropShadowIslandStuff);
                    imagesToReturn.add(student);
                    i++;
                }
            }
        }
        return imagesToReturn;
    }

    private ArrayList<ImageView> spawnTowersIsland(StrippedIsland island) {
        ArrayList<ImageView> towersToReturn = new ArrayList<>();
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


        for (int i = 0; i < island.getNumOfTowers(); i++) {
            ImageView towerImage = new ImageView(rightColor);
            towerImage.setEffect(dropShadowIslandStuff);
            towersToReturn.add(towerImage);
        }

        return towersToReturn;
    }

    public void reloadEntrance() {
        board.setOnMouseClicked((event) -> {
            if (GUI.client.getLocalModel().getState().equals(State.ACTIONPHASE_1) && GUI.client.getLocalModel().getCurrentPlayer().equals(GUI.client.getNickname())) {
                try {
                    String filePath = ResourcesPath.FXML_FILE_PATH + "MoveStudentsView" + ResourcesPath.FILE_EXTENSION;
                    FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new MoveStudentsController(gui));
                    Controller.loadScene(loader);
                } catch (IOException e) {
                    Controller.showErrorDialogBox(StringNames.ERROR_IO);
                }
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
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
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

        if (GUI.client.getLocalPlayerList().size() > 2) {
            cloudv1.setOnMouseClicked(event -> pickCloud());
        }
        cloudv2.setOnMouseClicked(event -> pickCloud());
    }

    private void pickCloud() {
        if (GUI.client.getLocalModel().getState().equals(State.ACTIONPHASE_3) && GUI.client.getLocalModel().getCurrentPlayer().equals(GUI.client.getNickname())) {
            try {
                String filePath = ResourcesPath.FXML_FILE_PATH + "TakeStudentsFromCloudView" + ResourcesPath.FILE_EXTENSION;
                FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                loader.setController(new TakeFromCloudTilesController(gui));

                Controller.loadScene(loader);
            } catch (IOException e) {
                Controller.showErrorDialogBox(StringNames.ERROR_IO);
            }
        }
    }

    public void reloadTowers() {
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
                i++;
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
        }
    }

    public void reloadProfs() {
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
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
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
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
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
            cloud1.setImage(new Image(ResourcesPath.CLOUD_1));
            cloud2.setImage(new Image(ResourcesPath.CLOUD_2));
            cloud3.setImage(new Image(ResourcesPath.CLOUD_3));
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
            if (GUI.client.getLocalModel().getState().equals(State.PLANNINGPHASE) && GUI.client.getLocalModel().getCurrentPlayer().equals(GUI.client.getNickname())) {
                try {
                    String filePath = ResourcesPath.FXML_FILE_PATH + "SelectAssistantView" + ResourcesPath.FILE_EXTENSION;
                    FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new AssistantCardController(gui));

                    Controller.loadScene(loader);
                } catch (IOException e) {
                    Controller.showErrorDialogBox(StringNames.ERROR_IO);
                }
            }
        });
    }
}
