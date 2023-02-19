package unsw.loopmania.enemies;

import java.util.Random;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Character;

public class Slug extends BasicEnemy {

    private Random random;
    
    /**
     * Constructor for Slug enemy
     * @param position
     */
    public Slug(PathPosition position) {
        super(position);
        this.setHealth(6);
        this.setDamage(3);
        this.setBattleRadius(1);
        this.setSupportRadius(1);
        this.setGold(5);
        this.setExperience(10);
        this.setType("Slug");
        this.setInBattle(false);
        this.random = new Random(5);
        this.setHealthCap(6);
    }

    @Override
    public void activateAbility(Character charcter) {
        // Do not perform special ability if tranced
        // inherit the characteristics of an ally
        if (getTranceTick() != 0) {
            super.activateAbility(charcter);
            return;
        }
    }
}
