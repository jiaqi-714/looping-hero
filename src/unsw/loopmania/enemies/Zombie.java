package unsw.loopmania.enemies;
import java.util.Random;

import unsw.loopmania.Ally;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;

public class Zombie extends BasicEnemy {
    private Random random;
    /**
     * Constructor for Zombie enemy
     * @param position
     */
    public Zombie(PathPosition position) {
        super(position);
        this.setHealth(10);
        this.setDamage(5);
        this.setBattleRadius(2);
        this.setSupportRadius(3);
        this.setExperience(20);
        this.setGold(10);
        this.setType("Zombie");
        this.setInBattle(false);
        this.random = new Random(5);
        this.setHealthCap(10);
    }

    /**
     * Zombie's special ability
     * 20% chance for critical bite to occur
     * @param character
     */
    @Override
    public void activateAbility(Character character) {
        
        // Do not perform special ability if tranced
        if (getIsTranced()) return;

        // Random ran = new Random();
        // int r = ran.nextInt(100);
        int randomInt = random.nextInt(100);
        for (Ally ally : character.getAllies()) {
            if (randomInt <= 19 && randomInt >= 0) {
                System.out.println("Zombie Critical bite");
                ally.setConverted(true);
                randomInt = random.nextInt(100);
                System.out.println("Ally at " + ally.getX() + ally.getY() + " converted to Zombie");
            }
        }
    }
    
    /**
     * The zombie has a 33% chance to move up, down or nowhere
     */
    @Override
    public void move() {
        int directionChoice = this.random.nextInt(4); // 2, 0, 0, 2, 1, 3, 1, 3
        if (directionChoice == 0){
            moveUpPath();
        }
        else if (directionChoice == 1){
            moveDownPath();
        }
    }

}
