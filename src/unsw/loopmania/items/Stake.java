package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.enemies.Vampire;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Stake extends Weapon {

    /**
     * Constructor for Stake
     * @param x
     * @param y
     */
    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStat(3);
        this.setGoldWorth(45);
        this.setType("Stake");
    }

    /**
     * Damage done to vampire is more than other enemies
     * @param character
     * @param enemy
     */
    @Override
    public double activateAbility(Character character, BasicEnemy enemy) {
        
        // Critical damage given to vampire
        if (enemy instanceof Vampire) {
            return (this.getStat() * 2) - character.getDamage();
        } else { // Base damage to other enemies
            return this.getStat() - character.getDamage();
        }
    }

}
