package unsw.loopmania;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * controller for the main menu.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    private MenuSwitcher settingsSwitcher;

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    public void setSettingsSwitcher(MenuSwitcher settingsSwitcher){
        this.settingsSwitcher = settingsSwitcher;
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

    /**
     * facilitates switching to settings upon button click
     * @throws IOException
     */
    @FXML
    private void switchToSettings() throws IOException {
        LoopManiaWorldController.playButtonClickMediaPlayerMusic();
        settingsSwitcher.switchMenu();
    }
}
