package test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.PathPosition;
import unsw.loopmania.buildings.VampireCastleBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.enemies.BasicEnemy;


public class VampireCastleTest {
    @Test
    // Vampire castle spawns 1 vampire after every 5 cycles of the character
    public void VampireCastleSpawnTest() throws FileNotFoundException {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(0,0)); //top left to right
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(0,2));
        orderedPath.add(Pair.with(0,3)); //top right to bottom right
        orderedPath.add(Pair.with(1,3));
        orderedPath.add(Pair.with(2,3));
        orderedPath.add(Pair.with(3,3)); //bottom right to left
        orderedPath.add(Pair.with(3,2));
        orderedPath.add(Pair.with(3,1));
        orderedPath.add(Pair.with(3,0)); //bottom left to top left
        orderedPath.add(Pair.with(2,0));
        orderedPath.add(Pair.with(1,0));

        LoopManiaWorld world = new LoopManiaWorld(4, 4, orderedPath);
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Character character = new Character(add);
        world.setCharacter(character);
        
        VampireCastleBuilding vampireCastleBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addEnemyBuilding(vampireCastleBuilding);
        assertEquals(world.getEnemies().size(), 0);
        assertEquals(world.getCharacter().getCyclesCompleted(), 0);
        for (int i = 0; i < 4; i++) {
            world.checkCycle();
            assertEquals(world.getEnemies().size(), 0);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 4);
        world.getCharacter().setCyclesCompleted();
        world.checkCycle();
        assertEquals(world.getEnemies().size(), 1);
        for (BasicEnemy enemy : world.getEnemies()) {
            assertEquals(enemy.getType(), "Vampire");
        }
       
    }

}