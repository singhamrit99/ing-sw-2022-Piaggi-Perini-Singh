package it.polimi.ingsw;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

//Each Player has to have a deck of Assistant Cards, numbered 1 thorough 10.
public class AssistantCardDeck {

   /* public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));



    }*/
    private ArrayList<AssistantCard> assistantCards;
    private AssistantCard AssistantCard;
    private String type;

    public AssistantCard getAssistantCard(int index){

            System.out.println("I am playing the card number " +assistantCards.get(index).getValue() );

            AssistantCard= assistantCards.get(index);
            assistantCards.remove(index);

            return AssistantCard;



    }



}
