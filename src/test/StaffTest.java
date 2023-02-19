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
import unsw.loopmania.items.Staff;

import org.javatuples.Pair;

public class StaffTest {
    @Test
    public void damageTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create and equip the Staff
        character.setWeapon(new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        // Need to consider the staff inflicting trance with seed value
        world.runBattles();
        
        // Zombie health - Staff damage
        // = 10 - 4 = 6
        assertEquals(zombie.getHealth(), 8);

    }

    @Test
    public void aliveTranceTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        character.setHealth(1000);

        // Create and equip the Staff
        character.setWeapon(new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a Slug 
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        slug.setHealth(100);
        
        // Create a Vampire
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        vampire.setHealth(100);

        // Create a Zombie
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 0));
        add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        zombie.setHealth(100);

        for (int currTick = 0; currTick < 10; currTick++) {
            world.runBattles();
            // Trance has started
            if (currTick == 1) {
                assertEquals(false, slug.getIsTranced());
                
                assertEquals(true, vampire.getIsTranced());
                assertEquals(20, vampire.getHealth());
                assertEquals(1, vampire.getDamage());

                assertEquals(true, zombie.getIsTranced());
                assertEquals(20, vampire.getHealth());
                assertEquals(1, vampire.getDamage());
            }

            // Trance has worn off
            // Enemies regain their original health before the trance
            if (currTick == 4) {
                assertEquals(false, vampire.getIsTranced());
                assertEquals(97, vampire.getHealth());
                assertEquals(6, vampire.getDamage());

                assertEquals(false, zombie.getIsTranced());
                assertEquals(98, zombie.getHealth());
                assertEquals(5, zombie.getDamage());
            }
        }


    }

    @Test
    public void deadTranceTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        character.setHealth(1000);
        
        // Create and equip the Staff
        character.setWeapon(new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a Slug 
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        slug.setHealth(6);
        
        // Create a Vampire
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        vampire.setHealth(100);

        // Create a Zombie
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 0));
        add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        zombie.setHealth(100);

        for (int currTick = 0; currTick < 4; currTick++) {
            world.runBattles();
            // Slug has died and therefore the fight ends. All tranced enemies die.
            if (currTick == 3) {
                assertEquals(0, world.getEnemies().size());
                assertEquals(true, world.getEnemies().isEmpty());
            }
        
        }

    }

}

