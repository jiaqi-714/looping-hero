package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.items.Stake;

import org.javatuples.Pair;

public class StakeTest {
    @Test
    public void baseDamageTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        
        // Create and equip the Staff
        character.setWeapon(new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a Slug
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);

        assertEquals(world.getEnemies().contains(slug), true);

        world.runBattles();
        
        // Slug health - Stake damage
        // = 6 - 6 = 0
        assertEquals(slug.getHealth(), 3);
        world.runBattles();
        assertEquals(world.getEnemies().isEmpty(), true);
        
        // Create a Zombie
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        
        world.runBattles();
        
        // Zombie health - Stake damage
        // = 10 - 6 = 4
        assertEquals(zombie.getHealth(), 7);
        
        world.runBattles();
        world.runBattles();
        world.runBattles();
        
        // Zombie health - Stake damage
        // = 4 - 6 = -2
        assertEquals(world.getEnemies().isEmpty(), true);

    }

    @Test
    public void vampireDamageTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        
        // Create and equip the Staff
        character.setWeapon(new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a Vampire
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);

        assertEquals(world.getEnemies().contains(vampire), true);
        
        world.runBattles();
        
        // Vampire health - Stake damage
        // = 20 - 12 = 8
        assertEquals(vampire.getHealth(), 14);
        
        world.runBattles();
        world.runBattles();
        world.runBattles();
        
        assertEquals(world.getEnemies().isEmpty(), true);

    }
}
