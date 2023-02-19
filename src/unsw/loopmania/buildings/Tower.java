package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.enemies.BasicEnemy;

/**
 * a basic form of building in the world
 */
public class Tower extends Building {
    private int range = 5;
    private int attack = 3;

    /**
     * Constructor for Tower
     * @param x
     * @param y
     */
    public Tower(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Tower");
        this.setRange(range);
    }

    /**
     * deal damage to enemy near this tower(range = 5)
     * @param enemy
     */
    @Override
    public void dealDamage(BasicEnemy enemy) {
        if (!enemy.getIsTranced()) {
            enemy.setHealth(enemy.getHealth()-attack);
        }
    }
}
