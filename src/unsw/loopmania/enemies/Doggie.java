package unsw.loopmania.enemies;

import java.util.Random;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Character;

public class Doggie extends BasicEnemy{
    private Random random;
    private int attackCount;
    
    public Doggie(PathPosition position) {
        super(position);
        this.setHealth(45);
        this.setDamage(5);
        this.setBattleRadius(1);
        this.setSupportRadius(1);
        this.setGold((new Random(5).nextInt(5)) + 1);
        this.setExperience(500);
        this.setType("Doggie");
        this.setInBattle(false);
        this.random = new Random(5);
        this.setHealthCap(45);
    }

    /**
     * Doggie runs away from the character as he is afriad to be convert into doge coin but stops once 
     * in a while to check how far the character as caught up.
     */
    @Override
    public void move() {
        int chance = random.nextInt(4); //2 0 0 2 1 3
        if (chance < 3) {
            moveDownPath();
        }
    }

    public boolean abilityStun() {
        if (this.attackCount % 2 == 1) {
            return true;
        }
        return false;
    }

    public void attackCountPlus() {
        this.attackCount++;
    }

    @Override
    public void activateAbility(Character character) {
        attackCountPlus();
        if (abilityStun()) {
            character.setStun(true);
        }
    }
}
