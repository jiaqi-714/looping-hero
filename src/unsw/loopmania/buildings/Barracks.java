package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Ally;
import unsw.loopmania.Character;

/**
 * a basic form of building in the world
 */
public class Barracks extends Building {
    private int range = 0;
    public Barracks(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Barracks");
        this.setRange(range);
    }

    /**
     * When character pass a Barrack, it will get a Allied Soldier
     * @param character
     */
    @Override
    public void activateAbility(Character character) { 
        // character will not have more than 6 ally
        // v-- This implementation for moving entity ally 
        // c.getAllies().add(new Ally2(c.getPosition())); 

        SimpleIntegerProperty x = new SimpleIntegerProperty(this.getX());
        SimpleIntegerProperty y = new SimpleIntegerProperty(this.getY());
        character.getAllies().add(new Ally(x, y));  
        
    }
}
