package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

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
        switcher = 0;
    }

    @Override
    public void create() {
        super.create();
        addActors();
        playTheMusic();
    }
    public void playTheMusic() {
        MusicServiceDirectory dict = new  MusicServiceDirectory();
        MusicService musicScreen = new MusicService(dict.main_menu);
        musicScreen.playMusic();
    }

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {

        table = new Table();
        table.setFillParent(true);
        sprite = new Sprite(new Texture("images/title_screen_clean.png"));
        table.setBackground(new SpriteDrawable(sprite)); // Set background

        /**
         * Creates the button size and position based off
         * of the users computer screen dimensions.
         */
        int centreWidth1 = Gdx.graphics.getWidth()/2;
        int centreHeight1 = Gdx.graphics.getHeight()/2;
        int buttonDimensions = (int) Math.round(centreWidth1*0.2 + centreHeight1*0.2);
        //int titleDimension = (int) Math.round(centreWidth1*0.8 + centreHeight1*0.8);
        int centreWidth = centreWidth1 - buttonDimensions/2; // Moves middle of button to Centre
        int centreHeight = centreWidth1 - buttonDimensions/2;
        //int centreTitleWidth = centreWidth1 - titleDimension/2; // Moves middle of button to Centre
        //int centreTitleHeight = centreWidth1 - titleDimension/2;

        int width35Percent = (int) Math.round(centreWidth*0.35);

        //int titleHeight = (int) Math.round(centreHeight*0.13);
        int height50Percent = (int) Math.round(centreHeight*0.45);
        int height65Percent = (int) Math.round(centreHeight*0.60);
        int height78Percent = (int) Math.round(centreHeight*0.75);
        int height90Percent = (int) Math.round(centreHeight*0.90);
        int height105Percent = (int) Math.round(centreHeight*1.05);

        /**
         * Creates the 'RUNTIME' title texture.
         */
//        Texture runtimeTitleTexture = new Texture(Gdx.files.internal("images/runtime_title.png"));
//        Drawable runtimeTitleDrawable = new TextureRegionDrawable(new TextureRegion(runtimeTitleTexture));
//        ImageButton runtimeTitle = new ImageButton(runtimeTitleDrawable);
//        /**
//         * Sets the size and position of the Runtime Title after texture applied.
//         */
//        runtimeTitle.setBounds(centreTitleWidth, centreTitleHeight-titleHeight,
//                titleDimension, titleDimension);

        /**
         * Creates the VirusHead texture for the virus image
         */
        Texture virusTexture = new Texture(Gdx.files.internal("images/VirusHead.png"));
        Drawable virusDrawable = new TextureRegionDrawable(new TextureRegion(virusTexture));
        ImageButton virusHead = new ImageButton(virusDrawable);
        /**
         * Sets the size and position of the virusHead after texture applied.
         */
        virusHead.setBounds(centreWidth,centreHeight-height78Percent,
                buttonDimensions, buttonDimensions);


        /**
         * Creates the button texture for the Start Button.
         */
        Texture startTexture = new Texture(Gdx.files.internal("images/button_start.png"));
        Drawable startDrawable = new TextureRegionDrawable(new TextureRegion(startTexture));
        ImageButton startBtn = new ImageButton(startDrawable);
        /**
         * Sets the size and position of the button after texture applied.
         */
        startBtn.setBounds(centreWidth,centreHeight-height50Percent,
                buttonDimensions, buttonDimensions);


        /**
         * Creates the button texture for the Level Selection Button.
         */
        Texture levelSelTexture = new Texture(Gdx.files.internal("images/button_levels.png"));
        Drawable levelSelDrawing = new TextureRegionDrawable(new TextureRegion(levelSelTexture));
        ImageButton levelSelectBtn = new ImageButton(levelSelDrawing);
        /**
         * Sets the size and position of the button after texture applied.
         */
        levelSelectBtn.setBounds(centreWidth-width35Percent,centreHeight-height65Percent,
                buttonDimensions, buttonDimensions);


        /**
         * Creates the button texture for the Settings Button.
         */
        Texture settingsTexture = new Texture(Gdx.files.internal("images/button_settings.png"));
        Drawable settingsDrawing = new TextureRegionDrawable(new TextureRegion(settingsTexture));
        ImageButton settingsBtn = new ImageButton(settingsDrawing);
        /**
         * Sets the size and position of the button after texture applied.
         */
        settingsBtn.setBounds(centreWidth-width35Percent,centreHeight-height90Percent,
                buttonDimensions, buttonDimensions);


        /**
         * Creates the button texture for the Exit Button.
         */
        Texture exitTexture = new Texture(Gdx.files.internal("images/button_exit.png"));
        Drawable exitDrawing = new TextureRegionDrawable(new TextureRegion(exitTexture));
        ImageButton exitBtn = new ImageButton(exitDrawing);
        /**
         * Sets the size and position of the button after texture applied.
         */
        exitBtn.setBounds(centreWidth,centreHeight-height105Percent,
                buttonDimensions, buttonDimensions);

        /**
         * Creates the button texture for the Mute Button. Also loads the 'Muted' button.
         */
        Texture muteTexture = new Texture(Gdx.files.internal("images/button_mute.png"));
        Texture currentlyMutedTexture = new Texture(Gdx.files.internal("images/button_unmute.png"));
        Drawable muteDrawing = new TextureRegionDrawable(new TextureRegion(muteTexture));
        ImageButton muteBtn = new ImageButton(muteDrawing);
        /**
         * Sets the size and position of the button after texture applied, for Mute and Currently Muted both.
         */
        muteBtn.setBounds(centreWidth+width35Percent,centreHeight-height90Percent,
                buttonDimensions, buttonDimensions);


        /**
         * Creates the button texture for the Leaderboard Button.
         */
        Texture leaderTexture = new Texture(Gdx.files.internal("images/button_leader_board.png"));
        Drawable leaderDrawing = new TextureRegionDrawable(new TextureRegion(leaderTexture));
        ImageButton leaderBoardBtn = new ImageButton(leaderDrawing);
        /**
         * Sets the size and position of the button after texture applied.
         */
        leaderBoardBtn.setBounds(centreWidth+width35Percent,centreHeight-height65Percent,
                buttonDimensions, buttonDimensions);


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
                            muteBtn.getStyle().imageUp = new TextureRegionDrawable(muteTexture);
                        } else {
                            muteBtn.getStyle().imageChecked = new TextureRegionDrawable(currentlyMutedTexture);
                        } switcher++;
                        entity.getEvents().trigger("mute");
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
