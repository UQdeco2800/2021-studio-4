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
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
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
        MusicServiceDirectory mainMenuSong = new MusicServiceDirectory();
        MusicService musicScreen = new MusicService(mainMenuSong.main_menu);
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

        File levelDirectory = new File("levels");
        HashMap<String, TextButton> levelBtns = new HashMap<String, TextButton>();

        // Add exit button to go back to main menu.
        TextButton exitBtn = new TextButton("Exit", skin);

        // List all the files in the levels folder and create a button for each
        for (String level : levelDirectory.list()) {
            levelBtns.put(level, new TextButton(level, skin));
            levelBtns.get(level).addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug(level + " button clicked");
                        entity.getEvents().trigger("start");
                    }
                }
            );
            levelBtns.get(level).setColor(Color.ROYAL);

        }

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
        table.add(exitBtn).center().padBottom(50f); // Places the button in the centre.
        table.row();

        for (TextButton btn : levelBtns.values()) {
            table.add(btn).pad(10f);
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


