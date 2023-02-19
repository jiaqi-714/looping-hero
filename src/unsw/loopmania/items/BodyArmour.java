package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

public class BodyArmour extends Armour {

    /**
     * Constructor for BodyArmour
     * @param x
     * @param y
     */
    public BodyArmour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        // this.setS
        this.setGoldWorth(65);
        this.setType("BodyArmour");
    }    

    /**
     * Halves the enemy's attack when equipped
     * @param character
     * @param enemy
     */
    @Override
    public double activateAbility(Character character, BasicEnemy enemy) {
        // Enemy attacks halved
        return enemy.getDamage() / 2;
    }

}
