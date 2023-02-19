package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

public class HealthPotion extends Healing {
    
    /**
     * Constructor for HealthPotion
     * @param x
     * @param y
     */
    public HealthPotion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setHealthRestore(100);
        this.setGoldWorth(20);
        this.setType("HealthPotion");
    }    

}
