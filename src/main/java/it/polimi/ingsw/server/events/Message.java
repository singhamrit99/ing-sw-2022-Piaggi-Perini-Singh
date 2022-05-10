package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class Message implements Event {
    private String message;

    public Message(PropertyChangeEvent evt) {
        this.message = (String) evt.getNewValue();
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel){
        return;
    }
}
