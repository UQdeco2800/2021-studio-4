package com.deco2800.game.components.levelselect;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.components.levelselect.LevelDisplay;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.services.MuteManager;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LevelDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Sprite sprite;


    @Override
    public void create() {
        super.create();
        addActors();
        playTheMusic();
    }

    private void playTheMusic() {
        MuteManager mute = MuteManager.getInstance();
        if (mute.getMute() == false) {
            MusicServiceDirectory mainMenuSong = new MusicServiceDirectory();
            MusicService musicScreen = new MusicService(mainMenuSong.main_menu);
            musicScreen.playMusic();
        }
        //MusicSingleton s = MusicSingleton.getInstance();
        //s.playSingleton("sounds/MainMenuMusic.mp3");
    }

    /**
     * Add level select screen image and buttons.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        sprite = new Sprite(new Texture("images/title_screen_clean.png"));
        table.setBackground(new SpriteDrawable(sprite)); // Set background.

        // Add exit button to go back to main menu.
        TextButton exitBtn = new TextButton("Exit", skin);

        exitBtn.addListener(
          new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {

                  logger.debug("Exit button clicked");
                  entity.getEvents().trigger("exit");
              }
          });
        exitBtn.setColor(Color.ROYAL);

        table.row();
        table.add(exitBtn).center().padBottom(50f); // Places the button in the centre.
        table.row();

        // List all the files in the levels folder and create a button for each
        for (LevelDefinition level : LevelDefinition.values()) {
            TextButton startBtn = new TextButton(level.getName(), skin);
            startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug(level + " button clicked");
                        entity.getEvents().trigger("start", level);
                    }
                }
            );
            startBtn.setColor(Color.ROYAL);

            TextButton editorBtn = new TextButton("Edit", skin);
            editorBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      logger.debug(level + " button clicked");
                      entity.getEvents().trigger("levelEditor", level);
                  }
              }
            );
            editorBtn.setColor(Color.ROYAL);

            table.add(startBtn).pad(10f);
            table.add(editorBtn).pad(1f);
            table.row();
        }

        stage.addActor(table);
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


