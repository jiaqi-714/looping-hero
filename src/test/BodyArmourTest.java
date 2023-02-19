package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.items.BodyArmour;

import org.javatuples.Pair;


public class BodyArmourTest {

    @Test
    public void activateAbilityTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        
        // Equip the BodyArmour 
        character.setBody(new BodyArmour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a Slug
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        
        // 2 hits required to kill the slug
        for (int i = 0; i < 2; i++) world.runBattles();
        
        // Slug: health = 6,    damage = 3/2 = 1.5
        // Char: health = 100 - 3,  damage = 5
        assertEquals(character.getHealth(), 97);
        
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        
        // 2 hits required to kill the slug
        for (int i = 0; i < 2; i++) world.runBattles();
        
        // Zombie: health = 10      damage = 5/2 = 2.5
        // Char: health = 97 - 5,  damage = 5
        assertEquals(character.getHealth(), 92);
    }
}
