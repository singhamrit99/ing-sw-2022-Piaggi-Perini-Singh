package it.polimi.ingsw;

import java.util.LinkedList;

public class Game {
    private int expertMode = 0;
    private State state;
    private int numOfPlayer;
    private Player[] players;
    private int[] orderPlayers;
    private int firstPlayerPlanPhase;
    private int currentPlayer;
    private LinkedList<IslandTile> islands;
    private LinkedList<CloudTile> clouds;

    public void initializeGame(){
        expertMode = 0;
        players = new Player[numOfPlayer];
        for(int i =0; i<numOfPlayer; i++){
            orderPlayers[i]=i;
        }
        currentPlayer = 0;

        //pickCharacter()
        // inizializzare LinkedList<IslandTile> islands;
        // inizializzare LinkedList<CloudTile> clouds;

        state=State.PLANNINGPHASE;
        updateState();

    }

    public void updateState(){
        if(state==State.PLANNINGPHASE){
            if(currentPlayer<numOfPlayer){
                //Bag.drawStudents(); //and place them on a cloud
            }
            else{
                firstPlayerPlanPhase = orderPlayers[0]; //first of the next planning phase
                state=State.ACTIONPHASE; //planning phase finished, notify il player orderPlayers[0] che é la sua action phase
            }
        }
        else if(state==State.ACTIONPHASE){
            //moveStudents()
            //takeStudentsFromCloud();
            //checkUnificationIslands();
            //checkAndPlaceProfessor();
            //checkAndPlaceTower();
            //if(orderPlayers queue has length 0) state==State.ENDTURN
        }
        else if(state==State.ENDTURN){
            if(checkGameOver()){
                state=State.END;
            }
            else{
                state=State.PLANNINGPHASE;
            }
        }
        else if(state==State.END){
            checkWinner();
        }
    }


    public void nextRound(){

    }

    public void drawFromBag(){} // remove it, we don't need it

    //public void moveStudents(ArrayList<Integer> destination,HashMap<StudentDisc, Integer> students){

    //}

    public boolean checkGameOver(){
        return true;
    }

    public void playAssistantCard(){
        //it has to update the priority queue of orderPlayer[]
        currentPlayer++; // next turn into Planning Phase (clockwise)
        updateState();
    }

    public void moveMotherNature(int distance){
        //if(state==State.ACTIONPHASE) //MotherNature.moveTile(distance)
            // else {
                //exception
        //}
    }

    public void checkUnificationIslands(){

    }

    public void checkAndPlaceProfessor(){

    }

    public void checkAndPlaceTower(){

    }

    public void takeStudentsFromCloud(int index){

    }

    public void checkWinner(){

    }

    public void pickCharacter(int playerNumber){

    }

}
