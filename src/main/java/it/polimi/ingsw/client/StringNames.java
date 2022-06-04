package it.polimi.ingsw.client;

/**
 * @author Amrit
 */
public interface StringNames {
    //used for various type of display displaying
    String TITLE = "Eryantis";
    String JOIN = "Join";

    //used for view definition
    String LOBBY = "Lobby";
    String LAUNCHER = "Launcher";
    String ROOM = "Room";
    String BOARD = "Board"; //todo remove?

    String INGAME = "InGame";
    String CREATE_NEW_ROOM = "CreateRoom";

    //used for alerts
    String NICKNAME_ALREADY_EXISTS = "Nickname is already taken";
    String CONNECTION_ERROR = "There is an issue with the connection";
    String NICKNAME_FIELD_NULL = "Nickname field is not filled";
    String NO_SUCH_ROOM = "No such room found";
    String USER_NOT_REGISTERED = "User is not registered in the server";
    String NO_LEADER = "You don't have permission to perform this action";
    String NOT_IN_ROOM = "You are not in the room";
    String ROOM_EXISTS = "Room name already taken";

    String ERROR_LOCALMODEL = "Critical bug with local model";
    String ALONE_IN_ROOM = "You are alone in this room. You must be at least 2 players to play Eryantis";
}
