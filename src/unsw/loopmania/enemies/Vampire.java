package unsw.loopmania.enemies;

import java.util.List;
import java.util.Random;

import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.buildings.Building;

public class Vampire extends BasicEnemy {
    
    private boolean rageMode;
    private int randAttacks;
    private int curRandAttacks;
    private Random random;
    
    /**
     * Constructor for Vampire enemy
     * @param position
     */
    public Vampire(PathPosition position) {
        super(position);
        this.setHealth(20);
        this.setDamage(6);
        this.setBattleRadius(2);
        this.setSupportRadius(3);
        this.setGold(25);
        this.setExperience(50);
        this.rageMode = false;
        this.randAttacks = 0;
        this.curRandAttacks = 0;
        this.setType("Vampire");
        // For reliable testing
        this.random = new Random(5);
        this.setHealthCap(20);
    }

    /**
     * Vampire has a random chance to critically bite the Character
     * Inflicting a random damage in the range of 8 - 10 for a random
     * number of attacks (1 - 4)
     * @param character
     */
    @Override
    public void activateAbility(Character character) {

        // Do not perform special ability if tranced
        if (getIsTranced()) return;

        // chance for ability to activate
        if (this.curRandAttacks == 0) {
            int randomInt = random.nextInt(100); // 87, 92, 74, 24, 6, 5, 54, 91
            if (randomInt <= (79 - character.haveShield()) && randomInt >= 0) {
                this.rageMode = true;
                // Note that the actual random attack will be 1-4
                // but for the condition to be met, the upper and lower
                // range must be increased by 1
                this.randAttacks = random.nextInt(4) + 2; // 2, 0, 0, 2, 1, 3, 1, 3
            }
        }
        
        // This decides the amount of additional damage
        if (this.rageMode == true) {
            // Damage can be 8-10
            int damageRange = random.nextInt(3) + 8; // 2, 1, 2, 2, 0, 2, 1, 2
            this.setDamage(damageRange);
            this.curRandAttacks++;
        }
        
        // Reset
        if (this.rageMode == true && this.curRandAttacks == this.randAttacks) {
            this.curRandAttacks = 0;
            this.randAttacks = 0;
            this.rageMode = false;
            // Default damage
            this.setDamage(6);
        }

        // c.setHealth(c.getHealth() - ((1 - c.getDefense()) * this.getDamage()));
    }

    /**
     * Vampires run away from campfires
     * @param building
     */
    public void move(List<Building> building) {
        // If vampire is in range of campfire (radius of 3)

        if (!building.isEmpty()) {
            boolean vampireInFire = false;
            for (Building ab2 : building) {
                if (ab2.getType().equals("Campfire")) {
                    // if vampire is within range of the campfire
                    if (Math.pow((this.getX()-ab2.getX()), 2) +  Math.pow((this.getY()-ab2.getY()), 2) <= 4) {
                        // find optimal path to escape
                        moveDownPath();
                        double distanceDown = Math.pow((this.getX()-ab2.getX()), 2) +  Math.pow((this.getY()-ab2.getY()), 2);
                        moveUpPath();
                        moveUpPath();
                        double distanceUp = Math.pow((this.getX()-ab2.getX()), 2) +  Math.pow((this.getY()-ab2.getY()), 2);
                        moveDownPath();
                        
                        if (distanceDown >= distanceUp) {
                            moveDownPath();
                        }
                        else {
                            moveUpPath();
                        }
                        vampireInFire = true;
                        break;
                    }
                }
                
            }
            // after checking all campfire, the vampire is not in range
            // goes randomly
            if (vampireInFire == false){
                int directionChoice = random.nextInt(2); // 1 0 0 1 0 1 0 1
                if (directionChoice == 0){
                    moveUpPath();
                }
                else if (directionChoice == 1){
                    moveDownPath();
                }
            }
        }
        else{
            int directionChoice = random.nextInt(2); // 1 0 0 1 0 1 0 1
            if (directionChoice == 0){
                moveUpPath();
            }
            else if (directionChoice == 1){
                moveDownPath();
            }
        }
    }
}
