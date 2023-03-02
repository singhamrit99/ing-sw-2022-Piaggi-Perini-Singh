package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.network.server.commands.PlayCharacterCardD;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterOneSelectController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;

    @FXML
    private Text description;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ImageView image;

    /**
     * Binds this stage to a user GUI.
     *
     * @param gui the GUI to bind to.
     */
    public CharacterOneSelectController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the unique character controller stage.
     */
    @Override
    public void initialize() {
        StrippedCharacter selectedCharacter = GUI.client.getLocalModel().selectedCharacter;
        int indexSelectedCharacter = GUI.client.getLocalModel().getCharacters().indexOf(selectedCharacter);

        description.setText(selectedCharacter.getDescription());
        image.setImage(new Image(ResourcesPath.CHARACTERS + selectedCharacter.getCharacterID() + ResourcesPath.IMAGE_EXTENSION_CHAR));

        if (selectedCharacter.getCharacterID() == 3) {
            int j = 0;
            for (int i = 1; i <= GUI.client.getLocalModel().getIslands().size(); i++) {
                if (!GUI.client.getLocalModel().getIslands().get(i - 1).getName().equals("EMPTY")) {
                    if (GUI.client.getLocalModel().getIslands().get(i - 1).hasMotherNature()) {
                        choiceBox.getItems().add(j + 1 + ": MN present");
                    } else {
                        choiceBox.getItems().add(Integer.toString(j + 1));
                    }
                    j++;
                }
            }
        } else {
            for (Colors color : Colors.values()) {
                choiceBox.getItems().add(color.toString());
            }
        }
        choiceBox.getSelectionModel().selectFirst();

        AtomicInteger chosen = new AtomicInteger();
        chosen.set(choiceBox.getSelectionModel().getSelectedIndex());

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (selectedCharacter.getCharacterID() == 3) {
                    int count = 0;

                    for (int island = 1; island <= GUI.client.getLocalModel().getIslands().size(); island++) {
                        if (!GUI.client.getLocalModel().getIslands().get(island - 1).getName().equals("EMPTY")) {
                            if (count == t1.intValue() - 1) {
                                chosen.set(island);
                            }
                            count++;
                        }
                    }
                } else {
                    chosen.set(t1.intValue());
                }
            }
        });

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
            } catch (MotherNatureLostException e) {
                Controller.showErrorDialogBox(StringNames.MOTHER_NATURE_LOST);
            } catch (ProfessorNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.PROFESSOR_NOT_FOUND);
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (IncorrectPlayerException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_PLAYER);
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