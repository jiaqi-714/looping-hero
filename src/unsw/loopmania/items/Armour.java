package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Armour extends Item {
    
    /**
     * Constructor for Armour
     * @param x
     * @param y
     */
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }    

}
