package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Amrit
 */
public class GameOverController extends InitialStage implements Controller {

    protected static AtomicBoolean opened = new AtomicBoolean(false);

    @FXML
    private HBox expertBox;
    @FXML
    private GridPane playersList;
    @FXML
    private Text roomTitle;
    @FXML
    private Button startGameButton, leaveButton;

    private Image blackTowerImage, whiteTowerImage, greyTowerImage;

    private SwitchButton setExpertMode;
    private Label expertMode;

    public GameOverController(GUI gui) {
        super(gui);
    }

    public static void setOpened(boolean b) {
        opened.set(false);
    }

    String winnerTeam = null;
    String leavingPlayer = null;
    @FXML
    Text winnerDeclare;

    @FXML
    Button exitButton;

    @FXML
    public void initialize() {
        opened.set(true);
        if(winnerTeam==null){
            if(leavingPlayer!=null)winnerDeclare.setText("Game interrupted by "+ leavingPlayer + " who leaved the game!");
            else winnerDeclare.setText("There is a tie!");
        }
        else{
            winnerDeclare.setText("Team " + winnerTeam + " won! Congratulations!");
        }

        exitButton.setOnAction((event) -> {
            GUI.client.leaveGameOverScreenGUI();
        });
    }

    public void setWinner(String winner){
        winnerTeam = winner;
    }

    public void setLeavingPlayer(String leavingPlayer){
        this.leavingPlayer = leavingPlayer;
    }

}