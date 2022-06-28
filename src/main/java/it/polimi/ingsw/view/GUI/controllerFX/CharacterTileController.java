package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.PlayCharacterCardD;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.network.server.stripped.StrippedIsland;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterTileController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;

    @FXML
    private Text description, availableTiles;

    @FXML
    private ChoiceBox<String> choiceBox;

    /**
     * Binds this stage to a user GUI.
     *
     * @param gui the GUI to bind to.
     */
    public CharacterTileController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the Character Tile Controller Stage (such as in the case of students on card scenarios)
     */
    @Override
    public void initialize() {
        StrippedCharacter selectedCharacter = GUI.client.getLocalModel().selectedCharacter;
        int indexSelectedCharacter = GUI.client.getLocalModel().getCharacters().indexOf(selectedCharacter);

        description.setText(selectedCharacter.getDescription());

        availableTiles.setText("Tiles Available " + GUI.client.getLocalModel().getCharacters().get(indexSelectedCharacter).getNoEntryTiles());

        for (StrippedIsland island : GUI.client.getLocalModel().getIslands()) {
            choiceBox.getItems().add(island.getName());
        }
        choiceBox.getSelectionModel().selectFirst();

        AtomicInteger chosen = new AtomicInteger();
        chosen.set(choiceBox.getSelectionModel().getSelectedIndex());

        choiceBox.addEventFilter(ActionEvent.ANY, e -> chosen.set(choiceBox.getSelectionModel().getSelectedIndex()));

        confirmButton.setOnAction((event) -> {
            PlayCharacterCardD playCharacterCardD = new PlayCharacterCardD(GUI.client.getNickname(), indexSelectedCharacter, chosen.get());
            try {
                GUI.client.performGameAction(playCharacterCardD);
                Window window = ((Node) (event.getSource())).getScene().getWindow();
                window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (NotEnoughCoinsException e) {
                Controller.showErrorDialogBox(StringNames.NOT_ENOUGH_COINS);
            } catch (AssistantCardNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_NOT_FOUND);
            } catch (NegativeValueException e) {
                Controller.showErrorDialogBox(StringNames.NEGATIVE_VALUE);
            } catch (IncorrectStateException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_STATE);
            } catch (ProfessorNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.PROFESSOR_NOT_FOUND);
            } catch (MotherNatureLostException e) {
                Controller.showErrorDialogBox(StringNames.MOTHER_NATURE_LOST);
            } catch (IncorrectPlayerException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_PLAYER);
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (FullDiningException e) {
                Controller.showErrorDialogBox(StringNames.FULL_DINING);
            } catch (CardPlayedInTurnException e) {
                Controller.showErrorDialogBox(StringNames.CARD_PLAYED_IN_TURN);
            } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
            }
        });

        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}
