package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class Board implements Event {
    private PropertyChangeEvent evt;

    public Board(PropertyChangeEvent evt) {
        this.evt = evt;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel) {
        localModel.setBoard(evt);
    }

    @Override
    public String message() {
        return null;
    }
}
