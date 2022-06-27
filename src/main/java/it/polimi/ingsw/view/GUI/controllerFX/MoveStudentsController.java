package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.MoveStudents;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;

public class MoveStudentsController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;

    @FXML
    private Text totalYellow, totalBlue, totalGreen, totalRed, totalPink;

    @FXML
    private ComboBox islandNumber,
            totalDiningYellow, totalDiningBlue, totalDiningGreen, totalDiningRed, totalDiningPink,
            totalIslandYellow, totalIslandBlue, totalIslandGreen, totalIslandRed, totalIslandPink;

    private int motherNatureIndex;

    /**
     * Method used to bind this scene to a GUI
     *
     * @param gui the GUI to bind to
     */
    public MoveStudentsController(GUI gui) {
        super(gui);
    }

    /**
     * Initializes the Move Students controller scene.
     */
    @Override
    public void initialize() {
        motherNatureIndex = -1;
        loadComboBox(islandNumber, GUI.client.getLocalModel().getIslands().size());

        ArrayList<Text> text = new ArrayList<>();
        text.add(totalYellow);
        text.add(totalBlue);
        text.add(totalGreen);
        text.add(totalRed);
        text.add(totalPink);

        ArrayList<ComboBox> diningComboBoxes = new ArrayList<>();
        diningComboBoxes.add(totalDiningYellow);
        diningComboBoxes.add(totalDiningBlue);
        diningComboBoxes.add(totalDiningGreen);
        diningComboBoxes.add(totalDiningRed);
        diningComboBoxes.add(totalDiningPink);

        ArrayList<ComboBox> islandsComboBoxes = new ArrayList<>();
        islandsComboBoxes.add(totalIslandYellow);
        islandsComboBoxes.add(totalIslandBlue);
        islandsComboBoxes.add(totalIslandGreen);
        islandsComboBoxes.add(totalIslandRed);
        islandsComboBoxes.add(totalIslandPink);

        for (ComboBox islandBox : islandsComboBoxes) {
            islandBox.getSelectionModel().selectFirst();
            islandBox.setDisable(true);
        }

        try {
            EnumMap<Colors, Integer> entrance = GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getEntrance();

            int i = 0;
            for (Text textEntrance : text) {
                textEntrance.setText(entrance.get(Colors.getStudent(i)).toString());
                i++;
            }
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
        }

        int k = 0;
        for (ComboBox diningBox : diningComboBoxes) {
            loadComboBox(diningBox, Integer.parseInt(text.get(k).getText()));
            diningBox.getSelectionModel().selectFirst();
            k++;
        }

        islandNumber.setOnAction((event) -> {
            int selectedIndex = islandNumber.getSelectionModel().getSelectedIndex();
            if (selectedIndex != 0) {
                int j = 0;
                for (ComboBox islandBox : islandsComboBoxes) {
                    loadComboBox(islandBox, Integer.parseInt(text.get(j).getText()));
                    islandBox.setDisable(false);
                    islandBox.getSelectionModel().selectFirst();
                    j++;
                }
            } else {
                for (ComboBox islandBox : islandsComboBoxes) {
                    islandBox.setDisable(true);
                }
            }
        });

        confirmButton.setOnAction((event) -> {
            EnumMap<Colors, ArrayList<String>> studentToMove = new EnumMap<>(Colors.class);
            ArrayList<String> emptyString = new ArrayList<>();
            for (Colors color : Colors.values()) {
                studentToMove.put(color, emptyString);
            }

            String dining = "dining";
            ArrayList<String> destinations;
            int value;
            for (int i = 0; i < Colors.values().length; i++) {
                destinations = new ArrayList<>();

                if (diningComboBoxes.get(i).getSelectionModel().getSelectedIndex() != 0) {
                    value = Integer.parseInt(diningComboBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                    for (int j = 0; j < value; j++) {
                        destinations.add(dining);
                    }
                }

                if (islandNumber.getSelectionModel().getSelectedIndex() != 0 && islandsComboBoxes.get(i).getSelectionModel().getSelectedItem() != null) {
                    String islandNum;
                    if (islandNumber.getSelectionModel().getSelectedIndex() == motherNatureIndex) {
                        islandNum = String.valueOf(motherNatureIndex);
                    } else {
                        islandNum = islandNumber.getSelectionModel().getSelectedItem().toString();
                    }
                    value = Integer.parseInt(islandsComboBoxes.get(i).getSelectionModel().getSelectedItem().toString());

                    for (int j = 0; j < value; j++) {
                        destinations.add("island" + islandNum);
                    }
                }
                studentToMove.put(Colors.getStudent(i), destinations);
            }


            MoveStudents moveStudents = new MoveStudents(GUI.client.getNickname(), studentToMove);
            try {
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
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (FullDiningException e) {
                Controller.showErrorDialogBox(StringNames.FULL_DINING);
            } catch (CardPlayedInTurnException e) {
                Controller.showErrorDialogBox(StringNames.CARD_PLAYED_IN_TURN);
            }
        });

        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }

    /**
     * Loads combo box for student choices
     *
     * @param comboBox The Combo box selector
     * @param num      Number of choices
     */
    public void loadComboBox(ComboBox comboBox, int num) {
        ObservableList<String> choices = FXCollections.observableArrayList();

        for (int i = 0; i <= num; i++) {
            if (i > 0 && comboBox.getId().equals(islandNumber.getId())) {
                if (!GUI.client.getLocalModel().getIslands().get(i - 1).getName().equals("EMPTY")) {
                    if (GUI.client.getLocalModel().getIslands().get(i - 1).hasMotherNature()) {
                        choices.add(i + ": MN present");
                        motherNatureIndex = i;
                    } else {
                        choices.add(Integer.toString(i));
                    }
                }
            } else {
                choices.add(Integer.toString(i));
            }
        }

        comboBox.setItems(choices);
    }
}
