package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.items.*;
import unsw.loopmania.LoopManiaWorld;

public class CharacterTest {

    @Test
    public void changeCharacCycles() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(1, 1));
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        character.setCyclesCompleted();    
        assertEquals(character.getCyclesCompleted(), 1); 
    }

    @Test
    public void characterEquip() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        
        Item shield = world.addUnequippedItem("itemShield");
        Item helmet = world.addUnequippedItem("itemHelmet");
        Item bodyArmour = world.addUnequippedItem("itemBodyArmour");
        Item sword = world.addUnequippedItem("itemSword");

        // world.equipItem(shield.getX(), shield.getY(), 2, 1);
        
        // Create a Slug
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);

        world.equipItem(sword);
        world.equipItem(shield);
        world.equipItem(helmet);
        world.equipItem(bodyArmour);
        assertEquals(character.getWeaponSlot().getClass(), Sword.class);
        assertEquals(character.getHelmet().getClass(), Helmet.class);
        assertEquals(character.getShield().getClass(), Shield.class);
        assertEquals(character.getBody().getClass(), BodyArmour.class);
        
        // Body slot is (1, 1)
        // Shield slot is (2, 1)
        // Helmet slot is (1, 0)
        // Ring slot is (2, 0)
    }

    @Test
    public void healerTraitTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld(orderedPath);
        Character c = s.createCharacter(orderedPath, world);
        
        c.setHealth(10);
        c.setCyclesCompleted();
        assertEquals(c.getHealth(), 10);

        c.setTrait("Healer");
        c.setCyclesCompleted();
        assertEquals(c.getHealth(), 15);
    }

    @Test
    public void stabbyTraitTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld(orderedPath);
        Character c = s.createCharacter(orderedPath, world);
        
        assertEquals(c.getDamage(), 5);

        c.setTrait("Stabby");
        assertEquals(c.getDamage(), 6);
    }

    @Test
    public void turtleTraitTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld(orderedPath);
        Character c = s.createCharacter(orderedPath, world);
        
        assertEquals(c.getDefense(), 0);

        c.setTrait("Turtle");
        assertEquals(c.getDefense(), 1);
    }

    @Test
    public void bigSpenderTraitTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld(orderedPath);
        Character c = s.createCharacter(orderedPath, world);
        
        c.setCyclesCompleted();
        assertEquals(c.getGoldBalance(), 0);

        c.setTrait("BigSpender");
        c.setCyclesCompleted();
        assertEquals(c.getGoldBalance(), 20);
    }

    @Test
    public void librarianTraitTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld(orderedPath);
        Character c = s.createCharacter(orderedPath, world);
        
        assertEquals(c.getExperience(), 0);
        c.setExperience(5);
        assertEquals(c.getExperience(), 5);

        c.setTrait("Librarian");
        c.setExperience(5);
        assertEquals(c.getExperience(), 15);
    }
}
