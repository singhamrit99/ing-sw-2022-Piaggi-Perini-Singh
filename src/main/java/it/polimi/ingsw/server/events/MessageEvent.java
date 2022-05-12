package it.polimi.ingsw.server.events;
import java.beans.PropertyChangeEvent;

public class MessageEvent extends Event{

    final private PropertyChangeEvent evt;
    final private boolean isError;
    public MessageEvent(PropertyChangeEvent evt, boolean error){
        this.evt = evt;
        isError = error;
    }

    public boolean isError() {
        return isError;
    }
}
