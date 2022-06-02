package it.polimi.ingsw.client.GUI.controller;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

/**
 * @author Amrit
 */
public interface Utility {
    static void showErrorDialogBox(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
