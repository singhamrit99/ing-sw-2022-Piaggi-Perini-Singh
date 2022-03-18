package it.polimi.ingsw;

import java.util.LinkedList;

public class Game {
    private int expertMode = 0;
    private State state = State.START;
    private int numOfPlayer;
    private Player[] players;
    private int[] orderPlayers;
    private int firstPlayerPlanPhase; //renamed
    private int currentPlayer;
    private LinkedList<IslandTile> islands;
    private LinkedList<CloudTile> clouds;
    private int distanceMotherNature;

    public void gameManager(){
        if(state==State.START){
            initializeGame();
        }
        else if(state==State.PLANNINGPHASE){
            while(currentPlayer<=numOfPlayer) {
                //Bag.drawStudents(); //and place them on a cloud



                //notify/listener pattern o comunque uno sleep appropriato{
                    //waiting that player playAssistanCard
                    // playAssistantCard();
                //}

                /*if(assistantCardPlayed)*/ currentPlayer++;
            }
            state=State.ACTIONPHASE; //planning phase finished
        }
        else if(state==State.ACTIONPHASE){
            //moveStudents()
            moveMotherNature(distanceMotherNature);
            //takeStudentsFromCloud();
            checkUnificationIslands();
            //checkAndPlaceProfessor();
            //checkAndPlaceTower();
        }
        else if(state==State.ENDTURN){
            checkGameOver();
        }
        else if(state==State.END){
            checkWinner();
        }
    }

    public void initializeGame(){
        expertMode = 0;
        players = new Player[numOfPlayer];
        for(int i =0; i<numOfPlayer; i++){
            orderPlayers[i]=i;
        }
        currentPlayer = 0;

    }

    public void nextRound(){

    }

    public void drawFromBag(){} // remove it, we don't need it

    //public void moveStudents(ArrayList<Integer> destination,HashMap<StudentDisc, Integer> students){

    //}

    public void checkGameOver(){

    }

    public void playAssistantCard(){
        //it has to update the priority queue of orderPlayer[]
    }

    public void moveMotherNature(int distance){

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

    public void nextPhase(){ //added

    }
}
