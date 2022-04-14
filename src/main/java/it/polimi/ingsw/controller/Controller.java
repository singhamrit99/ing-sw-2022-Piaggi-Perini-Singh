package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.enumerations.State;

public class Controller {

    private Game game;

    public Controller(Game game) {
        this.game = game;
    }


    public void playermove(PlayerMove playerMove) {
            if(game.getCurrentPlayer()!=playerMove.getPlayer()&&game.getCurrentState()!= State.PLANNINGPHASE)
            {
                playerMove.getView().errorMessage("It's not your turn to play!\n");
                return;
            }
            //Depending on the move the player wants to do there will be more checks
            switch (playerMove.getMove())
            {
                //Play assistant card
                case 1:
                {



                }
                //Play character card
                case 2:
                {

                }
                //Move students
                case 3:
                {

                }
                //Move mother Nature
                case 4:
                {

                }
                //Take students from cloud
                case 5:
                {

                }


            }


    }


}
