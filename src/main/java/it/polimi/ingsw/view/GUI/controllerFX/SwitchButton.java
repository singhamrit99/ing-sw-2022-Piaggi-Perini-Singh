package it.polimi.ingsw.view.GUI.controllerFX;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SwitchButton extends StackPane {
    private final Rectangle back = new Rectangle(30, 10, Color.RED);
    private final Button button = new Button();
    private final String buttonStyleOff = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: WHITE;";
    private final String buttonStyleOn = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: #00893d;";
    private boolean state;

    /**
     * Initializes the Switch Button
     */
    private void init() {
        getChildren().addAll(back, button);
        setMinSize(30, 15);
        back.maxWidth(30);
        back.minWidth(30);
        back.maxHeight(10);
        back.minHeight(10);
        back.setArcHeight(back.getHeight());
        back.setArcWidth(back.getHeight());
        back.setFill(Color.valueOf("#ced5da"));
        double r = 2.0;
        button.setShape(new Circle(r));
        setAlignment(button, Pos.CENTER_LEFT);
        button.setMaxSize(15, 15);
        button.setMinSize(15, 15);
        button.setStyle(buttonStyleOff);
        state = false;
    }

    /**
     * Switch button constructor
     */
    public SwitchButton() {
        init();
        button.setFocusTraversable(false);
    }

    /**
     * Switch button state setter method
     *
     * @param state the state to set to (boolean)
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Returns the current state
     *
     * @return the state to return
     */
    public boolean getState() {
        return state;
    }

    /**
     * Changes the state to the parameter and the style accordingly.
     *
     * @param state The state to change to (boolean)
     */
    public void changeState(boolean state) {
        this.state = state;
        setButtonStyle();
    }

    /**
     * Changes the style of the button according to the action performed on it.
     */
    private void setButtonStyle() {
        if (state) {
            button.setStyle(buttonStyleOn);
            back.setFill(Color.valueOf("#80C49E"));
            setAlignment(button, Pos.CENTER_RIGHT);
        } else {
            button.setStyle(buttonStyleOff);
            back.setFill(Color.valueOf("#ced5da"));
            setAlignment(button, Pos.CENTER_LEFT);
        }
    }
}