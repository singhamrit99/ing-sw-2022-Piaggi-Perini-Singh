package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.MoveMotherNature;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;

public class MoveMotherNatureController extends InitialStage implements Controller {
    @FXML
    private Slider slider;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    public MoveMotherNatureController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize() {
        //mostra uno slider da un minimo a un massimo
        try {
            slider.setMin(1);
            slider.setMax(GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getMoves());
            System.out.println(GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getMoves());
            slider.setValue(1);
            slider.setMajorTickUnit(1.0);
            slider.setShowTickMarks(false);
            slider.setSnapToTicks(true);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
        } catch (LocalModelNotLoadedException e) {
            e.printStackTrace();
        }

        //fai bottone conferma per muovere
        confirmButton.setOnAction((event) -> {
            //prendo il numero da slider
            MoveMotherNature moveMotherNature = new MoveMotherNature(GUI.client.getNickname(), (int) slider.getValue());

            try {
                GUI.client.performGameAction(moveMotherNature);
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

        //button cancel
        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}
