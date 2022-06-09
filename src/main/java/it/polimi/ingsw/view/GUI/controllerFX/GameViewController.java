package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.LocalModelNotLoadedException;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.controllerFX.Controller;
import it.polimi.ingsw.view.GUI.controllerFX.InitialStage;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
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
    private ArrayList<ImageView> studentsCloud1v3;
    private ArrayList<ImageView> studentsCloud1v4;
    private ArrayList<ImageView> studentsCloud2v3;
    private ArrayList<ImageView> studentsCloud2v4;
    private ArrayList<ImageView> studentsCloud3v3;
    private ArrayList<ImageView> studentsCloud3v4;
    private ArrayList<ImageView> studentsCloud4v4;

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

    @FXML
    StackPane prova;

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

    private void reloadClouds() {
        ArrayList<StrippedCloud> clouds = GUI.client.getLocalModel().getClouds();
        int numPlayers = GUI.client.getLocalPlayerList().size();
        ArrayList<ArrayList<ImageView>> clouds3p = new ArrayList<>();
        ArrayList<ArrayList<ImageView>> clouds4p = new ArrayList<>();
        if (numPlayers == 3) {
            clouds3p.add(studentsCloud1v3);
            clouds3p.add(studentsCloud2v3);
            clouds3p.add(studentsCloud3v3);
        } else {
            clouds4p.add(studentsCloud1v3);
            clouds4p.add(studentsCloud2v3);
            if (numPlayers == 4) {
                clouds4p.add(studentsCloud3v3);
                clouds4p.add(studentsCloud3v3);
            }
        }

        for (int cloudIndex = 0; cloudIndex < numPlayers; cloudIndex++) {
            int indexStudentsAssets = 0;
            EnumMap<Colors, Integer> students = clouds.get(cloudIndex).getStudents();
            for (Colors c : students.keySet()) {
                Image rightColor = studentImgFromColor(c);
                if (students.get(c) != 0){
                    if (numPlayers == 3) clouds3p.get(cloudIndex).get(indexStudentsAssets).setImage(rightColor);
                    else
                        clouds4p.get(cloudIndex).get(indexStudentsAssets).setImage(rightColor);
                    indexStudentsAssets++;
                }
            }

            //hides remaining students
            int studentsForEachCloud = 3;
            if(numPlayers==3)studentsForEachCloud=4;
            while(indexStudentsAssets<studentsForEachCloud-1){
                if (numPlayers == 3) clouds3p.get(cloudIndex).get(indexStudentsAssets).setVisible(false);
                else
                    clouds4p.get(cloudIndex).get(indexStudentsAssets).setVisible(false);
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

    private void reloadDining() {
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
    private ArrayList<ImageView> entranceStudentsImgs;
    private ArrayList<ImageView> towersImgs;
    @FXML
    private ImageView redProf, yellowProf,
            greenProf, pinkProf, blueProf;

    @FXML
    private ImageView cloud1, cloud2, cloud3, cloud4;

    @FXML
    private StackPane cloud1v3, cloud1v4, cloud2v3,
            cloud2v4, cloud3v3, cloud3v4, cloud4v4;

    @FXML
    StackPane entrance;

    @FXML
    StackPane towers;

    @FXML
    StackPane yellowDining, redDining, greenDining,
            blueDining, pinkDining;

}
