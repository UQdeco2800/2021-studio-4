package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.ui.UIComponent;

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

    CharSequence timer = String.format("Current Score: %d", timeScore); // Time not changing
    timeLabel = new Label(timer, skin, "large");
    timeLabel.getStyle().fontColor.add(Color.MAGENTA);

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        entity.getEvents().trigger("updateScore");
      }
    };

    Timer myTimer = new Timer();
    myTimer.schedule(task, 0, 1000);

    table.add(timeLabel);
    stage.addActor(table);
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
    CharSequence text = String.format("Current Score: %d", timeScore);
    timeLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    healthLabel.remove();
    timeLabel.remove();
  }
}
