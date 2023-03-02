package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.network.server.commands.PlayAssistantCard;
import it.polimi.ingsw.view.GUI.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import java.rmi.RemoteException;

public class AssistantCardController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ImageView image;

    /**
     * Binds this stage to a user GUI.
     *
     * @param gui the GUI to bind to.
     */
    public AssistantCardController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the Assistant Card Controller stage.
     */
    @Override
    public void initialize() {
        ArrayList<AssistantCard> cards = new ArrayList<>();
        try {
            cards = GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).getDeck().getAllCards();
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
        }

        for (AssistantCard card : cards) {
            choiceBox.getItems().add("Assistant " + card.getImageName());
        }

        ArrayList<AssistantCard> finalCards = cards;
        AtomicReference<String> chosenCard = new AtomicReference<>("");
        choiceBox.getSelectionModel().selectFirst();
        int firstIndex = choiceBox.getSelectionModel().getSelectedIndex();
        chosenCard.set(finalCards.get(firstIndex).getImageName());
        try {
            GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).setMoves(finalCards.get(firstIndex).getMove());
        } catch (LocalModelNotLoadedException e) {
            Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
        }

        image.setImage(new Image(ResourcesPath.ASSISTANT_CARDS + finalCards.get(firstIndex).getImageName() + ResourcesPath.IMAGE_EXTENSION_ASS));
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                try {
                    image.setImage(new Image(ResourcesPath.ASSISTANT_CARDS + finalCards.get(t1.intValue()).getImageName() + ResourcesPath.IMAGE_EXTENSION_ASS));
                    chosenCard.set(finalCards.get(t1.intValue()).getImageName());
                    GUI.client.getLocalModel().getBoardOf(GUI.client.getNickname()).setMoves(finalCards.get(t1.intValue()).getMove());
                } catch (LocalModelNotLoadedException v) {
                    Controller.showErrorDialogBox(StringNames.LOCAL_MODEL_ERROR);
                }
            }
        });

        confirmButton.setOnAction((event) -> {
            PlayAssistantCard playAssistantCard = new PlayAssistantCard(GUI.client.getNickname(), chosenCard.get());
            try {
                GUI.client.performGameAction(playAssistantCard);
                Window window = ((Node) (event.getSource())).getScene().getWindow();
                window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (NotEnoughCoinsException e) {
                Controller.showErrorDialogBox(StringNames.NOT_ENOUGH_COINS);
            } catch (AssistantCardNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.ASSISTANT_CARD_NOT_FOUND);
            } catch (NegativeValueException e) {
                Controller.showErrorDialogBox(StringNames.NEGATIVE_VALUE);
            } catch (MotherNatureLostException e) {
                Controller.showErrorDialogBox(StringNames.MOTHER_NATURE_LOST);
            } catch (IncorrectStateException e) {
                Controller.showErrorDialogBox(StringNames.INCORRECT_STATE);
            } catch (ProfessorNotFoundException e) {
                Controller.showErrorDialogBox(StringNames.PROFESSOR_NOT_FOUND);
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
