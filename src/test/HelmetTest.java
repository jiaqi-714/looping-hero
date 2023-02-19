package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.items.Helmet;

public class HelmetTest {

    @Test
    public void activateAbilityTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create the helmet
        Helmet helmet = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));

        // Equip the helmet
        character.setHelmet(helmet);
        
        // Create a Slug
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);

        // 3 hits required to kill the slug
        for (int i = 0; i < 3; i++) world.runBattles();
        
        // Slug: health = 6,    damage = 3-2 = 1
        // Char: health = 100 - 3,  damage = 5/2 = 2.5
        assertEquals(character.getHealth(), 97); 
        
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        
        // 4 hits required to kill the zombie
        for (int i = 0; i < 4; i++) world.runBattles();
        
        // Zombie: health = 10, damage = 5-2 = 3
        // Char: health = 97 - 12,  damage = 5/2 = 2.5
        assertEquals(character.getHealth(), 85);
    }


}
