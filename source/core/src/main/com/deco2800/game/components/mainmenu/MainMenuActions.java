package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.MusicSingleton;
import com.deco2800.game.services.MuteManager;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;

  public MainMenuActions(GdxGame game) {
    this.game = game;
  }

  private static final String[] gameTextures = {
          "images/animatedvoid.png",
          "images/void_spritesheet2.png",
  };

  private static final String[] gameTextureAtlases = {
          "images/void.atlas",
  };

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("levelSelect", this::onLoad);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("mute", this::onMute);
    entity.getEvents().addListener("scoreSelect", this::onScore);
    entity.getEvents().addListener("levelEditor", this::onLevelEditor);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    game.setLevel(GdxGame.ScreenType.MAIN_GAME, LevelDefinition.LEVEL_1);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load completed levels");
    game.setScreen(GdxGame.ScreenType.LOAD_LEVELS);
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.SETTINGS);
  }

  /**
   * Mutes/Unmutes the MainMenuMusic
   */
  private void onMute() {
    logger.info("Muting Music");

    MuteManager mute = MuteManager.getInstance();
    MusicSingleton music = MusicSingleton.getInstance();
    if (mute.getMute() == true) {
      mute.setMute(false);
      music.playMusicSingleton("sounds/MainMenuMusic.mp3");
    } else {
      mute.setMute(true);
      music.pauseMusicSingleton("sounds/MainMenuMusic.mp3");
    }

  }

  /**
   * Swaps to score screen.
   */
  private void onScore() {
    logger.info("Launching score screen");
    game.setScreen(GdxGame.ScreenType.SCORE_SCREEN);
  }

  /**
   * Launches level editor
   */
  private void onLevelEditor() {
    logger.info("Launching level editor");
    game.setLevel(GdxGame.ScreenType.LEVEL_EDITOR, LevelDefinition.LEVEL_1);
  }
}
