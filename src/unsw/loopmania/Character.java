package unsw.loopmania;

import java.util.*;

import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.items.*;
import unsw.loopmania.items.ItemAbilityBehaviour;
import unsw.loopmania.modes.Mode;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity {
    private List<Ally> allies = new ArrayList<Ally>();
    private double health;
    private double damage;
    private double defense; 
    private int experience;
    private Item helmetSlot;
    private Item bodySlot;
    private Item shieldSlot;
    private Item weaponSlot;
    private Item ringSlot;
    private List<Item> potionsSlot;
    // private Healing theOneRingSlot;
    private Integer numCyclesCompleted;
    private Integer goldBalance;
    private boolean compfireBonus;
    private boolean inBattle;
    private boolean stunned;
    private String currTrait = "";

    // Potential strategy pattern. Refactoring needed.
    ItemAbilityBehaviour itemAbilityBehaviour;
    

    /**
     * Constructor for Character
     * @param position
     */
    public Character(PathPosition position) {
        super(position);
        this.health = 100;
        this.damage = 5;
        this.defense = 0;
        this.experience = 0;
        this.helmetSlot = null;
        this.bodySlot = null;
        this.shieldSlot = null;
        this.weaponSlot = null;
        this.inBattle = false;

        this.potionsSlot = new ArrayList<>();
        this.numCyclesCompleted = 0;
        this.goldBalance = 0;
        this.stunned = false;
    }

    /**
     * Sets the current trait to a chosen perk
     * @param trait
     */
    public void setTrait(String trait) {
        this.currTrait = trait;
    } 
    
    /**
     * Gets the current trait to a chosen perk
     * @return trait
     */
    public String getTrait() {
        return this.currTrait;
    } 

    /**
     * Used by by vampire for critical attack consideration
     * @return int
     */
    public int haveShield() {
        if (shieldSlot != null) {
            return 60;
        } else {
            return 0;
        }
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
        return inBattle;
    }

    /**
     * Getter for potions
     * @return List<Item>
     */
    public List<Item> getPotions() {
        return this.potionsSlot;
    }

    /**
     * Getter for allies
     * @return List<Ally>
     */
    public List<Ally> getAllies() {
        return this.allies;
    }

    /**
     * Setter for allies
     * @param allies
     */
    public void setAllies(List<Ally> allies) {
        this.allies = allies;
    }

    /**
     * Getter for health
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
        this.health = health;
        if (this.health > 100) {
            this.health = 100;
        }
    }

    /**
     * Getter for damage
     * @return damage
     */
    public double getDamage() {
        if (currTrait.equals("Stabby")) {
            return this.damage+1;
        }
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
     * Getter for defense
     * @return defense
     */
    public double getDefense() {
        if (currTrait.equals("Turtle")) {
            return this.defense+1;
        }
        return this.defense;
    }

    /**
     * Setter for defense
     * @param defense
     */
    public void setDefense(double defense) {
        this.defense = defense;
    }

    /**
     * Getter for experience
     * @return experience
     */
    public int getExperience() {
        return this.experience;
    }

    /**
     * Setter for experience
     * @param experience
     */
    public void setExperience(int experience) {
        if (currTrait.equals("Librarian")) {
            experience = experience * 2;
        }
        this.experience += experience;
    }

    /**
     * Setter for helmet
     * @param helmet
     */
    public void setHelmet(Item helmet) {
        this.helmetSlot = helmet;
    }

    /**
     * Getter for helmet
     * @param helmet
     */
    public Item getHelmet() {
        return this.helmetSlot;
    }

    /**
     * Setter for body armour
     * @param bodyArmour
     */
    public void setBody(Item bodyArmour) {
        this.bodySlot = bodyArmour;
    }

    /**
     * Getter for body armour
     */
    public Item getBody() {
        return this.bodySlot;
    }

    /**
     * Getter for helmet
     * @param helmet
     */
    public Item getShield() {
        return this.shieldSlot;
    }

    /**
     * Setter for shield
     * @param shield
     */
    public void setShield(Item shield) {
        this.shieldSlot = shield;
    }

    /**
     * Calculates and returns the amount of damage the character inflicts
     * on the enemy depending on what weapon is equipped
     * @param e
     * @return currDamage
     */
    public double activateWeapon(BasicEnemy e, Mode mode) {
        double currDamage = this.getDamage();

        if (weaponSlot != null) {
            mode.performIncreaseLimit(weaponSlot);
            currDamage += weaponSlot.activateAbility(this, e);
        }
        
        if (ringSlot != null) {
            mode.performIncreaseLimit(ringSlot);
            currDamage += ringSlot.activateAbility(this, e);
        }
        
        // Need to consider the helmet here since it reduces character's attack

        if (helmetSlot != null) {
            //Reduce hero damage by half
            currDamage *= helmetSlot.activateAbility(this, e);
        }
        return currDamage;
    }

    /**
     * Calculates the amount of defence that the character 
     * has depending on what armour is equipped
     * @param e
     * @return currAttack
     */
    public double activateArmour(BasicEnemy e, Mode mode) {

        // Tranced enemies do not harm the character
        if (e.getIsTranced()) return 0;
        
        double currAttack = e.getDamage();
        double scalarReduce = this.getDefense();
        if (currTrait.equals("Turtle")) {
            scalarReduce += 1;
        }
        
        if (this.helmetSlot != null) {
            scalarReduce += helmetSlot.activateAbility(this, e);
        }
        
        if (this.shieldSlot != null) {
            mode.performIncreaseLimit(shieldSlot);
            if (shieldSlot.activateAbility(this, e) == 1) {
                return 0;
            }
        }

        if ((this.weaponSlot instanceof Anduril)) {
            mode.performIncreaseLimit(weaponSlot);
            if (weaponSlot.activateAbility(this, e) == 1) {
                return 0;
            }
        }
        
        if (this.bodySlot != null) {
            scalarReduce += bodySlot.activateAbility(this, e);
        }
        
        currAttack -= scalarReduce;
        if (currAttack <= 0) {
            return 0;
        }
        return currAttack;
    }

    /**
     * Setter for weapon
     * @param weapon
     */
    public void setWeapon(Item weapon) {
        this.weaponSlot = weapon;
    }

    /**
     * Setter for weapon
     * @return weaponSlot
     */
    public Item getWeaponSlot() {
        return this.weaponSlot;
    }

    /**
     * Getter for ring
     * @param ring
     * @return Item
     */
    public Item getRing() {
        return this.ringSlot;
    }

    /**
     * Setter for ring
     * @param ring
     */
    public void setRing(Item ring) {
        this.ringSlot = ring;
    }

    /**
     * Getter for numCyclesCompleted
     * @return numCyclesCompleted
     */
    public Integer getCyclesCompleted() {
        return this.numCyclesCompleted;
    }

    /**
     * Setter for numCyclesCompleted
     * @param numCyclesCompleted
     */
    public void setCyclesCompleted() {
        if (currTrait.equals("Healer")) {
            this.setHealth(this.health + 5);
        } else if (currTrait.equals("BigSpender")) {
            this.setGoldBalance(this.goldBalance + 20);
        }
        this.numCyclesCompleted++;
    }

    /**
     * Getter for gold balance
     * @return goldBalance
     */
    public Integer getGoldBalance() {
        return this.goldBalance;
    }

    /**
     * Setter for gold balance
     * @param goldBalance
     */
    public void setGoldBalance(int goldBalance) {
        this.goldBalance = goldBalance;
    }

    /**
     * Setter for compfireBonus
     * @param newStatus
     */
    public void setCompfireBonus(boolean newStatus) {
        this.compfireBonus = newStatus;
    }

    /**
     * getter for compfireBonus
     * @return campfireBonus
     */
    public boolean getCompfireBonus() {
        return this.compfireBonus;
    }

    /**
     * Sets the stun status of the character
     * @param stun
     */
    public void setStun(boolean stun) {
        this.stunned = stun;
    }

    /**
     * Gets the stun status of the character
     * @return boolean
     */
    public boolean getStun() {
        return this.stunned;
    }
}
