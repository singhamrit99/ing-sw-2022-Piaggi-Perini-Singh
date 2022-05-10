package it.polimi.ingsw.server.events;

import it.polimi.ingsw.model.stripped.StrippedModel;

public interface Event {

    public void updateLocalModel(StrippedModel localModel);

    public String message();
}
