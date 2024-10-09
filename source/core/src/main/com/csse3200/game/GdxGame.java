package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.screens.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  public final GameOptions gameOptions = new GameOptions();

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();
    // Sets background to light grey
    setScreenColour(ScreenColour.GREY);
    setScreen(ScreenType.MAIN_MENU);
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
      return switch (screenType) {
          case MAIN_MENU -> new MainMenuScreen(this);
          case LOAD_GAME-> new LoadGameScreen(this);
          case MAIN_GAME -> new MainGameScreen(this);
          case SETTINGS -> new SettingsScreen(this);
          case ACHIEVEMENTS -> new AchievementsScreen(this);
          case HOW_TO_PLAY -> new HowToPlayScreen(this);
          case PLAYER_SELECT -> new PlayerSelectScreen(this);
          case CUTSCENE -> new IntroCutsceneScreen(this);
          case WIN -> new WinScreen(this);
          case LOSE -> new LoseScreen(this);
          case ANIMALS -> new AnimalScreen(this);
          case WEAPONS -> new WeaponScreen(this);
      };
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME,
    SETTINGS, HOW_TO_PLAY, ANIMALS, WEAPONS, ACHIEVEMENTS,LOAD_GAME,
    PLAYER_SELECT, CUTSCENE,
    WIN, LOSE
  }

  /**
   * Set background colour of the game window. I'm pretty sure setting a bg image will override
   * this.
   * @param colour The colour to set as the bg colour.
   */
  public void setScreenColour(ScreenColour colour) {
    switch (colour) {
      case DEFAULT -> Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);
      case BLACK -> Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        case GREY -> Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    }
  }

  /**
   * Possible background colours for the game window.
   */
  public enum ScreenColour {
    /**
     * Default colour (light yellow).
     */
    DEFAULT,
    /**
     * Black, currently used for cutscene for extra spookiness.
     */
    BLACK,
      /**
       * Light grey
       */
      GREY
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
