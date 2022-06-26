package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.view.GUI.GUI;

/**
 * @author Amrit
 * Class that sets the attributes needed by the windows involved in the initial set up phase
 */
public class InitialStage {
    protected GUI gui;
    /**
     * Binds this stage to a user GUI.
     * @param gui the GUI to bind to.
     */
    public InitialStage(GUI gui) {
        this.gui = gui;
    }

    /**
     * Returns the GUI associated to this Stage.
     * @return the GUI
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * Sets this stage's GUI.
     * @param gui the GUI to bind this stage to
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
