package it.polimi.ingsw.server.commands;

public class Message implements ServerCommand {
    private String choice;

    public Message(String choice){
        this.choice = choice;
    }

    @Override
    public String message() {
        return choice;
    }
}
