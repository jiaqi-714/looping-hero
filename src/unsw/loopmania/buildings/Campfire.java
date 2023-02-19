package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;

/**
 * a basic form of building in the world
 */
public class Campfire extends Building {
    private int range = 5;

    /**
     * Constructor for Campfire
     * @param x
     * @param y
     */
    public Campfire(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Campfire");
        this.setRange(range);
    }

    /**
     * Activitate the double damage for character in range of Campfire
     * @param character
     */
    @Override
    public void activateAbility(Character character){
        if (character.getCompfireBonus() == false) {
            character.setDamage(character.getDamage()*2);
            character.setCompfireBonus(true);
        }
    }
}
