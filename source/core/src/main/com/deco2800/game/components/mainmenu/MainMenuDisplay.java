package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
    private Long titleAnimationStartTime = null;
    private TitleAnimation titleAnimation;
    private float duration;
    private ImageButton runtimeTitle;
    private int rotationAmountItr;

    /**
     * used tp switch between button states
     */
    public MainMenuDisplay() {
        rotationAmountItr = 4;
    }

    private static final String[] gameTextures = {
            "images/animatedvoid.png",
            "images/void_spritesheet2.png",
    };

    private static final String[] gameTextureAtlases = {
            "images/void.atlas",
    };

    @Override
    public void create() {
        super.create();
        addActors();
        playTheMusic();
        //loadAssets();
    }

    public void playTheMusic() {
        MusicSingleton music = MusicSingleton.getInstance();
        music.playMusicSingleton("sounds/MainMenuMusic.mp3", true, 0.2f);
    }

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {
        InsertImageButton insImage = new InsertImageButton();

        table = insImage.setTable("ui-elements/title-screen.png");

        /**
         * Creates the button size and position based off
         * of the users computer screen dimensions.
         */
        int centreWidth1 = Gdx.graphics.getWidth() / 2;
        int centreHeight1 = Gdx.graphics.getHeight() / 2;

        // Buttons dimensions
        int buttonDimensionsWidth = (int) Math.round(centreWidth1 * 0.3);
        int buttonDimensionsHeight = (int) Math.round(centreHeight1 * 0.15);
        int titleHeightDimension = (int) Math.round(centreHeight1 * 0.45);
        int titleWidthDimension = (int) Math.round(centreWidth1 * 1.4);
        int centreWidth = centreWidth1 - buttonDimensionsWidth / 2; // Moves middle of button to Centre
        int screenRight = centreWidth * 2;
        int screenRightWithXOffset = screenRight - (int) Math.round(centreWidth1 * 0.1);
        int centreHeight = centreHeight1 - buttonDimensionsHeight / 2; // Height of top image
        // Gap Between buttons (Y-axis)
        int heightDifference = (int) Math.round(centreHeight * 0.2);

        // Titles Dimensions
        int centreTitleWidth = centreWidth1 - titleWidthDimension / 2;
        int titleWidth = (int) Math.round(centreTitleWidth * 0.5);
        int centreTitleHeight = centreHeight1 - titleHeightDimension / 2;
        int titleHeight = (int) Math.round(centreTitleHeight * 1.1);

        /**
         * Creates the 'RUNTIME' title texture.
         */
        String titleMainImage = "ui-elements/runtime-title.png";
        String titleHoverImage = "ui-elements/runtime-on-hover.png";
        runtimeTitle = insImage.setImage(titleMainImage, titleHoverImage,
                titleWidth, titleHeight,
                titleWidthDimension, titleHeightDimension);

        /**
         * Creates the button texture for the Start Button.
         */
        String startMainImage = "ui-elements/default_buttons/start-button.png";
        String startHoverImage = "ui-elements/hovered-buttons/start-button-hovered.png";
        ImageButton startBtn;
        startBtn = insImage.setImage(startMainImage, startHoverImage,
                screenRight, centreHeight + heightDifference * 3,
                buttonDimensionsWidth, buttonDimensionsHeight);


        /**
         * Creates the button texture for the Level Selection Button.
         */
        String levelMainImage = "ui-elements/default_buttons/levels-button.png";
        String levelHoverImage = "ui-elements/hovered-buttons/levels-button-hovered.png";
        ImageButton levelSelectBtn;
        levelSelectBtn = insImage.setImage(levelMainImage, levelHoverImage,
                screenRightWithXOffset, centreHeight + heightDifference * 2,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Settings Button.
         */
        String settingsMainImage = "ui-elements/default_buttons/settings-button.png";
        String settingsHoverImage = "ui-elements/hovered-buttons/settings-button-hovered.png";
        ImageButton settingsBtn;
        settingsBtn = insImage.setImage(settingsMainImage, settingsHoverImage,
                screenRightWithXOffset, centreHeight,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Exit Button.
         */
        String exitMainImage = "ui-elements/default_buttons/exit-button.png";
        String exitHoverImage = "ui-elements/hovered-buttons/exit-button-hovered.png";
        ImageButton exitBtn;
        exitBtn = insImage.setImage(exitMainImage, exitHoverImage,
                screenRight, centreHeight - heightDifference * 3,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Mute Button. Also loads the 'Muted' button.
         */
        Texture muteTexture = new Texture(Gdx
                .files.internal("ui-elements/default_buttons/mute-button.png"));
        Texture muteHoverTexture = new Texture(Gdx
                .files.internal("ui-elements/hovered-buttons/mute-button-hovered.png"));
        Texture currentlyMutedTexture = new Texture(Gdx
                .files.internal("ui-elements/default_buttons/unmute-button.png"));
        Texture currentlyMutedHoverTexture = new Texture(Gdx
                .files.internal("ui-elements/hovered-buttons/unmute-button-hovered.png"));
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
        muteBtn.setBounds(screenRight, (float) (centreHeight - heightDifference),
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Leaderboard Button.
         */
        String leaderBoardMainImage = "ui-elements/default_buttons/scores-button.png";
        String leaderBoardHoverImage = "ui-elements/hovered-buttons/scores-button-hovered.png";
        ImageButton leaderBoardBtn;
        leaderBoardBtn = insImage.setImage(leaderBoardMainImage, leaderBoardHoverImage,
                screenRight, centreHeight + heightDifference,
                buttonDimensionsWidth, buttonDimensionsHeight);

        /**
         * Creates the button texture for the Leaderboard Button.
         */
        String levelEditorImage = "ui-elements/default_buttons/level-editor-button.png";
        String levelEditorHoverImage = "ui-elements/hovered-buttons/level-editor-button-hovered.png";
        ImageButton levelEditorBtn;
        levelEditorBtn = insImage.setImage(levelEditorImage, levelEditorHoverImage,
                screenRightWithXOffset, centreHeight - heightDifference * 2,
                buttonDimensionsWidth, buttonDimensionsHeight);

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

        /**
         * Sets the animation for when pressed
         */
        runtimeTitle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Launching title Animation");
                        titlesAnimation();
                        if (rotationAmountItr < 20) {
                            rotationAmountItr += 2;
                        }
                    }
                });

        stage.addActor(table);
        stage.addActor(startBtn);
        stage.addActor(levelSelectBtn);
        stage.addActor(muteBtn);
        stage.addActor(settingsBtn);
        stage.addActor(exitBtn);
        stage.addActor(leaderBoardBtn);
        stage.addActor(runtimeTitle);
        //stage.addActor(new TitleAnimation(new Texture("ui-elements/runtime-title.png")));
        stage.addActor(levelEditorBtn);
    }

    /**
     * The animation when the title buttons has been pressed
     */
    private void titlesAnimation() {
        runtimeTitle.remove();
        // The is a visible size for the image, leaving room for enlargement
        int imageWidth = 100;
        int imageHeight = 100;
        /** This adds the tiny runtime logo */
        moveAnimationImage(imageWidth, imageHeight);
    }

    /**
     * moves the animationImage around thew screen in a cool loop
     *
     * @param imageWidth - width of image
     * @param imageHeight - height of image
     */
    private void moveAnimationImage(int imageWidth, int imageHeight) {
        int centreWidth = Gdx.graphics.getWidth() / 2 - imageWidth / 2;
        int centreHeight = Gdx.graphics.getHeight() / 2 - imageHeight / 2;
        duration = 3.5f; // in seconds
        String imageFile = "player/virus_head_front.png";

        titleAnimation = new TitleAnimation(
                new Texture(imageFile), imageWidth,
                imageHeight, centreWidth, centreHeight, duration, rotationAmountItr);

        //titleAnimationStartTime = ServiceLocator.getTimeSource().getTime();
        titleAnimationStartTime = System.currentTimeMillis();
        stage.addActor(titleAnimation);
    }

    /**
     * Used to remove the animation image of the title upon completion
     */
    @Override
    public void update() {

        if (titleAnimationStartTime != null) {
            long timePassed = System.currentTimeMillis() - titleAnimationStartTime;

            if (timePassed / 1000 >= duration) {
                titleAnimation.remove();
                stage.addActor(runtimeTitle);
            }
        }
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
