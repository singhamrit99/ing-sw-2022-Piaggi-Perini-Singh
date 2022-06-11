package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

public class AssistantCardController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton;

    public AssistantCardController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize() {
        //prendo e incorpo il selector
        ArrayList<AssistantCardDeck> cards = GUI.client.getLocalModel().getAssistantDecks();

        //cambio l immagine in base al selector
        //conferma

        cancelButton.setOnAction((event) -> {
            Window window = ((Node)(event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}
