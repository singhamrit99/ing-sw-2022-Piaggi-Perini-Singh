package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.StringNames;
import it.polimi.ingsw.exceptions.NotLeaderRoomException;
import it.polimi.ingsw.exceptions.RoomNotExistsException;
import it.polimi.ingsw.exceptions.UserNotInRoomException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Amrit
 */
public class GameViewController extends InitialStage implements Controller {
    private String currentViewPlayer;
    @FXML
    private Text roomTitle;
    @FXML
    private Menu changeView;

    @FXML
    private Image blackTowerImage;
    @FXML
    private Image whiteTowerImage;
    @FXML
    private Image greyTowerImage;

    public GameViewController(GUI gui) {
        super(gui);
        currentViewPlayer = GUI.client.getNickname();
    }

    @FXML
    public void initialize() {
        try {
            blackTowerImage = new Image(new FileInputStream(ResourcesPath.BLACK_TOWER));
            whiteTowerImage = new Image(new FileInputStream(ResourcesPath.WHITE_TOWER));
            greyTowerImage = new Image(new FileInputStream(ResourcesPath.GREY_TOWER));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setPlayersViewMenu(ArrayList<String> players) {
        List<MenuItem> items = changeView.getItems();
        int indexItem = 0;
        while (indexItem < 4) {
            if(indexItem>players.size()-1){
                items.get(indexItem).setVisible(false);
                indexItem++;
            }else{
                items.get(indexItem).setText(players.get(indexItem));
            }
        }}


}
