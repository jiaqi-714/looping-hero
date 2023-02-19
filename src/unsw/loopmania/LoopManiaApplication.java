package unsw.loopmania;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * the main application
 * 
 *  main method from this class
 */
public class LoopManiaApplication extends Application {

    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainController;
    private LoopManiaWorldControllerLoader loopManiaLoader;
    private Parent gameRoot;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // load the settings menu
        SettingsMenuController settingsController = new SettingsMenuController();
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("SettingsMenuView.fxml"));
        settingsLoader.setController(settingsController);
        Parent settingsMenuRoot = settingsLoader.load();

        Button map1 = new Button();
        //put image path here
        Image mapImage = new Image((new File("src/images/world1.png")).toURI().toString());
        ImageView view = new ImageView(mapImage);
        view.setFitHeight(400);
        view.setPreserveRatio(true);
        map1.setPrefSize(300, 200);
        map1.setTranslateY(100);
        map1.setGraphic(view);
        map1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    try {
                        setLoopManiaWorldLoader(new LoopManiaWorldControllerLoader("world1.json"));
                        mainController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
                        mainController.setSettingsSwitcher(() -> {switchToRoot(scene, settingsMenuRoot, primaryStage);});
                        mainMenuController.setGameSwitcher(() -> {
                            switchToRoot(scene, gameRoot, primaryStage);
                            if (!mainController.inHerosCastle()) {
                                mainController.startTimer();
                            }
                        });
                        mainMenuController.setSettingsSwitcher(() -> {switchToRoot(scene, settingsMenuRoot, primaryStage);});
                        settingsController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
                        settingsController.setGameSwitcher(() -> {
                            switchToRoot(scene, gameRoot, primaryStage);
                            if (!mainController.inHerosCastle()) {
                                mainController.startTimer();
                            }
                        });
                        settingsController.setGameOptions(mainController);
                        switchToRoot(scene, mainMenuRoot, primaryStage);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }           
        });
        
        Button map2 = new Button();
        // Put image path here
        Image mapImage2 = new Image((new File("src/images/world2.png")).toURI().toString());
        ImageView view2 = new ImageView(mapImage2);
        view2.setFitHeight(400);
        view2.setPreserveRatio(true);
        map2.setPrefSize(300, 200);
        map2.setGraphic(view2);
        map2.setTranslateX(400);
        map2.setTranslateY(100);
        map2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    try {
                        setLoopManiaWorldLoader(new LoopManiaWorldControllerLoader("world2.json"));
                        mainController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
                        mainController.setSettingsSwitcher(() -> {switchToRoot(scene, settingsMenuRoot, primaryStage);});
                        mainMenuController.setGameSwitcher(() -> {
                            switchToRoot(scene, gameRoot, primaryStage);
                            if (!mainController.inHerosCastle()) {
                                mainController.startTimer();
                            }
                        });
                        mainMenuController.setSettingsSwitcher(() -> {switchToRoot(scene, settingsMenuRoot, primaryStage);});
                        settingsController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
                        settingsController.setGameSwitcher(() -> {
                            switchToRoot(scene, gameRoot, primaryStage);
                            if (!mainController.inHerosCastle()) {
                                mainController.startTimer();
                            }
                        });
                        settingsController.setGameOptions(mainController);
                        switchToRoot(scene, mainMenuRoot, primaryStage);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }           
        });

        Button map3 = new Button();
        // Put image path here
        Image mapImage3 = new Image((new File("src/images/world3.png")).toURI().toString());
        ImageView view3 = new ImageView(mapImage3);
        view3.setFitHeight(400);
        view3.setPreserveRatio(true);
        map3.setPrefSize(300, 200);
        map3.setGraphic(view3);
        map3.setTranslateX(800);
        map3.setTranslateY(100);
        map3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    try {
                        setLoopManiaWorldLoader(new LoopManiaWorldControllerLoader("world3.json"));
                        mainController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
                        mainController.setSettingsSwitcher(() -> {switchToRoot(scene, settingsMenuRoot, primaryStage);});
                        mainMenuController.setGameSwitcher(() -> {
                            switchToRoot(scene, gameRoot, primaryStage);
                            if (!mainController.inHerosCastle()) {
                                mainController.startTimer();
                            }
                        });
                        mainMenuController.setSettingsSwitcher(() -> {switchToRoot(scene, settingsMenuRoot, primaryStage);});
                        settingsController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
                        settingsController.setGameSwitcher(() -> {
                            switchToRoot(scene, gameRoot, primaryStage);
                            if (!mainController.inHerosCastle()) {
                                mainController.startTimer();
                            }
                        });
                        settingsController.setGameOptions(mainController);
                        switchToRoot(scene, mainMenuRoot, primaryStage);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }           
        });

        Group root = new Group();

        Label mapPicker = new Label();
        mapPicker.setText("Choose a map to start your adventure");
        mapPicker.setTranslateX(410);
        mapPicker.setTranslateY(10);
        mapPicker.setAlignment(Pos.CENTER);
        mapPicker.setFont(new Font("Times New Roman", 18));
        
        root.getChildren().add(map1);
        root.getChildren().add(map2);
        root.getChildren().add(map3);
        root.getChildren().add(mapPicker);
        // create new scene with the main menu (so we start with the main menu)
        scene = new Scene(root);
            
        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu

        // deploy the main onto the stage
        mainMenuRoot.requestFocus();
        scene.getRoot().setStyle("-fx-font-family: 'serif', 'arial', 'helvetica'");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setLoopManiaWorldLoader(LoopManiaWorldControllerLoader something) throws IOException {
        this.loopManiaLoader = something;
        this.mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        this.gameRoot = gameLoader.load();
    }

    @Override
    public void stop(){
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage){
        scene.setRoot(root);
        root.requestFocus();
        scene.getRoot().setStyle("-fx-font-family: 'serif', 'arial', 'helvetica'");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
