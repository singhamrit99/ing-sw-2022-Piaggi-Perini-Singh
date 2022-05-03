package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;
import it.polimi.ingsw.model.exceptions.ProfessorNotFoundException;

import java.util.EnumMap;

public class CharacterCard extends Card {
    private int status;
    private int price;
    private String description;
    private Type type;
    private Ability ability;
    private Requirements requirements;

    public CharacterCard(int price, String description) {
        this("", price, description);
    }

    public CharacterCard(String imageName, int price, String description) {
        super(imageName);
        this.price = price;
        this.description = description;

        status = 0;
    }

    public CharacterCard(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        this(imageName, startingPrice, description);

        this.type = type;
        this.ability = ability;
        this.requirements = requirements;
    }

    public String getImageName() {
        return super.getImageName();
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Ability getAbility() {
        return ability;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public void activate(Game game) throws ProfessorNotFoundException, NegativeValueException, IncorrectArgumentException {
        status = 1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setChoiceIndex(int choiceIndex) {
    }

    public void increasePrice() {
        price++;
    }

    public void setEnums(EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
    }

    public void setChoices(int student, int island) {
    }
}
