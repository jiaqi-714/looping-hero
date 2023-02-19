package test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.buildings.ZombiePitBuilding;

public class ZombiePitTest {
    @Test
    // ZombiePit spawns 1 zombie after every cycle the character completed
    public void ZombiePitSpawnTest() throws FileNotFoundException {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        
        ZombiePitBuilding zombiePitBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(3), new SimpleIntegerProperty(3));
        world.addEnemyBuilding(zombiePitBuilding);
        assertEquals(world.getEnemies().size(), 0);
        
        //Character completes 1 cycle
        world.getCharacter().setCyclesCompleted();
        world.checkCycle();

        assertEquals(world.getEnemies().size(), 1);
        for (BasicEnemy enemy : world.getEnemies()) {
            assertEquals(enemy.getType(), "Zombie");
        }

        // character completes another cycle
        world.getCharacter().setCyclesCompleted();
        world.checkCycle();

        assertEquals(world.getEnemies().size(), 2);
        for (BasicEnemy enemy : world.getEnemies()) {
            assertEquals(enemy.getType(), "Zombie");
        }

        // character completes another 5 cycles
        for (int i = 0; i < 5; i++) {
            world.getCharacter().setCyclesCompleted();
            world.checkCycle();
        }

        assertEquals(world.getEnemies().size(), 7);
        for (BasicEnemy enemy : world.getEnemies()) {
            assertEquals(enemy.getType(), "Zombie");
        }
    }
}
