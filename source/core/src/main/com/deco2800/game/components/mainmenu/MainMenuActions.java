package com.deco2800.game.components.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.screens.MainMenuScreen;
import com.deco2800.game.services.MusicService;
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

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("levelSelect", this::onLoad);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("mute", this::onMute);
    entity.getEvents().addListener("scoreSelect", this::onScore);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    game.setScreen(GdxGame.ScreenType.MAIN_GAME);
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
   * Mutes the MainMenuMusic
   */
  private void onMute() {
    logger.info("Muting MainMenuMusic");
    MusicService musicService = new MusicService();
    if (musicService.getmusicPlaying()) {
      musicService.stopMusic();
    } else {
      musicService.playMusic();
    }
  }

  /**
   * Swaps to score screen.
   */
  private void onScore() {
    logger.info("Launching score screen");
    game.setScreen(GdxGame.ScreenType.SCORE_SCREEN);
  }
}