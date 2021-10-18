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
        Sprite sprite = new Sprite(new Texture("ui-elements/pause_game1.png"));
        table.setBackground(new SpriteDrawable(sprite));
        stage.addActor(table);
    }


    private void remove() {
        stage.dispose();
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
