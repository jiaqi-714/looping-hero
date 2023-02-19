package test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.buildings.Campfire;

public class VampireTest {
    
    @Test
    public void dealDamageTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);

        for (int i = 0; i < 8; i++) {
            world.runBattles();

            // Normal attack of 6 damage
            if (i == 0) assertEquals(character.getHealth(), 94);
            
            // Normal attack of 6 damage
            else if (i == 1) assertEquals(character.getHealth(), 88);
            
            // Critical bite of 8 damage
            else if (i == 2) assertEquals(character.getHealth(), 80);

            // Critical bite of 10 damage
            else if (i == 3) assertEquals(character.getHealth(), 70);
        }   
    }

    @Test
    public void runBattlesTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Create a vampire
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        assertEquals(world.getEnemies().size(), 1);
        
        // Takes 4 hits to kill a vampire
        for (int i = 0; i < 4; i++) world.runBattles();
        
        assertEquals(vampire.getHealth(), 0);
        assertEquals(world.getEnemies().size(), 0);
        
    }

    @Test
    public void VampireMovementTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(3, 1));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        for (int i = 0; i < 4; i++) {
            world.runBattles();

            if (i == 1) { // down
                assertEquals(vampire.getX(), 3);
                assertEquals(vampire.getY(), 1);
            }
            else if (i == 2) { // down
                assertEquals(vampire.getX(), 3);
                assertEquals(vampire.getY(), 2);
            }
            else if (i == 3) { // up
                assertEquals(vampire.getX(), 3);
                assertEquals(vampire.getY(), 1);
            }
        }
    }

    @Test
    public void runAwayFromCampFireDownPath() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Vampire spawned just outside of campfire radius
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(0, 3));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        assertEquals(world.getEnemies().size(), 1);
        for (BasicEnemy enemy : world.getEnemies()) {
            assertEquals(enemy.getType(), "Vampire");
        }
        
        Campfire campfire = new Campfire(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        world.addAllyBuilding(campfire);
        
        //assert vampire haven't moved
        assertEquals(vampire.getX(), 0);
        assertEquals(vampire.getY(), 3);

        world.runBattles();
        
        // Vampire is in campfire radius and needs to move back out
        assertEquals(vampire.getX(), 1);
        assertEquals(vampire.getY(), 3);
        
        world.runBattles();

        // Vampire should revisit spawn position
        assertEquals(vampire.getX(), 0);
        assertEquals(vampire.getY(), 3);
    }

    @Test
    public void runAwayFromCampFireUpPath() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Vampire spawned just outside of campfire radius
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(3, 1));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        assertEquals(world.getEnemies().size(), 1);
        for (BasicEnemy be : world.getEnemies()) {
            assertEquals(be.getType(), "Vampire");
        }
        
        Campfire campfire = new Campfire(new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        world.addAllyBuilding(campfire);
        
        world.runBattles(); world.runBattles();

        //assert vampire just outside of campfire radius
        assertEquals(vampire.getX(), 3);
        assertEquals(vampire.getY(), 1);

        world.runBattles();
        
        // Vampire has moved into campfire radius
        assertEquals(vampire.getX(), 3);
        assertEquals(vampire.getY(), 2);
        
        world.runBattles();
        
        // Vampire has moved back out of campfire radius
        assertEquals(vampire.getX(), 3);
        assertEquals(vampire.getY(), 1);
        
        // System.out.println("Vx: " + v.getX() + " Vy: " + v.getY() + " Cx: " + c.getX() + " Cy: " + c.getY());
    }
}
