package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.MoveStudents;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MoveStudentsController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Text totalYellow, totalBlue, totalGreen, totalRed, totalPink;
    @FXML
    private CheckBox check1, check2, check3, check4, check5;

    /**
     * Binds this stage to a user GUI.
     *
     * @param gui the GUI to bind to.
     */
    public MoveStudentsController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the Move student stage
     */
    @Override
    public void initialize() {
        ArrayList<Text> text = new ArrayList<>();
        text.add(totalYellow);
        text.add(totalBlue);
        text.add(totalGreen);
        text.add(totalRed);
        text.add(totalPink);

        ArrayList<CheckBox> checks = new ArrayList<>();
        checks.add(check1);
        checks.add(check2);
        checks.add(check3);
        checks.add(check4);
        checks.add(check5);

        AtomicReference<String> chosen = new AtomicReference<>("");

        choiceBox.getItems().add("dining");
        int j = 1;
        for (int i = 0; i < GUI.client.getLocalModel().getIslands().size(); i++) {
            if (!GUI.client.getLocalModel().getIslands().get(i).getName().equals("EMPTY")) {
                if (GUI.client.getLocalModel().getIslands().get(i).hasMotherNature()) {
                    choiceBox.getItems().add(j + ": MN present");
                } else {
                    choiceBox.getItems().add(Integer.toString(j));
                }
                j++;
            }
        }
        choiceBox.getSelectionModel().selectFirst();
        AtomicReference<EnumMap<Colors, Integer>> students = new AtomicReference<>();
        try {
            students.set(GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getDining());
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
        }
        if (students.get() != null) {
            for (int i = 0; i < students.get().size(); i++) {
                try {
                    text.get(i).setText(String.valueOf(students.get().get(Colors.getStudent(i))));
                } catch (IncorrectArgumentException e) {
                    Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
                }
            }
        }

        AtomicInteger index = new AtomicInteger();
        index.set(-1);

        for (CheckBox checkBox : checks) {
            checkBox.onMouseClickedProperty().set(mouseEvent -> {
                if (checkBox.isSelected()) {
                    for (CheckBox disable : checks) {
                        if (!disable.getId().equals(checkBox.getId())) {
                            disable.setDisable(true);
                            index.set(checks.indexOf(checkBox));
                        }
                    }
                } else {
                    for (CheckBox disable : checks) {
                        if (!disable.getId().equals(checkBox.getId())) {
                            disable.setDisable(false);
                            index.set(-1);
                        }
                    }
                }
            });
        }

        chosen.set("dining");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int count = 1;
                if (choiceBox.getSelectionModel().isSelected(0)) {
                    chosen.set("dining");
                    try {
                        students.set(GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getDining());
                    } catch (LocalModelNotLoadedException e) {
                        Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
                    }
                } else {
                    for (int island = 0; island < GUI.client.getLocalModel().getIslands().size(); island++) {
                        if (!GUI.client.getLocalModel().getIslands().get(island).getName().equals("EMPTY")) {
                            if (count == t1.intValue()) {
                                chosen.set(GUI.client.getLocalModel().getIslands().get(island).getName());
                            }
                            count++;
                        }
                    }
                    students.set(GUI.client.getLocalModel().getIslands().get(t1.intValue() - 1).getStudents());
                }

                if (students.get() != null) {
                    for (int i = 0; i < students.get().size(); i++) {
                        try {
                            text.get(i).setText(String.valueOf(students.get().get(Colors.getStudent(i))));
                        } catch (IncorrectArgumentException e) {
                            Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
                        }
                    }
                }
            }
        });

        confirmButton.setOnAction((event) -> {
            try {
                MoveStudents moveStudents = new MoveStudents(GUI.client.getNickname(), Colors.getStudent(index.get()), chosen.get());
                GUI.client.performGameAction(moveStudents);

                Window window = ((Node) (event.getSource())).getScene().getWindow();
                window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (AssistantCardNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_NOT_FOUND);
            } catch (NotEnoughCoinsException e) {
                Controller.showErrorDialogBox(StringNames.NOT_ENOUGH_COINS);
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
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (FullDiningException e) {
                Controller.showErrorDialogBox(StringNames.FULL_DINING);
            } catch (CardPlayedInTurnException e) {
                Controller.showErrorDialogBox(StringNames.CARD_PLAYED_IN_TURN);
            } catch (AssistantCardAlreadyPlayed e) {
                Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
            }
        });

        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}