package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

import java.beans.PropertyChangeEvent;

public class CharacterEvent extends Event {
    private PropertyChangeEvent evt;

    public CharacterEvent(PropertyChangeEvent evt) {
        this.evt = evt;
    }

    @Override
    public void updateLocalModel(StrippedModel localModel) {
        localModel.changePriceCharacterCard(evt);
    }

}
