package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Weapon extends Item {
    
    /**
     * Constructor for Weapon
     * @param x
     * @param y
     */
    public Weapon(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }    

}
