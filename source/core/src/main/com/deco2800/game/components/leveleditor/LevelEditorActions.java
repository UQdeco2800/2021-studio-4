package com.deco2800.game.components.leveleditor;

import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.LevelGameArea;
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
public class LevelEditorActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(LevelEditorActions.class);
  private GdxGame game;
  private PreviousLevel previousLevel = new PreviousLevel();
  private boolean paused;

  public LevelEditorActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("loadFile", this::onLoadFile);
    entity.getEvents().addListener("saveFile", this::onSaveFile);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  private void onLoadFile() {
    entity.getComponent(EditorUIComponent.class).generateSavePopup(true, false);
  }

  private void onSaveFile() {
    entity.getComponent(EditorUIComponent.class).generateSavePopup();
  }
}
