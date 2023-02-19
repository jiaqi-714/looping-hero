package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;

/**
 * a basic form of building in the world
 */
public class Village extends Building {
    
    private int healthIncrease = 25;
    private int range = 0;

    /**
     * Constructor for Village Building
     * @param x
     * @param y
     */
    public Village(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Village");
        this.setRange(range);
    }

    /**
     * Village heals the character health (25 point of health) per tick
     * @param character
     */
    @Override
    public void activateAbility(Character character) {
        character.setHealth(character.getHealth()+healthIncrease);
        if (character.getHealth() > 100) {
            character.setHealth(100);
        }
    }
}
