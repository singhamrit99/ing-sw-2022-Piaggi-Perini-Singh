package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.fusesource.jansi.Ansi.ansi;
import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    private final int columns=5;
    private final int studentRows=2;
    private final int diningRows=10;
    private final int professorRow=1;
    public EnumMap<Colors, Integer> enumMap= new EnumMap<>(Colors.class);
    @Test
    void printCharacterCards() {
    }

    @Test
    void printClouds() {
    }

    @Test
    void printPlayerBoard() {
        Player player1= new Player("Serine", Towers.WHITE,2);
        EnumMap<Colors,Integer> dining= new EnumMap<Colors, Integer>(Colors.class);
        EnumMap<Colors,Integer> entrance= new EnumMap<Colors, Integer>(Colors.class);
        StrippedBoard board= new StrippedBoard(player1);
        setupEnum(dining);
        setupEntrance(entrance);
        board.setDining(dining);
        board.setEntrance(entrance);
        printPlayerBoard(board);

    }
    public void printPlayerBoard(StrippedBoard board) {
        Integer i;
        int rows=0, printColumns;
        Ansi.Color color;
        System.out.println("O----------------------O");
        System.out.println(board.getOwner() + "'s board: ");
        System.out.println("Coins: " + board.getCoins());
        System.out.println("\nDining room configuration: ");
        System.out.println("----------------------╗");
        for (Colors c : board.getDining().keySet()) {
            i=board.getDining().get(c);
            //  System.out.println("I: "+i);
            color= colorsToColor(c);
            printColumns=0;
            while(i>0)
            {   if (printColumns==0) {
                System.out.print("|");
                System.out.print(ansi().fg(color).a("*\t").reset());
                printColumns++;
            }
            else
            {
                System.out.print(ansi().fg(color).a("*\t").reset());
                printColumns++;
            }
                i--;

            }
            System.out.println();
        }
        System.out.println("O----------------------O");
        for (Colors c : board.getDining().keySet()) {
            System.out.println(c + " students: " + board.getDining().get(c));
        }
        System.out.println("O----------------------O");
        System.out.println("\nEntrance configuration: ");
        System.out.println("O-----O");
        System.out.print("|");
        for (Colors c : board.getEntrance().keySet()) {
            i=board.getEntrance().get(c);
            color= colorsToColor(c);

            while (i > 0) {
                if ((rows % 3) < 2) {
                    if (rows != 6) {
                        System.out.print(ansi().fg(color).a("* ").reset());
                        rows++;
                    } else {
                        System.out.print(ansi().fg(color).a("  *  ").reset());
                        System.out.print("|");
                    }
                } else {
                    System.out.print(ansi().fg(color).a("*").reset());
                    System.out.print("|");
                    System.out.print("\n");
                   System.out.print("|");
                    rows++;
                }
                i--;
            }

        }
        System.out.println();
        System.out.println("O-----O");
        for (Colors c : board.getEntrance().keySet()) {
            System.out.println(c + " students: " + board.getEntrance().get(c));
        }
        System.out.println("\nNumber of towers: " + board.getNumberOfTowers());
        System.out.println("\nProfessors table: ");
        for (Colors c : board.getProfessorsTable()) {
            System.out.println(c + "\n");
        }
        System.out.println("O----------------------O");
    }


    void setupEnum(EnumMap<Colors,Integer> enumMap) {
        enumMap.put(Colors.GREEN, 2);
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 3);
        enumMap.put(Colors.YELLOW, 2);
        enumMap.put(Colors.RED, 2);
    }
    void setupEntrance(EnumMap<Colors,Integer> enumMap) {
        enumMap.put(Colors.GREEN, 1);
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 2);
        enumMap.put(Colors.YELLOW, 1);
        enumMap.put(Colors.RED, 1);
    }

    public Ansi.Color colorsToColor(Colors color)
    {
        Ansi.Color colorToReturn= null;
        switch (color) {
            case RED:
                colorToReturn= Ansi.Color.RED;
                break;
            case YELLOW:
                colorToReturn= Ansi.Color.YELLOW;
                break;
            case BLUE:
                colorToReturn= Ansi.Color.CYAN;
                break;
            case GREEN:
                colorToReturn= Ansi.Color.GREEN;
                break;
            case PINK:
                colorToReturn= Ansi.Color.MAGENTA;
                break;
        }
        return colorToReturn;
    }
}