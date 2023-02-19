package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.*;

public class TreeStump extends RareItem {
    private Random random;

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("TreeStump");
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
        if (this.getSecondActive()) {
            this.getSecondAbility().activateAbility(character, enemy);
        }
        int randomInt = random.nextInt(100);
        //check if boss and add to roll chance
        if (randomInt >= 0 && randomInt <= 59 + isBoss(enemy)) {
            System.out.println("Blocked");
            return 1;
        } else {
            System.out.println("Not Blocked");
            return 0;
        }  
    }   

    public int isBoss(BasicEnemy enemy) {
        if ((enemy instanceof Doggie) || (enemy instanceof Elan)) {
            return 20;
        } else {
            return 0;
        }
    }
}
