package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.*;
import unsw.loopmania.Character;
import unsw.loopmania.enemies.*;
import unsw.loopmania.items.*;

public class RareItemsTest {
    
    @Test
    public void andurilBossTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        Item anduril = world.addUnequippedItem("itemAnduril");
        world.equipItem(anduril);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vamp = new Vampire(add);
        world.addBasicEnemy(vamp);
        world.fight(vamp, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(vamp.getHealth(), 5);
        world.runBattles();

        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);
        world.fight(doge, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(doge.getHealth(), 10);
    }

    @Test
    public void treeStumpBossTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        Item treeStump = world.addUnequippedItem("itemTreeStump");
        world.equipItem(treeStump);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vamp = new Vampire(add);
        world.addBasicEnemy(vamp);
        character.setHealth(1000);
        vamp.setHealth(100);
        // Out of 10 battles, the character should block the vampires attacks 6-7 times
        for (int i = 0; i < 10; i++) {
            world.runBattles();
        }
        assertEquals(character.getHealth(), 75);

        vamp.setHealth(1);
        world.runBattles();

        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);
        // Out of 10 battles, the character should block the doges attacks 8-9 times
        for (int i = 0; i < 10; i++) {
            world.runBattles();
        }
        assertEquals(character.getHealth(), 65);
    }
}
