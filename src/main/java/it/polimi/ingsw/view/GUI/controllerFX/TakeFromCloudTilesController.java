package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.tiles.Cloud;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class TakeFromCloudTilesController extends InitialStage implements Controller {
    @FXML
    private ChoiceBox cloudChoice;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    public TakeFromCloudTilesController(GUI gui) {
        super(gui);
    }

    @Override
    public void initialize() {
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

        //posso mostrare gli studenti che ci sono nella nuvola selezionata
        //prendo studenti
        //aggiungo immagini in base al colore e il X n corretto

        //conferma
        /*confirmButton.setOnAction((event) -> {
            //prendo il numero da slider

            MoveMotherNature moveMotherNature = new MoveMotherNature(GUI.client.getNickname(), (int) slider.getValue());
            try {
                GUI.client.performGameAction(moveMotherNature);
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
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            }

            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });*/


        cancelButton.setOnAction((event) -> {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }

}
