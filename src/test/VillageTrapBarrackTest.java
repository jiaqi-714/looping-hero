package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.Card;
import unsw.loopmania.LoopManiaWorld;

public class VillageTrapBarrackTest {

    @Test
    public void trapTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);

        // Load the card trap to the character
        Card mustBeTrapCard = world.loadCard("cardTrap");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(mustBeTrapCard.getX(), mustBeTrapCard.getY(), 0, 2);

        // test whether the trap is on the map
        assertEquals(1, world.getAllBuildings().size());

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);

        world.addBasicEnemy(zombie);
        zombie.moveDownPath();

        world.checkTrapForEnemy(zombie);
        // test whether the zombie is damage by the trap
        assertEquals(5, zombie.getHealth());
        // test whether the trap is destory by the zombie
        assertEquals(0, world.getAllBuildings().size());
    }   

    @Test
    public void villageTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Load the card village to the character
        Card villageCard = world.loadCard("cardVillage");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(villageCard.getX(), villageCard.getY(), 0, 2);

        villageCard = world.loadCard("cardVillage");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(villageCard.getX(), villageCard.getY(), 0, 1);

        character.setHealth(75);
        world.runTickMoves();
        // now character in to village
        assertEquals(100, character.getHealth());
        
        // now character move in to village, but his health cannot over 100
        world.runTickMoves();
        assertEquals(100, character.getHealth());

    }   

    @Test
    public void barrackTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Load the card village to the character
        Card Card = world.loadCard("cardBarracks");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(Card.getX(), Card.getY(), 0, 2);

        Card = world.loadCard("cardBarracks");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(Card.getX(), Card.getY(), 0, 1);

        // Character is going back and forth between a barracks in order
        // to generate allies. The limit is 6. 
        character.moveDownPath();
        world.checkBarracks();
        assertEquals(1, character.getAllies().size());
        
        for (int i = 2; i < 6; i = i + 2) {
            character.moveDownPath();
            world.checkBarracks();
            assertEquals(i, character.getAllies().size());

            character.moveUpPath();
            world.checkBarracks();
            assertEquals(i+1, character.getAllies().size());
        }

        character.moveDownPath();
        world.checkBarracks();
        assertEquals(6, character.getAllies().size());
    }   

}
