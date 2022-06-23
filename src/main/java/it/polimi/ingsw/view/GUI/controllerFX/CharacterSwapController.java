package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.PlayCharacterCardC;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
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

public class CharacterSwapController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;
    @FXML
    private Text totalYellow1, totalBlue1, totalGreen1, totalRed1, totalPink1,
            totalYellow2, totalBlue2, totalGreen2, totalRed2, totalPink2,
            totalFirst, selectFirst,
            totalSecond, selectSecond,
            description;
    @FXML
    private ComboBox islandNumber,
            totalFirstYellow, totalFirstBlue, totalFirstGreen, totalFirstRed, totalFirstPink,
            totalSecondYellow, totalSecondBlue, totalSecondGreen, totalSecondRed, totalSecondPink;

    public CharacterSwapController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize() {
        ArrayList<Text> text1 = new ArrayList<>();
        text1.add(totalYellow1);
        text1.add(totalBlue1);
        text1.add(totalGreen1);
        text1.add(totalRed1);
        text1.add(totalPink1);

        ArrayList<Text> text2 = new ArrayList<>();
        text2.add(totalYellow2);
        text2.add(totalBlue2);
        text2.add(totalGreen2);
        text2.add(totalRed2);
        text2.add(totalPink2);

        ArrayList<ComboBox> firstBoxes = new ArrayList<>();
        firstBoxes.add(totalFirstYellow);
        firstBoxes.add(totalFirstBlue);
        firstBoxes.add(totalFirstGreen);
        firstBoxes.add(totalFirstRed);
        firstBoxes.add(totalFirstPink);

        ArrayList<ComboBox> secondBoxes = new ArrayList<>();
        secondBoxes.add(totalSecondYellow);
        secondBoxes.add(totalSecondBlue);
        secondBoxes.add(totalSecondGreen);
        secondBoxes.add(totalSecondRed);
        secondBoxes.add(totalSecondPink);

        StrippedCharacter selectedCharacter = GUI.client.getLocalModel().selectedCharacter;
        description.setText(selectedCharacter.getDescription());

        if (selectedCharacter.getCharacterID() == 7) {
            totalFirst.setText("Card Total");
            selectFirst.setText("Select Card");
            totalSecond.setText("Entrance Total");
            selectSecond.setText("Select Entrance");
            int i = 0;
            for (Text textEntrance : text1) {
                textEntrance.setText(selectedCharacter.getStudents().get(Colors.getStudent(i)).toString());
                i++;
            }

            try {
                int k = 0;
                EnumMap<Colors, Integer> entrance = GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getEntrance();

                for (Text textEntrance : text2) {
                    textEntrance.setText(entrance.get(Colors.getStudent(k)).toString());
                    k++;
                }
            } catch (LocalModelNotLoadedException e) {
                e.printStackTrace();
            }
        } else {
            totalFirst.setText("Entrance Total");
            selectFirst.setText("Select Entrance");
            totalSecond.setText("Dining Total");
            selectSecond.setText("Select Dining");
            try {
                int i = 0;
                EnumMap<Colors, Integer> entrance = GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getEntrance();

                for (Text textEntrance : text1) {
                    textEntrance.setText(entrance.get(Colors.getStudent(i)).toString());
                    i++;
                }
            } catch (LocalModelNotLoadedException e) {
                e.printStackTrace();
            }

            try {
                int k = 0;
                EnumMap<Colors, Integer> entrance = GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getDining();

                for (Text textEntrance : text2) {
                    textEntrance.setText(entrance.get(Colors.getStudent(k)).toString());
                    k++;
                }
            } catch (LocalModelNotLoadedException e) {
                e.printStackTrace();
            }
        }

        int i = 0;
        for (ComboBox cardBox : firstBoxes) {
            loadComboBox(cardBox, Integer.parseInt(text1.get(i).getText()));
            cardBox.getSelectionModel().selectFirst();
            i++;
        }

        i = 0;
        for (ComboBox cardBox : secondBoxes) {
            loadComboBox(cardBox, Integer.parseInt(text2.get(i).getText()));
            cardBox.getSelectionModel().selectFirst();
            i++;
        }

        confirmButton.setOnAction((event) -> {
            EnumMap<Colors, Integer> swap1, swap2;
            swap1 = StudentManager.createEmptyStudentsEnum();
            swap2 = StudentManager.createEmptyStudentsEnum();

            int c = 0;
            for (ComboBox cardBox : firstBoxes) {
                swap1.put(Colors.getStudent(c), Integer.parseInt(cardBox.getSelectionModel().getSelectedItem().toString()));
                c++;
            }

            c = 0;
            for (ComboBox cardBox : secondBoxes) {
                swap2.put(Colors.getStudent(c), Integer.parseInt(cardBox.getSelectionModel().getSelectedItem().toString()));
                c++;
            }

            PlayCharacterCardC playCharacterCardC = new PlayCharacterCardC(GUI.client.getNickname(), selectedCharacter.getCharacterID(), swap1, swap2);
            try {
                GUI.client.performGameAction(playCharacterCardC);
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
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
            } catch (IncorrectPlayerException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_PLAYER);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (IncorrectArgumentException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
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

    public void loadComboBox(ComboBox comboBox, int num) {
        ObservableList<String> choices = FXCollections.observableArrayList();

        for (int i = 0; i <= num; i++) choices.add(Integer.toString(i));

        comboBox.setItems(choices);
    }
}