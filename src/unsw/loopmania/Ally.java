package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class Ally extends StaticEntity{
    private static int id = 0;
    private int currentId;
    private double health = 20;
    private double damage = 1;
    private boolean converted = false;

    /**
     * Constructor for Ally
     * @param position
     */
    public Ally(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        //set Ally behind character
        this.currentId = id;
        id++;
    }

    /**
     * Getter for current_id 
     */
    public int getCurrent_id() {
        return currentId;
    }

    /**
     * Setter for current_id 
     */
    public void setCurrent_id(int currentId) {
        this.currentId = currentId;
    }

    /**
     * Getter for health 
     */
    public double getHealth() {
        return health;
    }

    /**
     * Setter for health 
     */
    public void setHealth(double newHealth) {
        this.health = newHealth;
    }

    /**
     * Getter for Damage 
     */
    public double getDamage() {
        return damage;
    }

    /**
     * If this ally is converted return true, else return false
     */
    public boolean getConverted() {
        return this.converted;
    }

    /**
     * set the ally to a converted Zombie
     */
    public void setConverted(boolean converted) {
        this.converted = converted;
    }
}
