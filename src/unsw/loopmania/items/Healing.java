package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Healing extends Item {
    private int healthRestore;
    
    /**
     * Constructor for Healing
     * @param x
     * @param y
     */
    public Healing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }    

    /**
     * Getter healthRestore
     * @return healthRestore
     */
    public int getHealthRestore() {
        return this.healthRestore;
    }

    /**
     * Setter for healthRestore
     * @param healthRestore
     */
    public void setHealthRestore(int healthRestore) {

    }

}
