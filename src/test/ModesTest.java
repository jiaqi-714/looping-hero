package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.items.*;
import unsw.loopmania.modes.*;

public class ModesTest {
    
    @Test
    public void standardModeTest() {
        Mode mode = new Mode();
        mode.setModeBehaviour(new StandardMode());

        // Assert mode name is Standard
        assertEquals(mode.performGetModeName(), "Standard mode");

        // Standard has no limits
        assertEquals(mode.performGetLimit(), 0);
        Item testPotion = new HealthPotion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        mode.performIncreaseLimit(testPotion);
        assertEquals(mode.performGetLimit(), 0);
        assertEquals(mode.performCanBuy("itemHealthPotion"), true);
        mode.performSetLimit(1);
        assertEquals(mode.performGetLimit(), 0);
    }

    @Test
    public void survivalModeTest() {
        Mode mode = new Mode();
        mode.setModeBehaviour(new SurvivalMode());

        // Assert mode name is Standard
        assertEquals(mode.performGetModeName(), "Survival mode");

        // Survival has 1 potion at shop limit
        // Currently is at 0
        assertEquals(mode.performGetLimit(), 0);
        assertEquals(mode.performCanBuy("itemHealthPotion"), true);
        assertEquals(mode.performCanBuy("itemShield"), true);

        // Buy one potion, assert that you can't buy anymore
        Item testPotion = new HealthPotion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        mode.performIncreaseLimit(testPotion);
        assertEquals(mode.performGetLimit(), 1);
        assertEquals(mode.performCanBuy("itemHealthPotion"), false);

        // Buy another item thats not a restriction
        assertEquals(mode.performCanBuy("itemSword"), true);

        // Reset the limit
        mode.performSetLimit(0);
        assertEquals(mode.performCanBuy("itemHealthPotion"), true);
        mode.performIncreaseLimit(testPotion);
        assertEquals(mode.performGetLimit(), 1);
        assertEquals(mode.performCanBuy("itemHealthPotion"), false);
    }

    @Test 
    public void berserkerModeTest() {
        Mode mode = new Mode();
        mode.setModeBehaviour(new BerserkerMode());

        // Assert mode name is Standard
        assertEquals(mode.performGetModeName(), "Berserker mode");

        // Berserker has 1 armour at shop limit
        // Currently is at 0
        assertEquals(mode.performGetLimit(), 0);
        assertEquals(mode.performCanBuy("itemHelmet"), true);

        // Buy one helmet, assert that you can't buy anymore
        Item testHelmet = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        mode.performIncreaseLimit(testHelmet);
        assertEquals(mode.performGetLimit(), 1);
        assertEquals(mode.performCanBuy("itemHelmet"), false);
        
        // Buy a potion, assert that nothing changes
        assertEquals(mode.performCanBuy("itemHealthPotion"), true);

        // Unable to buy another piece of armour
        assertEquals(mode.performCanBuy("itemShield"), false);

        // Reset the limit
        mode.performSetLimit(0);
        assertEquals(mode.performCanBuy("itemBodyArmour"), true);
        Item testBody = new BodyArmour(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        mode.performIncreaseLimit(testBody);
        assertEquals(mode.performGetLimit(), 1);
        assertEquals(mode.performCanBuy("itemShield"), false);
    }

    @Test
    public void switchingModesTest() {
        Mode mode = new Mode();
        mode.setModeBehaviour(new SurvivalMode());
        
        // Do a potion limiting action
        Item testPotion = new HealthPotion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        mode.performIncreaseLimit(testPotion);
        assertEquals(mode.performCanBuy("itemHealthPotion"), false);
        Item testHelmet = new Helmet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertEquals(mode.performCanBuy("itemHelmet"), true);
        assertEquals(mode.performCanBuy("itemShield"), true);
        assertEquals(mode.performCanBuy("itemBodyArmour"), true);
        mode.performIncreaseLimit(testHelmet);
        assertEquals(mode.performGetLimit(), 1);

        // Switch to berserker
        mode.setModeBehaviour(new BerserkerMode());

        // Assert you can buy more potions
        mode.performIncreaseLimit(testPotion);
        assertEquals(mode.performCanBuy("itemHealthPotion"), true);
        assertEquals(mode.performGetLimit(), 0);
        // Restriction on armour
        assertEquals(mode.performCanBuy("itemHelmet"), true);
        mode.performIncreaseLimit(testHelmet);
        assertEquals(mode.performGetLimit(), 1);
        assertEquals(mode.performCanBuy("itemShield"), false);
        assertEquals(mode.performCanBuy("itemBodyArmour"), false);
        assertEquals(mode.performCanBuy("itemHealthPotion"), true);
        assertEquals(mode.performGetLimit(), 1);
    }

    @Test
    public void confusingModeTest() {  
        Mode mode = new Mode();
        mode.setModeBehaviour(new ConfusingMode());

        // Create new rare item
        RareItem testAnduril = new Anduril(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        mode.performIncreaseLimit(testAnduril);

        // Check original behaviour of item
        assertEquals(testAnduril.getStat(), 10);

        // Increase the properties/behaviour of the item with one other rare item
        assertEquals(testAnduril.getSecondActive(), true);
        
        // Change to modes, assert not confusing mode is applied
        mode.setModeBehaviour(new SurvivalMode());
        mode.performIncreaseLimit(testAnduril);

        assertEquals(testAnduril.getSecondActive(),false);
    }
}
