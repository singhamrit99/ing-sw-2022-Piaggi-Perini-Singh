package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.FullDiningException;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Actions;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;
import it.polimi.ingsw.view.GUI.controllerFX.Controller;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class StudentCharacter extends CharacterCard implements Serializable {
    private EnumMap<Colors, Integer> students;
    private Bag bag;

    private int studentIndex;
    private int islandIndex;
    private EnumMap<Colors, Integer> students1;
    private EnumMap<Colors, Integer> students2;

    /**
     * StudentCharacter constructor
     *
     * @param imageName     Taken from father CharacterCard in CharacterCardFactory.
     * @param startingPrice Taken from father CharacterCard in CharacterCardFactory.
     * @param description   Taken from father CharacterCard in CharacterCardFactory.
     * @param type          Taken from father CharacterCard in CharacterCardFactory.
     * @param ability       Taken from father CharacterCard in CharacterCardFactory.
     * @param requirements  Taken from father CharacterCard in CharacterCardFactory.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public StudentCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) throws NegativeValueException {
        super(imageName, startingPrice, description, type, ability, requirements);
        bag = Bag.getInstance();
        studentIndex = -1;
        islandIndex = -1;
        students1 = StudentManager.createEmptyStudentsEnum();
        students2 = StudentManager.createEmptyStudentsEnum();
        students = StudentManager.createEmptyStudentsEnum();

        try {
            students = bag.drawStudents(this.getType().getValue());
        } catch (NegativeValueException e) {
            throw new NegativeValueException();
        } catch (IncorrectArgumentException e) {
            Controller.showErrorDialogBox(StringNames.INCORRECT_ARGUMENT);
        }
    }

    /**
     * Method used to add students to the card.
     *
     * @param studentsToAdd Students to add.
     * @throws IncorrectArgumentException Thrown when the provided EnumMap is null or incorrect.
     */
    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getStudents(), studentsToAdd);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    /**
     * Method used to remove students to the card.
     *
     * @param studentsToRemove Students to remove
     * @throws IncorrectArgumentException Thrown when the provided EnumMap is null or incorrect.
     */
    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(students, studentsToRemove);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    /**
     * Checks if the provided EnumMap is correct, and if it is checks if it has enough students to perform the requested action.
     *
     * @param students The students EnumMap to check
     * @return boolean value of the check or an exception.
     * @throws IncorrectArgumentException Thrown when the EnumMap is incorrect.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     */
    public boolean hasEnoughStudents(EnumMap<Colors, Integer> students) throws NegativeValueException, IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = getStudents();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IncorrectArgumentException("EnumMap is not correct");
                }
            } else {
                throw new NegativeValueException("EnumMap is not correct");
            }
        }
        return true;
    }

    /**
     * Activate method for StudentCharacter cards
     *
     * @param game The game in which the card is being activated
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown when the values required for card power activation are incorrect.
     * @throws ProfessorNotFoundException Thrown if the professor assignment method fails to find the related professor, either because of a color
     *                                    mismatch or other internal error.
     */
    @Override
    public void activate(Game game) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, FullDiningException {
        Actions action = this.getAbility().getAction();
        PropertyChangeEvent event;
        switch (action) {
            case ADD_ISLAND:
                EnumMap<Colors, Integer> studentsToAddIsland = StudentManager.createEmptyStudentsEnum();
                studentsToAddIsland.put(Colors.getStudent(studentIndex), 1);

                if (hasEnoughStudents(studentsToAddIsland)) {
                    game.notifyIsland(game.getIsland(islandIndex), studentsToAddIsland);
                    removeStudents(studentsToAddIsland);
                    addStudents(bag.drawStudents(1));
                    setStatus(2);
                    game.notifyCharacterEvent(game.getCurrentPlayer().getPlayedCharacterCard());
                }
                break;
            case SWAP_ENTRANCE:
                int count = 0;
                int count1 = 0;

                for (Map.Entry<Colors, Integer> set : students1.entrySet()) count += set.getValue();

                if (getAbility().getValue() < count) {
                    game.getCharacterCards().get(game.getSelectedCharacterIndex()).setStatus(0);
                    game.getCurrentPlayer().setPlayedCharacterCard(null);
                    throw new IncorrectArgumentException("Too many students are given");
                }

                for (Map.Entry<Colors, Integer> set : students2.entrySet()) count1 += set.getValue();

                if (count != count1) {
                    game.getCharacterCards().get(game.getSelectedCharacterIndex()).setStatus(0);
                    game.getCurrentPlayer().setPlayedCharacterCard(null);
                    throw new IncorrectArgumentException("The given students number do not match");
                }

                game.getCurrentPlayer().getSchoolBoard().removeStudents(students2);
                game.getCurrentPlayer().getSchoolBoard().addStudents(students1);
                removeStudents(students1);
                addStudents(students2);
                setStatus(2);

                event =
                        new PropertyChangeEvent(this, "entrance", game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getSchoolBoard().getEntrance());
                game.getGameListener().propertyChange(event);

                game.notifyCharacterEvent(game.getCurrentPlayer().getPlayedCharacterCard());
                break;
            case SWAP_ENTRANCE_DINING:
                count = 0;
                count1 = 0;

                for (Map.Entry<Colors, Integer> set : students1.entrySet()) count += set.getValue();
                if (getAbility().getValue() < count) {
                    game.getCharacterCards().get(game.getSelectedCharacterIndex()).setStatus(0);
                    game.getCurrentPlayer().setPlayedCharacterCard(null);
                    throw new IncorrectArgumentException("Too many students are given");
                }

                for (Map.Entry<Colors, Integer> set : students2.entrySet()) count1 += set.getValue();
                if (count != count1) {
                    game.getCharacterCards().get(game.getSelectedCharacterIndex()).setStatus(0);
                    game.getCurrentPlayer().setPlayedCharacterCard(null);
                    throw new IncorrectArgumentException("The given students number do not match");
                }

                int oldCoins = game.getCurrentPlayer().getCoins(), newCoins;
                game.getCurrentPlayer().getSchoolBoard().moveStudents(students1);
                game.getCurrentPlayer().getSchoolBoard().removeDiningStudents(students2);
                game.getCurrentPlayer().getSchoolBoard().addStudents(students2);
                game.checkAndPlaceProfessor();
                setStatus(2);

                newCoins = game.getCurrentPlayer().getCoins();
                if (newCoins != oldCoins) {
                    PropertyChangeEvent coinsAddedEvent =
                            new PropertyChangeEvent(this, "coins", game.getCurrentPlayer().getNickname(), newCoins);
                    game.getGameListener().propertyChange(coinsAddedEvent);
                }

                event =
                        new PropertyChangeEvent(this, "entrance", game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getSchoolBoard().getEntrance());
                game.getGameListener().propertyChange(event);

                event =
                        new PropertyChangeEvent(this, "dining", game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getSchoolBoard().getDining());
                game.getGameListener().propertyChange(event);
                break;
            case ADD_DINING:
                EnumMap<Colors, Integer> studentsToAddDining = StudentManager.createEmptyStudentsEnum();
                studentsToAddDining.put(Colors.getStudent(studentIndex), 1);

                if (hasEnoughStudents(studentsToAddDining)) {
                    game.getCurrentPlayer().getSchoolBoard().addStudentsDining(studentsToAddDining);
                    game.checkAndPlaceProfessor();
                    removeStudents(studentsToAddDining);
                    addStudents(bag.drawStudents(getAbility().getValue()));
                    setStatus(2);

                    event =
                            new PropertyChangeEvent(this, "dining", game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getSchoolBoard().getDining());
                    game.getGameListener().propertyChange(event);

                    game.notifyCharacterEvent(game.getCurrentPlayer().getPlayedCharacterCard());
                }
                break;
            default:
                break;
        }
    }

    /**
     * Setter methods for students
     *
     * @param students Students to set.
     */
    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    /**
     * Getter method for students field
     *
     * @return Students
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    /**
     * SetChoices method implemented in StudentCharacter card
     *
     * @param student The student, passed as an int (Color)
     * @param island  The island, passed as an int (number)
     */
    public void setChoices(int student, int island) {
        studentIndex = student;
        islandIndex = island;
    }

    /**
     * SetEnum method implemented in StudentCharacter card
     *
     * @param students1 first student enum.
     * @param students2 second student enum.
     */
    public void setEnums(EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
        this.students1 = students1;
        this.students2 = students2;
    }

    /**
     * studentIndex setter method
     *
     * @param index provided value for index.
     */
    public void setChoiceIndex(int index) {
        this.studentIndex = index;
    }
}
