package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class Event {
    private PropertyChangeEvent evt;
    public void updateLocalModel(StrippedModel localModel){
        return;
    }

    public SourceEvent getSource(){
        return (SourceEvent) evt.getSource();
    }

    public boolean isError() {
        return false;
    }

}
