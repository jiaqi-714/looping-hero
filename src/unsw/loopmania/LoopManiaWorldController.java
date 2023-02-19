package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;
import org.javatuples.Pair;
import org.json.JSONObject;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.EnumMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import unsw.loopmania.buildings.Building;
import unsw.loopmania.enemies.BasicEnemy;
import unsw.loopmania.items.Item;
import unsw.loopmania.modes.*;

/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    EQUIPPEDITEM,
    UNEQUIPPEDITEM,
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * The temporary menu that pops up everytime the Character completes a cycle of the path and
     * enters The Hero's Castle
     */
    @FXML
    private AnchorPane herosCastlePane;

    @FXML
    private AnchorPane sellConfirmation;

    @FXML
    private Label liveLabel;

    @FXML
    private AnchorPane traitsSelection;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private GridPane aliveAllies;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private Label hpLevel;

    @FXML
    private Label goldBalance;

    @FXML
    private Label numPotions;

    @FXML
    private Label numDoggieCoins;

    @FXML
    private Label goldWin;

    @FXML
    private Label cycleWin;
    
    @FXML
    private Label expWin;

    @FXML
    private Label attackStat;

    @FXML
    private Label defenceStat;

    @FXML
    private Label numBullets;

    @FXML
    private Label todaysDoggiePrice;

    @FXML
    private Label gameMode;

    @FXML
    private TextField amountDoggieSell;
    
    @FXML
    private Button playMusic;

    @FXML
    private Button mainMenuSwitch;

    @FXML 
    private Button playSpeed;

    @FXML
    private Button displayGoals;

    @FXML
    private Button exitCastle;

    @FXML
    private Button itemBought;

    @FXML
    private ImageView traitPic;

    @FXML
    private Label traitText;

    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    //IMAGES GO HERE
    private Image vampireCastleCardImage;
    private Image swordImage;
    private Image slugImage1;
    private Image slugImage2;
    private Image vampireImage1;
    private Image vampireImage2;
    private Image vampireImage3;
    private Image zombieImage1;
    private Image zombieImage2;
    private Image zombieImage3;
    private Image vampireBuildingImage;
    private Image zombieBuildingImage;
    private Image zombiePitCardImage;
    private Image bombCardImage;
    private Image bombImage;
    private Image towerBuildingImage;
    private Image towerCardImage;
    private Image villageBuildingImage;
    private Image villageCardImage;
    private Image barracksBuildingImage;
    private Image trapBuildingImage;
    private Image barracksCardImage;
    private Image trapCardImage;
    private Image campfireCardImage;
    private Image campfireBuildingImage;
    private Image stakeImage;
    private Image staffImage;
    private Image bodyArmourImage;
    private Image helmetImage;
    private Image shieldImage;
    private Image goldImage;
    private Image oneRingImage;
    private Image healthPotionImage;
    private Image allyImage;
    private Image doggieImage1;
    private Image doggieImage2;
    private Image elanImage1;
    private Image elanImage2;
    private Image sniperRifleImage;
    private Image heavyBulletImage;
    private Image andurilImage;
    private Image treeStumpImage;
    private Image healerImage;
    private Image stabbyImage;
    private Image turtleImage;
    private Image bigSpenderImage;
    private Image librarianImage;
    private Image randomImage;

    // add music to game
    private static MediaPlayer backgroundMediaPlayer;
    private static MediaPlayer killingMediaPlayer;
    private static MediaPlayer shoppingMediaPlayer;
    private static MediaPlayer buttonClickMediaPlayer;
    private static MediaPlayer getItemFromPathMediaPlayer;
    private static MediaPlayer explosionSoundMediaPlayer;
    private static MediaPlayer shottingSoundMediaPlayer;

    private static final double DEFAULT_PLAYSPEED = 0.15;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the building/item which should be dropped
    private ImageView currentlyDraggedImage;
    
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    private MenuSwitcher settingsSwitcher;

    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);
        loadAllImages();
    
        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {

        playSpeed.setFocusTraversable(false);
        playMusic.setFocusTraversable(false);
        displayGoals.setFocusTraversable(false);
        mainMenuSwitch.setFocusTraversable(false);

        liveLabel.setText("");
        gameMode.setText("Standard");

        healthBar.setStyle("-fx-accent: red;");

        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setId("nonpathCell");
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            entity.setId("pathCell");
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x=0; x<LoopManiaWorld.unequippedInventoryWidth; x++){
            for (int y=0; y<LoopManiaWorld.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                emptySlotView.setId("unequippedCell");
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        Building herosCastle = world.createHerosCastle();
        onLoad(herosCastle);

        herosCastlePane.setVisible(false);
        traitsSelection.setVisible(false);
        // sellValue.setVisible(false);

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

        amountDoggieSell.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountDoggieSell.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Media currentMusic = new Media(Paths.get("src/unsw/loopmania/music/background.mp3").toUri().toString());
		backgroundMediaPlayer = new MediaPlayer(currentMusic);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		currentMusic = new Media(Paths.get("src/unsw/loopmania/music/injure2.mp3").toUri().toString());
		killingMediaPlayer = new MediaPlayer(currentMusic);

        currentMusic = new Media(Paths.get("src/unsw/loopmania/music/buying.mp3").toUri().toString());
        shoppingMediaPlayer = new MediaPlayer(currentMusic);

        currentMusic = new Media(Paths.get("src/unsw/loopmania/music/buttonClick.mp3").toUri().toString());
        buttonClickMediaPlayer = new MediaPlayer(currentMusic);

        currentMusic = new Media(Paths.get("src/unsw/loopmania/music/getItemFromPath.mp3").toUri().toString());
        getItemFromPathMediaPlayer = new MediaPlayer(currentMusic);

        currentMusic = new Media(Paths.get("src/unsw/loopmania/music/explosionSound.mp3").toUri().toString());
        explosionSoundMediaPlayer = new MediaPlayer(currentMusic);

        currentMusic = new Media(Paths.get("src/unsw/loopmania/music/scarSound.mp3").toUri().toString());
        shottingSoundMediaPlayer = new MediaPlayer(currentMusic);
    }

    /**
     * create and run the timer
     */
    public void startTimer(){
        // TODO = handle more aspects of the behaviour required by the specification
        System.out.println("starting timer");
        liveLabel.setText("");
        isPaused = false;
        timeline = new Timeline(new KeyFrame(Duration.seconds(DEFAULT_PLAYSPEED), event -> {
            
            if (world.getCharacter().getHealth() < 40 && world.getCharacterPotions() > 0){
                liveLabel.setText("Health low. Consume a health potion!\n(H button)");
            }

            if (world.gameOver()) {
                System.out.println("You lose!");
                liveLabel.setText("You Lose!");
                timeline.stop();
            } 
            if (world.isGoalsMet()) {
                System.out.println("You win!");
                liveLabel.setText("You Win!");
                timeline.stop();
            }
            // Change this to check floor instead of tick moves
            world.runTickMoves();
            if (world.checkFloor()) {
                playGetItemFromPathMediaPlayerMusic();
            }
            List<BasicEnemy> defeatedEnemies = world.runBattles();
            for (BasicEnemy enemy: defeatedEnemies){
                reactToEnemyDefeat(enemy);
                System.out.println("Enemy kiled was " + enemy.getType());
            }
            List<BasicEnemy> newEnemies = world.possiblySpawnEnemies();
            for (BasicEnemy newEnemy: newEnemies){
                onLoad(newEnemy);
                System.out.println("Enemy spawned was " + newEnemy.getType());
            }
            Ally newAlly = world.checkBarracks();
            if (newAlly != null) {
                System.out.println("New ally created at " + newAlly.x() + " " + newAlly.y());
                onLoad(newAlly);
            }
            List<Item> newItems = world.possiblySpawnItems();
            for (Item newItem: newItems) {
                onLoad(newItem, squares);
            }
            updateLabels();
            // Enter the Hero's Castle
            if (world.checkCycle()) {
                timeline.stop();
                herosCastlePane.setVisible(true);
                exitCastle.setFocusTraversable(false);
                liveLabel.setText("Welcome to the Hero's Castle");
                todaysDoggiePrice.setText("$"+world.getDoggiePrices().toString());
                liveLabel.setText("Welcome to the Hero's Castle");
            }
            anchorPaneRoot.setFocusTraversable(false);
            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void changePlaySpeed(ActionEvent event) {

        String speed = ((Button)event.getSource()).getText();
        if (speed.equals("x1")) {
            playSpeed.setText("x2");
            timeline.setRate(2);
        } else if (speed.equals("x2")) {
            playSpeed.setText("x3");
            timeline.setRate(3);
        } else if (speed.equals("x3")) {
            playSpeed.setText("x1");
            timeline.setRate(1);
        }
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        liveLabel.setText("Game paused.");
        playSpeed.setText("x1");
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void terminate(){
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    public void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    // [{Sword, 4%}, {Stake, 5%}, {Staff, 4%}, {Armour, 3%}, {Helmet, 10%}, 
    // {Shield, 5%}, {Potion, 5%}, {TheOneRing, 1%}] = 37%
    // [{Each card, 5%} X 7}] = 35%
    private void loadReward(String type) {
        if (type.startsWith("item")) {
            Item item = world.addUnequippedItem(type);
            onLoadUnequipped(item);
        } else if (type.startsWith("card")) {
            Card card = world.loadCard(type);
            Item compensation = world.checkCompensation();
            onLoad(card);
            if (compensation != null) {
                onLoadUnequipped(compensation);
            }
        } else {
            System.out.println(type + "is not a valid reward type");
        }
    }


    
    /**
    * run GUI events after an enemy is defeated, such as spawning items/experience/gold
    * @param enemy defeated enemy for which we should react to the death of
    */
    public void reactToEnemyDefeat(BasicEnemy enemy){
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        // [{Sword, 4%}, {Stake, 5%}, {Staff, 4%}, {Armour, 3%}, {Helmet, 10%}, 
        // {Shield, 5%}, {Potion, 5%}, {TheOneRing, 1%}] = 37%
        // [{Each card, 5%} X 7}] = 35%
        // Gold and Exp, always given after
        final List<Pair<String, Integer>> rewardIndex = world.initialiseRewards();
        int randomInt = new Random().nextInt(100);
        // cProb = cumulative Prob
        // max cProb is 72 + 14%
        int cProb = 0;
        for (Pair<String, Integer> reward : rewardIndex) {
            if (randomInt >= cProb && randomInt < cProb + reward.getValue1()) {
                loadReward(reward.getValue0());
                break;
            }
            cProb += reward.getValue1();
        }
        System.out.println(cProb + " percent no reward given for chance " + randomInt);
        // loadReward("itemSword");
        // loadReward("cardVampireCastle");        
        playKillMusic();
    }

    /**
     * load an ally into the GUI.
     * @param card
     */
    private void onLoad(Ally ally) {
        ImageView view = new ImageView(allyImage);
        addEntity(ally, view);
        // Add behind character

        // Add into grid
        aliveAllies.getChildren().add(view);
    }

    /**
     * load a building card into the GUI.
     * @param card
     */
    private void onLoad(Card card) {
        ImageView view = null;
        switch (card.getType()) {
            case "VampireCastle":
                view = new ImageView(vampireCastleCardImage);
                view.setId("nonpathCell");
                break;
            case "ZombiePit":
                view = new ImageView(zombiePitCardImage);
                view.setId("nonpathCell");
                break;
            case "Bomb":
                view = new ImageView(bombCardImage);
                //view.setId("nonpathCell");
                break;
            case "Tower":
                view = new ImageView(towerCardImage);
                view.setId("nonpathCell");
                break;
            case "Village":
                view = new ImageView(villageCardImage);
                view.setId("pathCell");
                break;
            case "Barracks":
                view = new ImageView(barracksCardImage);
                view.setId("pathCell");
                break;
            case "Trap":
                view = new ImageView(trapCardImage);
                view.setId("pathCell");
                break;
            case "Campfire":
                view = new ImageView(campfireCardImage);
                view.setId("nonpathCell");
                break;
            default:
                System.out.println(card.getType() + "is not a valid card type");
                break;
        }
        System.out.println("Loaded a card of " + card.getType());
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);
        addEntity(card, view);
        cards.getChildren().add(view);
    }

    
    /**
     * load a gold pile or potion into the GUI.
     * and load the image onto the ground of the game
     * TO MAKE WEAP / HELM / ARMOUR / SHIELD / RING / POTION gridpanes
     * @param item
     */
    private void onLoad(Item item, GridPane squares) {
        ImageView view = null;
        switch(item.getType()){ 
            case "Gold":
                view = new ImageView(goldImage);
                break;
            case "HealthPotion":
                view = new ImageView(healthPotionImage);
                break;
            case "HeavyBullet":
                view = new ImageView(heavyBulletImage);
                break;
            default:
                System.out.println(item.getType() + " is not a floor item");
                break;
        }
        addEntity(item, view);
        squares.getChildren().add(view);
    }

    /**
     * load an item into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * TO MAKE WEAP / HELM / ARMOUR / SHIELD / RING / POTION gridpanes
     * @param item
     */
    private void onLoadUnequipped(Item item) {
        ImageView view = null;
        switch(item.getType()){
            case "Sword":
                view = new ImageView(swordImage);
                view.setId("weaponCell");
                break;
            case "Staff":
                view = new ImageView(staffImage);
                view.setId("weaponCell");
                break;
            case "Stake": 
                view = new ImageView(stakeImage);
                view.setId("weaponCell");
                break;
            case "SniperRifle":
                view = new ImageView(sniperRifleImage);
                view.setId("weaponCell");
                break;
            case "Helmet":
                view = new ImageView(helmetImage);
                view.setId("helmetCell");
                break;
            case "Shield":
                view = new ImageView(shieldImage);
                view.setId("shieldCell");
                break;
            case "BodyArmour": 
                view = new ImageView(bodyArmourImage);
                view.setId("bodyCell");
                break;
            case "HealthPotion":
                view = new ImageView(healthPotionImage);
                System.out.println("Adding potion to potion slot");
                updateLabels();
                return;
            case "TheOneRing":
                view = new ImageView(oneRingImage);
                view.setId("ringCell");
                break;
            case "Anduril":
                view = new ImageView(andurilImage);
                view.setId("weaponCell");
                break;
            case "TreeStump":
                view = new ImageView(treeStumpImage);
                view.setId("shieldCell");
                break;
            case "HeavyBullet":
                view = new ImageView(heavyBulletImage);
                System.out.println("Adding bullets to bullet slot");
                updateLabels();
                return;
            default:
                System.out.println(item.getType() + " item doesn't exist");
                break;
        }
        System.out.println(item.getType() + " unequipped item being added on frontend");
        addDragEventHandlers(view, DRAGGABLE_TYPE.UNEQUIPPEDITEM, unequippedInventory, equippedItems);
        addEntity(item, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an item into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * TO MAKE WEAP / HELM / ARMOUR / SHIELD / RING / POTION gridpanes
     * @param item
     */
    private void onLoadEquipped(Item item) {
        ImageView view = null;
        switch(item.getType()){
            case "Sword":
                view = new ImageView(swordImage);
                break;
            case "Staff":
                view = new ImageView(staffImage);
                break;
            case "Stake": 
                view = new ImageView(stakeImage);
                break;
            case "SniperRifle":
                view = new ImageView(sniperRifleImage);
                break;
            case "Helmet":
                view = new ImageView(helmetImage);
                break;
            case "Shield":
                view = new ImageView(shieldImage);
                break;
            case "BodyArmour": 
                view = new ImageView(bodyArmourImage);
                break;
            case "TheOneRing":
                view = new ImageView(oneRingImage);
                break;
            case "Anduril":
                view = new ImageView(andurilImage);
                break;
            case "TreeStump":
                view = new ImageView(treeStumpImage);
                break;
            default:
                System.out.println(item.getType() + " item doesn't exist");
                break;
        }
        view.setId("unequippedCell"); 
        System.out.println(item.getType() + " equipped item being added on frontend");
        liveLabel.setText(item.getType() + " equipped");
        addDragEventHandlers(view, DRAGGABLE_TYPE.EQUIPPEDITEM, equippedItems, unequippedInventory);
        addEntity(item, view);
        equippedItems.getChildren().add(view);
    }

    /**
     * CHANGE TO SLUG
     * load an enemy into the GUI
     * @param enemy
     */
    private void onLoad(BasicEnemy enemy) {
        ImageView view = null;
        switch(enemy.getType()){
            case "Zombie":
                view = new ImageView(zombieImage1);
                animateZombie(view);
                break;
            case "Slug":
                view = new ImageView(slugImage1);
                animateSlug(view);
                break;
            case "Vampire": 
                view = new ImageView(vampireImage1);
                animateVampire(view);
                break;
            case "Doggie":
                view = new ImageView(doggieImage1);
                animateDoge(view);
                break;
            case "Elan":
                view = new ImageView(elanImage1);
                animateElan(view);
                break;
            default:
                System.out.println(enemy.getType() + " Enemy doesn't exist");
                break;
        }
        convertTrancedEnemy(enemy, view);
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    private void animateZombie(ImageView view) {
        Random r = new Random();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(r.nextInt(1)), new KeyValue(view.imageProperty(), zombieImage1)),
            new KeyFrame(Duration.seconds(r.nextInt(2)), new KeyValue(view.imageProperty(), zombieImage3)),
            new KeyFrame(Duration.seconds(r.nextInt(3)), new KeyValue(view.imageProperty(), zombieImage2))
            );
        timeline.play();
    }

    private void animateSlug(ImageView view) {
        Random r = new Random();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(r.nextInt(1)), new KeyValue(view.imageProperty(), slugImage1)),
            new KeyFrame(Duration.seconds(r.nextInt(3)), new KeyValue(view.imageProperty(), slugImage2))
            );
        timeline.play();
    }

    private void animateVampire(ImageView view) {
        Random r = new Random();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(r.nextInt(1)), new KeyValue(view.imageProperty(), vampireImage1)),
            new KeyFrame(Duration.seconds(r.nextInt(2)), new KeyValue(view.imageProperty(), vampireImage2)),
            new KeyFrame(Duration.seconds(r.nextInt(3)), new KeyValue(view.imageProperty(), vampireImage3))
            );
        timeline.play();
    }

    private void animateDoge(ImageView view) {
        Random r = new Random();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(r.nextInt(1)), new KeyValue(view.imageProperty(), doggieImage1)),
            new KeyFrame(Duration.seconds(r.nextInt(2)), new KeyValue(view.imageProperty(), doggieImage2))
            );
        timeline.play();
    }

    private void animateElan(ImageView view) {
        Random r = new Random();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(r.nextInt(1)), new KeyValue(view.imageProperty(), elanImage1)),
            new KeyFrame(Duration.seconds(r.nextInt(2)), new KeyValue(view.imageProperty(), elanImage2))
            );
        timeline.play();
    }

    /**
     * load a building into the GUI
     * @param building
     * @throws Exception
     */
    private void onLoad(Building building){
        ImageView view = null;
        switch(building.getType()){
            case "Campfire":
                view = new ImageView(campfireBuildingImage);
                break;
            case "Tower":
                view = new ImageView(towerBuildingImage);
                break;
            case "Village": 
                view = new ImageView(villageBuildingImage);
                break;
            case "Barracks":
                view = new ImageView(barracksBuildingImage);
                break;
            case "VampireCastle":
                view = new ImageView(vampireBuildingImage);
                break;
            case "ZombiePit":
                view = new ImageView(zombieBuildingImage);
                break;
            case "Bomb":
                view = new ImageView(bombImage);
                break;
            case "Trap":
                view = new ImageView(trapBuildingImage);
                break;
            case "HerosCastle":
                view = new ImageView(new Image((new File("src/images/heros_castle.png")).toURI().toString()));
                break;
            default:
                System.out.println(building.getType() + " building doesn't exist");
                break;
        }
        System.out.println("Loaded building from card " + building.getType());
        addEntity(building, view);
        squares.getChildren().add(view);
        if (building.getType().equals("Bomb")) {
            LoopManiaWorldController.playExplosionSoundMediaPlayerMusic();
            world.explosion(building.getX(), building.getY());
        }
    }

    
    /**
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        // TODO = be more selective about where something can be dropped
        // for example, in the specification, villages can only be dropped on path, whilst vampire castles cannot go on the path
        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                // TODO = for being more selective about where something can be dropped, consider applying additional if-statement logic
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != targetGridPane && db.hasImage()){

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        // ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);

                        System.out.println("frontend - Before draggable type " + draggableType + targetGridPane.getId());
                        switch (draggableType){
                            case CARD:
                                if (targetGridPane == squares) { 
                                    Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                    if (newBuilding != null) {
                                        onLoad(newBuilding);
                                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                        // System.out.println(newBuilding.getType() + " was converted");
                                    } 
                                    else {
                                        return;
                                    }
                                }
                                break;
                            case UNEQUIPPEDITEM:
                                // If dragging to equipped and is valid equip spot
                                System.out.println(nodeX + " " + nodeY);
                                System.out.println(x + " " + y);
                                
                                Pair<Item,Item> equipItem = convertUnequippedToEquippedItemByCoordinates(nodeX, nodeY, x, y);
                                if (targetGridPane == equippedItems && equipItem != null) {
                                    // System.out.println(equipItem.getType());
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    if (equipItem.getValue0() != null) {
                                        onLoadEquipped(equipItem.getValue0());
                                    }
                                    if (equipItem.getValue1() != null) {
                                        onLoadUnequipped(equipItem.getValue1());
                                    }
                                    // onLoadEquipped(equipItem);
                                    // targetGridPane.add(image, x, y, 1, 1);
                                    System.out.println("frontend - unequipped item is now equipped");
                                }
                                // else {
                                //     return;
                                // }
                                break;
                            case EQUIPPEDITEM:
                                System.out.println(nodeX + " " + nodeY);
                                System.out.println(x + " " + y);
                                
                                Pair<Item,Item> unequipItem = convertEquippedToUnequippedItemByCoordinates(nodeX, nodeY, x, y);
                                if  (targetGridPane == unequippedInventory && unequipItem != null) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    if (unequipItem.getValue0() != null) {
                                        onLoadEquipped(unequipItem.getValue0());
                                    }
                                    if (unequipItem.getValue1() != null) {
                                        onLoadUnequipped(unequipItem.getValue1());
                                    } 
                                    System.out.println("frontend - equipped item is now unequipped");
                                } 
                                // else {
                                //     return;
                                // }
                                break;
                            default:
                                break;
                        }
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }


    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    private Pair<Item,Item> convertUnequippedToEquippedItemByCoordinates(int nodeX, int nodeY, int x, int y) {
        return world.convertUnequippedToEquippedItemByCoordinates(nodeX, nodeY, x, y);
    }

    // private Item convertUnequippedToEquippedItemByCoordinates(String itemType, double x, double y) {
    //     return world.convertUnequippedToEquippedItemByCoordinates(itemType, x, y);
    // }

    private Pair<Item,Item> convertEquippedToUnequippedItemByCoordinates(int nodeX, int nodeY, int x, int y) {
        return world.convertEquippedToUnequippedItemByCoordinates(nodeX, nodeY, x, y);
    }

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);

                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                draggedEntity.setImage(view.getImage());
                // set the Id for the draggedEntity here based off it's draggable type
                // switch (draggableType){
                // }
                
                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = be more selective about whether highlighting changes - if it cannot be dropped in the location, the location shouldn't be highlighted!
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                            //The drag-and-drop gesture entered the target
                            //show the user that it is an actual gesture target
                                if(n.getId() == view.getId() && event.getGestureSource() != n && event.getDragboard().hasImage()){
                                    n.setOpacity(0.7);
                                    // System.out.println("Drag item to path" + n);
                                    // System.out.println("Drag image to path" + view);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you could program the game so if the new highlight location is invalid the highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (n.getId() == view.getId() && currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                                // ((ImageView) n).setImage(zombieImage);
                                System.out.println("Drag item to path" + n);
                                System.out.println("Drag image to path" + view);
                            } 
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        System.out.println("handleKeyPress: " + event.getCode());
        //Key press for space needs changing
        switch (event.getCode()) {
        case SPACE:
            if (isPaused && !inHerosCastle()){
                startTimer();
            }
            else{
                pause();
            }
            break;
        case H:
            if (world.getCharacterPotions() > 0 && world.getCharacter().getHealth() < 100) {
                world.consumePotion();
                updateLabels();
                liveLabel.setText("Potion consumed.");
            } else {
                System.out.println("No Potions to consume.");
                liveLabel.setText("No Potions to consume.");
            }
            break;
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    public void setSettingsSwitcher(MenuSwitcher settingsSwitcher){
        this.settingsSwitcher = settingsSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        playButtonClickMediaPlayerMusic();
        mainMenuSwitcher.switchMenu();
    }

    @FXML
    private void switchToSettingsMenu() throws IOException {
        pause();
        playButtonClickMediaPlayerMusic();
        settingsSwitcher.switchMenu();
    }

    public void loadGoals(JSONObject goals) {
        world.loadGoals(goals);
    }

    public void setMode(String changeMode) {
        switch (changeMode) {
            case "Standard":
                world.setMode(new StandardMode());
                gameMode.setText("Standard");
                break;
                case "Survival":
                world.setMode(new SurvivalMode());
                gameMode.setText("Survival");
                break;
                case "Berserker":
                world.setMode(new BerserkerMode());
                gameMode.setText("Berserker");
                break;
                case "Confusing":
                world.setMode(new ConfusingMode());
                gameMode.setText("Confusing");
                break;
            default:
                throw new IllegalArgumentException("Frontend - Invalid game mode chosen :" + changeMode);
        }
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     */
    @FXML
    private void switchToGame() {
        playSpeed.setText("x1");
        anchorPaneRoot.requestFocus();
        startTimer();
    }
    
    @FXML
    private void switchFromTraits() {
        traitsSelection.setVisible(false);
        if (!herosCastlePane.isVisible()) {
            switchToGame();
        }
    }

    @FXML
    private void switchFromShop() {
        herosCastlePane.setVisible(false);
        switchToGame();
    }

    /**
     * Returns true if the Character is currently in the heros castle and false otherwise
     * Is used in LoopManiaApplication to decide what state the game should be in
     */
    public boolean inHerosCastle() {
        return herosCastlePane.isVisible();
    } 

    @FXML
    public void sellDoggieCoin() {
        // Take in label amount
        Integer amountToSell = 0;
        if (!amountDoggieSell.getText().isEmpty()) {
            amountToSell = Integer.parseInt(amountDoggieSell.getText());
        }
        if (world.sellDoggieCoin(amountToSell)) {
            liveLabel.setText("To the moon!");
        } else {
            liveLabel.setText("Not enough DogeCoin for this purchase");
        }
        updateLabels();
        amountDoggieSell.clear();
    }

    @FXML
    public void clickUnequipped(MouseEvent event) {
        Node itemToSell = event.getPickResult().getIntersectedNode();
        if (itemToSell.getId() != "unequippedCell" && herosCastlePane.isVisible()) {
            Integer colIndex = GridPane.getColumnIndex(itemToSell);
            Integer rowIndex = GridPane.getRowIndex(itemToSell);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().setStyle(("-fx-font-family: 'serif', 'arial', 'helvetica'")); 
            String itemType = itemToSell.getId().substring(0, itemToSell.getId().length()-4);
            alert.setTitle("Are you sure?");
            alert.setContentText("Selling " + itemType + " for 5 gold");
            if (alert.showAndWait().get() == ButtonType.OK) {
                world.sellUnequippedItem(colIndex, rowIndex);
                liveLabel.setText(itemType + " sold for 5 gold.");
                updateLabels();
            }
        }
    }

    @FXML
    public void clickEquipped(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode.getId() == "unequippedCell" && herosCastlePane.isVisible()) {
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().setStyle(("-fx-font-family: 'serif', 'arial', 'helvetica'")); 
            alert.setTitle("Selling item for 5 gold");
            alert.setContentText("Are you sure?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                world.sellEquippedItem(colIndex, rowIndex);
                updateLabels();
            } 
        }
    }

    @FXML
    private void revealTrait() {  
        pause();
        traitsSelection.setVisible(true);
        traitsSelection.requestFocus();
    }

    @FXML
    private void pickTrait(ActionEvent e) throws IOException {
        pause();
        final Node source = (Node) e.getSource();
        world.setCharacterTrait(source.getId());
        switch (source.getId()) {
            case "Healer":
                traitPic.setImage(healerImage);
                break;
            case "Stabby":
                traitPic.setImage(stabbyImage);
                break;
            case "Librarian":
                traitPic.setImage(librarianImage);
                break;
            case "Random":
                traitPic.setImage(randomImage);
                break;
            case "BigSpender":
                traitPic.setImage(bigSpenderImage);
                break;
            case "Turtle":
                traitPic.setImage(turtleImage);
                break;
        }
        traitText.setText(source.getId());
        traitPic.setFitWidth(32);
        traitPic.setFitHeight(32);
        switchFromTraits();
    }

    /**
     * this method is triggered when a user clicks one of the 'Buy' buttons in the Hero's castle.
     * @throws IOException
     */
    @FXML
    private void buyItem(ActionEvent e) throws IOException {
        
        final Node source = (Node) e.getSource();

        int cost = 0;
        switch(source.getId()) {
            case "Sword":
                cost = 100;
                break;
            case "Stake": 
                cost = 45;
                break;
            case "Staff":
                cost = 80;
                break;
            case "BodyArmour": 
                cost = 65;
                break;
            case "Shield":
                cost = 30; 
                break;
            case "Helmet":
                cost = 60;
                break;
            case "HealthPotion":
                cost = 20;
                break;
            default:
                System.out.println(source.getId() + " doesn't exist");
                return;
        }
        // Check item limit for game mode here (performIsLimit())

        Item newItem = world.buyItem("item"+source.getId(), cost);
        if (newItem != null) {  
            onLoadUnequipped(newItem);
            goldBalance.setText(world.getCharacter().getGoldBalance().toString());
            playShoppingMusic();
            liveLabel.setText(newItem.getType() + " bought for " + newItem.getGoldWorth());
        } else {
            if (gameMode.getText().equals("Survival")) {
                liveLabel.setText("Can only purchase 1 health potion each visit to the Hero's Castle.");
            } else if (gameMode.getText().equals("Berserker")) {
                liveLabel.setText("Can only purchase 1 piece of protective gear each visit to the Hero's Castle.");
            } else if (!world.canAfford(cost)) {
                liveLabel.setText("Cannot buy " + source.getId() + "." + "\nCharacter only has " + world.getCharacter().getGoldBalance() + " gold.");
            }
        }
    }

    public void convertTrancedEnemy(BasicEnemy enemy, ImageView image) {  
        ChangeListener<Number> tranceListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                        if (newValue.intValue() == 1) { // First trance tick
                            image.setImage(allyImage);
                        } else if (newValue.intValue() >= 3) { // Last trance tick
                            switch(enemy.getType()){
                                case "Zombie":
                                    image.setImage(zombieImage1);
                                    animateZombie(image);
                                    break;
                                case "Slug":
                                    image.setImage(slugImage1);
                                    animateSlug(image);
                                    break;
                                case "Vampire": 
                                    image.setImage(vampireImage1);
                                    animateVampire(image);
                                    break;
                                default:
                                    System.out.println(enemy.getType() + " Enemy doesn't exist");
                                    break;
                            }
                        }
            }
        };

        ListenerHandle handleTrance = ListenerHandles.createFor(enemy.getIntegerTrance(), image)
                                                .onAttach((o, l) -> o.addListener(tranceListener))
                                                .onDetach((o, l) -> o.removeListener(tranceListener))
                                                .buildAttached();
        handleTrance.attach();

        enemy.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleTrance.detach();
            }
        });
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        // If the item being changed is an unequipped item now being equipped
        // change grid to equippedItems

        // If the item being changed is an equipped item now being unequipped
        // Change grid to unequippedInventory
        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {   
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());  
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                    aliveAllies.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                   aliveAllies.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
    }

    @FXML
    private void updateLabels() {

        Double charHealth = world.getCharacter().getHealth();
        hpLevel.setText(charHealth.toString());
        healthBar.setProgress((charHealth)/100);

        numPotions.setText("x"+world.getCharacterPotions());
        numDoggieCoins.setText("x"+world.getDoggieCoins());
        numBullets.setText("x"+world.getCharacterBullets());

        Integer charGold = world.getCharacter().getGoldBalance();
        goldBalance.setText(charGold.toString());
        goldWin.setText(charGold.toString());

        Integer charCycles = world.getCharacter().getCyclesCompleted();
        cycleWin.setText(charCycles.toString());

        Integer charExp = world.getCharacter().getExperience();
        expWin.setText(charExp.toString());
    
    }
    
    /**
     * This function will set all images for the game world front end
     */
    private void loadAllImages() {
        // Enemies
        slugImage1 = new Image((new File("src/images/slug1.png")).toURI().toString());
        slugImage2 = new Image((new File("src/images/slug2.png")).toURI().toString());
        vampireImage1 = new Image((new File("src/images/vampire1.png")).toURI().toString()); 
        vampireImage2 = new Image((new File("src/images/vampire2.png")).toURI().toString()); 
        vampireImage3 = new Image((new File("src/images/vampire3.png")).toURI().toString()); 
        doggieImage1 = new Image((new File("src/images/doge1.png")).toURI().toString());
        doggieImage2 = new Image((new File("src/images/doge2.png")).toURI().toString());
        elanImage1 = new Image((new File("src/images/elan1.png")).toURI().toString());
        elanImage2 = new Image((new File("src/images/elan2.png")).toURI().toString());
        zombieImage1 = new Image((new File("src/images/zombie1.png")).toURI().toString());
        zombieImage2 = new Image((new File("src/images/zombie2.png")).toURI().toString());
        zombieImage3 = new Image((new File("src/images/zombie3.png")).toURI().toString());

        // animateSlug(new ImageView(slugImage));
        // Buildings + their cards  
        vampireBuildingImage = new Image((new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        zombieBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        bombCardImage = new Image((new File("src/images/bomb_card.png").toURI().toString()));
        bombImage = new Image((new File("src/images/bomb.png").toURI().toString()));
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village_card.png")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/barracks.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());

        // Items + rare items
        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        bodyArmourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        goldImage = new Image((new File("src/images/gold_pile.png")).toURI().toString());
        healthPotionImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        oneRingImage = new Image((new File("src/images/the_one_ring.png")).toURI().toString());
        sniperRifleImage = new Image((new File("src/images/sniper_rifle.png")).toURI().toString());
        heavyBulletImage = new Image((new File("src/images/heavy_bullets.png")).toURI().toString());
        andurilImage = new Image((new File("src/images/anduril_flame_of_the_west.png")).toURI().toString());
        treeStumpImage = new Image((new File("src/images/tree_stump.png")).toURI().toString());

        // // Character + interface images
        allyImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());
        
        healerImage = new Image((new File("src/images/Healthpack.png")).toURI().toString());
        stabbyImage = new Image((new File("src/images/sword.png")).toURI().toString());
        turtleImage = new Image((new File("src/images/turtle.png")).toURI().toString());
        bigSpenderImage = new Image((new File("src/images/bigspender.png")).toURI().toString());
        librarianImage = new Image((new File("src/images/librarian.png")).toURI().toString());
        randomImage = new Image((new File("src/images/random.png")).toURI().toString());
        
    }
    
    @FXML
    public void clickSnipe(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
        boolean shot = false;
        if (colIndex != null && rowIndex != null) {
            shot = world.StreamSniping(colIndex, rowIndex);
        }
        updateLabels();
        if (shot) {
            LoopManiaWorldController.playShottingSoundMediaPlayer();
        }
        
    }

    @FXML
    public void playMusic(ActionEvent event) {
        playButtonClickMediaPlayerMusic();
        boolean playing = backgroundMediaPlayer.getStatus().equals(Status.PLAYING);
        if (playing) {
            backgroundMediaPlayer.pause();
            liveLabel.setText("Music paused");
            // playMusic.setText("PlayMusic");
        }
        else {
            backgroundMediaPlayer.play();
            liveLabel.setText("Music playing");
            // playMusic.setText("StopMusic");
        }
    }   

    public static void playKillMusic() {
        killingMediaPlayer.seek(killingMediaPlayer.getStartTime());
        killingMediaPlayer.play();
    }

    public static void playShoppingMusic() {
        shoppingMediaPlayer.seek(shoppingMediaPlayer.getStartTime());
        shoppingMediaPlayer.play();
    }

    public static void playButtonClickMediaPlayerMusic() {
        buttonClickMediaPlayer.seek(buttonClickMediaPlayer.getStartTime());
        buttonClickMediaPlayer.play();
    }

    public static void playGetItemFromPathMediaPlayerMusic() {
        getItemFromPathMediaPlayer.seek(getItemFromPathMediaPlayer.getStartTime());
        getItemFromPathMediaPlayer.play();
    }

    public static void playExplosionSoundMediaPlayerMusic() {
        explosionSoundMediaPlayer.seek(explosionSoundMediaPlayer.getStartTime());
        explosionSoundMediaPlayer.play();
    }

    public static void playShottingSoundMediaPlayer() {
        shottingSoundMediaPlayer.seek(shottingSoundMediaPlayer.getStartTime());
        shottingSoundMediaPlayer.play();
    }
    
    public static MediaPlayer getBackgroundMediaPlayer() {
        return backgroundMediaPlayer;
    }

    public static MediaPlayer getKillingMediaPlayer() {
        return killingMediaPlayer;
    }

    public static MediaPlayer getShoppingMediaPlayer() {
        return shoppingMediaPlayer;
    }
    
    public static MediaPlayer getButtonClickMediaPlayer() {
        return buttonClickMediaPlayer;
    }

    public static MediaPlayer getItemFromPathMediaPlayer() {
        return getItemFromPathMediaPlayer;
    }

    public static MediaPlayer getExplosionSoundMediaPlayer() {
        return explosionSoundMediaPlayer;
    }

    public static MediaPlayer getShottingSoundMediaPlayer() {
        return shottingSoundMediaPlayer;
    }

    @FXML
    public void displayGoals(ActionEvent event) {
        System.out.println("Displaying goals");
        pause();
            
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game Goals");
        window.setMinWidth(300);
        window.setMinHeight(200);

        Label label = new Label();
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setMaxWidth(275);
        label.setText(world.printGoals());

        Button returnToGame = new Button("Close window");
        returnToGame.setOnAction(e -> {
            window.close();
            // startTimer();
            if (!herosCastlePane.isVisible()) {
                switchToGame();
            }
        });

        VBox layout = new VBox(50);
        layout.setStyle(("-fx-font-family: 'serif', 'arial', 'helvetica'"));
        layout.getChildren().addAll(label, returnToGame);
        VBox.setMargin(returnToGame, new Insets(0, 0, 20, 0));
        layout.setAlignment(Pos.BOTTOM_CENTER);

        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

    }

}
