package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class Character implements Event {
    private PropertyChangeEvent evt;

    public Character(PropertyChangeEvent evt) {
        this.evt = evt;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel) {
        localModel.changePriceCharacterCard(evt);
    }

    @Override
    public String message() {
        return null;
    }
}
