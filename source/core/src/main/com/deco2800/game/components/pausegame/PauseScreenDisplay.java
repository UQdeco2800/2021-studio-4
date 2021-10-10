package com.deco2800.game.components.pausegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.components.InsertImageButton;
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseScreenDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(PauseScreenDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
    //playTheMusic();
  }

  /**
   * Add death screen image, exit and retry buttons.
   */
  private void addActors() {

    InsertImageButton insImage = new InsertImageButton();

    table = insImage.setTable("images/background_pause.png");

    // Add exit button to go back to main menu.
    TextButton exitBtn = new TextButton("Exit", skin);
    exitBtn.setColor(Color.ROYAL);
    exitBtn.setBounds(20, 20, 50, 25);
    TextButton retryBtn = new TextButton("Retry", skin);
    retryBtn.setColor(Color.ROYAL);
    retryBtn.setBounds(20, 20, 50, 25);
    TextButton resumeBtn = new TextButton("Play", skin);
    resumeBtn.setColor(Color.ROYAL);
    resumeBtn.setBounds(20,20,50,25);

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
    // Unpause button event
    resumeBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {

          logger.debug("Unpause button clicked");
          entity.getEvents().trigger("unpause");
        }
      });
    table.add(exitBtn).pad(30f);
    table.row();
    table.add(retryBtn).pad(50f);
    table.row();
    table.add(resumeBtn).pad(30f);
    table.row();
//    table.add(exitBtn).center().padRight(50).padTop(200); // Places the button in the centre.
//    table.add(retryBtn).center().padLeft(50).padTop(200); // Places the button in the centre.
//    table.add(resumeBtn).center().padRight(50).padBottom(200);
    stage.addActor(table);
    //stage.
  }

  /**
   * Play death music
   */
//  public void playTheMusic() {
//    MusicServiceDirectory deathSong = new MusicServiceDirectory();
//    MusicService music = new MusicService(deathSong.death_noise_2);
//    music.changeVolume((float)0.8);
//  }

  @Override
  protected void draw(SpriteBatch batch) {
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
