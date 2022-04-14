package it.polimi.ingsw.controller;

public class PlayAssistantCardCommand implements Commands {

    Controller controller;
    int assistantCardID;

    public PlayAssistantCardCommand(Controller controller, int assistantCardID) {
        this.controller = controller;
        this.assistantCardID = assistantCardID;
    }

    @Override
    public void execute() {controller.CallPlayAssistantCard(assistantCardID);}
}
