package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.enemies.BasicEnemy;

public class Item extends StaticEntity {
    
    private int goldWorth;
    private String type;
    private double stat;
    

    ItemAbilityBehaviour itemAbilityBehaviour;

    /**
     * Constructor for Item
     * @param x
     * @param y
     */
    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }    
    
    /**
     * Setter for type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for type
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for stat
     * @param stat
     */
    public void setStat(double stat) {
        this.stat = stat;
    }

    /**
     * Getter for stat
     * @return stat
     */
    public double getStat() {
        return this.stat;
    }

    /**
     * Getter for goldWorth
     * @return goldWorth
     */
    public int getGoldWorth() {
        return this.goldWorth;
    }

    /**
     * Setter for goldWorth
     * @param goldWorth
     */
    public void setGoldWorth(int goldWorth) {
        this.goldWorth = goldWorth;
    }

    // This should be overridden by a more specific item class
    public double activateAbility(Character character, BasicEnemy enemy) {
        return 0;
    }

    // public void setAbilityBehaviour(ItemAbilityBehaviour itemAbilityBehaviour) {
    //     this.itemAbilityBehaviour = itemAbilityBehaviour;
    // }

    // public double performAbility(Character c, BasicEnemy e) {
    //     return itemAbilityBehaviour.activateAbility(c, e);
    // }
}
