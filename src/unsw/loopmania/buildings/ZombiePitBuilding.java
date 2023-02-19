package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a basic form of building in the world
 */
public class ZombiePitBuilding extends Building {
    
    /**
     * Constructor for ZombiePitBuilding
     * @param x
     * @param y
     */
    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setnumCycleSpawn(1);
        setType("ZombiePit");
        setCycleCount(0);
    }
}
