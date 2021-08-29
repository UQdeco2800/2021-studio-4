package com.deco2800.game.components;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.endgame.DeathScreenActions;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.nio.file.AccessMode;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;

  public CombatStatsComponent(int health, int baseAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    this.health = Math.max(health, 0);
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public void hit(CombatStatsComponent attacker) {

    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);

    if (this.health == 0) { // could be isDead()
      //entity.scaleHeight(12);
      entity.getEvents().trigger("playerDeath");
//      MainGameExitDisplay main = new MainGameExitDisplay();
//      main.deathInitialiser();

//      this.getEntity().getEvents().trigger("playerDeath");
//      Actor actor = new Actor();
//      ActionEvent action = new ActionEvent(true, entity.getId(), "playerDeath");
//      EventListener eventListener = new ChangeListener() {
//        @Override
//        public void changed(ChangeEvent event, Actor actor) {
//
//        }
//      };
//      ChangeListener.ChangeEvent event1 = new ChangeListener.ChangeEvent();
//      eventListener;
//      actor.addCaptureListener();
//      event.setListenerActor(actor);
      //main.getEntity().getEvents().trigger("playerDeath");
//      logger.info("Show Death Screen");
//      this.entity.getEvents().trigger("playerDeath");
    }

//    int newHealth = getHealth() - attacker.getBaseAttack();
//    setHealth(newHealth);
//    if (isDead()) {
//      MainGameExitDisplay main = new MainGameExitDisplay();
//      main.goToDeath();
//    }
    //setHealth(newHealth);
    //setScreen(GdxGame.ScreenType.MAIN_MENU);
//    do {
//      int newHealth = getHealth() - attacker.getBaseAttack();
//      setHealth(newHealth);
//    } while (!(isDead()));
    // Add death component to the player.

    //entity.getEvents().trigger("playerDeath");
  }
}
