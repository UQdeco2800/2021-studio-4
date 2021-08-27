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

        //table = new Table();
        //table.setFillParent(true);
        //Image title =
        //new Image(
        //ServiceLocator.getResourceService()
        //.getAsset("images/box_boy_title.png", Texture.class));

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
        int ButtonDimensions = (int) Math.round(centreWidth1*0.2 + centreHeight1*0.2);
        int centreWidth = centreWidth1 - ButtonDimensions/2; // Moves middle of button to Centre
        int centreHeight = centreWidth1 - ButtonDimensions/2;

        int Width25Percent = (int) Math.round(centreWidth*0.35);
        int Width60Percent = (int) Math.round(centreWidth*0.35);

        int Height45Percent = (int) Math.round(centreHeight*0.50);
        int Height66Percent = (int) Math.round(centreHeight*0.65);
        int Height95Percent = (int) Math.round(centreHeight*0.90);
        int Height110Percent = (int) Math.round(centreHeight*1.05);

        /**
         * Creates the button texture for the Start Button (Currently Level Button as
         * placeholder until font decided upon).
         */
        Texture startTexture = new Texture(Gdx.files.internal("images/button_start.png"));
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(startTexture));
        ImageButton startBtn = new ImageButton(drawable);
        /**
         * Sets the size and position of the button after texture applied.
         */
        //startBtn.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2-80);
        //startBtn.setSize(250, 250);
        startBtn.setBounds(centreWidth,centreHeight-Height45Percent,
                ButtonDimensions, ButtonDimensions);
        /**
         * Creates the button texture for the Level Selection Button.
         */
        Texture levelSelTexture = new Texture(Gdx.files.internal("images/button_levels.png"));
        Drawable levelSelDrawing = new TextureRegionDrawable(new TextureRegion(levelSelTexture));
        ImageButton levelSelectBtn = new ImageButton(levelSelDrawing);
        //TextButton levelSelectBtn = new TextButton("Level Select", skin);
        //levelSelectBtn.setColor(Color.ROYAL);
        /**
         * Sets the size and position of the button after texture applied.
         */
        //levelSelectBtn.setPosition(Gdx.graphics.getWidth()/2-160, Gdx.graphics.getHeight()/2-220);
        //levelSelectBtn.setSize(250, 250);
        levelSelectBtn.setBounds(centreWidth-Width60Percent,centreHeight-Height66Percent,
                ButtonDimensions, ButtonDimensions);


        /**
         * Creates the button texture for the Settings Button (Currently Level Button as
         * placeholder until font decided upon).
         */
        Texture settingsTexture = new Texture(Gdx.files.internal("images/button_settings.png"));
        Drawable settingsDrawing = new TextureRegionDrawable(new TextureRegion(settingsTexture));
        ImageButton settingsBtn = new ImageButton(settingsDrawing);
        //TextButton settingsBtn = new TextButton("Settings", skin);
        //settingsBtn.setColor(Color.ROYAL);
        /**
         * Sets the size and position of the button after texture applied.
         */
        //settingsBtn.setPosition(Gdx.graphics.getWidth()/2-160, Gdx.graphics.getHeight()/2-360);
        //settingsBtn.setSize(100, 100);
        settingsBtn.setBounds(centreWidth-Width60Percent,centreHeight-Height95Percent,
                ButtonDimensions, ButtonDimensions);


        /**
         * Creates the button texture for the Exit Button (Currently Level Button as
         * placeholder until font decided upon).
         */
        Texture exitTexture = new Texture(Gdx.files.internal("images/button_exit.png"));
        Drawable exitDrawing = new TextureRegionDrawable(new TextureRegion(exitTexture));
        ImageButton exitBtn = new ImageButton(exitDrawing);
        //TextButton exitBtn = new TextButton("Exit", skin);
        //exitBtn.setColor(Color.ROYAL);
        /**
         * Sets the size and position of the button after texture applied.
         */
        //exitBtn.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2-500);
        //exitBtn.setSize(100, 100);
        exitBtn.setBounds(centreWidth,centreHeight-Height110Percent,
                ButtonDimensions, ButtonDimensions);


        /**
         * Creates the button texture for the Mute Button (Currently Level Button as
         * placeholder until font decided upon). Also loads the 'Muted' button (place holder Box Boy)
         */
        Texture muteTexture = new Texture(Gdx.files.internal("images/button_mute.png"));
        Texture currentlyMutedTexture = new Texture(Gdx.files.internal("images/button_unmute.png"));
        Drawable muteDrawing = new TextureRegionDrawable(new TextureRegion(muteTexture));
        //Drawable currentlyMutedDrawing = new TextureRegionDrawable(new TextureRegion(currentlyMutedTexture));
        ImageButton muteBtn = new ImageButton(muteDrawing);
        //ImageButton currentlyMutedButton = new ImageButton(currentlyMutedDrawing);
        //TextButton muteBtn = new TextButton("Mute", skin);
        //muteBtn.setColor(Color.ROYAL);
        /**
         * Sets the size and position of the button after texture applied, for Mute and Currently Muted both.
         */
        //muteBtn.setPosition(Gdx.graphics.getWidth()/2+160, Gdx.graphics.getHeight()/2-360);
        //muteBtn.setSize(100, 100);
        muteBtn.setBounds(centreWidth+Width25Percent,centreHeight-Height95Percent,
                ButtonDimensions, ButtonDimensions);
        //currentlyMutedButton.setPosition(Gdx.graphics.getWidth()/2+160, Gdx.graphics.getHeight()/2-360);
        //currentlyMutedButton.setSize(100, 100);

        /**
         * Creates the button texture for the Leaderboard Button (Currently Level Button as
         * placeholder until font decided upon).
         */
        Texture leaderTexture = new Texture(Gdx.files.internal("images/button_leader_board.png"));
        Drawable leaderDrawing = new TextureRegionDrawable(new TextureRegion(leaderTexture));
        ImageButton leaderBoardBtn = new ImageButton(leaderDrawing);
        //TextButton leaderBoardBtn = new TextButton("Leaderboard", skin);
        //leaderBoardBtn.setColor(Color.ROYAL);
        /**
         * Sets the size and position of the button after texture applied, for Mute and Currently Muted both.
         */
        //leaderBoardBtn.setPosition(Gdx.graphics.getWidth()/2+160, Gdx.graphics.getHeight()/2-220);
        //leaderBoardBtn.setSize(100, 100);
        leaderBoardBtn.setBounds(centreWidth+Width25Percent,centreHeight-Height66Percent,
                ButtonDimensions, ButtonDimensions);

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
