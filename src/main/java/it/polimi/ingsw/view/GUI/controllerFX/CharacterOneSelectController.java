package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.PlayCharacterCardB;
import it.polimi.ingsw.network.server.commands.PlayCharacterCardD;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.network.server.stripped.StrippedIsland;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterOneSelectController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Text description;

    @FXML
    private ChoiceBox<String> choiceBox;

    public CharacterOneSelectController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize() {
        StrippedCharacter selectedCharacter = GUI.client.getLocalModel().selectedCharacter;
        description.setText(selectedCharacter.getDescription());

        System.out.println("SELECTED CHARACTER ASDAS: " + selectedCharacter.getCharacterID());
        if (selectedCharacter.getCharacterID() == 3) {
            for (StrippedIsland island : GUI.client.getLocalModel().getIslands()) {
                choiceBox.getItems().add(island.getName());
            }
        } else {
            for (Colors color : Colors.values()) {
                choiceBox.getItems().add(color.toString());
            }
        }
        choiceBox.getSelectionModel().selectFirst();

        AtomicInteger chosen = new AtomicInteger();
        chosen.set(choiceBox.getSelectionModel().getSelectedIndex());

        choiceBox.setOnAction((event) -> {
            chosen.set(choiceBox.getSelectionModel().getSelectedIndex());
        });

        //conferma
        confirmButton.setOnAction((event) -> {
            PlayCharacterCardD playCharacterCardD = new PlayCharacterCardD(GUI.client.getNickname(), selectedCharacter.getCharacterID(), chosen.get());
            try {
                GUI.client.performGameAction(playCharacterCardD);
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
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            }

            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}