package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.LoopManiaWorld;


public class TowerTest{
    public LoopManiaWorld helperCreateWorld(List<Pair<Integer, Integer>> orderedPath) {
        orderedPath.add(Pair.with(0, 0));
        orderedPath.add(Pair.with(0, 1));
        orderedPath.add(Pair.with(0, 2));
        orderedPath.add(Pair.with(1, 2));
        orderedPath.add(Pair.with(2, 2));
        orderedPath.add(Pair.with(2, 1));
        orderedPath.add(Pair.with(2, 0));
        orderedPath.add(Pair.with(1, 0));
        orderedPath.add(Pair.with(4, 3));
        orderedPath.add(Pair.with(4, 4));
        orderedPath.add(Pair.with(5, 3));
        orderedPath.add(Pair.with(5, 2));
        orderedPath.add(Pair.with(5, 1));
        orderedPath.add(Pair.with(8, 8));
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath);


        
        return world;
    }
    
    @Test
    public void oneTowerTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character c  = new Character(add);
        world.setCharacter(c);

        Card card = world.loadCard("cardTower");
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 3);
        // test whether the Campfire is on the map
        assertEquals(1, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Zombie z = new Zombie(add);
        world.addBasicEnemy(z);
        z.setHealth(100);
        assertEquals(100, z.getHealth());

        world.runBattles();
        assertEquals(92, z.getHealth());
        // test whether the Zombie is damage by the double damage
        assertEquals(95, c.getHealth());

        world.runBattles();

        assertEquals(84, z.getHealth());
        assertEquals(90, c.getHealth());
    }   

    @Test
    public void twoTowerTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character c  = new Character(add);
        world.setCharacter(c);

        Card card1 = world.loadCard("cardTower");
        world.convertCardToBuildingByCoordinates(card1.getX(), card1.getY(), 2, 3);
        Card card2 = world.loadCard("cardTower");
        world.convertCardToBuildingByCoordinates(card2.getX(), card2.getY(), 3, 2);
        assertEquals(2, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Zombie z = new Zombie(add);
        world.addBasicEnemy(z);
        z.setHealth(100);
        assertEquals(100, z.getHealth());

        world.runBattles();
        assertEquals(89, z.getHealth());
        // test whether the Zombie is damage by the double damage
        assertEquals(95, c.getHealth());

        world.runBattles();

        assertEquals(78, z.getHealth());
        assertEquals(90, c.getHealth());
    }   

    @Test
    public void notInRangeTowerTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character c  = new Character(add);
        world.setCharacter(c);

        Card card2 = world.loadCard("cardTower");
        world.convertCardToBuildingByCoordinates(card2.getX(), card2.getY(), 8, 9);
        assertEquals(1, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Zombie z = new Zombie(add);
        world.addBasicEnemy(z);
        z.setHealth(100);
        assertEquals(100, z.getHealth());

        world.runBattles();
        assertEquals(95, z.getHealth());
        // test whether the Zombie is damage by the double damage
        assertEquals(95, c.getHealth());

        world.runBattles();

        assertEquals(90, z.getHealth());
        assertEquals(90, c.getHealth());
    }   
}
