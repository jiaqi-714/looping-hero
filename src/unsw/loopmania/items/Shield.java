package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

public class Shield extends Armour {
    
    private Random random;
    /**
     * Constructor for Shield
     * @param x
     * @param y
     */
    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setGoldWorth(30);
        this.setType("Shield");
        // For testing purposes
        this.random = new Random(5);
    }

    /**
     * Activates Shield's attributes:
     * 100% or 0% enemy damage reduction based on chance
     * 60% less chance to receive Vampire critical attack -> refer to Vampire.java
     * @param character
     * @param enemy
     */
    @Override
    public double activateAbility(Character character, BasicEnemy enemy) {
        int randomInt = random.nextInt(100);

        // Does not block attack
        if (randomInt >= 0 && randomInt <= 39) return 0;
        // Blocks attack
        return 1;
    }

}
