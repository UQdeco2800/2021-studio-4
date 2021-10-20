package com.deco2800.game.components.maingame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.levelselect.PreviousLevel;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.tasks.TheVoidTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private final GdxGame game;
  private final PreviousLevel previousLevel = new PreviousLevel();
  private boolean paused;

  public MainGameActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("pause", this::onPause);
    entity.getEvents().addListener("retry", this::onRestart);
//    entity.getEvents().addListener("death", this::death);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    TheVoidTasks.paused = false;
    //KeyboardPlayerInputComponent.paused = false;
  }

  private void onPause() {
    logger.info("Pause the level");
    if (!paused) {
      paused = true;
      game.getScreen().pause();
    } else {
      paused = false;
      game.getScreen().resume();
    }
    PlayerStatsDisplay.paused = !PlayerStatsDisplay.paused;
    TheVoidTasks.paused = !TheVoidTasks.paused;

  }

  private void onRestart() {
    logger.info("Restart the level");
    game.setLevel(GdxGame.ScreenType.MAIN_GAME, previousLevel.getPreviousLevel());
    TheVoidTasks.paused = false;
    //KeyboardPlayerInputComponent.paused = false;
  }
}
