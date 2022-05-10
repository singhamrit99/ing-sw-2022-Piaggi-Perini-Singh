package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class Island implements Event {
    private PropertyChangeEvent evt;

    public Island(PropertyChangeEvent evt) {
        this.evt = evt;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel) {
        localModel.changeIsland(evt);
    }

    @Override
    public String message() {
        return null;
    }
}
