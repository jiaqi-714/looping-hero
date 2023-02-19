package test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Elan;
import unsw.loopmania.enemies.Doggie;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class ElanTest {
    
    @Test
    public void dealDamageTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Elan elon = new Elan(add);
        world.addBasicEnemy(elon);

        // s.dealDamage(c);
        world.runBattles();

        // Elan damage = 9
        assertEquals(character.getHealth(), 91);
    }

    @Test
    public void ElanMovementTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Create a Elan
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(3, 3));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Elan elon = new Elan(add);
        world.addBasicEnemy(elon);
        
        for (int i = 0; i < 6; i++) {
            world.runBattles();
            
            if (i == 0) { // Move up
                assertEquals(elon.getX(), 2);
                assertEquals(elon.getY(), 3);
            }
            else if (i == 1) { // Move up
                assertEquals(elon.getX(), 1);
                assertEquals(elon.getY(), 3);
            }
            else if (i == 2) { // Move up
                assertEquals(elon.getX(), 0);
                assertEquals(elon.getY(), 3);
            }
        }
    }

    @Test
    public void ElanSpawnTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        world.getCharacter().setExperience(10000);
        assertEquals(world.getEnemies().size(), 0);
        // character have 20 cycles to spawn doggie
        for (int i = 0; i < 19; i++) {
            world.getCharacter().setCyclesCompleted();
            world.spawnBoss();
            assertEquals(world.getEnemies().size(), 0);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 19);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 1);
        assertEquals(world.getEnemies().get(0).getType(), "Doggie");
        // character have another 20 cycles to spawn elan
        for (int i = 0; i < 19; i++) {
            world.getCharacter().setCyclesCompleted();
            world.spawnBoss();
            assertEquals(world.getEnemies().size(), 1);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 39);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 3);
        assertEquals(world.getEnemies().get(0).getType(), "Doggie");
        assertEquals(world.getEnemies().get(1).getType(), "Doggie");
        assertEquals(world.getEnemies().get(2).getType(), "Elan");
    
    }

    @Test
    public void ElanHealTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Create a Elan
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(0, 2));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Elan elon = new Elan(add);
        world.addBasicEnemy(elon);

        // Create a doggie
        int currPos1 = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add1 = new PathPosition(currPos1, world.getOrderedPath());
        Doggie doge = new Doggie(add1);
        world.addBasicEnemy(doge);
        assertEquals(world.getEnemies().size(), 2);
        assertEquals(world.getEnemies().get(1).getType(), "Doggie");
        assertEquals(world.getEnemies().get(0).getType(), "Elan");
        world.runBattles();
        // assert doggie's health is at full
        assertEquals(doge.getHealth(), 45);
    }
}

