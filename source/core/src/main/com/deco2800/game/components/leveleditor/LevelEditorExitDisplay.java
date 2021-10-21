package com.deco2800.game.components.leveleditor;

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
public class LevelEditorExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(LevelEditorExitDisplay.class);
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

    // Add exit button to go back to main menu.
    TextButton exitBtn = new TextButton("Exit", skin);
    //exitBtn.setColor(Color.ROYAL);
    // Add load button to load a different level
    TextButton loadBtn = new TextButton("Load File", skin);
    TextButton saveBtn = new TextButton("Save File", skin);


    table.add(exitBtn).padTop(10f).padRight(10f);
    table.row();
    table.add(loadBtn).padTop(10f).padRight(10f);
    table.row();
    table.add(saveBtn).padTop(10f).padRight(10f);
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

    // Load button event.
    loadBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {

          logger.debug("Load file button clicked");
          entity.getEvents().trigger("loadFile");
        }
      });

    // Save button event.
    saveBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {

          logger.debug("Save file button clicked");
          entity.getEvents().trigger("saveFile");
        }
      });
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
