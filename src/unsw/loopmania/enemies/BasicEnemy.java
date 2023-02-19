package unsw.loopmania.enemies;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.MovingEntity;
import unsw.loopmania.PathPosition;

/**
 * a basic form of enemy in the world
 */
public class BasicEnemy extends MovingEntity {
    private double health;
    private double healthCap;
    private double damage;
    private int battleRadius;
    private int supportRadius;
    private int experience;
    private int gold;
    private String type;
    private boolean inBattle;
    private Random random;

    private int tranceTick;
    private boolean isTranced;
    private double preTranceHealth;
    private double preTranceDamage;
    private SimpleIntegerProperty integerTrance;

    /**
     * Constructor for BasicEnemy
     * @param position
     */
    public BasicEnemy(PathPosition position) {
        super(position);
        this.random = new Random(5);
        this.tranceTick = 0;
        this.integerTrance = new SimpleIntegerProperty(0);
    }

    /**
     * Move the enemy
     */
    public void move(){
        // TODO = modify this, since this implementation doesn't provide the expected enemy behaviour
        // this basic enemy moves in a random direction... 25% chance up or down, 50% chance not at all...
        int directionChoice = this.random.nextInt(2); // 1, 0, 0, 1, 0, 1, 0, 1
        if (directionChoice == 0){
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
        }
    }

    /**
     * Getter for supportRadius
     * @return supportRadius
     */
    public int getSupportRadius() {
        return this.supportRadius;
    }

    /**
     * Setter for supportRadius
     * @return supportRadius
     */
    public void setSupportRadius(int supportRadius) {
        this.supportRadius = supportRadius;
    }

    /**
     * Getter for battleRadius
     * @return battleRadius
     */
    public int getBattleRadius() {
        return this.battleRadius;
    }

    /**
     * Setter for battleRadius
     * @param battleRadius
     */
    public void setBattleRadius(int battleRadius) {
        this.battleRadius = battleRadius;
    }

    /**
     * Getter for damage
     * @return damage
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Setter for damage
     * @param damage
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Setter for damage
     * @return health
     */
    public double getHealth() {
        return this.health;
    }

    /**
     * Setter for health
     * @param health
     */
    public void setHealth(double health) {
        if (!getIsTranced()) {
            this.health = health;
        }
    }

    /**
     * Sets the health for a tranced enemy specifically
     * Is segregated from setHealth method in order to reduce the amount of checks required
     * when setting the health for a normal enemy
     * @param health
     */
    public void setTrancedHealth(double health) {
        this.health = health;
    }

    // Overridden for each of the basic enemy's abilities
    // This is declared before every
    public void activateAbility(Character c) {
    }

    public boolean getIsTranced() {
        return isTranced;
    }

    public void setIsTranced(boolean isTranced) {
        this.isTranced = isTranced;
    }

    public void setPreTranceDamage(double preTranceDamage) {
        this.preTranceDamage = preTranceDamage;
    }

    public void setPreTranceHealth(double preTranceHealth) {
        this.preTranceHealth = preTranceHealth;
    }

    public void handleTrance() {
        
        if (isTranced) addTranceTick();

        // First tick of trance
        if (tranceTick == 1) {
            // Set the characteristics of the enemy to an Ally
            setDamage(1);
            setHealth(20);
            isTranced = true;
            // type = "TrancedEnemy";
        }

        // Last tick of trance
        if (getTranceTick() == 4) {
            // Enemy stats reset back to the tick before they were tranced
            damage = preTranceDamage;
            health = preTranceHealth;
            resetTranceTick();
            setIsTranced(false);
        }

    }

    /**
     * Getter for experience
     * @return experience
     */
    public int getExperience() {
        return this.experience;
    }

    /**
     * Setter for damage
     * @return experience
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Getter for gold
     * @return gold
     */
    public int getGold() {
        return this.gold;
    }

    /**
     * Setter for gold
     * @param gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Setter for damage
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for type
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for inBattle
     * @param inBattle
     */
    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    /**
     * Getter for inBattle
     * @return inBattle
     */
    public boolean getInBattle() {
        return this.inBattle;
    }

    public void setHealthCap(double healthCap) {
        this.healthCap = healthCap;
    }

    public double getHealthCap() {
        return this.healthCap;
    }

    /**
     * If this enemy is converted into an ally return true, else return false
     */
    public int getTranceTick() {
        return this.tranceTick;
    }

    public SimpleIntegerProperty getIntegerTrance() {
        return this.integerTrance;
    }

    /**
     * set the enemy to a converted ally
     */
    public void addTranceTick() {
        this.tranceTick++;
        this.integerTrance.set(tranceTick);        
    }

    public void resetTranceTick() {
        this.tranceTick = 0;
    }

}
