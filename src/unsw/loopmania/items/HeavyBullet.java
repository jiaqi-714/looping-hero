package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;

public class HeavyBullet extends Item {
    /**
     * Constructor for Item
     * @param x
     * @param y
     */
    public HeavyBullet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("HeavyBullet");
    }    

}
