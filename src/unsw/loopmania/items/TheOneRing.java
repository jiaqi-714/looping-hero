package unsw.loopmania.items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

public class TheOneRing extends RareItem {
    public boolean oneTimeUse = false;

    /**
     * Constructor for TheOneRing
     * @param x
     * @param y
     */
    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("TheOneRing");
    }    

    /**
     * Revives the character if they die
     * @param character
     */
    public double activateAbility(Character character, BasicEnemy enemy) {
        if (character.getHealth() <= 0 && !this.oneTimeUse) {
            character.setHealth(100); 
            this.oneTimeUse = true; 
            System.out.println("Activate Revive");  
        } else if (character.getHealth() > 0 && this.getSecondActive()) {
            System.out.println("Activate Rings Special");
            return this.getSecondAbility().activateAbility(character, enemy);       
        }
        return 0;
    }   
}
