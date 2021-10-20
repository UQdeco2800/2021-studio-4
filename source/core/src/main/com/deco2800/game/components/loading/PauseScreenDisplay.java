package com.deco2800.game.components.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.InsertImageButton;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.TheVoidController;
import com.deco2800.game.components.tasks.TheVoidTasks;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.TheVoidConfig;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.*;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * A ui component for displaying the Pause Screen
 */
public class PauseScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingScreenDisplay.class);
    private static final float Z_INDEX = 2f;



    public PauseScreenDisplay() {

    }

    @Override
    public void create() {

        super.create();
        addActors();
        // playTheMusic();
    }
    /* NOTE: Some of the code below is used with help from void team to create a atlas file in team 5's loading
    screen
    creatingLoadingBar(): adds in animation for loading animation.
     */

    /**
     * Added Background image and initialised buttons
     */
    private void addActors() {
        /* Docs on the stage use: https://www.tabnine.com/code/java/classes/com.badlogic.gdx.scenes.scene2d.ui.Label$LabelStyle */
        Table table = new Table();
        table.setFillParent(true);
        // TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(bground));
        Sprite sprite = new Sprite(new Texture("ui-elements/pause_notext.png"));
        table.setBackground(new SpriteDrawable(sprite));

        stage.addActor(table);
        Table table2 = new Table();
        table2.center();
        table2.setFillParent(true);

        // Add pause button to pause game
        TextButton pauseBtn = new TextButton("Pause", skin);


        // Add exit button to go back to main menu.
        TextButton exitBtn = new TextButton("Exit", skin);


        // Add retry button to restart level
        TextButton retryBtn = new TextButton("Retry", skin);




        // Pause button event
        pauseBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Pause button clicked");
                        entity.getEvents().trigger("pause");
                    }
                });
        table2.add(pauseBtn).center();
        table2.row();
        table2.add(exitBtn).center();
        table2.row();
        table2.add(retryBtn).center();
        stage.addActor(table2);

        // Exit button event.
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button from death screen is clicked");
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
    }


    public void remove() {
        stage.dispose();
    }

    public void add() {
        super.create();
        addActors();
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
        super.dispose();
    }
}
