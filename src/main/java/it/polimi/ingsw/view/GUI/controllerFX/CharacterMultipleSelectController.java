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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterMultipleSelectController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Text description;

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
    public CharacterMultipleSelectController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the Character select controller stage.
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

        AtomicInteger chosen = new AtomicInteger();

        StrippedCharacter selectedCharacter = GUI.client.getLocalModel().selectedCharacter;
        int indexSelectedCharacter = GUI.client.getLocalModel().getCharacters().indexOf(selectedCharacter);
        description.setText(selectedCharacter.getDescription());

        for (int i = 0; i < selectedCharacter.getStudents().size(); i++) {
            text.get(i).setText(String.valueOf(selectedCharacter.getStudents().get(Colors.getStudent(i))));
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

        if (selectedCharacter.getCharacterID() == 11) {
            choiceBox.visibleProperty().set(false);
        } else {
            choiceBox.visibleProperty().set(true);

            for (StrippedIsland island : GUI.client.getLocalModel().getIslands()) {
                choiceBox.getItems().add(island.getName());
            }
            choiceBox.getSelectionModel().selectFirst();

            chosen.set(choiceBox.getSelectionModel().getSelectedIndex());
            choiceBox.setOnAction((event) -> chosen.set(choiceBox.getSelectionModel().getSelectedIndex()));
        }

        confirmButton.setOnAction((event) -> {
            if (selectedCharacter.getCharacterID() == 1) {
                PlayCharacterCardB playCharacterCardB = new PlayCharacterCardB(GUI.client.getNickname(), indexSelectedCharacter, index.get(), chosen.get());
                try {
                    GUI.client.performGameAction(playCharacterCardB);
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
            } else {
                PlayCharacterCardD playCharacterCardD = new PlayCharacterCardD(GUI.client.getNickname(), indexSelectedCharacter, index.get());
                try {
                    GUI.client.performGameAction(playCharacterCardD);
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
                } catch (ProfessorNotFoundException e) {
                    Controller.showErrorDialogBox(StringNames.PROFESSOR_NOT_FOUND);
                } catch (MotherNatureLostException e) {
                    Controller.showErrorDialogBox(StringNames.MOTHER_NATURE_LOST);
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
                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                    Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                }
            }
        });

        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}
