package it.polimi.ingsw.view.GUI.controller;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.LocalModelNotLoadedException;
import it.polimi.ingsw.model.enumerations.Colors;
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

    // assets on screen
    @FXML
    private ArrayList<ImageView> entranceStudentsImgs;
    @FXML
    private ImageView studentEntrance1;
    @FXML
    private ImageView studentEntrance2;
    @FXML
    private ImageView studentEntrance3;
    @FXML
    private ImageView studentEntrance4;
    @FXML
    private ImageView studentEntrance5;
    @FXML
    private ImageView studentEntrance6;
    @FXML
    private ImageView studentEntrance7;
    @FXML
    private ImageView studentEntrance8;
    @FXML
    private ImageView studentEntrance9;

    public GameViewController(GUI gui) {
        super(gui);
        opened.set(false);
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
        if (!opened.get()) { //first openining
            opened.set(true);
            setPlayersViewMenu(GUI.client.getLocalPlayerList());
            firstRefreshBoard();
        }

        setCurrentPlayer(GUI.client.getLocalModel().getCurrentPlayer()); //todo before I could refresh the current player from the client/local model ... now there is a bug to find

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

}
