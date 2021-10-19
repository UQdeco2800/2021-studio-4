package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.components.loading.LoadingScreenDisplay;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.levels.LevelInfo;
import com.deco2800.game.screens.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;
import static com.deco2800.game.screens.MainGameScreen.timeScore;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  private LevelInfo levelInfo;

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

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

  public void setLevel(ScreenType screenType, LevelInfo levelInfo) {
    this.levelInfo = levelInfo;
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }

    switch (screenType) {
      case MAIN_GAME:
        setScreen(new MainGameScreen(this, levelInfo));
        break;
      case LEVEL_EDITOR:
        setScreen(new LevelEditorScreen(this, levelInfo));
        break;
      case LOADING:
        setScreen(new LoadingScreen(this));
        break;

    }
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

  public void setPauseScreen() {
    logger.info("Setting up pause screen");
    setScreen(newScreen(ScreenType.PAUSE));
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
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case SETTINGS:
        return new SettingsScreen(this);
      case LOAD_LEVELS:
        return new LevelSelectScreen(this);
      case DEATH_SCREEN:
        return new DeathScreen(this);
      case SCORE_SCREEN:
        return new ScoreScreen(this, levelInfo, getCompletionTime());
      case PAUSE:
        return new PauseScreen(this);
      case LOADING:
        return new LoadingScreen(this);
      default:
        return null;
    }
  }

  private int getCompletionTime() {
    return Math.round((float)timeScore/1000);
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, SETTINGS, LOAD_LEVELS, PAUSE, DEATH_SCREEN, SCORE_SCREEN, LEVEL_EDITOR, LOADING
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}