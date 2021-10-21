package com.deco2800.game.components.maingame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    // Add pause button to pause game
    TextButton pauseBtn = new TextButton("Pause", skin);

    // Add exit button to go back to main menu.
    TextButton exitBtn = new TextButton("Exit", skin);

    // Add retry button to restart level
    TextButton retryBtn = new TextButton("Retry", skin);


    // Pause button event
    pauseBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Pause button clicked");
          entity.getEvents().trigger("pause");
        }
      });
    table.add(pauseBtn).padTop(10f).padRight(10f);
    table.row();
    table.add(exitBtn).padTop(10f).padRight(10f);
    table.row();
    table.add(retryBtn).padTop(10f).padRight(10f);
    stage.addActor(table);

    // Exit button event.
    exitBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {

          logger.debug("Exit button from death screen is clicked");
          entity.getEvents().trigger("exit");
        }
      });

    // Retry button event.
    retryBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {

          logger.debug("Retry button clicked");
          entity.getEvents().trigger("retry");
        }
      });
  }

  public void addRetryExitOnPause() {
    TextButton exitBtn = new TextButton("Exit", skin);

  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}
