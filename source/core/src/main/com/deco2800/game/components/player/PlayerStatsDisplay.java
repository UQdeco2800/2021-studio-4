package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.ui.UIComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import static com.deco2800.game.screens.MainGameScreen.timeScore;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;

  private Label healthLabel;
  private Label timeLabel;
  public static boolean gameOver = false;
  private int iterator;
  private int initialValue;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("updateScore", this::updatePlayerScore);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f);

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    table.add(healthLabel);

    table.row();
    iterator = 0;

    CharSequence timer = String.format("Current Score: %d", timeScore); // Time not changing
//    skin.getFont("fonts/EvilEmpire_32.fnt").getData().setScale(0.33f,0.33f); // doesn't work
    timeLabel = new Label(timer, skin, "large");
    timeLabel.getStyle().fontColor.add(Color.MAGENTA);

    int middleScreen = Gdx.graphics.getWidth()/2;
    int heightOfTimeText = (int) Math.round(Gdx.graphics.getHeight()/1.25);

    timeLabel.setBounds(middleScreen, heightOfTimeText, 200, 200); // Try to increase Font size
    timeLabel.setSize(100, 100);
    System.out.println(timeLabel.setText(100));

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        entity.getEvents().trigger("updateScore");
      }
    };

    Timer myTimer = new Timer();
    myTimer.schedule(task, 0, 1000);

    stage.addActor(table);
    stage.addActor(timeLabel);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
    if (health == 0) {
      gameOver = true;
    }
  }

  /**
   * Updates the player's score on the ui.
   */
  public void updatePlayerScore() {
    // Seems to be the perfect time to start on
    if (iterator < 3) {
      initialValue = (int) Math.round(timeScore/1000);
      iterator++;
    }

    int seconds;

    seconds = Math.round(timeScore / 1000) - initialValue;

    CharSequence text = String.format("Current Score: %d", seconds);
    timeLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    healthLabel.remove();
    timeLabel.remove();
  }
}
