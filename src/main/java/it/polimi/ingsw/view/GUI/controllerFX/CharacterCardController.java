package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class CharacterCardController extends InitialStage implements Controller {
    @FXML
    private Button cancelButton, confirmButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ImageView image;

    public CharacterCardController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize() {
        ArrayList<StrippedCharacter> cards = GUI.client.getLocalModel().getCharacters();

        for (StrippedCharacter card : cards) {
            choiceBox.getItems().add("Character " + card.getCharacterID());
        }

        ArrayList<StrippedCharacter> finalCards = cards;
        AtomicReference<String> chosenCard = new AtomicReference<>("");

        choiceBox.getSelectionModel().selectFirst();
        int firstIndex = choiceBox.getSelectionModel().getSelectedIndex();
        chosenCard.set(String.valueOf(finalCards.get(firstIndex).getCharacterID()));
        GUI.client.getLocalModel().selectedCharacter = finalCards.get(firstIndex);
        try {
            image.setImage(new Image(Files.newInputStream(Paths.get(ResourcesPath.CHARACTERS + finalCards.get(firstIndex).getCharacterID() + ResourcesPath.IMAGE_EXTENSION_CHAR))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        choiceBox.setOnAction((event) -> {
            int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
            try {
                image.setImage(new Image(Files.newInputStream(Paths.get(ResourcesPath.CHARACTERS + finalCards.get(selectedIndex).getCharacterID() + ResourcesPath.IMAGE_EXTENSION_CHAR))));
                chosenCard.set(String.valueOf(finalCards.get(selectedIndex).getCharacterID()));
                GUI.client.getLocalModel().selectedCharacter = finalCards.get(selectedIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        confirmButton.setOnAction((event) -> {
            switch (GUI.client.getLocalModel().selectedCharacter.getCharacterID()) {
                case 2:
                case 4:
                case 6:
                case 8:
                    String filePath = ResourcesPath.FXML_FILE_PATH + "CharacterNoControlsView" + ResourcesPath.FILE_EXTENSION;
                    FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterNoControlsController(gui));

                    Scene scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();
                    stage.setTitle(StringNames.TITLE);
                    stage.setResizable(false);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                    break;
                case 3:
                case 9:
                case 12:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterOneSelectView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterOneSelectController(gui));

                    scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage = new Stage();
                    stage.setTitle(StringNames.TITLE);
                    stage.setResizable(false);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                    break;
                case 1:
                case 11:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterMultipleSelectView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterMultipleSelectController(gui));

                    scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage = new Stage();
                    stage.setTitle(StringNames.TITLE);
                    stage.setResizable(false);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                    break;
                case 7:
                case 10:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterSwapView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterSwapController(gui));

                    scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage = new Stage();
                    stage.setTitle(StringNames.TITLE);
                    stage.setResizable(false);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                    break;
                case 5:
                    filePath = ResourcesPath.FXML_FILE_PATH + "CharacterTileView" + ResourcesPath.FILE_EXTENSION;
                    loader = new FXMLLoader(Controller.class.getResource(filePath));
                    loader.setController(new CharacterTileController(gui));

                    scene = null;
                    try {
                        scene = new Scene(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stage = new Stage();
                    stage.setTitle(StringNames.TITLE);
                    stage.setResizable(false);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
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