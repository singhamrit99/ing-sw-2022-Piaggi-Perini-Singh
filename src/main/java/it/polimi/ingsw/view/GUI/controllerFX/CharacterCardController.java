package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class CharacterCardController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ImageView image;

    @FXML
    private Text coins;

    /**
     * Binds this stage to a user GUI.
     *
     * @param gui the GUI to bind to.
     */
    public CharacterCardController(GUI gui) {
        super(gui);
    }

    /**
     * Method used to initialize the Character Card Controller stage.
     */
    @Override
    public void initialize() {
        ArrayList<StrippedCharacter> cards = GUI.client.getLocalModel().getCharacters();

        for (StrippedCharacter card : cards) {
            choiceBox.getItems().add("Character " + card.getCharacterID());
        }
        AtomicReference<String> chosenCard = new AtomicReference<>("");

        choiceBox.getSelectionModel().selectFirst();
        int firstIndex = choiceBox.getSelectionModel().getSelectedIndex();
        chosenCard.set(String.valueOf(cards.get(firstIndex).getCharacterID()));
        GUI.client.getLocalModel().selectedCharacter = cards.get(firstIndex);
        coins.setText("Card cost: " + cards.get(firstIndex).getPrice());
        image.setImage(new Image(ResourcesPath.CHARACTERS + cards.get(firstIndex).getCharacterID() + ResourcesPath.IMAGE_EXTENSION_CHAR));

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                coins.setText("Card cost: " + cards.get(t1.intValue()).getPrice());
                image.setImage(new Image(ResourcesPath.CHARACTERS + cards.get(t1.intValue()).getCharacterID() + ResourcesPath.IMAGE_EXTENSION_CHAR));
                chosenCard.set(String.valueOf(cards.get(t1.intValue()).getCharacterID()));
                GUI.client.getLocalModel().selectedCharacter = cards.get(t1.intValue());
            }
        });

        confirmButton.setOnAction(event -> {
            switch (GUI.client.getLocalModel().selectedCharacter.getCharacterID()) {
                case 2:
                case 4:
                case 6:
                case 8:
                    String filePath = ResourcesPath.FXML_FILE_PATH + "CharacterNoControlsView" + ResourcesPath.FILE_EXTENSION;
                    FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterNoControlsController(gui));

                    try {
                        Controller.loadScene(loader);
                    } catch (IOException e) {
                        Controller.showErrorDialogBox(StringNames.ERROR_IO);
                    }
                    Window window = ((Node) (event.getSource())).getScene().getWindow();
                    window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    break;
                case 3:
                case 9:
                case 12:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterOneSelectView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterOneSelectController(gui));
                    try {
                        Controller.loadScene(loader);
                    } catch (IOException e) {
                        Controller.showErrorDialogBox(StringNames.ERROR_IO);
                    }
                    window = ((Node) (event.getSource())).getScene().getWindow();
                    window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    break;
                case 1:
                case 11:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterMultipleSelectView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterMultipleSelectController(gui));

                    try {
                        Controller.loadScene(loader);
                    } catch (IOException e) {
                        Controller.showErrorDialogBox(StringNames.ERROR_IO);
                    }
                    window = ((Node) (event.getSource())).getScene().getWindow();
                    window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    break;
                case 7:
                case 10:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterSwapView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterSwapController(gui));
                    try {
                        Controller.loadScene(loader);
                    } catch (IOException e) {
                        Controller.showErrorDialogBox(StringNames.ERROR_IO);
                    }
                    window = ((Node) (event.getSource())).getScene().getWindow();
                    window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    break;
                case 5:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterTileView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterTileController(gui));
                    try {
                        Controller.loadScene(loader);
                    } catch (IOException e) {
                        Controller.showErrorDialogBox(StringNames.ERROR_IO);
                    }
                    window = ((Node) (event.getSource())).getScene().getWindow();
                    window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
                    break;
                default:
                    break;
            }
        });

        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}
