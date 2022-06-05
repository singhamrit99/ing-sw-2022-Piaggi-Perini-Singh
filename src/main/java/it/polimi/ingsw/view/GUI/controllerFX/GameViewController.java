package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.LocalModelNotLoadedException;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.controllerFX.Controller;
import it.polimi.ingsw.view.GUI.controllerFX.InitialStage;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Amrit
 */
public class GameViewController extends InitialStage implements Controller {
    @FXML
    private Menu changeViewBoard;
    @FXML
    private Menu currentViewPlayer;

    //image assets import
    private Image blackTowerImg;
    private Image whiteTowerImg;
    private Image greyTowerImg;
    private Image blueStudentImg;
    private Image greenStudentImg;
    private Image pinkStudentImg;
    private Image redStudentImg;
    private Image yellowStudentImg;

    private ArrayList<ImageView> blueDiningImgs;
    private ArrayList<ImageView> redDiningImgs;
    private ArrayList<ImageView> yellowDiningImgs;
    private ArrayList<ImageView> greenDiningImgs;
    private ArrayList<ImageView> pinkDiningImgs;

    private List<MenuItem> itemBoardViewArray; //the menu items necessary to change the view
    protected static AtomicBoolean opened = new AtomicBoolean(false);

    private String currentBoardView; //the owner of the board current visible on the screen


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
        } catch (IOException io) {
            System.out.println("Error importing img assets in GameViewController");
        }
    }

    @FXML
    public void initialize() {
        if (!opened.get()) { //first opening
            opened.set(true);
            setPlayersViewMenu(GUI.client.getLocalPlayerList());
            firstRefreshBoard();
        }

        if (itemBoardViewArray.size() > 0) {
            for (MenuItem boardView : itemBoardViewArray) {
                boardView.setOnAction((event) -> changeViewBoard(boardView.getText()));
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

    public void setCurrentPlayer(String currentPlayer) {
        this.currentViewPlayer.setText("Current player: " + currentPlayer);
    }

    public boolean isOpened() {
        return opened.get();
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
    }

    private void reloadEntrance() {
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
                    towersImgs.get(i).setVisible(true);
                    towersImgs.get(i).setImage(towerColor);
                } else {
                    towersImgs.get(i).setVisible(false);
                }
                i++;
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

    private void reloadDining(){
        try {
            EnumMap<Colors, Integer> dining = GUI.client.getLocalModel().getBoardOf(currentBoardView).getDining();
            for (Colors c : dining.keySet()) {
                int i = 0;
                while (i < 10) {
                    if (i < dining.get(c)) {
                        switch(c){
                            case BLUE:
                                blueDiningImgs.get(i).setVisible(true);
                            case PINK:
                                pinkDiningImgs.get(i).setVisible(true);
                            case YELLOW:
                                yellowDiningImgs.get(i).setVisible(true);
                            case GREEN:
                                greenDiningImgs.get(i).setVisible(true);
                            case RED:
                                redDiningImgs.get(i).setVisible(true);
                        }
                    } else {
                        switch (c) {
                            case BLUE:
                                blueDiningImgs.get(i).setVisible(false);
                            case PINK:
                                pinkDiningImgs.get(i).setVisible(false);
                            case YELLOW:
                                yellowDiningImgs.get(i).setVisible(false);
                            case GREEN:
                                greenDiningImgs.get(i).setVisible(false);
                            case RED:
                                redDiningImgs.get(i).setVisible(false);
                        }
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
        entranceStudentsImgs.add(studentEntrance1);
        entranceStudentsImgs.add(studentEntrance2);
        entranceStudentsImgs.add(studentEntrance3);
        entranceStudentsImgs.add(studentEntrance4);
        entranceStudentsImgs.add(studentEntrance5);
        entranceStudentsImgs.add(studentEntrance6);
        entranceStudentsImgs.add(studentEntrance7);
        entranceStudentsImgs.add(studentEntrance8);
        entranceStudentsImgs.add(studentEntrance9);
    }

    private void initializeImagesTowers() {
        towersImgs = new ArrayList<>();
        towersImgs.add(tower1);
        towersImgs.add(tower2);
        towersImgs.add(tower3);
        towersImgs.add(tower4);
        towersImgs.add(tower5);
        towersImgs.add(tower6);
        towersImgs.add(tower7);
        towersImgs.add(tower8);
    }

    private void initializeImagesDining() {
        blueDiningImgs = new ArrayList<>();
        blueDiningImgs.add(blueDining0);
        blueDiningImgs.add(blueDining1);
        blueDiningImgs.add(blueDining2);
        blueDiningImgs.add(blueDining3);
        blueDiningImgs.add(blueDining4);
        blueDiningImgs.add(blueDining5);
        blueDiningImgs.add(blueDining6);
        blueDiningImgs.add(blueDining7);
        blueDiningImgs.add(blueDining8);
        blueDiningImgs.add(blueDining9);

        pinkDiningImgs = new ArrayList<>();
        pinkDiningImgs.add(pinkDining0);
        pinkDiningImgs.add(pinkDining1);
        pinkDiningImgs.add(pinkDining2);
        pinkDiningImgs.add(pinkDining3);
        pinkDiningImgs.add(pinkDining4);
        pinkDiningImgs.add(pinkDining5);
        pinkDiningImgs.add(pinkDining6);
        pinkDiningImgs.add(pinkDining7);
        pinkDiningImgs.add(pinkDining8);
        pinkDiningImgs.add(pinkDining9);

        greenDiningImgs = new ArrayList<>();
        greenDiningImgs.add(greenDining0);
        greenDiningImgs.add(greenDining1);
        greenDiningImgs.add(greenDining2);
        greenDiningImgs.add(greenDining3);
        greenDiningImgs.add(greenDining4);
        greenDiningImgs.add(greenDining5);
        greenDiningImgs.add(greenDining6);
        greenDiningImgs.add(greenDining7);
        greenDiningImgs.add(greenDining8);
        greenDiningImgs.add(greenDining9);

        yellowDiningImgs = new ArrayList<>();
        yellowDiningImgs.add(yellowDining0);
        yellowDiningImgs.add(yellowDining1);
        yellowDiningImgs.add(yellowDining2);
        yellowDiningImgs.add(yellowDining3);
        yellowDiningImgs.add(yellowDining4);
        yellowDiningImgs.add(yellowDining5);
        yellowDiningImgs.add(yellowDining6);
        yellowDiningImgs.add(yellowDining7);
        yellowDiningImgs.add(yellowDining8);
        yellowDiningImgs.add(yellowDining9);

        redDiningImgs = new ArrayList<>();
        redDiningImgs.add(redDining0);
        redDiningImgs.add(redDining1);
        redDiningImgs.add(redDining2);
        redDiningImgs.add(redDining3);
        redDiningImgs.add(redDining4);
        redDiningImgs.add(redDining5);
        redDiningImgs.add(redDining6);
        redDiningImgs.add(redDining7);
        redDiningImgs.add(redDining8);
        redDiningImgs.add(redDining9);
    }
    private void setPlayersViewMenu(ArrayList<String> players) {
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

    // assets on screen
    @FXML
    private ArrayList<ImageView> entranceStudentsImgs;
    @FXML
    private ImageView studentEntrance1, studentEntrance2,
            studentEntrance3, studentEntrance4;
    @FXML
    private ImageView studentEntrance5, studentEntrance6,
            studentEntrance7, studentEntrance8, studentEntrance9;

    private ArrayList<ImageView> towersImgs;
    @FXML
    private ImageView tower1, tower2, tower3, tower4,
            tower5, tower6, tower7, tower8;

    @FXML
    private ImageView redProf, yellowProf,
            greenProf, pinkProf, blueProf;

    @FXML
    private ImageView blueDining0, blueDining1, blueDining2, blueDining3,
            blueDining4, blueDining5, blueDining6, blueDining7, blueDining8, blueDining9;
    @FXML
    private ImageView pinkDining0, pinkDining1, pinkDining2, pinkDining3,
            pinkDining4, pinkDining5, pinkDining6, pinkDining7, pinkDining8, pinkDining9;
    @FXML
    private ImageView greenDining0, greenDining1, greenDining2, greenDining3, greenDining4,
            greenDining5, greenDining6, greenDining7, greenDining8, greenDining9;

    @FXML
    private ImageView yellowDining0, yellowDining1, yellowDining2, yellowDining3, yellowDining4,
            yellowDining5, yellowDining6, yellowDining7, yellowDining8, yellowDining9;

    @FXML
    private ImageView redDining0, redDining1, redDining2, redDining3, redDining4,
            redDining5, redDining6, redDining7, redDining8, redDining9;

}
