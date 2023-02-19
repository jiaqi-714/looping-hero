package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.LoopManiaWorld;


public class CampfireTest{
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
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath);


        
        return world;
    }
    
    // test whether compfire work if character in range
    @Test
    public void campfireTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);

        // Load the card trap to the character
        Card card = world.loadCard("cardCampfire");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 4);

        // test whether the Campfire is on the map
        assertEquals(1, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        assertEquals(10, zombie.getHealth());

        // make compfire bonus the character
        world.runBattles();
        // c.dealDamage(z);

        // test whether the Zombie is damage by the double damage
        assertEquals(95, character.getHealth());
    }   

    // test how two compfire work if character in range of these two
    @Test
    public void twoCompfireTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);

        // Load the card trap to the character
        Card card1 = world.loadCard("cardCampfire");
        Card card2 = world.loadCard("cardCampfire");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(card1.getX(), card1.getY(), 2, 3);
        world.convertCardToBuildingByCoordinates(card2.getX(), card2.getY(), 2, 4);
        // test whether 2 Campfire are on the map
        assertEquals(2, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);

        assertEquals(20, vampire.getHealth());

        // make compfire bonus the character
        world.runBattles();
        // c.dealDamage(z);

        // test whether the vampire is damage by the double damage
        assertEquals(94, character.getHealth());
    }

    // test whether compfire work if character not in range
    @Test
    public void compfireTestNotIn() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);

        // Load the card trap to the character
        Card card1 = world.loadCard("cardCampfire");
        Card card2 = world.loadCard("cardCampfire");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(card1.getX(), card1.getY(), 7, 7);
        world.convertCardToBuildingByCoordinates(card2.getX(), card2.getY(), 7, 8);
        // test whether 2 Campfire are on the map
        assertEquals(2, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);

        assertEquals(20, vampire.getHealth());

        // make compfire bonus the character
        for (int i = 0; i < 10;i++){
            world.runBattles();
        }
        

        // test whether the vampire is damage by the double damage
        assertEquals(70, character.getHealth());
    }

    // test how two compfire work if character one of campfire and not in another
    @Test
    public void twoCompfireTestOneInOneNot() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = helperCreateWorld(orderedPath);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);

        // Load the card trap to the character
        Card card1 = world.loadCard("cardCampfire");
        Card card2 = world.loadCard("cardCampfire");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(card1.getX(), card1.getY(), 3, 0);
        world.convertCardToBuildingByCoordinates(card2.getX(), card2.getY(), 7, 8);
        // test whether 2 Campfire are on the map
        assertEquals(2, world.getAllBuildings().size());

        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);

        assertEquals(20, vampire.getHealth());

        // make compfire bonus the character
        for (int i = 0; i < 10;i++){
            world.runBattles();
        }

        // test whether the vampire is damage by the double damage
        assertEquals(94, character.getHealth());
    }
    
}
