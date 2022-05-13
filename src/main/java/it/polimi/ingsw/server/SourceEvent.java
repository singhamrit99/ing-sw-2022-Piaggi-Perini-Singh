package it.polimi.ingsw.server;

public class SourceEvent {
    final private String who;
    final private String what;

    public SourceEvent(String who, String what) {
        this.who = who;
        this.what = what;
    }

    public String getWho() {
        return who;
    }

    public String getWhat() {
        return what;
    }

}
