package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

public class HerosCastle extends Building {

    /**
     * Constructor for Heros Castle
     * @param x
     * @param y
     */
    public HerosCastle(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("HerosCastle");
    }

    public void activateAbility(Character character) {
    }
    
}
