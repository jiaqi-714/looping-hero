package unsw.loopmania.buildings;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Character;

public abstract class Building extends StaticEntity {
    private int range;
    private String type;
    private int cycleCount;
    private int numCycleSpawn;

    /**
     * Constructor for Building
     * @param x
     * @param y
     */
    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    /**
     * Getter for type
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Getter for range
     * @return range
     */
    public int getRange() {
        return range;
    }

    /**
     * Setter for range
     * @return range
     */
    public void setRange(int range) {
        this.range = range;
    }


    public  void activateAbility(Character character){
    }

    public void dealDamage(BasicEnemy enemy){
    }

    /**
     * Updates the current cycle
     * @return boolean
     */
    public boolean updateCycle() {
        this.cycleCount += 1;
        if (this.cycleCount % this.numCycleSpawn == 0) {
            return true;
        }
        return false;
    }

    /**
     * Setter for numCycleSpawn
     * @param numCycleSpawn
     */
    public void setnumCycleSpawn(int numCycleSpawn) {
        this.numCycleSpawn = numCycleSpawn;
    }

    /**
     * Setter for numCycleSpawn
     * @param cycleCount
     */
    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }
}
