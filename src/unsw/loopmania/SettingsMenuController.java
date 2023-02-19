package unsw.loopmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;

public class SettingsMenuController {
    private MenuSwitcher gameSwitcher;
    private MenuSwitcher mainMenuSwitcher;
    private LoopManiaWorldController gameController;

    @FXML
    private MenuButton menuOptions;

    @FXML
    private Button confirmCustom;

    @FXML
    private Label invalidGoal;

    @FXML
    private Button clearCustom;

    @FXML
    private TextField Cycles;

    @FXML
    private TextField Experience;

    @FXML
    private TextField Gold;

    @FXML
    private CheckBox killBosses;

    @FXML 
    private Slider volumeSlider;

    @FXML
    public void initialise() {
        EventHandler<ActionEvent> modeChosen = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                gameController.setMode(((MenuItem)e.getSource()).getText());
                menuOptions.setText(((MenuItem)e.getSource()).getText() + " selected");
            }
        };

        for (MenuItem m : menuOptions.getItems()) {
            System.out.println("Setting menu option");
            m.setOnAction(modeChosen);
        }

        initialiseIntFields();
      
        EventHandler<ActionEvent> confirmGoals = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                List<JSONObject> newGoals = new ArrayList<JSONObject>();
                if (Cycles.getText().isEmpty() && Experience.getText().isEmpty() && Gold.getText().isEmpty() && !killBosses.isSelected()) {
                    invalidGoal.setText("No goals entered");
                    return;
                } 
                
                if (!Cycles.getText().isEmpty()) {
                    JSONObject newGoal = new JSONObject().put("goal", "cycles");
                    newGoal.put("quantity", Integer.parseInt(Cycles.getText()));
                    newGoals.add(newGoal);
                }

                if (!Experience.getText().isEmpty()) {
                    JSONObject newGoal = new JSONObject().put("goal", "experience");
                    newGoal.put("quantity", Integer.parseInt(Experience.getText()));
                    newGoals.add(newGoal);
                }

                if (!Gold.getText().isEmpty()) {
                    JSONObject newGoal = new JSONObject().put("goal", "gold");
                    newGoal.put("quantity", Integer.parseInt(Gold.getText()));
                    newGoals.add(newGoal);
                }

                if (killBosses.isSelected()) {
                    JSONObject newGoal = new JSONObject().put("goal", "bosses");
                    newGoals.add(newGoal);
                }

                if (!newGoals.isEmpty()) {
                    if (newGoals.size() == 1) {
                        gameController.loadGoals(newGoals.get(0));
                    } else if (newGoals.size() == 2) {
                        JSONObject finalGoal = new JSONObject().put("goal", "AND");
                        JSONArray array = new JSONArray().put(newGoals.get(0));
                        array.put(newGoals.get(1));
                        finalGoal.put("subgoals", array);
                        gameController.loadGoals(finalGoal);
                    } else if (newGoals.size() == 3) {
                        JSONObject finalGoal = new JSONObject().put("goal", "AND");
                        JSONObject g1 = new JSONObject().put("goal", "AND");

                        JSONArray g1array = new JSONArray();
                        g1array.put(newGoals.get(1));
                        g1array.put(newGoals.get(2));
                        g1.put("subgoals", g1array);

                        JSONArray finalArray = new JSONArray();
                        finalArray.put(newGoals.get(0));
                        finalArray.put(g1);
                        finalGoal.put("subgoals", finalArray);
                        gameController.loadGoals(finalGoal);
                    } else if (newGoals.size() == 4) {
                        JSONObject finalGoal = new JSONObject().put("goal", "AND");
                        JSONObject g1 = new JSONObject().put("goal", "AND");
                        JSONObject g2 = new JSONObject().put("goal", "AND");

                        JSONArray g1array = new JSONArray();
                        g1array.put(newGoals.get(0));
                        g1array.put(newGoals.get(1));
                        g1.put("subgoals", g1array);

                        JSONArray g2array = new JSONArray();
                        g2array.put(newGoals.get(2));
                        g2array.put(newGoals.get(3));
                        g2.put("subgoals", g2array);

                        JSONArray finalArray = new JSONArray();
                        finalArray.put(g1);
                        finalArray.put(g2);
                        finalGoal.put("subgoals", finalArray);
                        gameController.loadGoals(finalGoal);
                    }
                    invalidGoal.setText("Successfully changed");
                }
            }
        };
        confirmCustom.setOnAction(confirmGoals);

        clearCustom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    Cycles.clear();
                    Experience.clear();
                    Gold.clear();
                    killBosses.setSelected(false);
                    invalidGoal.setText("");
                }
        });

        // set the VolumeSlider
		MediaPlayer backgroundMediaPlayer = LoopManiaWorldController.getBackgroundMediaPlayer();
        MediaPlayer killingMediaPlayer = LoopManiaWorldController.getKillingMediaPlayer();
        MediaPlayer shoppingMediaPlayer = LoopManiaWorldController.getShoppingMediaPlayer();
        MediaPlayer buttonClickMediaPlayer = LoopManiaWorldController.getButtonClickMediaPlayer();
        MediaPlayer getItemFromPathMediaPlayer = LoopManiaWorldController.getItemFromPathMediaPlayer();
        MediaPlayer explosionSound = LoopManiaWorldController.getExplosionSoundMediaPlayer();
        MediaPlayer shottingSoundMediaPlayer = LoopManiaWorldController.getShottingSoundMediaPlayer();
        // volumeSlider.setValue(backgroundMediaPlayer.getVolume() * 100);
        // volumeSlider.setValue(killingMediaPlayer.getVolume() * 100);
        backgroundMediaPlayer.setVolume(0.1);
        killingMediaPlayer.setVolume(0.1);
        shoppingMediaPlayer.setVolume(0.1);
        buttonClickMediaPlayer.setVolume(0.1);
        getItemFromPathMediaPlayer.setVolume(0.1);
        explosionSound.setVolume(0.1);
        shottingSoundMediaPlayer.setVolume(0.1);
        volumeSlider.setValue(shoppingMediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener(){
            @Override
            public void invalidated(Observable arg0) {
                backgroundMediaPlayer.setVolume(volumeSlider.getValue() / 100);
                killingMediaPlayer.setVolume(volumeSlider.getValue() / 100);
                shoppingMediaPlayer.setVolume(volumeSlider.getValue() / 100);
                buttonClickMediaPlayer.setVolume(volumeSlider.getValue() / 100);
                getItemFromPathMediaPlayer.setVolume(volumeSlider.getValue() / 100);
                explosionSound.setVolume(volumeSlider.getValue() / 100);
                shottingSoundMediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

    // public static MediaPlayer getBackgroundMediaPlayer() {
    //     return backgroundMediaPlayer;
    // }

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        initialise();
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    public void setGameOptions(LoopManiaWorldController gameController) {
        this.gameController = gameController;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        LoopManiaWorldController.playButtonClickMediaPlayerMusic();
        gameSwitcher.switchMenu();
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        LoopManiaWorldController.playButtonClickMediaPlayerMusic();
        mainMenuSwitcher.switchMenu();
    }

    @FXML
    private void setVolume() {
    }

    private void initialiseIntFields() {
        Cycles.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    Cycles.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Experience.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    Experience.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Gold.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    Gold.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}
