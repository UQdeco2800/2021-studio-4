package com.deco2800.game.components.pausegame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.levelselect.LevelDisplayActions;
import com.deco2800.game.components.levelselect.PreviousLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseScreenActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(LevelDisplayActions.class);
  private final GdxGame game;
  private final PreviousLevel previousLevel = new PreviousLevel();

  public PauseScreenActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().trigger("StopVoidIfPlayerDead");
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("retry", this::onRestart);
    entity.getEvents().addListener("unpause", this::onUnpause);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Restarts the level.
   */
  private void onRestart() {
    logger.info("Restart the level");
    game.setLevel(GdxGame.ScreenType.MAIN_GAME, previousLevel.getPreviousLevel());
  }

  /**
   * Unpauses the level
   */
  private void onUnpause() {
    logger.info("Unpause the level");
    game.dispose();
  }

}
