package com.deco2800.game.components.endgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.levelselect.LevelDisplay;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(DeathScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Sprite sprite;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Add death screen image, exit and retry buttons.
     */
    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        sprite = new Sprite(new Texture("images/DeathScreenPlaceholder.png"));
        table.setBackground(new SpriteDrawable(sprite)); // Set background.

        // Add exit button to go back to main menu.
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton retryBtn = new TextButton("Retry", skin);

        // Exit button event.
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        // Retry button event.
        retryBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Retry button clicked");
                        entity.getEvents().trigger("retry");
                    }
                });

        table.row();
        table.add(exitBtn).center(); // Places the button in the centre.
        table.add(retryBtn).center(); // Places the button in the centre.
        stage.addActor(table);
        //stage.
    }

    @Override
    protected void draw(SpriteBatch batch) {
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
