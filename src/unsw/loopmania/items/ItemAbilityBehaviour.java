package unsw.loopmania.items;

import unsw.loopmania.Character;
import unsw.loopmania.enemies.BasicEnemy;

public interface ItemAbilityBehaviour {
    public double activateAbility(Character character, BasicEnemy enemy);
}
