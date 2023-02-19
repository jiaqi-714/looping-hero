package test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Doggie;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class DoggieTest {
    
    @Test
    public void dealDamageTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);

        // s.dealDamage(c);
        world.runBattles();

        // Doggie damage = 5
        assertEquals(character.getHealth(), 95);
    }

    @Test
    public void DoggieMovementTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Create a doggie
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(3, 3));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);
        // 2 0 0 2 1 3
        for (int i = 0; i < 6; i++) {
            world.runBattles();
            
            if (i == 0) { // Move down
                assertEquals(doge.getX(), 3);
                assertEquals(doge.getY(), 2);
            }
            else if (i == 1) { // down
                assertEquals(doge.getX(), 3);
                assertEquals(doge.getY(), 1);
            }
            else if (i == 2) { // down
                assertEquals(doge.getX(), 3);
                assertEquals(doge.getY(), 0);
            }
            else if (i == 3) { // down
                assertEquals(doge.getX(), 2);
                assertEquals(doge.getY(), 0);
            }
            else if (i == 4) { // down
                assertEquals(doge.getX(), 1);
                assertEquals(doge.getY(), 0);
            }
            else if (i == 5) { // stop
                assertEquals(doge.getX(), 1);
                assertEquals(doge.getY(), 0);
            }
        }
    }

    @Test
    public void DoggieSpawnTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        assertEquals(world.getEnemies().size(), 0);
        for (int i = 0; i < 19; i++) {
            world.getCharacter().setCyclesCompleted();
            world.spawnBoss();
            assertEquals(world.getEnemies().size(), 0);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 19);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 1);
    }

    @Test
    public void DoggieStunTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);

        // s.dealDamage(c);
        world.runBattles();

        // Doggie damage = 5
        assertEquals(character.getHealth(), 95);
        assertEquals(doge.getHealth(), 40);

        // character get stunned for one round, no damage 
        world.runBattles();
        assertEquals(character.getHealth(), 90);
        assertEquals(doge.getHealth(), 40);

        // character can deal damage again
        world.runBattles();
        assertEquals(character.getHealth(), 85);
        assertEquals(doge.getHealth(), 35);
    }
}
