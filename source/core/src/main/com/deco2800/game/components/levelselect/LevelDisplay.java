package com.deco2800.game.components.levelselect;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.components.InsertImageButton;
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
    public static boolean loadingScreen = false;
    //public static boolean selected;

    @Override
    public void create() {
        super.create();
        addActors();
        playTheMusic();
    }

    private void playTheMusic() {
        MusicServiceDirectory mainMenuSong = new MusicServiceDirectory();
        MusicService musicScreen = new MusicService(mainMenuSong.main_menu);
        //musicScreen.playMusic();
        musicScreen.playSong(true, 0.2f);
        //MusicSingleton s = MusicSingleton.getInstance();
        //s.playSingleton("sounds/MainMenuMusic.mp3");
    }

    /**
     * Add level select screen image and buttons.
     */
    private void addActors() {
        InsertImageButton insImage = new InsertImageButton();

        table = insImage.setTable("ui-elements/title_screen_clean.png");

        int centreWidth1 = Gdx.graphics.getWidth()/2;
        int centreHeight1 = Gdx.graphics.getHeight()/2;
        int buttonDimensionsWidth = (int) Math.round(centreWidth1*0.3);
        int buttonDimensionsHeight = (int) Math.round(centreHeight1*0.15);
        int centreWidth = centreWidth1 - buttonDimensionsWidth/2; // Moves middle of button to Centre
        int centreHeight = centreWidth1 - buttonDimensionsHeight/2;
        int height105Percent = (int) Math.round(centreHeight*0.98);

        int titleWidth = buttonDimensionsWidth*4;
        int titleHeight = buttonDimensionsHeight*4;
        int centreTitleWidth = centreWidth1 - titleWidth/2; // Moves middle of button to Centre
        int centreTitleHeight = centreHeight1 + titleHeight/2;

        /**
         * Creates the button texture for the Exit Button.
         */
        String titleImage = "ui-elements/levels-heading.png";
        ImageButton titleBtn;
        titleBtn = insImage.setImage(titleImage, titleImage,
                centreTitleWidth,centreTitleHeight,
                titleWidth, titleHeight);

        /**
         * Creates the button texture for the Exit Button.
         */
        String exitMainImage = "ui-elements/default_buttons/exit-button.png";
        String exitHoverImage = "ui-elements/hovered-buttons/exit-button-hovered.png";
        ImageButton exitBtn;
        exitBtn = insImage.setImage(exitMainImage, exitHoverImage,
                centreWidth,centreHeight-height105Percent,
                buttonDimensionsWidth, buttonDimensionsHeight);

        // Exit button event.
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button from score screen is clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        TextButton loadBtn = new TextButton("Load Level File", skin);
        loadBtn.setColor(Color.ROYAL);

        // Load button event.
        loadBtn.addListener(
          new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                  entity.getEvents().trigger("loadLevel");
              }
          });

        table.bottom().right();
        table.add(loadBtn).padRight(10f).padBottom(10f);
        table.row();

        ArrayList<ImageButton> imageButtons = new ArrayList<>();

        // List all the files in the levels folder and create a button for each
        for (LevelDefinition level : LevelDefinition.values()) {

            String pathName = level.getMenuButtonName();
            String hoverPathName = level.getMenuButtonHoverName();
            int posX = centreWidth1;
            int posY = centreHeight1;
            int widthX = centreWidth1/4; // Sets buttons dimensions
            int widthY = centreHeight1/3;
            int middleX = posX - widthX/2;

            switch (level) {
                case LEVEL_1:
                    posX = middleX;
                    break;
                case LEVEL_2:
                    posX = middleX - widthX;
                    posY = posY - widthY;
                    break;
                case LEVEL_3:
                    posX = middleX;
                    posY = posY - widthY*2;
                    break;
                case LEVEL_4:
                    posX = middleX + widthX;
                    posY = posY - widthY;
            }

            ImageButton levelButton = insImage.setImage(pathName, hoverPathName, posX, posY, widthX, widthY);
            imageButtons.add(levelButton);

            levelButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug(level + " button clicked");
                        entity.getEvents().trigger("start", level);
                        loadingScreen = true;
                    }
                }
            );

//            TextButton editorBtn = new TextButton("Edit " + level.getLevelInfo().getName(), skin);
//            editorBtn.addListener(
//              new ChangeListener() {
//                  @Override
//                  public void changed(ChangeEvent changeEvent, Actor actor) {
//                      logger.debug(level + " button clicked");
//                      entity.getEvents().trigger("levelEditor", level);
//                  }
//              }
//            );
//
//            editorBtn.setColor(Color.BLACK);
//            table.add(editorBtn).padLeft(centreWidth * 1.5f).padBottom(30f);
//            table.row();
        }

        /**
         * Creates the button texture for the virus Head.
         */
        int posX = centreWidth1;
        int posY = centreHeight1;
        int widthX = centreWidth1/4;
        int widthY = centreHeight1/3;
        posX = posX - widthX/2;
        posY = posY - widthY;
        String virusHeadImage = "player/virus_head_front.png";
        ImageButton virusHeadBtn;
        virusHeadBtn = insImage.setImage(virusHeadImage, virusHeadImage, posX, posY, widthX, widthY);

        stage.addActor(table);
        for (ImageButton image : imageButtons) {
            stage.addActor(image);
        }
        stage.addActor(exitBtn);
        stage.addActor(titleBtn);
        stage.addActor(virusHeadBtn);
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


