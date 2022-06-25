package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.PickCloud;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.view.GUI.GUI;
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
    private ChoiceBox cloudChoice;
    @FXML
    private Button cancelButton, confirmButton;
    @FXML
    private Text totalYellow, totalBlue, totalGreen, totalRed, totalPink;

    public TakeFromCloudTilesController(GUI gui) {
        super(gui);
    }

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
        cloudChoice.setOnAction((event) -> {
            selectedItem.set(cloudChoice.getSelectionModel().getSelectedItem().toString());
            EnumMap<Colors, Integer> students = GUI.client.getLocalModel().getCloudByName(selectedItem).getStudents();

            for (int i = 0; i < students.size(); i++) {
                text.get(i).setText(String.valueOf(students.get(Colors.getStudent(i))));
            }
        });

        confirmButton.setOnAction((event) -> {
            PickCloud pickCloud = new PickCloud(GUI.client.getNickname(), selectedItem.get());

            try {
                GUI.client.performGameAction(pickCloud);
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
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
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
