package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MoveMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

public abstract class View extends Observable<PlayerMove> implements Observer<MoveMessage> {

    private Player player;

    protected View(Player player) { this.player= player;}

    protected Player  getPlayer(){ return player;}

    protected abstract void Message(Object message);

    void HandleMove(PlayerMove playerMove)
    {
        System.out.println("move ID "+playerMove.getMove()+"\n");
        notify(new PlayerMove(player,this,playerMove.getMove()));

    }

    public void errorMessage(String message){ Message(message);}

}
