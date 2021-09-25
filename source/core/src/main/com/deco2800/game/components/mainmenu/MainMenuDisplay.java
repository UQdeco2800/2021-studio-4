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
import com.deco2800.game.components.InsertImageButton;
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
    }

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {

        table = new Table();
        table.setFillParent(true);
        Sprite sprite = new Sprite(new Texture("images/title_screen_clean.png"));
        table.setBackground(new SpriteDrawable(sprite)); // Set background

        InsertImageButton insImage = new InsertImageButton();

        /**
         * Creates the button size and position based off
         * of the users computer screen dimensions.
         */
        int centreWidth1 = Gdx.graphics.getWidth()/2;
        int centreHeight1 = Gdx.graphics.getHeight()/2;
        int buttonDimensionsWidth = (int) Math.round(centreWidth1*0.35);
        int buttonDimensionsHeight = (int) Math.round(centreHeight1*0.25);
        int titleHeightDimension = (int) Math.round(centreHeight1*0.45);
        int titleWidthDimension = (int) Math.round(centreWidth1*1.4);
        int centreWidth = centreWidth1 - buttonDimensionsWidth/2; // Moves middle of button to Centre
        int screenRight = centreWidth*2;
        int screenRightWithXOffset = screenRight - (int) Math.round(centreWidth1 * 0.1);
        int centreHeight = centreHeight1 - buttonDimensionsHeight/2; // Height of top image
        int heightDifference = (int) Math.round(centreHeight * 0.3);
        int centreTitleWidth = centreWidth1 - titleWidthDimension/2;
        int titleWidth = (int) Math.round(centreTitleWidth * 0.5);
        int centreTitleHeight = centreHeight1 - titleHeightDimension/2;

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
        runtimeTitle.setBounds(titleWidth, centreTitleHeight,
                titleWidthDimension, titleHeightDimension);
        runtimeTitle.setDisabled(true);


//        /**
//         * Creates the VirusHead texture for the virus image
//         */
//        Texture virusTexture = new Texture(Gdx.files.internal("images/VirusHead.png"));
//        Drawable virusDrawable = new TextureRegionDrawable(new TextureRegion(virusTexture));
//        ImageButton virusHead = new ImageButton(virusDrawable);
//        /**
//         * Sets the size and position of the virusHead after texture applied.
//         */
//        virusHead.setBounds(centreWidth,centreHeight-height79Percent,
//                buttonDimensionsWidth, buttonDimensionsHeight*2);
//        virusHead.setDisabled(true);

        /**
         * Creates the button texture for the Start Button.
         */
        String startMainImage = "images/button_start.png";
        String startHoverImage = "images/button_start_hover.png";
        ImageButton startBtn;
        startBtn = insImage.setImage(startMainImage, startHoverImage,
                screenRight,centreHeight + heightDifference * 3,
                buttonDimensionsWidth, buttonDimensionsHeight);


        /**
         * Creates the button texture for the Level Selection Button.
         */
        String levelMainImage = "images/button_levels.png";
        String levelHoverImage = "images/button_levels_hover.png";
        ImageButton levelSelectBtn;
        levelSelectBtn = insImage.setImage(levelMainImage, levelHoverImage,
                screenRightWithXOffset,centreHeight + heightDifference*2,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Settings Button.
         */
        String settingsMainImage = "images/button_settings.png";
        String settingsHoverImage = "images/button_settings_hover.png";
        ImageButton settingsBtn;
        settingsBtn = insImage.setImage(settingsMainImage, settingsHoverImage,
                screenRightWithXOffset,centreHeight,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Exit Button.
         */
        String exitMainImage = "images/button_exit.png";
        String  exitHoverImage = "images/button_exit_hover.png";
        ImageButton exitBtn;
        exitBtn = insImage.setImage(exitMainImage, exitHoverImage,
                screenRightWithXOffset,centreHeight - heightDifference * 2,
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

        //Sets the size and position of the button after texture applied, for Mute and Currently Muted both.
        muteBtn.setBounds(screenRight,centreHeight - heightDifference,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Leaderboard Button.
         */
        String leaderBoardMainImage = "images/button_leader_board.png";
        String  leaderBoardHoverImage = "images/button_leader_board_hover.png";
        ImageButton leaderBoardBtn;
        leaderBoardBtn = insImage.setImage(leaderBoardMainImage, leaderBoardHoverImage,
                screenRight,centreHeight + heightDifference,
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


        stage.addActor(table);
        stage.addActor(startBtn);
        stage.addActor(levelSelectBtn);
        stage.addActor(muteBtn);
        stage.addActor(settingsBtn);
        stage.addActor(exitBtn);
        stage.addActor(leaderBoardBtn);
//        stage.addActor(virusHead);
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
