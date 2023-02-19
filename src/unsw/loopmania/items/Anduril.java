package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.*;

public class Anduril extends RareItem {
    private boolean increaseAttackPower = false;
    
    /**
     * Constructor for Shield
     * @param x
     * @param y
     */
    public Anduril(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStat(10);
        this.setType("Anduril");
    }

    /**
     * Activates Sword's attributes, then shields when activate armour is called
     * Activates Shield's attributes:
     * 100% or 0% enemy damage reduction based on chance
     * 60% less chance to receive Vampire critical attack -> refer to Vampire.java
     * @param character
     * @param enemy
     */
    @Override
    public double activateAbility(Character character, BasicEnemy enemy) {
        if (this.getSecondActive()) {
            if (!this.increaseAttackPower) {
                this.increaseAttackPower = true;
                if ((enemy instanceof Doggie) || (enemy instanceof Elan)) {
                    return (this.getStat()*3);
                }
                return this.getStat();
            } else {
                this.increaseAttackPower = false;
                return this.getSecondAbility().activateAbility(character, enemy);         
            }
        }
        // Triple damage is type is boss
        if ((enemy instanceof Doggie) || (enemy instanceof Elan)) {
            return (this.getStat()*3);
        }
        return this.getStat();
    }
}
