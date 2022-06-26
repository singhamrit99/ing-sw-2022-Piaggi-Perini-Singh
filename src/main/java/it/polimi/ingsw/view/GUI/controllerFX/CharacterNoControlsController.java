package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.PlayCharacterCardA;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;

public class CharacterNoControlsController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;
    @FXML
    private Text description;
    /**
     * Binds this stage to a user GUI.
     * @param gui the GUI to bind to.
     */
    public CharacterNoControlsController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the controller stage for characters which require no additional user input after activation.
     */
    @Override
    public void initialize() {
        StrippedCharacter selectedCharacter = GUI.client.getLocalModel().selectedCharacter;
        int indexSelectedCharacter = GUI.client.getLocalModel().getCharacters().indexOf(selectedCharacter);
        description.setText(selectedCharacter.getDescription());

        confirmButton.setOnAction((event) -> {
            PlayCharacterCardA playCharacterCardA = new PlayCharacterCardA(GUI.client.getNickname(), indexSelectedCharacter);
            try {
                GUI.client.performGameAction(playCharacterCardA);
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
            } catch (FullDiningException e) {
                Controller.showErrorDialogBox(StringNames.DINING_WILL_FULL);
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
