package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.PickCloud;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicReference;

public class TakeFromCloudTilesController extends InitialStage implements Controller {
    @FXML
    private ChoiceBox<String> cloudChoice;
    @FXML
    private Button cancelButton, confirmButton;
    @FXML
    private Text totalYellow, totalBlue, totalGreen, totalRed, totalPink;

    /**
     * Method used to bind this scene to a GUI
     *
     * @param gui the GUI to bind to
     */
    public TakeFromCloudTilesController(GUI gui) {
        super(gui);
    }

    /**
     * Initializes the Take Students from Clouds controller scene.
     */
    @Override
    public void initialize() {
        ArrayList<Text> text = new ArrayList<>();
        text.add(totalYellow);
        text.add(totalBlue);
        text.add(totalGreen);
        text.add(totalRed);
        text.add(totalPink);

        boolean toAdd;
        for (int i = 0; i < GUI.client.getLocalModel().getClouds().size(); i++) {
            StrippedCloud cloud = GUI.client.getLocalModel().getClouds().get(i);

            toAdd = false;
            for (Colors color : Colors.values()) {
                if (cloud.getStudents().get(color) != 0) {
                    toAdd = true;
                }
            }
            if (toAdd) {
                cloudChoice.getItems().add(cloud.getName());
            }
        }

        AtomicReference<String> selectedItem = new AtomicReference<>("");
        cloudChoice.getSelectionModel().selectFirst();
        selectedItem.set(cloudChoice.getSelectionModel().getSelectedItem());
        AtomicReference<EnumMap<Colors, Integer>> students = new AtomicReference<>(GUI.client.getLocalModel().getCloudByName(selectedItem).getStudents());

        for (int i = 0; i < students.get().size(); i++) {
            try {
                text.get(i).setText(String.valueOf(students.get().get(Colors.getStudent(i))));
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            }
        }

        cloudChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                selectedItem.set(cloudChoice.getSelectionModel().getSelectedItem());
                students.set(GUI.client.getLocalModel().getCloudByName(selectedItem).getStudents());

                for (int i = 0; i < students.get().size(); i++) {
                    try {
                        text.get(i).setText(String.valueOf(students.get().get(Colors.getStudent(i))));
                    } catch (IncorrectArgumentException e) {
                        Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
                    }
                }
            }
        });

        confirmButton.setOnAction((event) -> {
            PickCloud pickCloud = new PickCloud(GUI.client.getNickname(), selectedItem.get());

            try {
                GUI.client.performGameAction(pickCloud);
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
            } catch (MotherNatureLostException e) {
                Controller.showErrorDialogBox(StringNames.MOTHER_NATURE_LOST);
            } catch (ProfessorNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.PROFESSOR_NOT_FOUND);
            } catch (IncorrectPlayerException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_PLAYER);
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_IN_ROOM);
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
