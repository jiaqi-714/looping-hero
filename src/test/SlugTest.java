package test;


import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;

public class SlugTest {
    
    @Test
    public void dealDamageTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);

        // s.dealDamage(c);
        world.runBattles();

        // Slug damage = 3 
        assertEquals(character.getHealth(), 97);
    }

    @Test
    public void slugMovementTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Create a Slug
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(3, 3));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        
        for (int i = 0; i < 6; i++) {
            world.runBattles();
            
            if (i == 0) { // Move Up
                assertEquals(slug.getX(), 3);
                assertEquals(slug.getY(), 2);
            }
            else if (i == 1) { // Move Down
                assertEquals(slug.getX(), 3);
                assertEquals(slug.getY(), 3);
            }
            else if (i == 2) { // Move Up
                assertEquals(slug.getX(), 2);
                assertEquals(slug.getY(), 3);
            }
        }
    }
}
