package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.AddListenerAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.endgame.DeathScreenActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.VariableElement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    TextButton mainMenuBtn = new TextButton("Exit", skin);
    // Create a button to trigger the death.
    TextButton deathBtn = new TextButton("Death", skin);

    // Causes the death screen to pop up.
    deathBtn.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("death button clicked");
            entity.getEvents().trigger("playerDeath");
          }
        }
    );

    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Exit button clicked");
          entity.getEvents().trigger("exit");
        }
      });

    table.add(mainMenuBtn).padTop(10f).padRight(10f);
    table.add(deathBtn).padTop(10f).padRight(10f);
    stage.addActor(table);
  }

  public void deathInitialiser() {
    entity.getEvents().trigger("playerDeath");
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
