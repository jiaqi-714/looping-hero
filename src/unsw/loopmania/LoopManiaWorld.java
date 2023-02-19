package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.javatuples.Pair;
import org.json.JSONObject;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.items.*;
import unsw.loopmania.modes.*;
import unsw.loopmania.enemies.*;
import unsw.loopmania.goals.GoalEvaluator;
import unsw.loopmania.buildings.*;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {
    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    public static final int equippedInventoryWidth = 3;
    public static final int equippedInventoryHeight = 2;
    
    public static final int alliesInventoryWidth = 2;
    public static final int alliesInventoryHeight = 3;

    public static Integer doggieCoinPrice = 0;

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    private List<BasicEnemy> enemies;
    private List<BasicEnemy> spawnerEnemies;
    private List<BasicEnemy> trancedEnemies;

    private List<Card> cardEntities;

    private Integer characterPotions = 0;
    private Integer characterBullets = 0;
    private Integer doggieCoins = 0;
    private boolean doggiePricePlummet = false;
    private boolean elanDead = false;
    private boolean doggieDead = false;
    private Integer compensationItems = 0;
    private List<Item> unequippedInventoryItems;
    private List<Item> equippedInventoryItems;
    private List<Item> onFloorItems;
    private List<String> rareItems;
    private Random rand;

    private List<Building> allBuildingEntities;
    private List<Building> allyBuildingEntities; 
    private List<Building> enemyBuildingEntities; 
    private List<Building> towerEntities;

    // Goals to check
    private GoalEvaluator gameGoals;
    private Mode gameMode;
    
    /**
     * This function read the json oject and prints it 
     * @param json 
     */
    public void loadGoals(JSONObject json) {
        this.gameGoals = new GoalEvaluator(json, this);
        System.out.println(gameGoals.prettyPrint());
    }

    /**
     * Referenced by the controller in order to display goals
     */
    public String printGoals() {
        return gameGoals.prettyPrint();
    }
    
    /**
     * Adding rareItems
     * @param item
     */
    public void loadRareItems(String item) {
        rareItems.add(item);
    }

    /**
     * Checking if character's current experience is above a certain number
     * @param quantity
     * @return true if character's experience is greater
     */
    public boolean isExperienceGoal(Integer quantity) {
        return (character.getExperience() >= quantity);
    }

    /**
     * Checking if character's current gold is above a certain number
     * @param quantity
     * @return true if character's gold is greater
     */
    public boolean isGoldGoal(Integer quantity) {
        return (character.getGoldBalance() >= quantity);
    }

    /**
     * Checking if character's current number of cycles is above a certain number
     * @param quantity
     * @return true if character's cycle is greater
     */
    public boolean isCyclesGoal(Integer quantity) {;
        return (character.getCyclesCompleted() >= quantity);
    }

    /**
     * Checking if all the bosses are dead
     */
    public boolean isBosses() {
        return (elanDead && doggieDead);
    }

    /**
     * Checking if all the goals are met inorder to win
     * @return
     */
    public boolean isGoalsMet() {
        if (gameGoals.evaluate()) {
            return true;
        } else {
            return false;
        }     
    }

    public void setMode(ModeBehaviour mb) {
        gameMode.setModeBehaviour(mb);
    }

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        nonSpecifiedEntities = new ArrayList<>();
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<>();
        this.orderedPath = orderedPath;
        allBuildingEntities = new ArrayList<>();
        allyBuildingEntities = new ArrayList<>();
        enemyBuildingEntities = new ArrayList<>();
        onFloorItems = new ArrayList<>();
        equippedInventoryItems = new ArrayList<>();
        spawnerEnemies = new ArrayList<>();
        towerEntities = new ArrayList<>();
        trancedEnemies = new ArrayList<>();
        gameMode = new Mode();
        setMode(new StandardMode());
        rareItems = new ArrayList<>();
        rand = new Random(5);
        // equippedAlly = new ArrayList<>();
    }

    /**
     * Creates hero's castle object and adds to the world.
     * @return the hero's castle building object
     */
    public Building createHerosCastle() {
        Building herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        allBuildingEntities.add(herosCastle);
        return herosCastle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<BasicEnemy> getEnemies() {
        return this.enemies;
    }
    
    public void addAllBuilding(Building building) {
        this.allBuildingEntities.add(building);
    }
    
    public void addAllyBuilding(Building building) {
        this.allyBuildingEntities.add(building);
    }

    public List<Building> getAllyBuilding() {
        return this.allyBuildingEntities;
    }
    
    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return this.character;
    }



    /**
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(entity);
    }

    /**
     * Returns a list of generic entities in the world
     * @return nonSpecifiedEntitiesList
     */
    public List<Entity> getNonSpecifiedEntity() {
        return nonSpecifiedEntities;
    }

    /**
     * add an enemy for testing purposes
     * @param enemy
     */
    public void addBasicEnemy(BasicEnemy enemy) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(enemy);
        enemies.add(enemy);
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies(){
        // TODO = expand this very basic version
        Pair<Integer, Integer> position = possiblyGetSpawnPosition(enemies.size(), 3, 2); 
        List<BasicEnemy> spawningEnemies = new ArrayList<>();
        
        for (BasicEnemy basicEnemy : this.spawnerEnemies) {
            spawningEnemies.add(basicEnemy);
        }
        this.spawnerEnemies = new ArrayList<BasicEnemy>();
        
        if (position != null){
            int indexInPath = orderedPath.indexOf(position);
            BasicEnemy enemy = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            spawningEnemies.add(enemy);
        }

        return spawningEnemies;
    }

    /**
     * enemy building spawn enemy if the conditions warrant it, adds to world
     * @return enemy to be displayed on screen
     */
    public void buildingSpawnEnemies(int x, int y, String type){
        Pair<Integer, Integer> position = possibleAdvancedEnemySpawnPosition(x, y);
        if (position != null){
            int indexInPath = orderedPath.indexOf(position);
            if (type.equals("VampireCastle")) {
                BasicEnemy vampire = new Vampire(new PathPosition(indexInPath, orderedPath));
                spawnerEnemies.add(vampire);
                enemies.add(vampire);
            }
            else if (type.equals("ZombiePit")) {
                BasicEnemy zombie = new Zombie(new PathPosition(indexInPath, orderedPath));
                spawnerEnemies.add(zombie);
                enemies.add(zombie);
            }
        }
    }

    /**
     * Check if the player reaches the requirement for spawning a boss.
     */
    public void spawnBoss() {
        // checking if character has completed 20 cycles to spawn doggie
        if (this.character.getCyclesCompleted() % 20 == 0 && this.character.getCyclesCompleted() != 0) {
            Pair<Integer, Integer> position1 = possiblyGetSpawnPosition(0, 1, 1); 
            int indexInPath1 = orderedPath.indexOf(position1);
            BasicEnemy doge = new Doggie(new PathPosition(indexInPath1, orderedPath));
            spawnerEnemies.add(doge);
            enemies.add(doge);
        }
        // checking if character has completed 40 cycles and has 10,000 exp
        if (this.character.getExperience() >= 10000 && this.character.getCyclesCompleted() % 40 == 0 && this.character.getCyclesCompleted() != 0) {
            Pair<Integer, Integer> position2 = possiblyGetSpawnPosition(0, 1, 1);
            int indexInPath2 = orderedPath.indexOf(position2);
            BasicEnemy ElonMask = new Elan(new PathPosition(indexInPath2, orderedPath));
            doggiePricePlummet = false;
            spawnerEnemies.add(ElonMask);
            enemies.add(ElonMask);
        }
    }
    /**
     * this returns the possible position to spawn an enemy from a enemy building that is adjacent 
     * to the path.
     * @param x x coordinate
     * @param y y coordinate
     * @return
     */
    public Pair<Integer, Integer> possibleAdvancedEnemySpawnPosition(int x, int y) {
        Random random = new Random();
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        
        // check left
        if ((y-1) >= 0 && isValidPath(x, y-1)) {
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(x, y-1));
            orderedPathSpawnCandidates.add(orderedPath.get(indexPosition));
        }
        // check right
        if ((y+1) <= this.height && isValidPath(x, y+1)) {
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(x, y+1));
            orderedPathSpawnCandidates.add(orderedPath.get(indexPosition));
        }
        // check up
        if ((x-1) >= 0 && isValidPath(x-1, y)) {
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(x-1, y));
            orderedPathSpawnCandidates.add(orderedPath.get(indexPosition));
        }
        // check down
        if ((x+1) <= this.width && isValidPath(x+1, y)) {
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(x+1, y));
            orderedPathSpawnCandidates.add(orderedPath.get(indexPosition));
        }
        // choose random choice
        Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(random.nextInt(orderedPathSpawnCandidates.size()));
        return spawnPosition;
    }

    /**
     * spawns potion/gold/bullet if the conditions warrant it, adds to world
     * Chance of spawning is 5%
     * @return list of the items to be displayed on screen
     */
    public List<Item> possiblySpawnItems() {
        List<Item> spawningItems = new ArrayList<>();
        List<String> possibleSpawns = initialisePossibleSpawns();
        for (String toSpawn : possibleSpawns) {
            Item spawnItem = possiblySpawnItem(toSpawn);
            if (spawnItem != null && !spawningItems.contains(spawnItem)) {
                spawningItems.add(spawnItem);
                // System.out.println(toSpawn + " was spawned");
            }
        }
        return spawningItems;
    }

    /**
     * Rolls the dice on possible spawns for potions/gold
     * @param toSpawn
     * @return
     */
    public Item possiblySpawnItem(String toSpawn) {
        Pair<Integer, Integer> posItem = possiblyGetSpawnPosition(onFloorItems.size(), 2, 30);
        if (posItem != null){
            Item item = null;
            switch (toSpawn) {
                case "HealthPotion":
                    item = new HealthPotion(new SimpleIntegerProperty(posItem.getValue0()), new SimpleIntegerProperty(posItem.getValue1()));
                    break;
                case "Gold":
                    item = new Gold(new SimpleIntegerProperty(posItem.getValue0()), new SimpleIntegerProperty(posItem.getValue1()));
                    break;
                case "Bullet":
                    item = new HeavyBullet(new SimpleIntegerProperty(posItem.getValue0()), new SimpleIntegerProperty(posItem.getValue1()));
                    break;
                default:
                    // System.out.println("Not a valid item to be spawned");
                    break;
            }   
            onFloorItems.add(item);
            return item;
        }
        return null;
    }

    /**
     * Getter for world's on floor items
     * @return
     */
    public List<Item> getOnFloorItems() {
        return this.onFloorItems;
    }

    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    public void killEnemy(BasicEnemy enemy){
        enemy.destroy();
        enemies.remove(enemy);
    }

    //This will return a list of building which character in the range of them
    //If building is in range, add stats to character
    public void analyseBuildings(){
        List<Building> supportingBuilding = new ArrayList<Building>();
        for (Building building: allBuildingEntities){
            //check within range
            //if it is, add to supportingBuilding list
            supportingBuilding.add(building);
        }
        for (Building building2: supportingBuilding){
            // if character in supportingBuilding b 
            if (Math.pow((character.getX()-building2.getX()), 2) +  Math.pow((character.getY()-building2.getY()), 2) < Math.pow((building2.getRange()), 2)) {
                building2.activateAbility(character);
            }
        }
    }

    /**
     * Analyses campfires in support radius of a character
     * @param supportBuildings
     */
    public void analyseCompfire(List<Building> supportBuildings){
        for (Building building: supportBuildings) {
            if (building.getType().equals("Campfire")){
                building.activateAbility(character);
            }
        }
    }

    /**
     * Analyses the towers in support radius of the character
     * @param supportBuildings List of supporting buildings
     * @param enemy enemy
     */
    public void analyseTower(List<Building> supportBuildings, BasicEnemy enemy){
        for (Building building: supportBuildings) {
            if (building.getType().equals("Tower")){
                // System.out.println(" was spawned");
                building.dealDamage(enemy);
            }
        }
    }
    
    /**
     * Run a tick or all ticks of a fight
     * @param enemy 
     * @param character
     * @param supportEnemies
     */
    public void fight(BasicEnemy enemy, Character character, List<BasicEnemy> supportEnemies, List<Building> supportBuildings) {        
        // Calculate final damage, considering the weapon equipped
        // activateWeapon is for specialAbility AND damage
        analyseCompfire(supportBuildings);
        double damageToEnemy = character.activateWeapon(enemy, gameMode);
        if (character.getCompfireBonus()) {
            damageToEnemy = damageToEnemy*2;
        }
        // activateArmour only considers helmet for now
        // enemy only gets damaged when character is not stunned
        if (!character.getStun()) {
            enemy.setHealth(enemy.getHealth() - damageToEnemy);
        }
        else {
            character.setStun(false);
        }
        // if Elan is in support radius, Elan can heal near by NPCS for 5 health
        if (withRangeElan(enemy)) {
            if (enemy.getHealth() + 5 <= enemy.getHealthCap()) {
                enemy.setHealth(enemy.getHealth() + 5);
            }
            else {
                enemy.setHealth(enemy.getHealthCap());
            }
        }

        for (Building tower : towerEntities) {
            if (supportBuildings.contains(tower)) {
                tower.dealDamage(enemy);;
            }
        }

        // CONSIDER HELMET SCALAR REDUCE ENEMY DMG HERE
        enemy.activateAbility(character); // aka e.dealDamage(c);

        enemy.handleTrance();

        trancedEnemies = findTrancedEnemies();

        if (!enemy.getIsTranced()) {    
            // Tranced enemies list is updated here to ensure changes are made on a per fight basis
            for (BasicEnemy trancedEnemy : trancedEnemies) {
                // Tranced Enemies will attack each enemy once per fight
                enemy.setHealth(enemy.getHealth() - trancedEnemy.getDamage());
                // The current enemy will attack each tranced enemy once per fight
                trancedEnemy.setTrancedHealth(trancedEnemy.getHealth() - enemy.getDamage());
            }
        }

        //^-- this sets the damage for the tick 
        List<Ally> convertedAllies = new ArrayList<>();
        for (Ally ally : character.getAllies()) {
            // if ally was turned into a zombie
            // they change to zombie and spawn behind character
            if (ally.getConverted()) {
                convertedAllies.add(ally);
            } else {
                enemy.setHealth(enemy.getHealth() - ally.getDamage());
                ally.setHealth(ally.getHealth() - enemy.getDamage());
            }
        }
        
        convertAlliesToZombies(convertedAllies);

        // v-- change getDefense to activate armour
        character.setHealth(character.getHealth() - character.activateArmour(enemy, gameMode));
        
        // ^-- slug is 3 then c.getDefense is Shield + Helmet + BodyArmour <= 1

        // Do we need to have this?
        if (supportEnemies != null) {
            character.setHealth(character.getHealth() - supportEnemies.size());
        }

        if (character.getHealth() <= 0) {
            if (character.getRing() != null) {
                // System.out.println("Popped ring revive");
                character.getRing().activateAbility(character, enemy);
                removeEquippedInventoryItem(character.getRing());
                character.setRing(null);
            }
            if (character.getShield() != null) {
                // System.out.println("Popped shield revive");
                character.getShield().activateAbility(character, enemy); 
            }
        }
        // Analyse helper buildings like tower HERE
        // Analyse enemy helpers HERE
        // analyseTower(supportBuildings,e);
        character.setCompfireBonus(false);        
    }
    
    /**
     * run the expected battles in the world, based on current world state
     * @return list of enemies which have been killed
     */
    public List<BasicEnemy> runBattles() {
        List<BasicEnemy> battleEnemies = new ArrayList<BasicEnemy>();
        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        for (BasicEnemy enemy: enemies){

            int ePosition = orderedPath.indexOf(new Pair<Integer, Integer>(enemy.getX(), enemy.getY()));
            int cPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));

            int behindE = (ePosition - enemy.getBattleRadius() + orderedPath.size()) % orderedPath.size();
            int aheadE = (ePosition + enemy.getBattleRadius() + orderedPath.size()) % orderedPath.size();
            
            for (int i = 0; i <= enemy.getBattleRadius(); i++) {
                
                if (orderedPath.get(cPosition).equals(orderedPath.get(behindE)) || 
                orderedPath.get(cPosition).equals(orderedPath.get(aheadE))    ) {
                    battleEnemies.add(enemy);
                    enemy.setInBattle(true);
                    character.setInBattle(true);
                } 
                behindE = (behindE + 1 + orderedPath.size()) % orderedPath.size();
                aheadE = (aheadE - 1 + orderedPath.size()) % orderedPath.size();
            } 

        }
        List<BasicEnemy> trancedEnemies = findTrancedEnemies();

        for (BasicEnemy currBattleEnemy: battleEnemies){
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from the enemies list
            // if we killEnemy in prior loop, we get java.util.ConcurrentModificationException
            // due to mutating list we're iterating over
            List<BasicEnemy> supportEnemies = willSupport(currBattleEnemy, character);
            List<Building> supportBuildings = withinSupportRange(character);
            // character.setHealth(1);
            fight(currBattleEnemy, character, supportEnemies, supportBuildings);
            
            // Check character health
            if (currBattleEnemy.getHealth() <= 0 || trancedEnemies.size() == battleEnemies.size()) {
                character.setInBattle(false);
                defeatedEnemies.add(currBattleEnemy);
                killEnemy(currBattleEnemy);
                if (currBattleEnemy.getType().equals("Elan")) {
                    doggiePricePlummet = true;
                    elanDead = true;
                }
                if (currBattleEnemy.getType().equals("Doggie")) {
                    giveDoggieGoldAndExp(currBattleEnemy.getGold(), currBattleEnemy.getExperience());
                    doggieDead = true;
                } else {
                    giveGoldAndExp(currBattleEnemy.getGold(), currBattleEnemy.getExperience());
                }
                // reset in battle for supporting enemies only
                for (BasicEnemy supporEnemy : supportEnemies) {
                    supporEnemy.setInBattle(false);
                }
            } 
        }
        processAlliesAfterCombat();
        moveBasicEnemies();
        // System.out.println("defeatedEnemies: " + defeatedEnemies);
        return defeatedEnemies;
    }

    /**
     * Create a new list of tranced enemies
     * @return Enemies that are tranced
     */
    public List<BasicEnemy> findTrancedEnemies() {
        List<BasicEnemy> trancedEnemies = new ArrayList<BasicEnemy>();
        for (BasicEnemy enemy : enemies) {
            if (enemy.getIsTranced()) trancedEnemies.add(enemy);
        }
        return trancedEnemies;
    }

    /**
     * Checks the HP of the character and will return true for game over if character hp is <= zero
     * @return true/false
     */
    public boolean gameOver() {

        if (character.getHealth() <= 0) {
            character.destroy();
            // System.out.println("Game Over");
            return true;
        }
        return false;
    }

    /**
     * Checks if hero is eligible for compensation reward
     * @return
     */
    public Item checkCompensation() {
        if (compensationItems > 0){
            compensationItems--;
            return initialiseCompensation();      
        }
        return null;
    }

    /**
     * spawn a card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(String cardName){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            removeCard(0);
            compensationItems++;    
        }
        Card newCard = new Card(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0), cardName.substring(4));
        cardEntities.add(newCard);
        return newCard;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index){
        Card card = cardEntities.get(index);
        int x = card.getX();
        card.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * spawn a item in the world and return the item entity
     * @return a item to be spawned in the controller as a JavaFX node
     */
    public Item addUnequippedItem(String type){
        // TODO = expand this - we would like to be able to add multiple types of items, apart from swords
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null){
            // eject the oldest unequipped item and replace it... oldest item is that at beginning of items
            giveGoldAndExp(20, 20);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }
        Item newItem = createNewItem(type, firstAvailableSlot.getValue0(), firstAvailableSlot.getValue1());
        if (newItem != null) {
            if (!(newItem instanceof HealthPotion)) {
                unequippedInventoryItems.add(newItem);
            }        
            return newItem;
        }
        return null;
    }

    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y){
        Item item = getInventoryItemEntityByCoordinates(x, y, unequippedInventoryItems);
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * spawn a ally in the world and return the item entity
     * @return a ally to be spawned in the controller as a JavaFX node
     */
    public Ally checkBarracks(){
        for (Building building: allBuildingEntities) {
            if (building.getType().equals("Barracks") && character.getX() == building.getX() && character.getY() == building.getY()) {
                building.activateAbility(character);
                // c.getallies has a new ally
                // find the new ally entity if it was created
                // return that newAlly
                // Remove oldest ally if excess allies
                Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForAlly();
                if (firstAvailableSlot == null) {
                    removeAllyByIndex(0);
                    firstAvailableSlot = getFirstAvailableSlotForAlly();
                }
                Ally newAlly = character.getAllies().get(character.getAllies().size() - 1);
                newAlly.x().setValue(firstAvailableSlot.getValue0());
                newAlly.y().setValue(firstAvailableSlot.getValue1());
                // equippedAlly.add(newAlly);
                return newAlly;
            }
        }
        return null;
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForAlly(){
        // first available slot for an Ally...
        for (int y=0; y<alliesInventoryHeight; y++){
            for (int x=0; x<alliesInventoryWidth; x++){
                if (getAllyByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Killing ally that have health <= 0
     */
    public void processAlliesAfterCombat() {
        List<Ally> killAlly = new ArrayList<>();
        for (Ally ally : character.getAllies()) {
            if (ally.getHealth() <= 0) {              
                killAlly.add(ally);
            }
        }
        for (Ally ally : killAlly) {
            removeAllyByCoordinates(ally.getX(), ally.getY());
        }
    }

    /**
     * Getting ally based on the coordinate in the frontend
     * @param x position x
     * @param y position y
     * @return the ally in (x, y)
     */
    public Ally getAllyByCoordinates(int x, int y) {
        for (Ally ally : character.getAllies()) {
            if ((ally.getX() == x) && (ally.getY() == y)) {
                return ally;
            }
        }
        return null;
    }

    // Might need to check if overlapping destroy for same x y will cause problem
    /**
     * Removes the ally by index which stores in character
     * @param index
     */
    private void removeAllyByIndex(int index){
        Ally ally = character.getAllies().get(index);
        ally.destroy();
        character.getAllies().remove(index);
        // equippedAlly.remove(index);
    }

    /**
     * Remove an ally by the coordinates in the frontend
     * @param x
     * @param y
     */
    private void removeAllyByCoordinates(int x, int y) {
        Ally ally = getAllyByCoordinates(x, y);
        ally.destroy();
        // equippedAlly.remove(ally);
        character.getAllies().remove(ally);
    }

    /**
     * check the Barrack and Village when character is on it,and bouns to character
     */
    public void checkVillage(){
        for (Building building: allBuildingEntities) {
            if (character.getX() == building.getX() && character.getY() == building.getY()){
                if (building.getType().equals("Village")) {
                    building.activateAbility(character);
                }
            }
        }
    }

    public void convertAlliesToZombies(List<Ally> alliesToConvert) {
        for (Ally ally : alliesToConvert) {
            int indexInPath = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            BasicEnemy convertedAlly = new Zombie(new PathPosition(indexInPath, orderedPath));
            enemies.add(convertedAlly);
            spawnerEnemies.add(convertedAlly);

            ally.destroy();
            character.getAllies().remove(ally);

        }
    }

    /**
     * run moves which occur with every tick without needing to spawn anything immediately
     */
    public void runTickMoves(){
        if (!character.getInBattle()) {
            character.moveDownPath();
        }
        checkVillage();
        //checkCycle(); - causing zombie pit to spawn 2 zombies
        // mode
        // heros castle
        // moveBasicEnemies();  
    }

    public boolean checkFloor() {
        boolean itemGet = false;
        Character character = this.character;
        List<Item> toRemove = new ArrayList<>();
        for (Item currItem : onFloorItems) {
            if (characOnCoord(character.getX(), character.getY(), currItem.getX(), currItem.getY())) {
                itemGet = true;
                if (currItem.getType().equals("HealthPotion")) {
                    characterPotions++;
                    toRemove.add(currItem);
                } else if (currItem.getType().equals("Gold")) {
                    character.setGoldBalance(character.getGoldBalance() + currItem.getGoldWorth());
                    toRemove.add(currItem);
                }
                else if (currItem.getType().equals("HeavyBullet")) {
                    characterBullets += 5;
                    toRemove.add(currItem);
                }
            }
        }
        for (Item item : toRemove) {
            item.destroy();
            onFloorItems.remove(item);     
        }
        return itemGet;
    }

    /**
     * Checks if a character has completed a cycle and adds more monsters
     */
    public boolean checkCycle() {
        if (character.getX() == 0 && character.getY() == 0) {
            resetBuyLimits();
            setDoggiePrices();
            character.setCyclesCompleted();
            // notify all enemy building to update cycle count
            for (Building building : enemyBuildingEntities) {
                if (building.updateCycle() == true) {
                    buildingSpawnEnemies(building.getX(), building.getY(), building.getType());
                }  
            }
            spawnBoss();
            return true;
        }
        return false;
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Item item){
        item.destroy();
        // System.out.println("Backend - Destroying Unequipped item at " + item.getX() + " " + item.getY());
        unequippedInventoryItems.remove(item);
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeEquippedInventoryItem(Item item){
        // System.out.println("Backend - Destroying Equipped item at " + item.getX() + " " + item.getY());
        item.destroy();
        equippedInventoryItems.remove(item);
    }

    /**
     * return an inventory item by x and y coordinates
     * assumes that no 2 equipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return equipped inventory item at the input position
     */
    private Item getInventoryItemEntityByCoordinates(int x, int y, List<Item> inventoryItems){
        for (Item item: inventoryItems){
            // System.out.println("Item " + item.getType() + " is at " + item.getX() + " " + item.getY());
            if ((item.getX() == x) && (item.getY() == y)){
                // System.out.println("Found item " + item.getType() + " is at " + item.getX() + " " + item.getY());
                return item;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index){
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y=0; y<unequippedInventoryHeight; y++){
            for (int x=0; x<unequippedInventoryWidth; x++){
                if (getInventoryItemEntityByCoordinates(x, y, unequippedInventoryItems) == null){
                    // System.out.println("x coord for available item slot is  " + x);
                    // System.out.println("y coord for available item slot is  " + y);
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x){
        for (Card card: cardEntities){
            if (card.getX() >= x){
                card.x().set(card.getX()-1);
            }
        }
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        for (BasicEnemy enemy : enemies){
            if (!enemy.getInBattle()) {
                if (enemy instanceof Vampire) {
                    Vampire vampire = (Vampire) enemy;
                    vampire.move(allyBuildingEntities);
                }
                else{
                    enemy.move();
                }
                checkTrapForEnemy(enemy);
            }      
        }
    }

    public void checkTrapForEnemy(BasicEnemy enemy) {
        for (Building building : allBuildingEntities){
            if (building.getType().equals("Trap")) {
                if (building.getX() == enemy.getX() && building.getY() == enemy.getY()) {
                    building.dealDamage(enemy);
                    // destory the trap
                    building.destroy();
                    allBuildingEntities.remove(building);
                    break;
                }
            }
        }
    }
    

    /**
     * get a randomly generated position which could spawn an enemy or an item on the floor
     * @return null if random choice is that wont be spawning an item/gold or it isn't possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetSpawnPosition(int numOnFloor, int max, int chance){
        // has a chance spawning a potion/gold pile on a tile the character isn't on or immediately before or after (currently space required = 2)...
        // Random rand = new Random();
        int choice = rand.nextInt(chance); 
        if ((choice == 0 && numOnFloor < max)){
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
            int endNotAllowed = (indexPosition + 3)%orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }
            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));
            return spawnPosition;
        }
        return null;
    }

    /**
     * remove a card by its x, y coordinates
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c: cardEntities){
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)){
                card = c;
                break;
            }
        }   
        Building newBuilding = null;
        // now spawn building
        switch(card.getType()){
            case "Campfire":
                if (!isValidPath(buildingNodeX, buildingNodeY)) {
                    newBuilding = new Campfire(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                    allyBuildingEntities.add(newBuilding);
                }
                break;
            case "Tower":
                if (isValidAdjacent(buildingNodeX, buildingNodeY)) {
                    newBuilding = new Tower(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                    allyBuildingEntities.add(newBuilding);
                    towerEntities.add(newBuilding);
                } 
                break;      
            case "Village": 
                if (isValidPath(buildingNodeX, buildingNodeY)) {
                    newBuilding = new Village(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                    allyBuildingEntities.add(newBuilding);
                }
                break;
            case "Barracks":
                if (isValidPath(buildingNodeX, buildingNodeY)) {    
                    newBuilding = new Barracks(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                    allyBuildingEntities.add(newBuilding);
                }
                break;
            case "VampireCastle":
                if (isValidAdjacent(buildingNodeX, buildingNodeY)) { 
                    newBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                    enemyBuildingEntities.add(newBuilding);
                }
                break;
            case "ZombiePit":
                if (isValidAdjacent(buildingNodeX, buildingNodeY)) { 
                    newBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                    enemyBuildingEntities.add(newBuilding);
                }
                break;
            case "Bomb":
                newBuilding = new Bomb(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                allBuildingEntities.add(newBuilding);
                break;
            case "Trap":
                if (isValidPath(buildingNodeX, buildingNodeY)) {
                    newBuilding = new Trap(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY)); 
                }
                break;
            default:
                //default is trap as it doesnt fit the other categories
                // System.out.println(card.getType() + " is not a valid card type");

        }

        if (newBuilding != null) {
            allBuildingEntities.add(newBuilding);
            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);
            return newBuilding;
        }
        return null;  
    }

    /**
     * Helper function to check support range of enemies
     * @param e
     * @return
     */
    public List<BasicEnemy> willSupport(BasicEnemy enemy, Character character) {
        List<BasicEnemy> supportEnemies = new ArrayList<>();
        // a^2 + b^2 < radius^2
        // radius is enemy support radius
        // ps = possible support

        for (BasicEnemy ps: enemies) {
            // the supporting enemy must have a support range that includes BOTH e and c positions
            int ePosition = orderedPath.indexOf(new Pair<Integer, Integer>(enemy.getX(), enemy.getY()));
            int cPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            int psPosition = orderedPath.indexOf(new Pair<Integer, Integer>(ps.getX(), ps.getY()));
            
            int behind = (psPosition - ps.getSupportRadius() + orderedPath.size()) % orderedPath.size();
            int ahead = (psPosition + ps.getSupportRadius() + orderedPath.size()) % orderedPath.size();

            boolean inEnemyRange = false;
            boolean inCharRange = false;

            for (int i = 0; i <= ps.getSupportRadius(); i++) {
                if (ps != enemy) {
                    if (orderedPath.get(ePosition).equals(orderedPath.get(behind)) || 
                        orderedPath.get(ePosition).equals(orderedPath.get(ahead))    ) {
                        inEnemyRange = true;
                    } 

                    if (orderedPath.get(cPosition).equals(orderedPath.get(behind)) || 
                        orderedPath.get(cPosition).equals(orderedPath.get(ahead))    ) {
                        inCharRange = true;
                    } 
                }   
                behind = (behind + 1 + orderedPath.size()) % orderedPath.size();
                ahead = (ahead - 1 + orderedPath.size()) % orderedPath.size();
            } 
            if (inEnemyRange && inCharRange) {
                supportEnemies.add(ps);
                ps.setInBattle(true);
            } 
        }
        return supportEnemies; 
    }

    /**
     * Helper function to check support range of buildings
     * @param e
     * @return
     */
    public List<Building> withinSupportRange(Character character) {
        List<Building> supportBuildings = new ArrayList<>();
        // a^2 + b^2 < radius^2
        // radius is enemy support radius
        // ps = possible support
        for (Building building: allBuildingEntities) {
            if (Math.pow((character.getX() - building.getX()), 2) + Math.pow((character.getY() - building.getY()), 2) < Math.pow(building.getRange(), 2)){
                supportBuildings.add(building);
                // supportEnemies.setInBattle(true);
            }
        }
        return supportBuildings; 
    }

    /**
     * Checks that a building is on the path
     * Makes sure that there are no other buildings on the same (x,y) coordinate
     * @param x
     * @param y
     * @return
     */
    public boolean isValidPath(int x, int y) { 
        for (Building building : allBuildingEntities) {
            if (building.getX() == x && building.getY() == y) {
                return false;
            }
        }
        if (orderedPath.contains(new Pair<Integer, Integer>(x,y))) {
            return true;   
        } else {
            return false;
        }    
    }

    /**
     * Checks that a building is adjacent to the path
     * Makes sure that there are no other buildings on the same (x,y) coordinate
     * @param x
     * @param y
     * @return
     */
    public boolean isValidAdjacent(int x, int y) {
        // Not on path
        // Next to path
        if (!orderedPath.contains(new Pair<Integer, Integer>(x,y))) {
            for (Building building : allBuildingEntities) {
                if (building.getX() == x && building.getY() == y) {
                    return false;
                }
            }
            if (!validAdjacency(x, y)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper function that checks if an (x,y) coordinate
     * has an adjacent path
     * @param x
     * @param y
     * @return
     */
    public boolean validAdjacency(int x, int y) {
        int i = -1;
        while (i <= 1) {
            if (i != 0) {
                if (orderedPath.contains(new Pair<Integer, Integer>((x + i + orderedPath.size()) % orderedPath.size(),y)) 
                    || orderedPath.contains(new Pair<Integer, Integer>(x, (y + i + orderedPath.size()) % orderedPath.size()))) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    /**
     * When reaching the heros castle, reset any buying limits you had before
     */
    public void resetBuyLimits() { 
        gameMode.performSetLimit(0);
    }

    /**
     * Buy an item from the hero's castle if the character has enough money
     * @param item
     */
    public Item buyItem(String itemType, int cost) {
        if (validGameModePurchase(itemType) && canAfford(cost)) {
            Item boughtItem = addUnequippedItem(itemType);
            gameMode.performIncreaseLimit(boughtItem);
            character.setGoldBalance(character.getGoldBalance() - cost);
            return boughtItem;
        }
        return null;
    }

    public boolean canAfford(int cost) {
        return character.getGoldBalance() >= cost;
    }
    public boolean validGameModePurchase(String itemType) {
        return gameMode.performCanBuy(itemType);
    }
    
    /**
     * Sells an unequipped item from the hero's castle for a flat value of 5 gold
     * @param item
     * @return
     */
    public void sellUnequippedItem(double x, double y) {
        
        Item toRemove = getInventoryItemEntityByCoordinates((int)x, (int)y, unequippedInventoryItems);
        character.setGoldBalance(character.getGoldBalance() + 5);
        removeUnequippedInventoryItem(toRemove);
    }

    /**
     * Sells an equipped item from the hero's castle for a flat value of 5 gold
     * @param item
     * @return
     */
    public void sellEquippedItem(double x, double y) {
        Item toRemove = getInventoryItemEntityByCoordinates((int)x, (int)y, equippedInventoryItems);
        character.setGoldBalance(character.getGoldBalance() + 5);
        unequipItem(toRemove);
        removeEquippedInventoryItem(toRemove);
    }

    /**
     * Gets the equipped item by coordinates
     * unequips it and returns it to the unequipped inventory
     * 
     * @param x
     * @param y
     * @return
     */
    public Pair<Item,Item> convertUnequippedToEquippedItemByCoordinates(int nodeX, int nodeY, int x, int y) {
        Pair<Item, Item> equippingPair;
        Item equippingItem = getInventoryItemEntityByCoordinates(nodeX, nodeY, unequippedInventoryItems);
        Item toUnequip = null;
        if (equippingItem != null && isValidToEquippedItemByCoordinates(equippingItem, x, y)) {
            removeUnequippedInventoryItem(equippingItem);
            // Check equipped slot for an already equipped item
            Item oldItem = getInventoryItemEntityByCoordinates(x, y, equippedInventoryItems);
            if (oldItem != null) {
                // System.out.println("Backend - there's an " + oldItem.getType() + " to 1 UnequipToEquip");
                toUnequip = addUnequippedItem("item"+oldItem.getType());
                removeEquippedInventoryItem(oldItem);
            }
            // System.out.println("Backend - there's an " + equippingItem.getType() + " to 2 UnequipToEquip");
            Item toEquip = createNewItem("item"+equippingItem.getType(), x, y);
            equipItem(toEquip);
            // System.out.println(toEquip);
            // System.out.println(equippingPair.getValue0());
            equippingPair = new Pair<Item, Item>(toEquip, toUnequip);
            return equippingPair;
        }   
        return null;
    }

    /**
     * Gets the equipped item by coordinates
     * unequips it and returns it to the unequipped inventory
     * 
     * @param x
     * @param y
     * @return
     */
    public Pair<Item, Item> convertEquippedToUnequippedItemByCoordinates(int nodeX, int nodeY, int x, int y) {
        Pair<Item, Item> unequippingPair;       
        Item unequippingItem = getInventoryItemEntityByCoordinates(nodeX, nodeY, equippedInventoryItems);
        if (unequippingItem != null) {
            removeEquippedInventoryItem(unequippingItem);
            Item toEquip = null;
            Item oldItem = getInventoryItemEntityByCoordinates(x, y, unequippedInventoryItems);
            if (oldItem != null && isValidToEquippedItemByCoordinates(oldItem, nodeX, nodeY)) {
                // unequipped to replace equip
                // System.out.println("Backend - there's an " + oldItem.getType() + " to 1 EquipToUnequip");
                toEquip = createNewItem("item"+oldItem.getType(), nodeX, nodeY);
                equipItem(toEquip);
                removeUnequippedInventoryItem(oldItem);
            }
            // System.out.println("Backend - there's an " + unequippingItem.getType() + " to 2 EquipToUnequip");
            Item toUnequip = addUnequippedItem("item"+unequippingItem.getType());
            unequippingPair = new Pair<Item, Item>(toEquip, toUnequip);
            // System.out.println(unequippingPair.getValue0());
            return unequippingPair;
        }
        return null;
    }

    public Boolean isNowUnequipped(Item item) {
        if (getInventoryItemEntityByCoordinates(item.getX(), item.getY(), equippedInventoryItems) != null) {
            return true;
        }
        return false;
    }

    public Boolean isNowEquipped(Item item) {
        if (getInventoryItemEntityByCoordinates(item.getX(), item.getY(), unequippedInventoryItems) != null) {
            return true;
        }
        return false;
    }
    
    /**
     * If the item type is equipped by a character already
     * And it's the same item type, return false
     * Otherwise swap items
     * If it can, unequip current and add new to character
     */
    public void equipItem(Item equip) {
        switch (equip.getType()) {
            case "Sword":
                // System.out.println("Equipping + " + currItem);
                character.setWeapon(equip);
                break;
            case "Staff":
                character.setWeapon(equip);
                break;
            case "Stake":
                character.setWeapon(equip);
                break;
            case "Anduril":
                character.setWeapon(equip);
                break;
            case "SniperRifle":
                character.setWeapon(equip);
                break;
            case "TreeStump":
                character.setShield(equip);
                break;
            case "Helmet":
                character.setHelmet(equip);
                break;
            case "Shield":
                character.setShield(equip);
                break;
            case "BodyArmour":
                character.setBody(equip);
                break;
            case "TheOneRing":
                character.setRing(equip);
                break;
            default:
                // System.out.println(equip.getType() + " is not a valid equipped item to add");
                break;
            }
        equippedInventoryItems.add(equip);
        // System.out.println("Backend - Equipped item " + equip.getType());    
    }
    
    /**
     * If the item type is equipped by a character already
     * And it's the same item type, return false
     * Otherwise swap items
     * If it can, unequip current and add new to character
     */
    public void unequipItem(Item unequip) {
        switch (unequip.getType()) {
            case "Sword":
                // System.out.println("Equipping + " + currItem);
                character.setWeapon(null);
                break;
            case "Staff":
                character.setWeapon(null);
                break;
            case "Stake":
                character.setWeapon(null);
                break;
            case "Anduril":
                character.setWeapon(null);
                break;
            case "SniperRifle":
                character.setWeapon(null);
                break;
            case "TreeStump":
                character.setShield(null);
                break;
            case "Helmet":
                character.setHelmet(null);
                break;
            case "Shield":
                character.setShield(null);
                break;
            case "BodyArmour":
                character.setBody(null);
                break;
            case "TheOneRing":
                character.setRing(null);
                break;
            default:
                // System.out.println(unequip.getType() + " is not a valid unequipped item");
                break;
            }
        unequippedInventoryItems.add(unequip);
        // System.out.println("Backend - Unequipped item " + unequip.getType());
    }

    /**
     * Checks if an item can be placed in a certain inventory slot
     * Firstly checks the instance of the item
     * Checks if it is being placed in the correct slot
     * Removes an old weapon if it existed previously
     * @return newItem
     */
    public boolean isValidToEquippedItemByCoordinates(Item oldItem, int x, int y) {
        if ((oldItem instanceof Weapon)) {
            if (x == 0 && y == 1) {
                return true;
            } 
        } else if ((oldItem instanceof Anduril)) {
            if (x == 0 && y == 1) {
                return true;
            }
        } else if ((oldItem instanceof BodyArmour)) {
            if (x == 1 && y == 1) {
                return true;
            } 
        } else if ((oldItem instanceof Shield)) {
            if (x == 2 && y == 1) {
                return true;
            }
        } else if ((oldItem instanceof TreeStump)) {
            if (x == 2 && y == 1) {
                return true;
            }
        } else if ((oldItem instanceof Helmet)) {
            if (x == 1 && y == 0) {
                return true;
            } 
        } else if ((oldItem instanceof TheOneRing)) {
            if (x == 2 && y == 0) {
                return true;
            }     
        } else {
            // System.out.println(oldItem + "is not in a valid item spot");
            throw new IllegalArgumentException(oldItem.getType() + "is not in a valid item spot");
        }
        return false;
    }

    /**
     * Helper function to return all possible on floor drops
     * @return
     */
    public List<String> initialisePossibleSpawns() {
        List<String> currentSpawns = new ArrayList<>();
        currentSpawns.add("HealthPotion");
        currentSpawns.add("Gold");
        currentSpawns.add("Bullet");
        return currentSpawns;
    }

    /**
     * Gives the character gold and exp for set amounts
     * @param goldAmount
     * @param expAmount
     */
    public void giveGoldAndExp(int goldAmount, int expAmount) {
        character.setGoldBalance(character.getGoldBalance() + goldAmount);
        character.setExperience(expAmount);
    }

    public void giveDoggieGoldAndExp(int coinAmount, int expAmount) {
        doggieCoins = doggieCoins + coinAmount;
        character.setExperience(expAmount);
    }
    
    /**
     * Sets the doggie price fluctuation for selling
     */
    public void setDoggiePrices() {
        // DoggieCoin takes a price between 0~50 gold every cycle
        doggieCoinPrice = rand.nextInt(50);
        if (enemies.stream().anyMatch(e -> e instanceof Elan)) {
            // If elan alive, price increase 30-52 gold per cycle alive
            doggieCoinPrice += rand.nextInt(22) + 30;
        }
        if (doggiePricePlummet) {
            // If elan is dead, prices will plummet towards 0
            doggieCoinPrice -= (rand.nextInt(22));
            if (doggieCoinPrice < 0) {
                doggieCoinPrice = 0;
            }
        }
    }

    public Integer getDoggiePrices() {
        return doggieCoinPrice;
    }

    public Integer getDoggieCoins() {
        return doggieCoins;
    }

    /**
     * Sells the amount of doggie count given
     * @param amount
     * @return
     */
    public boolean sellDoggieCoin(Integer amount) {
        if (doggieCoins != 0 && doggieCoins >= amount) {
            doggieCoins -= amount;
            character.setGoldBalance(character.getGoldBalance() + (amount * doggieCoinPrice));
            return true;
        }
        return false;
    }

    /**
     * Returns the orderedPath of the map
     * @return
     */
    public List<Pair<Integer, Integer>> getOrderedPath() {
        return this.orderedPath;
    }

    /**
     * Adds an enemy building
     * @param building
     */
    public void addEnemyBuilding(Building building) {
        this.enemyBuildingEntities.add(building);
    }

    public List<Building> getEnemyBuilding() {
        return this.enemyBuildingEntities;
    }

    /**
     * Helper function that checks if the coordinates of the character is on the specific entities coordinates
     * @param cX
     * @param cY
     * @param x
     * @param y
     * @return
     */
    public boolean characOnCoord(int characterX, int characterY, int x, int y) {
        if (characterX == x && characterY == y) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper function that creates a new item based on the item type, and its (x,y) coordinate
     * @param type
     * @param x
     * @param y
     * @return
     */
    public Item createNewItem(String type, int x, int y) {
        Item newItem = null;
        switch (type) {
            case "itemSword":
                newItem = new Sword(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            case "itemStake":
                newItem = new Stake(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            case "itemStaff":
                newItem = new Staff(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            case "itemBodyArmour":
                newItem = new BodyArmour(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            case "itemHelmet":
                newItem = new Helmet(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            case "itemShield":
                newItem = new Shield(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            case "itemHealthPotion":
                newItem = new HealthPotion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
                characterPotions++;
                break;
            // case "itemHeavyBullet":
            //     newItem = new HeavyBullet(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            //     characterBullets++;
            //     break;
            case "itemTheOneRing":
                newItem = new TheOneRing(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                //add Andurils behaviour
                Anduril addAnduril = new Anduril(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
                RareItem setAndruil = (RareItem) newItem;
                setAndruil.setSecondAbility(addAnduril);
                gameMode.performIncreaseLimit(newItem);
                break;
            case "itemAnduril":
                newItem = new Anduril(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                // Add tree stump behaviour
                TreeStump addTree = new TreeStump(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
                RareItem setTree = (RareItem) newItem;
                setTree.setSecondAbility(addTree);
                gameMode.performIncreaseLimit(newItem);
                break;
            case "itemTreeStump":
                newItem = new TreeStump(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                // Add One Ring Behaviour
                TheOneRing addRing = new TheOneRing(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
                RareItem setRing = (RareItem) newItem;
                setRing.setSecondAbility(addRing);
                gameMode.performIncreaseLimit(newItem);
                break; 
            case "itemSniperRifle":
                newItem = new SniperRifle(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                break;
            default:
                // System.out.println(type + " is not a valid item for creating new item");
                break;
        } 
        return newItem;
    }

    /**
     * Helper function that returns all compensation for excess cards
     * @return
     */
    public Item initialiseCompensation() {
        final List<Pair<String, Integer>> result = new ArrayList<>();
        //Item drop chances
        result.add(new Pair<String, Integer>("itemSword", 9));
        result.add(new Pair<String, Integer>("itemStake", 10));
        result.add(new Pair<String, Integer>("itemStaff", 10));
        result.add(new Pair<String, Integer>("itemBodyArmour", 10));
        result.add(new Pair<String, Integer>("itemHelmet", 10));
        result.add(new Pair<String, Integer>("itemShield", 10));
        result.add(new Pair<String, Integer>("itemHealthPotion", 10));
        result.add(new Pair<String, Integer>("itemSniperRifle", 1));

        int randomInt = rand.nextInt(71);
        int cProb = 0;
        String finalReward = "";
        for (Pair<String, Integer> reward : result) {
            if (randomInt>= cProb && randomInt < cProb + reward.getValue1()) {
                finalReward = reward.getValue0();
                // System.out.println("Compensation reward is " + reward.getValue0());
                break; 
            }
            cProb += reward.getValue1();
        }
        return addUnequippedItem(finalReward);
    }

    /**
     * Helper function that returns all possible enemy kill drops
     * [{Sword, 5%}, {Stake, 5%}, {Staff, 5%}, {Armour, 5%}, {Helmet, 5%}, 
     * {Shield, 5%}, {Potion, 10%}, {Sniper, 1%}, {TheOneRing, 1%}, 
     * {Anduril, 1%}, {TreeStump, 1%}] = 45%
     * [{Each card, 6%} X 7}] = 42%
     * @return
     */
    public List<Pair<String, Integer>> initialiseRewards() {
        final List<Pair<String, Integer>> result = new ArrayList<>();
        //Item drop chances
        result.add(new Pair<String, Integer>("itemSword", 5));
        result.add(new Pair<String, Integer>("itemStake", 5));
        result.add(new Pair<String, Integer>("itemStaff", 5));
        result.add(new Pair<String, Integer>("itemBodyArmour", 5));
        result.add(new Pair<String, Integer>("itemHelmet", 5));
        result.add(new Pair<String, Integer>("itemShield", 5));
        result.add(new Pair<String, Integer>("itemHealthPotion", 10));
        result.add(new Pair<String, Integer>("itemSniperRifle", 1));
        if (rareItems.contains("the_one_ring")) {
            result.add(new Pair<String, Integer>("itemTheOneRing", 1));
        }
        if (rareItems.contains("anduril_flame_of_the_west")) {
            result.add(new Pair<String, Integer>("itemAnduril", 1));
        }
        if (rareItems.contains("tree_stump")) {
            result.add(new Pair<String, Integer>("itemTreeStump", 1));
        }

        //Card drop chances
        result.add(new Pair<String, Integer>("cardVampireCastle", 6));
        result.add(new Pair<String, Integer>("cardZombiePit", 6));
        result.add(new Pair<String, Integer>("cardBomb", 6));
        result.add(new Pair<String, Integer>("cardTower", 6));
        result.add(new Pair<String, Integer>("cardVillage", 6));
        result.add(new Pair<String, Integer>("cardBarracks", 6));
        result.add(new Pair<String, Integer>("cardTrap", 6));
        result.add(new Pair<String, Integer>("cardCampfire", 6));
        return result;
    }

    public Integer getCharacterPotions(){
        return this.characterPotions;
    }
    /**
     * Set character's health to 100 and the number of potions - 1
     */
    public void consumePotion() {
        this.characterPotions--;
        getCharacter().setHealth(100);
    }

    public List<Building> getAllBuildings(){
        return allBuildingEntities;
    }

    public void setCharacterTrait(String trait) {  
        if (trait.equals("Random")) {
            int r = rand.nextInt(5);
            if (r == 0) {
                trait = "Healer";
            } else if (r == 1) {
                trait = "Stabby";
            } else if (r == 2) {
                trait = "Turtle";
            } else if (r == 3) {
                trait = "BigSpender";
            } else {
                trait = "Librarian";
            }
        }
        character.setTrait(trait);
    }


    /**
     * Checks if elan is with in the raduis of 2
     * @param enemy
     */
    public boolean withRangeElan(BasicEnemy enemy) {
        for (BasicEnemy Elon: enemies) {
            if (Elon.getType().equals("Elan")) {
                if (Math.pow((enemy.getX() - Elon.getX()), 2) + Math.pow((enemy.getY() - Elon.getY()), 2) <= 4){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Given the coordinates on the gridpane, damage the enemies on that spot if any
     * @param colIndex x coordinate
     * @param rowIndex y coordinate
     */
    public boolean StreamSniping(int colIndex, int rowIndex) {
        boolean shotOrNot = false;
        List<BasicEnemy> shot = new ArrayList<>();
        if (getCharacter().getWeaponSlot() != null) {    
            if (character.getWeaponSlot().getType().equals("SniperRifle") && getCharacterBullets() > 0) {                
                characterBullets--;
                shotOrNot = true;
                for (BasicEnemy enemy: enemies) {
                    if (enemy.getX() == colIndex && enemy.getY() == rowIndex) {
                        enemy.setHealth(enemy.getHealth() - 5);
                        if (enemy.getHealth() <= 0) {
                            shot.add(enemy);
                        }
                    }
                }
            }
        }
        for (BasicEnemy enemy : shot) {
            killEnemy(enemy);
            if (enemy.getType().equals("Doggie")) {
                giveDoggieGoldAndExp(enemy.getGold(), enemy.getExperience());
            } else {
                giveGoldAndExp(enemy.getGold(), enemy.getExperience());
            } 
        }
        return shotOrNot;
    }

    public int getCharacterBullets() {
        return this.characterBullets;
    }

    /**
     * Destory all buildings and enemies with in the radius of 3 the center is at (x, y)
     * @param x x coordinate
     * @param y y coordinate
     */
    public void explosion(int x, int y) {
        List<Building> deadBuilding = new ArrayList<>();
        for (Building building: allBuildingEntities) {
            if (Math.pow((building.getX() - x), 2) + Math.pow((building.getY() - y), 2) <= 9 && !building.getType().equals("HerosCastle")) {
                building.destroy();
                deadBuilding.add(building);
            }
        }
        eraseBuilding(deadBuilding);
       
        List<BasicEnemy> deadEnemies = new ArrayList<>();
        for (BasicEnemy enemy: enemies) {
            if (Math.pow((enemy.getX() - x), 2) + Math.pow((enemy.getY() - y), 2) <= 9) {
                enemy.destroy();
                deadEnemies.add(enemy);
            }
        }
        eraseEnemy(deadEnemies);
        
        if (Math.pow((character.getX() - x), 2) + Math.pow((character.getY() - y), 2) <= 9) {
            character.setHealth(1);
        }
    }

    /**
     * Given a list of buildings remove them from the list in the world
     * @param deadBuilding
     */
    public void eraseBuilding(List<Building> deadBuilding) {
        for (Building building: deadBuilding) {
            allBuildingEntities.remove(building);
            allyBuildingEntities.remove(building);
            enemyBuildingEntities.remove(building);
            towerEntities.remove(building);
        }
    }

    /**
     * Given a list of enemies, remove them from the list in the world
     * @param deadenemies
     */
    public void eraseEnemy(List<BasicEnemy> deadenemies) {
        for (BasicEnemy enemy: deadenemies) {
            enemies.remove(enemy);
        }
    }
    
    public List<BasicEnemy> getTrancedEnemies() {
        return this.trancedEnemies;
    }

    public void setPotion(int potion) {
        this.characterPotions = potion;
    }

    public void setBullet(int bullet) {
        this.characterBullets = bullet;
    }

    public List<Item> getUnequippedInventoryItems() {
        return this.unequippedInventoryItems;
    }

    public List<Item> getEquippedInventoryItems() {
        return this.equippedInventoryItems;
    }

    public List<Card> getCardEntities() {
        return this.cardEntities;
    }

    public void addOnFloorItems(Item item) {
        this.onFloorItems.add(item);
    }
}