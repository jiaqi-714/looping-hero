package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class SniperRifle extends Weapon {

    /**
     * Constructor for Sword
     * @param x
     * @param y
     */
    public SniperRifle(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStat(6);
        this.setGoldWorth(100);
        this.setType("SniperRifle");
    }

    /**
     * Ability to add damage to the Character
     * @param character
     * @param enemy
     */
    @Override
    public double activateAbility(Character character, BasicEnemy enemy) {
        // Deals 6 damage
        return this.getStat() - character.getDamage();
    }

}
