package unsw.loopmania.enemies;

import unsw.loopmania.PathPosition;

public class Elan extends BasicEnemy{
    public Elan(PathPosition position) {
        super(position);
        this.setHealth(60);
        this.setDamage(9);
        this.setBattleRadius(1);
        this.setSupportRadius(1);
        this.setGold(300);
        this.setExperience(1000);
        this.setType("Elan");
        this.setInBattle(false);
    }

    /**
     * Elan charges up the path, clashing with the character head on, as he has no fear
     */
    @Override
    public void move() {
        moveUpPath();
    }
}
