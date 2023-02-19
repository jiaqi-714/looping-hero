package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

public class Helmet extends Armour {
    private boolean reduceAttackPower;

    /**
     * Constructor for Helmet
     * @param x
     * @param y
     */
    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setGoldWorth(60);
        this.setType("Helmet");
        this.reduceAttackPower = false;
    }    

    /**
     * Halves the Character's attack when equipped
     * Enemy attacks are reduced by a scalar value
     * @param character
     * @param enemy
     */
    @Override
    public double activateAbility(Character character, BasicEnemy enemy) {       
        // Character attacks halved
        if (!this.reduceAttackPower) {
            this.reduceAttackPower = true;
            return 0.5;
        } else { // defence reduced by scalar
            this.reduceAttackPower = false;
            return 2;
        }   
    }

}
