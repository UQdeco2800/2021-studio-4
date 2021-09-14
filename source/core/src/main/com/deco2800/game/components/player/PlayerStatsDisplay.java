package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.ui.UIComponent;

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


    long time = timeScore;

    CharSequence timer = String.format("           Your previous Score: %d", time);
    timeLabel = new Label(timer, skin, "large");
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

  @Override
  public void dispose() {
    super.dispose();
    healthLabel.remove();
  }
}
