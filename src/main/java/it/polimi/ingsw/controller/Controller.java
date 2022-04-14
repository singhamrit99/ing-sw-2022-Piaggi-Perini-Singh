package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.exceptions.MotherNatureLostException;

import java.util.ArrayList;
import java.util.EnumMap;

public class Controller {

    private Game game;

    public Controller(Game game) {
        this.game = game;
    }

    public void CallMoveMotherNature() {
        try {
            game.moveMotherNature(game.getCurrentPlayer().getNickname(),game.getCurrentPlayer().getPlayedCard().getMove());
        } catch (IncorrectStateException | IncorrectArgumentException | IncorrectPlayerException | MotherNatureLostException e) {
            e.printStackTrace();
        }

    }

    public void CallMoveStudent(EnumMap<Colors, ArrayList<String>> students)
    {
        try
        {
            game.moveStudents(game.getCurrentPlayer().getNickname(), students);

        } catch (IncorrectStateException | IncorrectArgumentException | IncorrectPlayerException e) {
            e.printStackTrace();
        }

    }

    public void CallPickCloud(int cloudTileID)
    {
        try
        {
            game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), cloudTileID);
        } catch (IncorrectStateException | IncorrectArgumentException | IncorrectPlayerException e) {
            e.printStackTrace();
        }
    }
    public void CallPlayAssistantCard(int playedCard){

        try
        {
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), playedCard);
        } catch (IncorrectStateException | IncorrectPlayerException | IncorrectArgumentException e) {
            e.printStackTrace();
        }

    }

   /* public void CallPlayCharacterCard(){

        try{

        }
    }*/

    }



