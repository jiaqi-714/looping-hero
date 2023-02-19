package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.enemies.Zombie;
import unsw.loopmania.Character;
import unsw.loopmania.items.Sword;

public class SwordTest {
    
    @Test
    public void activateAbilityTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create and equip the Sword
        character.setWeapon(new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        
        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        world.runBattles();

        // Zombie health - Sword damage
        // = 10 - 9 = 1
        assertEquals(zombie.getHealth(), 5); 
        world.runBattles();
        
        // Create a vampire
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        world.runBattles();

        // Vampire health - Sword damage
        // = 20 - 9 = 11
        assertEquals(vampire.getHealth(), 15); 
        
        world.runBattles();

        // Vampire health - Sword damage
        // = 11 - 9 = 2
        assertEquals(vampire.getHealth(), 10); 
    }
}
