package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private Sprite sprite;
  private int switcher;

    /**
     * used tp switch between button states
     */
  public MainMenuDisplay() {
      switcher = 1;
  }

  @Override
  public void create() {
    super.create();
    addActors();
    loadTheMusic();
  }
    public void loadTheMusic() {
        MusicService musicScreen = new MusicService();
        musicScreen.playMusic();
    }

    /**
     * Added Background image and initialised buttons
     */
  private void addActors() {
      // This table not necessary?
    table = new Table();
    table.setFillParent(true);
    sprite = new Sprite(new Texture("images/box_boy_title.png"));
    table.setBackground(new SpriteDrawable(sprite));


//    Image title =
//        new Image(
//            ServiceLocator.getResourceService()
//                .getAsset("images/box_boy_title.png", Texture.class));

      table = new Table();
      table.setFillParent(true);
      sprite = new Sprite(new Texture("images/title_screen.png"));
      table.setBackground(new SpriteDrawable(sprite)); // Set background

    //table = new Table();
    //table.setFillParent(true);
    //Image title =
        //new Image(
            //ServiceLocator.getResourceService()
                //.getAsset("images/box_boy_title.png", Texture.class));

    TextButton startBtn = new TextButton("Start", skin);
    startBtn.setBounds(14, 14, 40, 41);
    startBtn.setColor(Color.BLUE);


    TextButton levelSelectBtn = new TextButton("Level Select", skin);
    levelSelectBtn.setColor(Color.ROYAL);

    TextButton settingsBtn = new TextButton("Settings", skin);
    settingsBtn.setColor(Color.ROYAL);

    TextButton exitBtn = new TextButton("Exit", skin);
    exitBtn.setColor(Color.ROYAL);

    TextButton muteBtn = new TextButton("Mute", skin);
    muteBtn.setColor(Color.ROYAL);

    // Triggers an event when the button is pressed
    startBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Start button clicked");
            entity.getEvents().trigger("start");
          }
        });

    levelSelectBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Level select button clicked");
            entity.getEvents().trigger("levelSelect");
          }
        });

    settingsBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Settings button clicked");
            entity.getEvents().trigger("settings");
          }
        });

    exitBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {

            logger.debug("Exit button clicked");
            entity.getEvents().trigger("exit");
          }
        });

      muteBtn.addListener(
              new ChangeListener() {
                  @Override
                  public void changed(ChangeEvent changeEvent, Actor actor) {
                      logger.debug("Mute button clicked");

                      if (switcher % 2 == 1) {
                          muteBtn.setText("Unmute");
                      } else {
                          muteBtn.setText("Mute");
                      } switcher++;
                      entity.getEvents().trigger("mute");
                  }
              });

    //table.add(title);
    table.row();
    table.add(startBtn).padTop(300f).width(250).height(60);

    table.row();
    table.add(levelSelectBtn).padTop(15f);
    table.row();
    table.add(muteBtn).padTop(15f);
    table.row();
    table.add(settingsBtn).padTop(15f);
    table.row();
    table.add(exitBtn).padTop(15f);


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
