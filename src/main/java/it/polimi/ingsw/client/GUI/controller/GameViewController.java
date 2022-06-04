package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    protected static AtomicBoolean opened = new AtomicBoolean(false);

    //image assets import
    private Image blackTowerImg ;
    private Image whiteTowerImg ;
    private Image greyTowerImg ;
    private Image blueStudentImg ;
    private Image greenStudentImg ;
    private Image pinkStudentImg ;
    private Image redStudentImg ;
    private Image yellowStudentImg ;
    private Image blueProfImg ;
    private Image greenProfImg ;
    private Image pinkProfImg ;
    private Image redProfImg;
    private Image yellowProfImg;


    public GameViewController(GUI gui) {
        super(gui);
        opened.set(true);

        try{
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
        }
        catch(IOException io){
            System.out.println("Error importing img assets in GameViewController");
        }
    }

        @FXML
        public void initialize () {
            opened.set(true);
        }

        public void setPlayersViewMenu (ArrayList < String > players) {
            List<MenuItem> items = changeViewBoard.getItems();
            int indexItem = 0;
            while (indexItem < 4) {
                if (indexItem > players.size() - 1) {
                    items.get(indexItem).setVisible(false);
                    indexItem++;
                } else {
                    items.get(indexItem).setText(players.get(indexItem));
                    indexItem++;
                }
            }
        }

        public void setCurrentPlayer (String currentPlayer){
            this.currentViewPlayer.setText("Current player: " + currentPlayer);
        }

        public boolean isOpened () {
            return opened.get();
        }
    }
