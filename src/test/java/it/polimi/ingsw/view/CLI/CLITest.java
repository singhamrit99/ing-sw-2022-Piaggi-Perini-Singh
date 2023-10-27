package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.fusesource.jansi.Ansi.ansi;
import static org.junit.jupiter.api.Assertions.*;

class CLITest {
    private final int columns = 5;
    private final int studentRows = 2;
    private final int diningRows = 10;
    private final int professorRow = 1;
    public EnumMap<Colors, Integer> enumMap = new EnumMap<>(Colors.class);

    @Test
    void printCharacterCards() {
        EnumMap<Colors, Integer> cards = new EnumMap<>(Colors.class);
        setupCard4(cards);
        printStudentsOnCard(cards);
        setupCard5(cards);
        printStudentsOnCard(cards);

    }

    @Test
    void printClouds() {
    }

    @Test
    void printPlayerBoard() {
        Player player1 = new Player("Serine", Towers.WHITE, 2);
        EnumMap<Colors, Integer> dining = new EnumMap<>(Colors.class);
        EnumMap<Colors, Integer> entrance = new EnumMap<>(Colors.class);
        StrippedBoard board = new StrippedBoard(player1);
        setupEnum(dining);
        setupEntrance(entrance);
        printEntrance(entrance);

    }

    public void printStudentsOnCard(EnumMap<Colors, Integer> students) {
        Integer i;
        int po = 0;
        int rows = 0;
        Ansi.Color color;
        System.out.println("*-----*");
        for (Colors c : students.keySet()) {
            i = students.get(c);
            po += i;
        }
        for (Colors c : students.keySet()) {
            i = students.get(c);
            color = colorsToColor(c);
            while (rows < 6 && i > 0) {
                if (rows % 2 == 0) {
                    if (rows < po) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("* ").reset());
                    } else
                        System.out.print(ansi().a("|  ").reset());

                } else if (rows % 2 == 1) {
                    if (rows < po) {
                        System.out.print(ansi().fg(color).a("  *").reset());
                        System.out.println("|");
                    } else
                        System.out.println(ansi().a("   |").reset());

                } else {
                    if (rows < po) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("*").reset());
                        System.out.print("|");
                    }
                }
                rows++;
                i--;

            }


        }
        while (rows < 6) {
            if (rows % 2 == 0)
                System.out.print(ansi().a("|  ").reset());
            else if (rows % 2 == 1)
                System.out.println(ansi().a("   |").reset());
            rows++;
        }

        System.out.println("*-----*");
    }

    public void printPlayerBoard(StrippedBoard board) {
        Integer i;
        int rows = 0, printColumns;
        Ansi.Color color;
        System.out.println("O----------------------O");
        System.out.println(board.getOwner() + "'s board: ");
        System.out.println("Coins: " + board.getCoins());
        System.out.println("\nDining room configuration: ");
        System.out.println("----------------------╗");
        for (Colors c : board.getDining().keySet()) {
            i = board.getDining().get(c);
            //  System.out.println("I: "+i);
            color = colorsToColor(c);
            printColumns = 0;
            while (i > 0) {
                if (printColumns == 0) {
                    System.out.print("|");
                    System.out.print(ansi().fg(color).a("*\t").reset());
                    printColumns++;
                } else {
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
            i = board.getEntrance().get(c);
            color = colorsToColor(c);

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


    void setupEnum(EnumMap<Colors, Integer> enumMap) {
        enumMap.put(Colors.GREEN, 2);
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 3);
        enumMap.put(Colors.YELLOW, 2);
        enumMap.put(Colors.RED, 2);
    }

    void setupCard5(EnumMap<Colors, Integer> enumMap) {
        enumMap.put(Colors.GREEN, 1);
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 2);
        enumMap.put(Colors.YELLOW, 0);
        enumMap.put(Colors.RED, 0);
    }

    void setupCard4(EnumMap<Colors, Integer> enumMap) {
        enumMap.put(Colors.GREEN, 1);
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 1);
        enumMap.put(Colors.YELLOW, 0);
        enumMap.put(Colors.RED, 0);
    }

    void setupEntrance(EnumMap<Colors, Integer> enumMap) {
        enumMap.put(Colors.GREEN, 1);
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 0);
        enumMap.put(Colors.YELLOW, 1);
        enumMap.put(Colors.RED, 1);
    }

    public Ansi.Color colorsToColor(Colors color) {
        Ansi.Color colorToReturn = null;
        switch (color) {
            case RED:
                colorToReturn = Ansi.Color.RED;
                break;
            case YELLOW:
                colorToReturn = Ansi.Color.YELLOW;
                break;
            case BLUE:
                colorToReturn = Ansi.Color.CYAN;
                break;
            case GREEN:
                colorToReturn = Ansi.Color.GREEN;
                break;
            case PINK:
                colorToReturn = Ansi.Color.MAGENTA;
                break;
        }
        return colorToReturn;
    }

    public void printEntrance(EnumMap<Colors, Integer> entrance) {
        Integer i;
        int numofstudents = 0;
        int rows = 0;
        Ansi.Color color = null;
        System.out.println("\nEntrance configuration: ");
        System.out.println("O-----O");
        for (Colors c : entrance.keySet())
            numofstudents += entrance.get(c);
        for (Colors c : entrance.keySet()) {
            i = entrance.get(c);
            color = colorsToColor(c);

            while (rows < 7 && i > 0) {
                if (rows % 3 == 0&&rows!=6) {
                    if (rows < numofstudents) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("*").reset());
                    } else
                        System.out.print(ansi().a("|  ").reset());

                } else if (rows % 3 == 1) {
                    if (rows < numofstudents) {
                        System.out.print(ansi().fg(color).a(" *").reset());
                    } else
                        System.out.print(ansi().fg(color).a("  ").reset());
                } else if (rows % 3 == 2) {
                    if (rows < numofstudents) {
                        System.out.print(ansi().fg(color).a(" *").reset());
                        System.out.println("|");
                    } else
                        System.out.println(ansi().a("   |").reset());

                } else {
                    if (rows < numofstudents) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("  *  ").reset());
                        System.out.print("|");
                    }
                    else {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("     ").reset());
                        System.out.print("|");
                    }

                }
                rows++;
                i--;

            }


        }
        while (rows < 7) {
            if (rows % 3 == 0&&rows!=6)
                System.out.print(ansi().a("|  ").reset());
            else if (rows % 3 == 1)
                System.out.print(ansi().fg(color).a("  ").reset());
            else if (rows%3==2)
                System.out.println(ansi().a("  |").reset());
            else{
                System.out.print("|");
                System.out.print(ansi().fg(color).a("     ").reset());
                System.out.print("|");
            }

            rows++;
        }
        System.out.println();
        System.out.println("O-----O");
    }
}