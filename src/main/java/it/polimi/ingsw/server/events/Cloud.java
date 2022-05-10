package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class Cloud implements Event {
    private PropertyChangeEvent evt;

    public Cloud(PropertyChangeEvent evt) {
        this.evt = evt;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel) {
        localModel.changeCloud(evt);
    }

    @Override
    public String message() {
        return null;
    }
}
