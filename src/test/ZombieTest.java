package test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class ZombieTest {
    
    @Test
    public void zombieMovementTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(3, 3));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        
        for (int i = 0; i < 8; i++) {
            world.runBattles();

            if (i == 0) { // Stay
                assertEquals(zombie.getX(), 3);
                assertEquals(zombie.getY(), 3);
            }
            else if (i == 1) { // Move down
                assertEquals(zombie.getX(), 2);
                assertEquals(zombie.getY(), 3);
            }
            else if (i == 6) { // Move up
                assertEquals(zombie.getX(), 3);
                assertEquals(zombie.getY(), 3);
            }
        }
    }
}
