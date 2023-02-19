package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import unsw.loopmania.*;
import unsw.loopmania.Character;
import unsw.loopmania.buildings.*;
import unsw.loopmania.enemies.*;
import unsw.loopmania.items.*;
import unsw.loopmania.modes.*;

public class LoopManiaWorldTest {

    @Test 
    public void willSupportTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        
        // Create Vampire
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        // Create Slug
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(2, 0));
        add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);

        // Create Character
        currPos = orderedPath.indexOf(new Pair<Integer, Integer>(2, 2));
        add = new PathPosition(currPos, orderedPath);
        Character character = new Character(add);
        world.setCharacter(character);
        
        // Assert that they are not withinrange
        List<BasicEnemy> support = world.willSupport(slug, character);
        assertEquals(support.contains(vampire), false);
        
        character.moveUpPath();

        // Assert that the slug is within the support range of another
        support = world.willSupport(slug, character);
        assertEquals(support.contains(vampire), true);

        character.moveUpPath();
        world.runBattles();
        assertEquals(character.getHealth(), 94);

    }

    @Test
    public void runBasicBattleSlugTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create a Slug
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        
        // Assert that they are not withinrange
        List<BasicEnemy> defeated = world.runBattles();
        assertEquals(defeated.isEmpty(), true);
        
        // Character in range of slug
        character.moveDownPath();
        
        // Slug takes 2 hits to kill
        for (int i = 0; i < 3; i++) {
            defeated = world.runBattles();
        } 

        // Assert that Slug is no longer in the world
        assertEquals(defeated.contains(slug), true);

        // Slug: health = 6,    damage = 3
        // Char: health = 100,  damage = 5
        assertEquals(character.getHealth(), 94); 
        assertEquals(character.getGoldBalance(), 5);
        assertEquals(character.getExperience(), 10);
    }

    @Test
    public void runBasicBattleZombieTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);
        
        // Assert that they are not within range
        List<BasicEnemy> defeated = world.runBattles();
        assertEquals(defeated.isEmpty(), true);
        
        // Character in range of zombie
        character.moveDownPath();

        // Zombie takes 2 hits to kill
        for (int i = 0; i < 2; i++) {
            defeated = world.runBattles();
        } 
        
        // Assert that Zombie is no longer in the world
        assertEquals(defeated.contains(zombie), true);

        // Zombie: health = 10, damage = 5
        // Char: health = 100,  damage = 5
        assertEquals(character.getHealth(), 90);
        assertEquals(character.getGoldBalance(), 10);
        assertEquals(character.getExperience(), 20);
    }
    
    @Test
    public void runBasicBattleVampireTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        
        // Create a Vampire
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        // Assert vampire not withinrange
        List<BasicEnemy> defeated = world.runBattles();
        assertEquals(defeated.isEmpty(), true);
        
        // Character in range of Vampire
        character.moveDownPath();

        // Vampire takes 4 hits to kill
        for (int i = 0; i < 5; i++) {
            defeated = world.runBattles();
        } 
        
        // Assert that Vampire is no longer in the world
        assertEquals(defeated.contains(vampire), true);

        // Vampire: health = 20,    damage = 6
        // Char:    health = 100,  damage = 5
        // Generate random seed to ensure normal attack
        assertEquals(character.getGoldBalance(), 25);
        assertEquals(character.getExperience(), 50);
        // Maybe test potential item drop

    }

    @Test
    public void runBasicBattleLoseTest() {
        
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create a Vampire
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(1, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Vampire vampire = new Vampire(add);
        world.addBasicEnemy(vampire);
        
        // Assert vampire not withinrange
        List<BasicEnemy> defeated = world.runBattles();
        assertEquals(defeated.isEmpty(), true);
        
        // Character health = 5 HP
        character.setHealth(5);

        // Character in range of zombie
        character.moveDownPath();

        // Assert that Vampire is no longer in the world
        // Generate random seed to ensure normal attack
        world.runBattles(); world.runBattles();
        // Vampire: health = 20,    damage = 6
        // Char:    health = 100,  damage = 5

        //assert(c.getHealth() <= 0);
        // Assert GAME OVER

    }

    @Test
    public void reachExpGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Create a 20 exp goal
        JSONObject simpleGoal = new JSONObject();
        simpleGoal.put("goal", "experience");
        simpleGoal.put("quantity", 20);
        world.loadGoals(simpleGoal);
        assertEquals(world.printGoals(), "Collect 20 experience");

        // Slugs will give 10 exp per kill
        // Spawn 2 slugs, after killing one, goals isnt met
        // Spawn 1st slug --> kill slug --> check goal
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug1 = new Slug(add);
        slug1.setHealth(1);
        world.addBasicEnemy(slug1);
        world.runBattles();
        assertEquals(world.isGoalsMet(), false);

        Slug slug2 = new Slug(add);
        slug2.setHealth(1);
        world.addBasicEnemy(slug2);
        world.runBattles();
        assertEquals(world.isGoalsMet(), true);

    }

    @Test
    public void reachGoldGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Create a 10 gold goal
        JSONObject simpleGoal = new JSONObject();
        simpleGoal.put("goal", "gold");
        simpleGoal.put("quantity", 10);
        world.loadGoals(simpleGoal);

        // Slugs will give 5 gold per kill
        // Spawn 2 slugs, after killing one, goals isnt met
        // Spawn 1st slug --> kill slug --> check goal
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug1 = new Slug(add);
        slug1.setHealth(1);
        world.addBasicEnemy(slug1);
        world.runBattles();
        assertEquals(world.isGoalsMet(), false);

        Slug slug2 = new Slug(add);
        slug2.setHealth(1);
        world.addBasicEnemy(slug2);
        world.runBattles();
        assertEquals(world.isGoalsMet(), true);
    }

    @Test
    public void reachCyclesGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Create a 2 cycles goal
        JSONObject simpleGoal = new JSONObject();
        simpleGoal.put("goal", "cycles");
        simpleGoal.put("quantity", 2);
        world.loadGoals(simpleGoal);
        
        // Complete 1 cycle
        int cycle = 9;
        for (int i = 0; i < cycle * 1; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        assertEquals(world.isGoalsMet(), false);
        
        // Complete another cycle
        for (int i = 0; i < cycle * 1; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        assertEquals(world.isGoalsMet(), true);
    }

    @Test
    public void reachBossesGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Create a 2 cycles goal
        JSONObject simpleGoal = new JSONObject();
        simpleGoal.put("goal", "bosses");
        world.loadGoals(simpleGoal);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        doge.setHealth(1);
        world.addBasicEnemy(doge);

        // Boss Doggie dies
        world.runBattles();

        assertEquals(world.isGoalsMet(), false);

        // Spawn elan
        // Set cycles to 39
        Elan elan = new Elan(add);
        elan.setHealth(1);
        world.addBasicEnemy(elan);

        // Boss Elan dies
        world.runBattles();

        assertEquals(world.isGoalsMet(), true);
    }
    
    @Test
    public void reachMixedAndGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Goal is complete 2 cycles AND collect 5 gold
        JSONObject json = new JSONObject();
        json.put("goal", "AND");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "cycles");
        goal1.put("quantity", 2);
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "gold");
        goal2.put("quantity", 5);
        array.put(goal1);
        array.put(goal2);
        json.put("subgoals", array);
        world.loadGoals(json);
        
        // Complete 1 cycle
        int cycle = 9;
        for (int i = 0; i < cycle * 1; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        assertEquals(world.isGoalsMet(), false);
        
        // Create a Slug --> kill -> gain 5 gold 
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        slug.setHealth(1);
        world.addBasicEnemy(slug);
        world.runBattles();
        // only 1 goal is completed at this point
        assertEquals(world.isGoalsMet(), false);

        // Complete both goals
        for (int i = 0; i < cycle * 1; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        assertEquals(world.isGoalsMet(), true);
    }

    @Test
    public void reachComplexGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Goal is Collect 10 gold OR (Complete 2 cycles AND collect 10 experience)
        JSONObject json = new JSONObject();
        json.put("goal", "OR");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "gold");
        goal1.put("quantity", 10);
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "AND");
        JSONArray g2array = new JSONArray();
        JSONObject g2a = new JSONObject();
        g2a.put("goal", "cycles");
        g2a.put("quantity", 2);
        JSONObject g2b = new JSONObject();
        g2b.put("goal", "experience");
        g2b.put("quantity", 10);
        g2array.put(g2a);
        g2array.put(g2b);
        array.put(goal1);
        goal2.put("subgoals", g2array);
        array.put(goal2);
        json.put("subgoals", array);
        world.loadGoals(json);
        
        // Complete 2 cycles --> still false
        int cycle = 9;
        for (int i = 0; i < cycle * 2; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        assertEquals(world.isGoalsMet(), false);
        
        // Create a Slug --> kill -> gain 5 gold + 10 experience --> now true
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Slug slug = new Slug(add);
        slug.setHealth(1);
        world.addBasicEnemy(slug);
        world.runBattles();
        // both goals are completed at this point even though 10 gold isnt collected
        assertEquals(world.isGoalsMet(), true);
    }

    @Test
    public void bossesGoal() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);
        setup.createCharacter(orderedPath, world);

        // Create a 10 gold goal
        JSONObject simpleGoal = new JSONObject();
        simpleGoal.put("goal", "bosses");
        world.loadGoals(simpleGoal);

        // Create and kill bosses
        // Assert false until all bosses dead
    }


    @Test
    public void loadAnUnequippedWeaponTest() {
        // Intialise a world
        LoopManiaWorld world = helperCreateWorld();

        // Add a sword item into the world
        // Assert its a sword
        Item mustBeSword = world.addUnequippedItem("itemSword");
        assertEquals(mustBeSword.getClass(), Sword.class);
        // Add a stake item into the world
        // Assert its a stake
        Item mustBeStake = world.addUnequippedItem("itemStake");
        assertEquals(mustBeStake.getClass(), Stake.class);

        // Add a staff item into the world
        // Assert its a staff
        Item mustBeStaff = world.addUnequippedItem("itemStaff");
        assertEquals(mustBeStaff.getClass(), Staff.class);

        // Move to the character's inventory
        // Assert it's in the character's slot

        // Remove it
        // Assert it's gone
        world.removeUnequippedInventoryItemByCoordinates(mustBeSword.getX(), mustBeSword.getY());
        //assertEquals(world.getUnequippedInventoryItemEntityByCoordinates(mustBeSword.getX(), mustBeSword.getY()), null);
    }

    @Test
    public void loadAnUnequippedArmourTest() {
        // Intialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Add a helmet item into the world
        // Assert its a helmet
        Item mustBeHelmet = world.addUnequippedItem("itemHelmet");
        assertEquals(mustBeHelmet.getClass(), Helmet.class);

        // Add a body armour item into the world
        // Assert its a body armour
        Item mustBeBody = world.addUnequippedItem("itemBodyArmour");
        assertEquals(mustBeBody.getClass(), BodyArmour.class);

        // Add a shield item into the world
        // Assert its a shield
        Item mustBeShield = world.addUnequippedItem("itemShield");
        assertEquals(mustBeShield.getClass(), Shield.class);

        // Move to the character's inventory
        // Assert it's in the character's slot


        // Remove it
        // Assert it's gone
        world.removeUnequippedInventoryItemByCoordinates(mustBeHelmet.getX(), mustBeHelmet.getY());
        // assertEquals(world.getUnequippedInventoryItemEntityByCoordinates(mustBeSword.getX(), mustBeSword.getY()), null);
    }

    @Test
    public void loadAnUnequippedHealingTest() {
        // Intialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Add a healing potion item into the world
        // Assert its a potion
        Item mustBePotion = world.addUnequippedItem("itemHealthPotion");
        assertEquals(mustBePotion.getClass(), HealthPotion.class);

    }

    @Test
    public void loadAVampireCastleTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card vampire castle to the character
        Card mustBeVampireCastleCard = world.loadCard("cardVampireCastle");
        // Convert the card to a building
        Building mustBeVampireCastleBuilding = world.convertCardToBuildingByCoordinates(mustBeVampireCastleCard.getX(), mustBeVampireCastleCard.getY(), 3, 3);
        // Activate it?
        assertEquals(mustBeVampireCastleBuilding.getClass(), VampireCastleBuilding.class);
    }
    
    @Test
    public void loadAZombiePitTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card zombie pit to the character
        Card mustBeZombiePitCard = world.loadCard("cardZombiePit");
        // Convert the card to a building
        Building mustBeZombiePitBuilding = world.convertCardToBuildingByCoordinates(mustBeZombiePitCard.getX(), mustBeZombiePitCard.getY(), 3, 3);
        // Activate it? 
        assertEquals(mustBeZombiePitBuilding.getClass(), ZombiePitBuilding.class);
    }

    @Test
    public void loadACampfireTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card zombie pit to the character
        Card mustBeCampfireCard = world.loadCard("cardCampfire");
        // Convert the card to a building
        Building mustBeCampfireBuilding = world.convertCardToBuildingByCoordinates(mustBeCampfireCard.getX(), mustBeCampfireCard.getY(), 3, 3);
        // Activate it? 
        assertEquals(mustBeCampfireBuilding.getClass(), Campfire.class);  
    }
     
    @Test
    public void loadATowerTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card zombie pit to the character
        Card mustBeTowerCard = world.loadCard("cardTower");
        // Convert the card to a building
        Building mustBeTowerBuilding = world.convertCardToBuildingByCoordinates(mustBeTowerCard.getX(), mustBeTowerCard.getY(), 3, 3);
        // Activate it?
        assertEquals(mustBeTowerBuilding.getClass(), Tower.class);
    }

    @Test
    public void loadAVillageTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card zombie pit to the character
        Card mustBeVillageCard = world.loadCard("cardVillage");
        // Convert the card to a building
        Building mustBeVillageBuilding = world.convertCardToBuildingByCoordinates(mustBeVillageCard.getX(), mustBeVillageCard.getY(), 2, 0);
        // Activate it?
        assertEquals(mustBeVillageBuilding.getClass(), Village.class);
        assertEquals(world.getAllyBuilding().size(), 1);

    }

    @Test
    public void loadABarracksTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card zombie pit to the character
        Card mustBeBarracksCard = world.loadCard("cardBarracks");
        // Convert the card to a building
        Building mustBeBarracksBuilding = world.convertCardToBuildingByCoordinates(mustBeBarracksCard.getX(), mustBeBarracksCard.getY(), 2, 0);
        // Activate it?
        assertEquals(mustBeBarracksBuilding.getClass(), Barracks.class);
    }

    @Test
    public void loadATrapTest() {
        // Initialise a world
        LoopManiaWorld world = helperCreateWorld();
        // Load the card trap to the character
        Card mustBeTrapCard = world.loadCard("cardTrap");
        // Convert the card to a building
        Building mustBeTrapBuilding = world.convertCardToBuildingByCoordinates(mustBeTrapCard.getX(), mustBeTrapCard.getY(), 2, 0);
        // Activate it
        assertEquals(mustBeTrapBuilding.getClass(), Trap.class);
    }

    @Test
    public void equipAndUnequipItemsTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Add an unequipped weapon into the world
        // Assert the type of weapon
        Item mustBeSword = world.addUnequippedItem("itemSword");

        // Assert character has no weapons to begin
        assertEquals(character.getWeaponSlot(), null);

        // Equip the weapon
        world.equipItem(mustBeSword);
        
        world.unequipItem(mustBeSword);
        assertEquals(character.getWeaponSlot(), null);
        // Repeat test for other types of items----------------
        Item stake = world.addUnequippedItem("itemStake");
      
        // Assert character has no weapons to begin
        assertEquals(character.getWeaponSlot(), null);

        // Equip the weapon
        world.equipItem(stake);
        
        world.unequipItem(stake);
        assertEquals(character.getWeaponSlot(), null);
        //-------------------------------------------------------
        Item staff = world.addUnequippedItem("itemStaff");

        // Equip the weapon
        world.equipItem(staff);
        assertEquals(character.getWeaponSlot().getClass(), Staff.class);
        
        world.unequipItem(staff);
        assertEquals(character.getWeaponSlot(), null);
        //-------------------------------------------------------
        Item sniper = world.addUnequippedItem("itemSniperRifle");

        // Assert character has no weapons to begin
        assertEquals(character.getWeaponSlot(), null);

        // Equip the weapon
        world.equipItem(sniper);
        assertNotEquals(character.getWeaponSlot(), null);
        
        world.unequipItem(sniper);
        assertEquals(character.getWeaponSlot(), null);
        //-------------------------------------------------------
        Item And = world.addUnequippedItem("itemAnduril");

        // Assert character has no weapons to begin
        assertEquals(character.getWeaponSlot(), null);

        // Equip the weapon
        world.equipItem(And);
        assertNotEquals(character.getWeaponSlot(), null);
        
        world.unequipItem(And);
        assertEquals(character.getWeaponSlot(), null);
        //-------------------------------------------------------
        Item tree = world.addUnequippedItem("itemTreeStump");

        // Assert character has no weapons to begin
        assertEquals(character.getShield(), null);

        // Equip the weapon
        world.equipItem(tree);
        assertNotEquals(character.getShield(), null);
        
        world.unequipItem(tree);
        assertEquals(character.getShield(), null);
        //-------------------------------------------------------
        Item helmet = world.addUnequippedItem("itemHelmet");

        // Assert character has no weapons to begin
        assertEquals(character.getHelmet(), null);

        // Equip the weapon
        world.equipItem(helmet);
        assertNotEquals(character.getHelmet(), null);
        
        world.unequipItem(helmet);
        assertEquals(character.getHelmet(), null);
        //-------------------------------------------------------
        Item shield = world.addUnequippedItem("itemShield");

        // Assert character has no weapons to begin
        assertEquals(character.getShield(), null);

        // Equip the weapon
        world.equipItem(shield);
        assertNotEquals(character.getShield(), null);
        
        world.unequipItem(shield);
        assertEquals(character.getShield(), null);
        //-------------------------------------------------------
        Item armour = world.addUnequippedItem("itemBodyArmour");

        // Assert character has no weapons to begin
        assertEquals(character.getBody(), null);

        // Equip the weapon
        world.equipItem(armour);
        assertNotEquals(character.getBody(), null);
        
        world.unequipItem(armour);
        assertEquals(character.getBody(), null);
        //-------------------------------------------------------
        Item ring = world.addUnequippedItem("itemTheOneRing");

        // Assert character has no weapons to begin
        assertEquals(character.getRing(), null);

        // Equip the weapon
        world.equipItem(ring);
        assertNotEquals(character.getRing(), null);
        
        world.unequipItem(ring);
        assertEquals(character.getRing(), null);
        //-------------------------------------------------------
    }

    @Test
    public void equipReplacementWeaponItemTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Add an unequipped weapon into the world
        // Assert the type of weapon
        Item mustBeSword = world.addUnequippedItem("itemSword");

        // Assert character has no weapons to begin
        // Also assert that you can't equip an item that doesnt exist in inventory
        assertEquals(character.getWeaponSlot(), null);
        assertEquals(world.convertUnequippedToEquippedItemByCoordinates(2, 2, 1, 1), null);
        assertEquals(world.convertEquippedToUnequippedItemByCoordinates(2, 2, 1, 1), null);

        // Equip the weapon
        Pair<Item, Item> equippingItems1 = world.convertUnequippedToEquippedItemByCoordinates(mustBeSword.getX(), mustBeSword.getY(), 0, 1);

        // Add another weapon into the world
        Item mustBeStake = world.addUnequippedItem("itemStake");

        // Equip the stake instead of the sword
        assertEquals(character.getWeaponSlot(), equippingItems1.getValue0());
        Pair<Item, Item> equippingItems2 = world.convertEquippedToUnequippedItemByCoordinates(equippingItems1.getValue0().getX(), equippingItems1.getValue0().getY(), mustBeStake.getX(), mustBeStake.getY());
        assertEquals(character.getWeaponSlot(), equippingItems2.getValue0());
    }

    @Test
    public void equipReplacementArmourItemTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Add an unequipped helmet into the world
        // Assert the type of helmet
        Item helmet1 = world.addUnequippedItem("itemHelmet");

        // Assert character has no weapons to begin
        assertEquals(character.getHelmet(), null);

        // Equip the helmet
        Pair<Item, Item> equippingItems1 = world.convertUnequippedToEquippedItemByCoordinates(helmet1.getX(), helmet1.getY(), 1, 0);

        // Add another helmet into the world
        Item helmet2 = world.addUnequippedItem("itemHelmet");

        // Equip the helmet2 instead of the helmet1
        assertEquals(character.getHelmet(), equippingItems1.getValue0());
        Pair<Item, Item> equippingItems2 = world.convertUnequippedToEquippedItemByCoordinates(helmet2.getX(), helmet2.getY(), equippingItems1.getValue0().getX(), equippingItems1.getValue0().getY());
        assertEquals(character.getHelmet(), equippingItems2.getValue0());
    }

    @Test
    public void unequipReplacementWeaponItemTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Add an unequipped weapon into the world
        // Assert the type of weapon
        Item mustBeSword = world.addUnequippedItem("itemSword");

        // Assert character has no weapons to begin
        assertEquals(character.getWeaponSlot(), null);

        // Equip the weapon
        Pair<Item, Item> equippingItems1 = world.convertUnequippedToEquippedItemByCoordinates(mustBeSword.getX(), mustBeSword.getY(), 0, 1);
        // world.equipItem(mustBeSword);

        // Add another weapon into the world
        Item mustBeStake = world.addUnequippedItem("itemStake");

        // Equip the stake instead of the sword
        assertEquals(character.getWeaponSlot(), equippingItems1.getValue0());
        Pair<Item, Item> equippingItems2 = world.convertUnequippedToEquippedItemByCoordinates(mustBeStake.getX(), mustBeStake.getY(), equippingItems1.getValue0().getX(), equippingItems1.getValue0().getY());
        assertEquals(character.getWeaponSlot(), equippingItems2.getValue0());
    }

    @Test
    public void buyItemTest() {
        // Initialise a world
        List<Pair<Integer, Integer>> orderedPath = helperOrderedPath();
        LoopManiaWorld world = new LoopManiaWorld(6, 6, orderedPath);
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);
        // Character doesnt have enough money to buy sword at start of game
        assertEquals(world.buyItem("itemSword", 100), null);

        // Give the character enough money to buy an item
        world.giveGoldAndExp(800, 30);
        Item boughtSword = world.buyItem("itemSword", 100);
        assertEquals(character.getGoldBalance(), 700);

        // Equip the sword
        // Check the character has it equipped
        world.equipItem(boughtSword);
        assertEquals(character.getWeaponSlot(), boughtSword);
    }

    @Test
    public void unequipReplacementArmourItemTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Add an unequipped helmet into the world
        // Assert the type of helmet
        Item shield1 = world.addUnequippedItem("itemShield");

        // Assert character has no weapons to begin
        assertEquals(character.getShield(), null);

        // Equip the helmet
        Pair<Item, Item> equippingItems1 = world.convertUnequippedToEquippedItemByCoordinates(shield1.getX(), shield1.getY(), 2, 1);
        // world.equipItem(shield1);

        // Add another helmet into the world
        Item shield2 = world.addUnequippedItem("itemShield");

        // Equip the helmet2 instead of the helmet1
        assertEquals(character.getShield(), equippingItems1.getValue0());
        Pair<Item, Item> equippingItems2 = world.convertUnequippedToEquippedItemByCoordinates(shield2.getX(), shield2.getY(), equippingItems1.getValue0().getX(), equippingItems1.getValue0().getY());
        assertEquals(character.getShield(), equippingItems2.getValue0());
    }

    @Test
    public void sellItemTest() {
        // Initialise a world
        List<Pair<Integer, Integer>> orderedPath = helperOrderedPath();
        LoopManiaWorld world = new LoopManiaWorld(6, 6, orderedPath);
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);
        // Assert character has no money beforehand
        // Give character a sword
        assertEquals(character.getGoldBalance(), 0);
        Item toSell = world.addUnequippedItem("itemSword");
   
        // Sell item
        world.sellUnequippedItem(toSell.getX(), toSell.getY());
        // Assert gold balance increased
        assertEquals(character.getGoldBalance(), 5);
    }

    @Test
    public void barracksAllyTest() {
        List<Pair<Integer, Integer>> orderedPath = helperOrderedPath();
        LoopManiaWorld world = new LoopManiaWorld(6, 6, orderedPath);
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);
        // Spawn barracks
        Card bCard = world.loadCard("cardBarracks");
        world.convertCardToBuildingByCoordinates(bCard.getX(), bCard.getY(), 0, 1);
        
        // Ally moves over barracks
        // Assert ally is now in characters party
        world.runTickMoves();
        world.checkBarracks();
        assertEquals(character.getAllies().size(), 1);

        // Spawn slug -> 3 dmg
        // Go into battle with slug
        // Assert ally hp has changed
        int slugPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        BasicEnemy slug = new Slug(new PathPosition(slugPos, orderedPath));
        world.addBasicEnemy(slug);
        world.fight(slug, character, new ArrayList<>(), new ArrayList<>());
        for (Ally ally : character.getAllies()) {
            assertEquals(ally.getHealth(), 17);
        }
        // Char dmg + ally dmg = 5 + 1 --> slug dies
        // Ally still not dead
        assertEquals(slug.getHealth(), 0);
        assertEquals(character.getAllies().size(), 1);

        // Spawn 3 vampires -> 18 dmg
        int vampPos1 = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        int vampPos2 = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        int vampPos3 = orderedPath.indexOf(new Pair<Integer, Integer>(1, 2));
        BasicEnemy vamp1 = new Vampire(new PathPosition(vampPos1, orderedPath));
        BasicEnemy vamp2 = new Vampire(new PathPosition(vampPos2, orderedPath));
        BasicEnemy vamp3 = new Vampire(new PathPosition(vampPos3, orderedPath));
        world.addBasicEnemy(vamp1);
        world.addBasicEnemy(vamp2);
        world.addBasicEnemy(vamp3);
        // Fight character
        world.runBattles();
        // Assert ally dies and is removed
        assertEquals(character.getAllies().size(), 0);
    }

    @Test
    public void allyToZombieTest() {

        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(3, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        // Need to make sure the zombie can last multiple hits so it can perform a critical bite
        zombie.setHealth(1000);
        character.setHealth(1000);

        // Load the card village to the character
        Card Card = world.loadCard("cardBarracks");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(Card.getX(), Card.getY(), 0, 3);
        
        for (int i = 0; i < 5; i++) {
            world.runTickMoves();
            world.checkBarracks();
            world.runBattles();
            
            // No allies before the barracks
            if (i == 1) assertEquals(0, character.getAllies().size());
            
            // 1 ally after the barracks
            if (i == 2) assertEquals(1, character.getAllies().size());
            
            // Ally -> Zombie
            if (i == 4) {
                assertEquals(0, character.getAllies().size());
                assertEquals(2, world.getEnemies().size());
            }
        }
    }

    @Test
    public void standardModeTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Test unlimited buying options
        world.setMode(new StandardMode());
        world.giveGoldAndExp(500, 0);
        world.buyItem("itemHealthPotion", 20);
        world.buyItem("itemHealthPotion", 20);
        world.buyItem("itemHelmet", 60);
        world.buyItem("itemHelmet", 60);
        world.buyItem("itemShield", 30);
        assertEquals(world.getCharacterPotions(), 2);


        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        // No difference in battle
        world.runBattles();
        assertEquals(character.getHealth(), 95);
        assertEquals(zombie.getHealth(), 5);
    }

    @Test
    public void survivalModeTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Test cant buy more than 1 potion
        world.setMode(new SurvivalMode());
        world.giveGoldAndExp(500, 0);
        world.buyItem("itemHealthPotion", 20);
        Item pot1 = world.buyItem("itemHealthPotion", 20);
        assertEquals(world.getCharacterPotions(), 1);
        assertEquals(pot1, null);
        // Assert not charged for second potion
        assertEquals(character.getGoldBalance(), 480);

        // Other items are fine
        Item helm1 = world.buyItem("itemHelmet", 60);
        world.buyItem("itemHelmet", 60);
        Item shield = world.buyItem("itemShield", 30);
        assertNotEquals(helm1, null);
        assertNotEquals(shield, null);

        // Complete a cycle --> Can buy another potion
        for (int i = 0; i < 17; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        world.buyItem("itemHealthPotion", 20);
        Item pot2 = world.buyItem("itemHealthPotion", 20);
        assertEquals(pot2, null);
        assertEquals(world.getCharacterPotions(), 2);
    }

    @Test
    public void berserkerModeTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Test cant buy more than 1 armour
        world.setMode(new BerserkerMode());
        world.giveGoldAndExp(500, 0);
        world.buyItem("itemShield", 30);
        Item helm2 = world.buyItem("itemHelmet", 60);
        Item shield = world.buyItem("itemShield", 30);
      
        assertEquals(helm2, null);
        assertEquals(shield, null);
        // Assert not charged for extra items
        assertEquals(character.getGoldBalance(), 470);

        // Other items are fine
        world.buyItem("itemHealthPotion", 20);
        world.buyItem("itemHealthPotion", 20);
        assertEquals(world.getCharacterPotions(), 2);

        // Complete a cycle --> Can buy another armour
        for (int i = 0; i < 17; i++) {
            world.runTickMoves();
        }
        world.buyItem("itemHelmet", 30);
        Item helm4 = world.buyItem("itemBodyArmour", 60);
        assertEquals(helm4, null);
    }

    @Test
    public void confusingModeTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setMode(new ConfusingMode());

        // Test rare ring item
        // Equip rare ring in confusing mode
        // Assert battle does Andruils damage too
        Item ring = world.addUnequippedItem("itemTheOneRing");
        world.equipItem(ring);

        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        // Character does 5 + 10 damage
        world.fight(zombie, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 95);
        assertEquals(zombie.getHealth(), (10-15));

        Zombie zombie2 = new Zombie(add);
        world.addBasicEnemy(zombie2);
        
        // Character dies and is resurrected by One Ring
        character.setHealth(1);
        world.fight(zombie2, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 100);
        assertEquals(zombie2.getHealth(), (10-15));

        // Andruil is no longer aiding in battle since ring is destroyed
        Zombie zombie3 = new Zombie(add);
        world.addBasicEnemy(zombie3);
        world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 95);
        assertEquals(zombie3.getHealth(), 5);

        // Test Andruil
        // Andruil does 20 + 5 dmg
        // TreeStump has higher chance to block attack --> failed cos random(5)
        Item anduril = world.addUnequippedItem("itemAnduril");
        world.equipItem(anduril);
        world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 90);
        assertEquals(zombie3.getHealth(), (5-15));
        // world.unequipItem(anduril, 3, 3);

        // Test TreeStump
        // Blocked two attacks with Andruil TreeStump addition too
        Item treeStump = world.addUnequippedItem("itemTreeStump");
        world.equipItem(treeStump);
        zombie3.setHealth(1000);
        world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 85);
        assertEquals(zombie3.getHealth(), (1000-15));

        world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 80);
        assertEquals(zombie3.getHealth(), ((1000-15)-15));

        world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 80);
        assertEquals(zombie3.getHealth(), ((1000-30)-15));

        world.unequipItem(anduril);

        // Test TreeStump's one ring
        // Andruil unequipped --> back to 5 dmg
        // Block fails on 5th hit
        // Ring special ability is used
        character.setHealth(1);
        for (int i = 0; i < 5; i++) {
            world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        }
        assertEquals(character.getHealth(), 100);
        assertEquals(zombie3.getHealth(), (930));

        // Assert ring ability won't work again
        character.setHealth(1);
        for (int i = 0; i < 5; i++) {
            world.fight(zombie3, character, new ArrayList<>(), new ArrayList<>());
        }
        assertEquals(world.gameOver(), true);
    }

    @Test
    public void switchingModeTest() {
        // Start in standard mode
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setMode(new StandardMode());

        // Add rare items
        // Equip rare ring in standard mode
        // Assert battle doesnt do Andruils damage too
        Item ring = world.addUnequippedItem("itemTheOneRing");
        world.equipItem(ring);

        // Create a Zombie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 2));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Zombie zombie = new Zombie(add);
        world.addBasicEnemy(zombie);

        // Character does 5 damage
        world.fight(zombie, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 95);
        assertEquals(zombie.getHealth(), (5));

        // Change to confusing mode
        // Character does 5 + 20 damage
        world.setMode(new ConfusingMode());
        world.fight(zombie, character, new ArrayList<>(), new ArrayList<>());
        assertEquals(character.getHealth(), 90);
        assertEquals(zombie.getHealth(), -10);
    }

    @Test
    public void initialiseCompensationTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Create a slug
        int currPos = world.getOrderedPath().indexOf(new Pair<Integer, Integer>(0, 2));
        PathPosition add = new PathPosition(currPos, world.getOrderedPath());
        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        // can't be tested it's random
        world.initialiseCompensation();
    }

    @Test
    public void doggieCoinMarketTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character= setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Spawn doggie into world
        // Set hp 1
        // Kill doggie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        doge.setHealth(1);
        world.addBasicEnemy(doge);

        // Assert character doggie coins is amount on random death
        world.runBattles();
        assertEquals(world.getDoggieCoins(), 3);

        // Reach hero's castle, check doggie price is the rolled random amount
        for (int i = 0; i < 16; i++) {
            world.runTickMoves();
            world.checkCycle();
        }
        assertEquals(world.getDoggiePrices(), 37);
        assertEquals(character.getGoldBalance(), 0);
        assertEquals(world.sellDoggieCoin(5), false);
        assertEquals(world.sellDoggieCoin(2), true);
        assertEquals(character.getGoldBalance(), 74);
        assertEquals(world.getDoggieCoins(), 1);
    }

    @Test
    public void elanDoggieCoinInfluenceTest() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        Setup setup = new Setup();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Spawn doggie into world
        // Set hp 1
        // Kill doggie
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        doge.setHealth(1);
        world.addBasicEnemy(doge);

        // Assert character doggie coins is amount on random death
        world.runBattles();
        assertEquals(world.getDoggieCoins(), 3);

        // Spawn elan
        // Set cycles to 39
        Elan elan = new Elan(add);
        elan.setHealth(1);
        world.addBasicEnemy(elan);
        world.setDoggiePrices();
        // Price increases when elan is alive
        assertEquals(world.getDoggiePrices(), 79);

        // Price plummets when elan is dead
        world.runBattles();
        world.setDoggiePrices();
        assertEquals(world.getDoggiePrices(), 14);
        // The price has a chance to fall to 0
        world.setDoggiePrices();
        assertEquals(world.getDoggiePrices(), 0);
    }

    public List<Pair<Integer, Integer>> helperOrderedPath() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
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

        return orderedPath;
    }

    public LoopManiaWorld helperCreateWorld() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
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
        LoopManiaWorld world = new LoopManiaWorld(6, 6, orderedPath);
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);

        return world;
    }

    @Test
    public void isNowEquippedAndUnequipped() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);

        // Add an unequipped weapon into the world
        // Assert the type of weapon
        Item stake = new Stake(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        Item mustBeSword = world.addUnequippedItem("itemSword");
        assertEquals(world.isNowEquipped(stake), false);
        assertEquals(world.isNowEquipped(mustBeSword), true);
        assertEquals(world.isNowUnequipped(mustBeSword), false);

        // Assert character has no weapons to begin
        assertEquals(character.getWeaponSlot(), null);

        // Equip the weapon
        world.equipItem(mustBeSword);
        assertEquals(world.isNowEquipped(mustBeSword), true);
        assertEquals(world.isNowUnequipped(mustBeSword), true);
        
        world.unequipItem(mustBeSword);
        assertEquals(character.getWeaponSlot(), null);
    }

    @Test
    public void sniperTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Add an unequipped weapon into the world
        // Assert the type of weapon
        world.StreamSniping(0, 0);
        world.setBullet(world.getCharacterBullets() + 5);
        world.StreamSniping(0, 0);
        world.setBullet(0);
        // Equip the weapon
        Item sniper = world.addUnequippedItem("itemSniperRifle");
        world.equipItem(sniper);

        world.StreamSniping(0, 0);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);
        world.setBullet(world.getCharacterBullets() + 5);
        world.setBullet(world.getCharacterBullets() + 5);
        world.setBullet(world.getCharacterBullets() + 5);

        assertEquals(doge.getHealth(), 45);
        for (int i = 0; i < 8; i++) world.StreamSniping(0, 1);        
        assertEquals(doge.getHealth(), 5);
        world.StreamSniping(0, 1);
        assertEquals(world.getDoggieCoins(), 3);     

        Slug slug = new Slug(add);
        world.addBasicEnemy(slug);
        world.StreamSniping(0, 1); 
        world.StreamSniping(0, 1);  
        assertEquals(character.getGoldBalance(), 5);
    }

    @Test
    public void nukeTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);
        
        VampireCastleBuilding vampireCastleBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addAllBuilding(vampireCastleBuilding);
        world.explosion(1, 1);

        assertEquals(character.getHealth(), 1);
        assertEquals(world.getAllBuildings().size(), 0);
    }

    @Test
    public void nukeTestOutOfRange() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld3(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 10));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);
        
        VampireCastleBuilding vampireCastleBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(10));
        world.addAllBuilding(vampireCastleBuilding);
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(10));
        world.addAllBuilding(herosCastle);
        HerosCastle herosCastle2 = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addAllBuilding(herosCastle2);
        world.explosion(1, 1);


        assertEquals(character.getHealth(), 100);
        assertEquals(world.getAllBuildings().size(), 3);
    }

    @Test
    public void isValidToEquippedItemByCoordinatesTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);

        // Add an unequipped weapon into the world
        Item mustBeSword = world.addUnequippedItem("itemSword");
        assertEquals(world.isValidToEquippedItemByCoordinates(mustBeSword, 0, 1), true);
        assertEquals(world.isValidToEquippedItemByCoordinates(mustBeSword, 0, 0), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(mustBeSword, 1, 1), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(mustBeSword, 2, 2), false);
        //-------------------------------------------------------
        Item body = world.addUnequippedItem("itemBodyArmour");
        
        assertEquals(world.isValidToEquippedItemByCoordinates(body, 1, 1), true);
        assertEquals(world.isValidToEquippedItemByCoordinates(body, 2, 1), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(body, 1, 2), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(body, 2, 2), false);
        //-------------------------------------------------------
        Item shield = world.addUnequippedItem("itemShield");
        assertEquals(world.isValidToEquippedItemByCoordinates(shield, 2, 1), true);
        assertEquals(world.isValidToEquippedItemByCoordinates(shield, 2, 2), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(shield, 1, 1), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(shield, 1, 2), false);
        //-------------------------------------------------------
        Item theonering = world.addUnequippedItem("itemTheOneRing");
        
        assertEquals(world.isValidToEquippedItemByCoordinates(theonering, 2, 0), true);
        assertEquals(world.isValidToEquippedItemByCoordinates(theonering, 2, 2), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(theonering, 0, 2), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(theonering, 0, 0), false);
        //-------------------------------------------------------
        Item anduril = world.addUnequippedItem("itemAnduril");
        assertEquals(world.isValidToEquippedItemByCoordinates(anduril, 0, 1), true);
        assertEquals(world.isValidToEquippedItemByCoordinates(anduril, 0, 0), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(anduril, 1, 1), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(anduril, 2, 2), false);
        //-------------------------------------------------------
        Item treeStump = world.addUnequippedItem("itemTreeStump");
        assertEquals(world.isValidToEquippedItemByCoordinates(treeStump, 2, 1), true);
        assertEquals(world.isValidToEquippedItemByCoordinates(treeStump, 2, 2), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(treeStump, 1, 1), false);
        assertEquals(world.isValidToEquippedItemByCoordinates(treeStump, 1, 2), false);
        //-------------------------------------------------------
        
    }

    @Test
    public void initialisePossibleSpawnsTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        List<String> list = world.initialisePossibleSpawns();
        assertEquals(list.get(0), "HealthPotion");
        assertEquals(list.get(1), "Gold");
        assertEquals(list.get(2), "Bullet");
    }

    @Test
    public void sellEquippedItemTest() {
        // Initialise a world
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // Add an unequipped weapon into the world
        Item mustBeSword = world.addUnequippedItem("itemSword");
        assertEquals(world.getUnequippedInventoryItems().size(), 1);

        world.equipItem(mustBeSword);
        assertEquals(world.getUnequippedInventoryItems().size(), 1);
        assertEquals(world.getEquippedInventoryItems().size(), 1);
       
        world.sellEquippedItem(0, 0);
        assertEquals(world.getEquippedInventoryItems().size(), 0);
    }

    @Test
    public void isValidAdjacentTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        
        VampireCastleBuilding vampireCastleBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addAllBuilding(vampireCastleBuilding);
        assertEquals(world.isValidAdjacent(0, 0), false);
        assertEquals(world.isValidAdjacent(1, 1), false);
        assertEquals(world.isValidAdjacent(1, 2), true);
        assertEquals(world.isValidAdjacent(6, 6), false);
    }

    @Test
    public void validAdjacencyTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        assertEquals(world.validAdjacency(6, 6), false);
    }

    @Test
    public void isValidPathTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld(orderedPath);

        // Load the card trap to the character
        Card mustBeTrapCard = world.loadCard("cardTrap");
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(mustBeTrapCard.getX(), mustBeTrapCard.getY(), 0, 2);

        // test whether the trap is on the map
        assertEquals(1, world.getAllBuildings().size());
        assertEquals(world.isValidPath(0, 2), false);

    }

    @Test
    public void potionsAndBulletsTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        world.setBullet(5);
        assertEquals(world.getCharacterBullets(), 5);
        world.setPotion(5);
        assertEquals(world.getCharacterPotions(), 5);
        assertEquals(world.getTrancedEnemies().size(),0);
        character.setHealth(5);
        assertEquals(character.getHealth(), 5);
        world.consumePotion();
        assertEquals(character.getHealth(), 100);
    }

    @Test
    public void characOnCoordTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        assertEquals(world.characOnCoord(1,2,1,2), true);
        assertEquals(world.characOnCoord(1,1,1,2), false);
        assertEquals(world.characOnCoord(1,2,2,2), false);
        assertEquals(world.characOnCoord(1,2,2,1), false);
    }

    @Test
    public void processAlliesAfterCombatTest() {
        List<Pair<Integer, Integer>> orderedPath = helperOrderedPath();
        LoopManiaWorld world = new LoopManiaWorld(6, 6, orderedPath);
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character character  = new Character(add);
        world.setCharacter(character);
        // Spawn barracks
        Card bCard = world.loadCard("cardBarracks");
        world.convertCardToBuildingByCoordinates(bCard.getX(), bCard.getY(), 0, 1);
        
        // Ally moves over barracks
        // Assert ally is now in characters party
        world.runTickMoves();
        world.checkBarracks();
        assertEquals(character.getAllies().size(), 1);
        for (Ally ally: character.getAllies()) {
            ally.setHealth(0);
        }
        world.processAlliesAfterCombat();
        assertEquals(character.getAllies().size(), 0);
    }

    @Test
    public void gameOverTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        assertEquals(world.gameOver(), false);
        character.setHealth(0);
        assertEquals(world.gameOver(), true);
    }

    @Test
    public void loadCardTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        world.loadCard("cardVampireCastle");
        world.loadCard("cardBarracks");
        world.loadCard("cardTrap");
        world.loadCard("cardVillage");
        assertEquals(world.getCardEntities().get(0).getType(), "VampireCastle");
        assertEquals(world.getCardEntities().size(), world.getWidth());
        assertEquals(world.getHeight(), 4);
        world.loadCard("cardZombiePit");
        assertEquals(world.getCardEntities().size(), world.getWidth());
        assertEquals(world.getCardEntities().get(0).getType(), "Barracks");   
    }

    @Test
    public void convertCardToBuildingByCoordinatesTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        // Load the card vampire castle to the character
        Card mustBeVampireCastleCard = world.loadCard("cardVampireCastle");
        Card barrack = world.loadCard("cardBarracks");
        Card trap = world.loadCard("cardTrap");
        Card village = world.loadCard("cardVillage");
        Card zombiepit = world.loadCard("cardZombiePit");
        Card bomb = world.loadCard("cardBomb");
        Card tower = world.loadCard("cardTower");
        Card campfire = world.loadCard("cardCampfire");
        
        // Convert the card to a building
        world.convertCardToBuildingByCoordinates(mustBeVampireCastleCard.getX(), mustBeVampireCastleCard.getY(), 0, 0);
        world.convertCardToBuildingByCoordinates(barrack.getX(), barrack.getY(), 1, 1);
        world.convertCardToBuildingByCoordinates(trap.getX(), trap.getY(), 1, 1);
        world.convertCardToBuildingByCoordinates(village.getX(), village.getY(), 1, 1);
        world.convertCardToBuildingByCoordinates(zombiepit.getX(), zombiepit.getY(), 0, 0);
        world.convertCardToBuildingByCoordinates(bomb.getX(), bomb.getY(), 1, 1);
        world.convertCardToBuildingByCoordinates(tower.getX(), tower.getY(), 1, 1);
        assertEquals(world.convertCardToBuildingByCoordinates(campfire.getX(), campfire.getY(), 0, 0), null);
    }

    @Test
    public void invalidConvertCardToBuilidingTests() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        // Load the card vampire castle to the character
        Card mustBeVampireCastleCard = world.loadCard("cardVampireCastle");
        Card barrack = world.loadCard("cardBarracks");
        Card trap = world.loadCard("cardTrap");
        Card village = world.loadCard("cardVillage");

        // Convert the card to a building in invalid coordinates
        assertNotEquals(world.convertCardToBuildingByCoordinates(mustBeVampireCastleCard.getX(), mustBeVampireCastleCard.getY(), 1, 1), null);
        assertNotEquals(world.convertCardToBuildingByCoordinates(barrack.getX(), barrack.getY(), 0, 1), null);
        assertNotEquals(world.convertCardToBuildingByCoordinates(trap.getX(), trap.getY(), 0, 0), null);
        assertNotEquals(world.convertCardToBuildingByCoordinates(village.getX(), village.getY(), 0, 2), null);
    }

    @Test
    public void possiblySpawnEnemiestest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        
        assertEquals(world.possiblySpawnEnemies().size(), 0);
        assertEquals(world.possiblySpawnEnemies().size(), 1);
        
        world.buildingSpawnEnemies(3, 3, "ZombiePit");
        world.buildingSpawnEnemies(0, 2, "VampireCastle");

        assertEquals(world.possiblySpawnEnemies().size(), 2);
    }
    
    @Test
    public void possibleAdvancedEnemySpawnPositionTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        world.possibleAdvancedEnemySpawnPosition(1, 2);
        world.possibleAdvancedEnemySpawnPosition(2, 2);
        world.possibleAdvancedEnemySpawnPosition(1, 1);
        //world.possibleAdvancedEnemySpawnPosition(7, 7);
        //world.possibleAdvancedEnemySpawnPosition(-1, -1);
        
        // List<Pair<Integer, Integer>> orderedPath2 = new ArrayList<>();
        // orderedPath2.add(Pair.with(0,0)); //top left to right
        // orderedPath2.add(Pair.with(0,1));
        // orderedPath2.add(Pair.with(1,1)); 
        // orderedPath2.add(Pair.with(1,0)); 
        // LoopManiaWorld world2 = new LoopManiaWorld(4, 4, orderedPath2);
        
        // assertEquals(world2.possibleAdvancedEnemySpawnPosition(2, 2).getValue0(), 1);
        // assertEquals(world2.possibleAdvancedEnemySpawnPosition(2, 2).getValue1(), 1);
    }

    @Test
    public void analyseBuildingsTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld3(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        Campfire campfire = new Campfire(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addAllBuilding(campfire);
        assertEquals(world.getCharacter().getDamage(), 5);
        world.analyseBuildings();
        assertEquals(world.getCharacter().getDamage(), 10);
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.runTickMoves();
        world.getCharacter().setDamage(5);
        world.analyseBuildings();
        assertEquals(world.getCharacter().getDamage(), 5);
    }

    @Test
    public void analyseTowerTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);

        Campfire campfire = new Campfire(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        world.addAllBuilding(campfire);
        Tower tower = new Tower(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addAllBuilding(tower);

        world.analyseTower(world.getAllBuildings(), doge);
        assertEquals(doge.getHealth(), 42);
    }

    @Test
    public void checkFloorTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        assertEquals(world.checkFloor(), false);
        Item Potion = new HealthPotion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addOnFloorItems(Potion);
        assertEquals(world.checkFloor(), true);

        world.runTickMoves();
        Item Gold = new Gold(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        world.addOnFloorItems(Gold);
        assertEquals(world.checkFloor(), true);

        world.runTickMoves();
        Item Bullet = new HeavyBullet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(2));
        world.addOnFloorItems(Bullet);
        assertEquals(world.checkFloor(), true);
    }

    @Test
    public void spawnBossTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);

        // doge
        for (int i = 0; i < 19; i++) {
            world.spawnBoss();
            world.getCharacter().setCyclesCompleted();
            assertEquals(world.getEnemies().size(), 0);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 19);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 1);
        
        // elan won't spawn because character doesn't have enought experience
        for (int i = 0; i < 19; i++) {
            world.getCharacter().setCyclesCompleted(); 
            world.spawnBoss();
            assertEquals(world.getEnemies().size(), 1);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 39);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 2); 

        // elan will spawn
        for (int i = 0; i < 19; i++) {
            world.getCharacter().setCyclesCompleted();
            world.spawnBoss();
            assertEquals(world.getEnemies().size(), 2);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 59);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 3); 

        world.getCharacter().setExperience(10000);
        for (int i = 0; i < 19; i++) {
            world.getCharacter().setCyclesCompleted();
            world.spawnBoss();
            assertEquals(world.getEnemies().size(), 3);
        }
        assertEquals(world.getCharacter().getCyclesCompleted(), 79);
        world.getCharacter().setCyclesCompleted();
        world.spawnBoss();
        assertEquals(world.getEnemies().size(), 5);
    }


    @Test 
    public void checkCycleTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        assertEquals(world.checkCycle(), true);
        world.runTickMoves();
        assertEquals(world.checkCycle(), false);
        for (int i = 0; i < 11; i++) {
            world.runTickMoves();
        }
        assertEquals(world.checkCycle(), true);
        Building zombiePitBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addEnemyBuilding(zombiePitBuilding);
        assertEquals(world.checkCycle(), true);
        world.runTickMoves();
        assertEquals(world.checkCycle(), false);
    }

    @Test 
    public void randomTest() {
        Setup setup = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = setup.createWorld2(orderedPath);
        Character character = setup.createCharacter(orderedPath, world);
        world.setCharacter(character);
        world.possiblySpawnItem("Gold");
        world.possiblySpawnItem("Bullet");
        world.possiblySpawnItem("HealthPotion");
        world.possiblySpawnItems();
        world.possiblySpawnEnemies();
    }

    @Test
    public void checkTraitTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        Character c = s.createCharacter(orderedPath, world);

        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Turtle");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Turtle");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Librarian");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Librarian");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Stabby");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Healer");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Librarian");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Stabby");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Turtle");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Stabby");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "Stabby");
        world.setCharacterTrait("Random");
        assertEquals(c.getTrait(), "BigSpender");
    }

    @Test
    public void initialiseRewardNoRaresTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);

        List<Pair<String, Integer>> rewards = world.initialiseRewards();
        int rewardNum = 0;
        // The first 8 rewards are items
        // The next 8 rewards are cards
        for (Pair<String, Integer> reward : rewards) {
            if (rewardNum < 8) {
                assertEquals("item", reward.getValue0().substring(0,4));
            } else {
                assertEquals("card", reward.getValue0().substring(0,4));
            }
            rewardNum++;
        }
    }

    @Test
    public void initialiseRewardAllRaresTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        world.loadRareItems("the_one_ring");
        world.loadRareItems("anduril_flame_of_the_west");
        world.loadRareItems("tree_stump");

        List<Pair<String, Integer>> rewards = world.initialiseRewards();
        int rewardNum = 0;
        // The first 11 rewards are items
        // The next 8 rewards are cards
        for (Pair<String, Integer> reward : rewards) {
            if (rewardNum < 11) {
                assertEquals("item", reward.getValue0().substring(0,4));
            } else {
                assertEquals("card", reward.getValue0().substring(0,4));
            }
            rewardNum++;
        }
    }

    @Test
    public void possiblySpawnPotionsTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        s.createCharacter(orderedPath, world);

        // The world will possibly spawn a health potion no more than 2 times at a 1/30 chance each time
        // After at around 60 tries, 2 potions should spawn on the floor
        for (int chance = 0; chance < 60; chance++) {
            world.possiblySpawnItem("HealthPotion");
        }
        assertEquals(world.getOnFloorItems().size(), 2);

    }

    @Test
    public void possiblySpawnGoldTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        s.createCharacter(orderedPath, world);

        // The world will possibly spawn a health potion no more than 2 times at a 1/30 chance each time
        // After at around 60 tries, 2 potions should spawn on the floor
        for (int chance = 0; chance < 60; chance++) {
            world.possiblySpawnItem("Gold");
        }
        assertEquals(world.getOnFloorItems().size(), 2);
    }

    @Test
    public void possiblySpawnBulletsTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        s.createCharacter(orderedPath, world);

        // The world will possibly spawn a health potion no more than 2 times at a 1/30 chance each time
        // After at around 60 tries, 2 potions should spawn on the floor
        for (int chance = 0; chance < 60; chance++) {
            world.possiblySpawnItem("Bullet");
        }
        assertEquals(world.getOnFloorItems().size(), 2);
    }

    @Test
    public void checkCompensationTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);

        // Assert beginning items compensation is null
        // Load excess cards
        // Run check Compensation 
        // Assert item is not null
        assertEquals(world.checkCompensation(), null);
        for (int loadCards = 0; loadCards < 12; loadCards++) {
            world.loadCard("VampireCastle");
        }
        assertEquals(world.checkCompensation().getClass(), Shield.class);

    }

    @Test
    public void excessItemsTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        Character character = s.createCharacter(orderedPath, world);

        // Load more than 16 items for the character
        // The character has been rewarded 20 gold and 20 exp
        for (int itemNum = 0; itemNum < 17; itemNum++) {
            world.addUnequippedItem("itemSword");
        }
        assertEquals(character.getGoldBalance(), 20);
        assertEquals(character.getExperience(), 20); 

    }

    @Test
    public void nullItemsTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        s.createCharacter(orderedPath, world);
        // Load more than 16 items for the character
        // The character has been rewarded 20 gold and 20 exp
        assertEquals(world.addUnequippedItem("Something"), null);
        assertEquals(world.addUnequippedItem("itemHealthPotion").getClass(), HealthPotion.class);
        assertEquals(world.addUnequippedItem("itemHeavyBullet"), null);
    }

    @Test
    public void noMovementInBattleTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        Character character = s.createCharacter(orderedPath, world);

        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 1));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Doggie doge = new Doggie(add);
        world.addBasicEnemy(doge);

        // A battle between character and doggie starts
        // After one tick of battle, the character and doggie are still in battle
        // The character will still be at 0,0
        world.runBattles();
        world.runTickMoves();
        assertEquals(character.getInBattle(), true);
    }

    @Test
    public void excessAlliesTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        Character character = s.createCharacter(orderedPath, world);
        Card barracksC = world.loadCard("cardBarracks");
        world.convertCardToBuildingByCoordinates(barracksC.getX(), barracksC.getY(), 0, 0);

        for (int spawnAlly = 0; spawnAlly < 8; spawnAlly++) {
            world.checkBarracks();
        }
        // Assert whilst barracks spawning happened 8 times, ally size still 5
        assertEquals(character.getAllies().size(), 5);
    }

    @Test
    public void createHerosCastleTest() {
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);

        Building mustBeHerosCastle = world.createHerosCastle();
        assertEquals(mustBeHerosCastle.getClass(), HerosCastle.class);
        world.addEntity(mustBeHerosCastle);
        assertEquals(world.getNonSpecifiedEntity().size(), 1);
    }

    @Test
    public void possiblySpawnItemsTest(){
        Setup s = new Setup();
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        LoopManiaWorld world = s.createWorld2(orderedPath);
        s.createCharacter(orderedPath, world);

        // For an empty world, the chance for an item to spawn on the floor is 1/30
        // After 120 turns, we are confident enough to say that 3 items have spawned on the floor
        for (int chance = 0; chance < 120; chance++) {
            world.possiblySpawnItems();
        }
    }

}
