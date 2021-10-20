package com.deco2800.game.components.player;

import static com.deco2800.game.screens.MainGameScreen.timeScore;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;

  private Label healthLabel;
  private Label timeLabel;
  public static boolean gameOver = false;
  public static boolean paused = false;

  private int iterator;
  private int initialValue;
  private int seconds;
  private int pausedTime = 0;
  private boolean pauseSet = false;
  int timeElapsedWhilePaused = 0;

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

    CharSequence timer = String.format("Time: %d", timeScore); // Time not changing


//    skin.getFont("fonts/EvilEmpire_32.fnt").getData().setScale(0.33f,0.33f); // doesn't work
    timeLabel = new Label(timer, skin, "large");



    timeLabel.getStyle().fontColor.add(Color.MAGENTA);


    // Changing the font size of the clock
    //timeLabel.setFontScaleX(6);
    //timeLabel.setFontScaleY(9);
    timeLabel.setFontScale(3);
    


    int middleScreen = Gdx.graphics.getWidth()/2;
    int timeSize = 50;
    int timeMiddleScreen = middleScreen - timeSize/2;
    int heightOfTimeText = (int) Math.round(Gdx.graphics.getHeight()/1.25);

    //timeLabel.setBounds(middleScreen, heightOfTimeText, 200, 200); // Try to increase Font size
    timeLabel.setSize(timeSize, timeSize);
    timeLabel.setPosition(timeMiddleScreen, heightOfTimeText, 0);
    //timeLabel.setFillParent(true);
    //System.out.println(timeLabel.setText(100));

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
      this.getEntity().getEvents().trigger("playerIsDead");
    }
  }


  public void playerIsDead() {
    gameOver = true;
  }


  /**
   * Updates the player's score on the ui.
   */
  public void updatePlayerScore() {
    // Seems to be the perfect time to start on
    if (!paused) {
      if (iterator < 3) {
        initialValue = Math.round(timeScore / 1000);
        iterator++;
      }
      seconds = Math.round(timeScore / 1000) - initialValue;
      if (pauseSet) {
        pauseSet = false;
        timeElapsedWhilePaused += seconds - pausedTime;
      }



      CharSequence text = String.format("Time: %d", seconds - timeElapsedWhilePaused);
      timeLabel.setText(text);
    } else {
      if (!pauseSet) {
        pauseSet = true;
        pausedTime = seconds;
      }
    }

  }

  @Override
  public void dispose() {
    super.dispose();
    healthLabel.remove();
    timeLabel.remove();
  }
}
