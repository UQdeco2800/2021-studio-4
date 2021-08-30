package com.deco2800.game.components.levelselect;

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
import com.deco2800.game.services.MusicService;
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
        loadTheMusic();
    }

    private void loadTheMusic() {
        MusicService musicScreen = new MusicService();
        musicScreen.playMusic();
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
        // Add exit button to go back to main menu.
        TextButton level1Btn = new TextButton("Level1", skin);

        // Level1 button event.
        level1Btn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Level1 button clicked");
                        entity.getEvents().trigger("start");
                    }
                });
        level1Btn.setColor(Color.ROYAL);

        // Exit button event.
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
        table.add(exitBtn).center(); // Places the button in the centre.
        table.add(level1Btn).center();
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


