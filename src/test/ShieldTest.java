package test;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.enemies.Slug;
import unsw.loopmania.enemies.Vampire;
import unsw.loopmania.items.Shield;

public class ShieldTest {

    @Test
    public void activateAbilitySlugTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Equip the shield
        character.setShield(new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

         // Create first slug to battle
         int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
         PathPosition add = new PathPosition(currPos, orderedPath);
         Slug slug1 = new Slug(add);
         world.addBasicEnemy(slug1);
        
        // Shield blocks both the slugs hits
        world.runBattles();
        assertEquals(character.getHealth(), 100);
        world.runBattles();
        assertEquals(character.getHealth(), 100);
        
        // Create a second slug to battle
        Slug slug2 = new Slug(add);
        world.addBasicEnemy(slug2);
        
        world.runBattles();
        assertEquals(character.getHealth(), 100);

        // No block for the 4th enemy hit -> Character takes damage
        world.runBattles();
        assertEquals(character.getHealth(), 97);

    }
    
    @Test
    public void activateAbilityVampireTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Equip the shield
        character.setShield(new Shield(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        // Create a vampire
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        // Complete 100 ticks of the battle.
        int nVampAttacks = 0;
        for (int i = 0; i < 100; i++) {
            world.runBattles();

            // 6 = Vampire default damage
            // Then a critical attack occurred 
            if (vampire.getDamage() != 6) {
                nVampAttacks++;
            }

            // To ensure that the battle lasts
            vampire.setHealth(100);
            character.setHealth(100);
        }
        
        assertEquals(nVampAttacks, 30);

    }
            
}
