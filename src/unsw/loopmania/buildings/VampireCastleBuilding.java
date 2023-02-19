package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a basic form of building in the world
 */
public class VampireCastleBuilding extends Building {
    
    /**
     * Constructor for VampireCastleBuilding
     * @param x
     * @param y
     */
    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setnumCycleSpawn(5);
        setType("VampireCastle");
        setCycleCount(0);
    }
}
