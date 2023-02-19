package unsw.loopmania.items;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Staff extends Weapon {

    private Random random;

    /**
     * Constructor for Staff
     * @param x
     * @param y
     */
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStat(2);
        this.setGoldWorth(80);
        this.setType("Staff");
        this.random = new Random(5); //  87 92 74 24 6 5 54 91 22
    }

    /**
     * - Trance has a 20% chance to activate
     * - The enemy is turned into an ally for 3 ticks
     * @param c
     * @param e
     */
    @Override
    public double activateAbility(Character c, BasicEnemy e) {
        
        // A tranced enemy cannot be tranced again. 
        // Only normal enemies have the chance to be tranced
        if (e.getTranceTick() == 0 && c.getAllies().size() < 6) {
            return chanceToTrance(c, e);
        }
        // Enemy is tranced and should not be attacked
        return 0;
    }

    private double chanceToTrance(Character c, BasicEnemy e) {
        
        int r = random.nextInt(100);
        if (r <= 19 && r >= 0) {
            System.out.println("TRANCE ACTIVATED");
            // Save the stats of the enemy before converting
            e.setPreTranceDamage(e.getDamage());
            e.setPreTranceHealth(e.getHealth());
            e.addTranceTick();
            return 0;
        }
        // Normal attack performed if the chance to trance failed!
        System.out.println("Normal Staff Attack Performed");
        return this.getStat() - c.getDamage();
    }

}

