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
    private Text roomTitle;
    @FXML
    private Menu changeViewBoard;
    @FXML
    private Menu currentViewPlayer;

    private List<MenuItem> itemBoardViewArray; //the menu items necessary to change the view
    protected static AtomicBoolean opened = new AtomicBoolean(false);

    private String currentBoardView; //the owner of the board current visible on the screen

    //image assets import
    private Image blackTowerImg;
    private Image whiteTowerImg;
    private Image greyTowerImg;
    private Image blueStudentImg;
    private Image greenStudentImg;
    private Image pinkStudentImg;
    private Image redStudentImg;
    private Image yellowStudentImg;
    private Image blueProfImg;
    private Image greenProfImg;
    private Image pinkProfImg;
    private Image redProfImg;
    private Image yellowProfImg;




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
            blueProfImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.BLUE_PROF)));
            greenProfImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.GREEN_PROF)));
            pinkProfImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.PINK_PROF)));
            redProfImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.RED_PROF)));
            yellowProfImg = new Image(Files.newInputStream(Paths.get(ResourcesPath.YELLOW_PROF)));
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
            StrippedBoard  b = GUI.client.getLocalModel().getBoardOf(currentBoardView);
            int numberOfTowers = b.getNumberOfTowers();
            int i = 0;
            Image towerColor;
            switch (b.getColorsTowers()){
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
            for(ImageView t : towersImgs){
                if(i<numberOfTowers){
                    towersImgs.get(i).setVisible(true);
                    towersImgs.get(i).setImage(towerColor);
                }else{
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
                if(c.equals(Colors.BLUE))blueProf.setVisible(true);
                if(c.equals(Colors.RED))redProf.setVisible(true);
                if(c.equals(Colors.PINK))pinkProf.setVisible(true);
                if(c.equals(Colors.YELLOW))yellowProf.setVisible(true);
                if(c.equals(Colors.GREEN))greenProf.setVisible(true);
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.ERROR_LOCALMODEL);
        }
    }

    /*
    private void reloadDining(){
        try {
            EnumMap<Colors, Integer> entrance = GUI.client.getLocalModel().getBoardOf(currentBoardView).getDining();

            for (Colors c : entrance.keySet()) {
                int indexDining = 0;
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
                //hiding others position if there aren't
                while (indexEntranceAssets < entranceStudentsImgs.size()) {
                    entranceStudentsImgs.get(indexEntranceAssets).setVisible(false);
                    indexEntranceAssets++;
                }
            }

        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.ERROR_LOCALMODEL);
        }
    }
    */


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

    private void initializeImagesTowers(){
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
    private ImageView tower1,tower2,tower3m,tower4,
            tower5,tower6,tower7,tower8;

    @FXML
    private ImageView redProf, yellowProf,
            greenProf, pinkProf,blueProf;

    @FXML
    private ImageView blueDining0,blueDining1,blueDining2,blueDining3,
            blueDining4,blueDining5,blueDining6,blueDining7,blueDining8,blueDining9;
    @FXML
    private ImageView pinkDining0,pinkDining1,pinkDining2,pinkDining3,
            pinkDining4,pinkDining5,pinkDining6,pinkDining7,pinkDining8,pinkDining9;
    @FXML
    private ImageView greenDining0,greenDining1,greenDining2,greenDining3,getGreenDining4,
    greenDining5,greenDining6,greenDining7,greenDining8, greenDining9;


}
