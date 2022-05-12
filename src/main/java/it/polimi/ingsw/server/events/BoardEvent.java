package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class BoardEvent extends Event {
    private PropertyChangeEvent evt;

    public BoardEvent(PropertyChangeEvent evt) {
        this.evt = evt;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel) {
        localModel.setBoard(evt);
    }

}
