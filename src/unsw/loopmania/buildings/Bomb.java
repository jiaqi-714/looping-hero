package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

public class Bomb extends Building{
    /**
     * Constructor for Bomb
     * @param x
     * @param y
     */
    public Bomb(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setType("Bomb");
    }
}
