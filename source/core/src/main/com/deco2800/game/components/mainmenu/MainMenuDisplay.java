package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.services.*;
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
    private ImageButton muteBtn;

    /**
     * used tp switch between button states
     */
    public MainMenuDisplay() {
    }

    @Override
    public void create() {
        super.create();
        addActors();
        playTheMusic();
    }
    public void playTheMusic() {
            MusicSingleton music = MusicSingleton.getInstance();
            music.playMusicSingleton("sounds/MainMenuMusic.mp3");
            /*MusicServiceDirectory dict = new  MusicServiceDirectory();
            MusicService musicScreen = new MusicService(dict.main_menu);
            musicScreen.playMusic();*/



    }

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {

        table = new Table();
        table.setFillParent(true);
        Sprite sprite = new Sprite(new Texture("images/title_screen_clean.png"));
        table.setBackground(new SpriteDrawable(sprite)); // Set background

        /**
         * Creates the button size and position based off
         * of the users computer screen dimensions.
         */
        int centreWidth1 = Gdx.graphics.getWidth()/2;
        int centreHeight1 = Gdx.graphics.getHeight()/2;
        int buttonDimensionsWidth = (int) Math.round(centreWidth1*0.35);
        int buttonDimensionsHeight = (int) Math.round(centreHeight1*0.31);
        int titleHeightDimension = (int) Math.round(centreHeight1*0.4);
        int titleWidthDimension = (int) Math.round(centreWidth1*1.3);
        int centreWidth = centreWidth1 - buttonDimensionsWidth/2; // Moves middle of button to Centre
        int centreHeight = centreWidth1 - buttonDimensionsHeight/2;
        int centreTitleWidth = centreWidth1 - titleWidthDimension/2; // Moves middle of button to Centre
        int centreTitleHeight = centreHeight1 - titleHeightDimension/2;

        int width35Percent = (int) Math.round(centreWidth*0.40);

        int titleHeight = (int) Math.round(centreHeight*0.35);
        int height38Percent = (int) Math.round(centreHeight*0.38);
        int height53Percent = (int) Math.round(centreHeight*0.53);
        int height79Percent = (int) Math.round(centreHeight*0.79);
        int height83Percent = (int) Math.round(centreHeight*0.83);
        int height98Percent = (int) Math.round(centreHeight*0.98);

        int topLeftWidth = (int) Math.round(Gdx.graphics.getWidth()*0.01);
        int topLeftHeight = (int) Math.round(Gdx.graphics.getHeight()*0.93);

        /**
         * Creates the 'RUNTIME' title texture.
         */
        Texture runtimeTitleTexture = new Texture(Gdx.files.internal("images/runtime-title.png"));
        Drawable runtimeTitleDrawable = new TextureRegionDrawable(new TextureRegion(runtimeTitleTexture));
        ImageButton runtimeTitle = new ImageButton(runtimeTitleDrawable);
        /**
         * Sets the size and position of the Runtime Title after texture applied.
         */
        runtimeTitle.setBounds(centreTitleWidth, centreTitleHeight+titleHeight,
                titleWidthDimension, titleHeightDimension);
        runtimeTitle.setDisabled(true);


        /**
         * Creates the VirusHead texture for the virus image
         */
        Texture virusTexture = new Texture(Gdx.files.internal("images/VirusHead.png"));
        Drawable virusDrawable = new TextureRegionDrawable(new TextureRegion(virusTexture));
        ImageButton virusHead = new ImageButton(virusDrawable);
        /**
         * Sets the size and position of the virusHead after texture applied.
         */
        virusHead.setBounds(centreWidth,centreHeight-height79Percent,
                buttonDimensionsWidth, buttonDimensionsHeight*2);
        virusHead.setDisabled(true);


        /**
         * Creates the button texture for the Start Button.
         */
        Texture startTexture = new Texture(Gdx.files.internal("images/button_start.png"));
        Texture startHoverTexture = new Texture(Gdx.files.internal("images/button_start_hover.png"));
        Drawable startDrawable = new TextureRegionDrawable(new TextureRegion(startTexture));
        ImageButton startBtn = new ImageButton(startDrawable);
        startBtn.getStyle().imageOver = new TextureRegionDrawable(startHoverTexture);
        /**
         * Sets the size and position of the button after texture applied.
         */
        startBtn.setBounds(centreWidth,centreHeight-height38Percent,
                buttonDimensionsWidth, buttonDimensionsHeight);


        /**
         * Creates the button texture for the Level Selection Button.
         */
        Texture levelSelTexture = new Texture(Gdx.files.internal("images/button_levels.png"));
        Texture levelSelHoverTexture = new Texture(Gdx.files.internal("images/button_levels_hover.png"));
        Drawable levelSelDrawing = new TextureRegionDrawable(new TextureRegion(levelSelTexture));
        ImageButton levelSelectBtn = new ImageButton(levelSelDrawing);
        levelSelectBtn.getStyle().imageOver = new TextureRegionDrawable(levelSelHoverTexture);
        /**
         * Sets the size and position of the button after texture applied.
         */
        levelSelectBtn.setBounds(centreWidth-width35Percent,centreHeight-height53Percent,
                buttonDimensionsWidth, buttonDimensionsHeight);


        /**
         * Creates the button texture for the Settings Button.
         */
        Texture settingsTexture = new Texture(Gdx.files.internal("images/button_settings.png"));
        Texture settingsHoverTexture = new Texture(Gdx.files.internal("images/button_settings_hover.png"));
        Drawable settingsDrawing = new TextureRegionDrawable(new TextureRegion(settingsTexture));
        ImageButton settingsBtn = new ImageButton(settingsDrawing);
        settingsBtn.getStyle().imageOver = new TextureRegionDrawable(settingsHoverTexture);
        /**
         * Sets the size and position of the button after texture applied.
         */
        settingsBtn.setBounds((float)(centreWidth-width35Percent),(float)(centreHeight-height83Percent),
                buttonDimensionsWidth, buttonDimensionsHeight);


        /**
         * Creates the button texture for the Exit Button.
         */
        Texture exitTexture = new Texture(Gdx.files.internal("images/button_exit.png"));
        Texture exitHoverTexture = new Texture(Gdx.files.internal("images/button_exit_hover.png"));
        Drawable exitDrawing = new TextureRegionDrawable(new TextureRegion(exitTexture));
        ImageButton exitBtn = new ImageButton(exitDrawing);
        exitBtn.getStyle().imageOver = new TextureRegionDrawable(exitHoverTexture);
        /**
         * Sets the size and position of the button after texture applied.
         */
        exitBtn.setBounds(centreWidth,(float)(centreHeight-height98Percent),
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Mute Button. Also loads the 'Muted' button.
         */
        Texture muteTexture = new Texture(Gdx.files.internal("images/button_mute.png"));
        Texture muteHoverTexture = new Texture(Gdx.files.internal("images/button_mute_hover.png"));
        Texture currentlyMutedTexture = new Texture(Gdx.files.internal("images/button_unmute.png"));
        Texture currentlyMutedHoverTexture = new Texture(Gdx.files.internal("images/button_unmute_hover.png"));
        Drawable muteDrawing = new TextureRegionDrawable(new TextureRegion(muteTexture));
        muteBtn = new ImageButton(muteDrawing);
        // Initialise the image of the button to muteTexture.
        MuteManager mute = MuteManager.getInstance();

        if (!mute.getMute()) {
            mute.setMute(false);
            muteBtn.getStyle().imageUp = new TextureRegionDrawable(muteTexture);
            muteBtn.getStyle().imageOver = new TextureRegionDrawable(muteHoverTexture);
        } else {
            mute.setMute(true);
            muteBtn.getStyle().imageUp = new TextureRegionDrawable(currentlyMutedTexture);
            muteBtn.getStyle().imageOver = new TextureRegionDrawable(currentlyMutedHoverTexture);
        }

        /**
         * Sets the size and position of the button after texture applied, for Mute and Currently Muted both.
         */
        muteBtn.setBounds((float)(centreWidth+width35Percent),(float)(centreHeight-height83Percent),
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Leaderboard Button.
         */
        Texture leaderTexture = new Texture(Gdx.files.internal("images/button_leader_board.png"));
        Texture leaderHoverTexture = new Texture(Gdx.files.internal("images/button_leader_board_hover.png"));
        Drawable leaderDrawing = new TextureRegionDrawable(new TextureRegion(leaderTexture));
        ImageButton leaderBoardBtn = new ImageButton(leaderDrawing);
        leaderBoardBtn.getStyle().imageOver = new TextureRegionDrawable(leaderHoverTexture);
        /**
         * Sets the size and position of the button after texture applied.
         */
        leaderBoardBtn.setBounds((float)(centreWidth+width35Percent),(float)(centreHeight-height53Percent),
                buttonDimensionsWidth, buttonDimensionsHeight);

        TextButton levelEditorBtn = new TextButton("LevelEditor", skin);
        levelEditorBtn.setColor(Color.ROYAL);
        levelEditorBtn.setBounds(topLeftWidth, topLeftHeight, 200, 50);

        levelEditorBtn.addListener(
                new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("level button clicked");
                entity.getEvents().trigger("levelEditor");
            }
        });

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
                        MuteManager mute = MuteManager.getInstance();

                        if (mute.getMute()) {
                            // Rewrite the existing imageUp and imageOver enums to be muteTexture.
                            // When the if condition is passed, imageUp and imageOver will be currentlyMuteTexture.
                            muteBtn.getStyle().imageUp = new TextureRegionDrawable(muteTexture);
                            muteBtn.getStyle().imageOver = new TextureRegionDrawable(muteHoverTexture);
                        } else {
                            // Rewrite the existing imageUp and imageOver enums to be currentlyMutedTexutre.
                            // When the else condition is passed, imageUp and imageOver will be muteTexutre.
                            muteBtn.getStyle().imageUp = new TextureRegionDrawable(currentlyMutedTexture);
                            muteBtn.getStyle().imageOver = new TextureRegionDrawable(currentlyMutedHoverTexture);
                        }

                        entity.getEvents().trigger("mute");
                    }
                });


        leaderBoardBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        // LeaderBoardBtn should be changed to Score button when the button is renamed.
                        logger.debug("Leaderboard button clicked");
                        entity.getEvents().trigger("scoreSelect");
                    }
                });

        virusHead.addListener(
          new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {

                  logger.debug("Level editor button clicked");
                  entity.getEvents().trigger("levelEditor");
              }
          });


        stage.addActor(table);
        stage.addActor(startBtn);
        stage.addActor(levelSelectBtn);
        stage.addActor(muteBtn);
        stage.addActor(settingsBtn);
        stage.addActor(exitBtn);
        stage.addActor(leaderBoardBtn);
        stage.addActor(virusHead);
        stage.addActor(runtimeTitle);
        stage.addActor(levelEditorBtn);
    }



    /**
     * Return Mute Button for testing purposes
     * @return ImageButton MuteBtn
     */
    public ImageButton getMuteBtn() {
        return muteBtn;
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
