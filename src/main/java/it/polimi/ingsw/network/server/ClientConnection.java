package it.polimi.ingsw.network.server;

public class ClientConnection{
    private String clientRoom = null;
    private String nickname;

    private  boolean up;
    private boolean inGame;

    /**
     * Client Connection constructor.
     * @param nickname the nickname of the user.
     */
    public ClientConnection(String nickname) {
        this.nickname = nickname;
        inGame = false;
        up = true;
    }

    /**
     * Getter method for client nickname.
     * @return nickname.
     */
    public synchronized String getNickname() {
        return nickname;
    }

    /**
     * Getter for clientRoom field.
     * @return clientRoom string value.
     */
    public synchronized String getRoom() {
        return clientRoom;
    }

    /**
     * Setter for room field.
     * @param room the name of the user's room.
     */
    public synchronized void setRoom(String room) {
        this.clientRoom = room;
    }

    /**
     * Getter for inGame field.
     * @return inGame boolean value.
     */
    public synchronized boolean inGame(){ return inGame;}

    /**
     * Setter method for inGame parameter
     * @param isPlaying sets whether the client is in game or not.
     */
    public synchronized void setInGame(boolean isPlaying){
        inGame = isPlaying;
    }
    /**
     * Getter for the Up field
     * @return boolean value of up
     */
    public synchronized boolean isUp(){
        return up;
    }
    /**
     * Method used to mark the connection as up.
     */
    public synchronized void setUp(){
        up = true;
    }
    /**
     * Used when connection is closed.
     */
    public synchronized void setDown(){
        up = false;
    }
}
