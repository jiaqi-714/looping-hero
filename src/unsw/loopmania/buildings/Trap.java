package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.enemies.BasicEnemy;

public class Trap extends Building {
    
    private int range = 0;
    boolean isActivated = false;
    int attack = 5;

    /**
     * Constructor for Trap
     * @param x
     * @param y
     */
    public Trap(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Trap");
        this.setRange(range);
    }
    
     /**
     * deal damage to enemy when they pass this trap (damage = 5)
     * @param enemy
     */
    @Override
    public void dealDamage(BasicEnemy enemy) {
        enemy.setHealth(enemy.getHealth()-attack);
        isActivated = true;
    }
}
