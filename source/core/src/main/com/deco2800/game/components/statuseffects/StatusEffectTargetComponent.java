package com.deco2800.game.components.statuseffects;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.npc.TheVoidController;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.services.MusicService;
import com.deco2800.game.services.MusicServiceDirectory;
import com.deco2800.game.services.MusicSingleton;
import com.deco2800.game.services.ServiceLocator;

public class StatusEffectTargetComponent extends Component {
  private StatusEffect currentStatusEffect = null;
  private Long currentStatusEffectStartTime = null;
  private StatusEffectResetTask currentStatusEffectResetTask = null;

  @Override
  public void create() {
    entity.getEvents().addListener("StatusEffectTrigger", this::applyStatusEffect);
  }

  @Override
  public void update() {
    super.update();

    if (currentStatusEffect != null && ServiceLocator.getTimeSource().getTimeSince(currentStatusEffectStartTime) >= currentStatusEffect.getDuration()*1000) {
      currentStatusEffect = null;
      currentStatusEffectStartTime = null;
      currentStatusEffectResetTask.run();
      currentStatusEffectResetTask = null;
    }
  }

  public StatusEffect getCurrentStatusEffect() {
    return currentStatusEffect;
  }


  /**
   * Applies the given status effect to the target. Note: only one status effect will be present at one time.
   * @param statusEffect The effect
   */
  public void applyStatusEffect(StatusEffect statusEffect) {
    if (currentStatusEffect != null) {
      currentStatusEffect = null;
      currentStatusEffectStartTime = null;
      currentStatusEffectResetTask.run();
      currentStatusEffectResetTask = null;
    }

    currentStatusEffect = statusEffect;
    currentStatusEffectStartTime = ServiceLocator.getTimeSource().getTime();

    switch (statusEffect) {
      case BOMB:
        break;
      case FAST:
      case SLOW:
        currentStatusEffectResetTask = speedEffect(statusEffect);
        break;
      case JUMP:
        currentStatusEffectResetTask = jumpBoost();
        break;
      case STUCK:
        currentStatusEffectResetTask = stuckInMud();
        break;
      case TIME_STOP:
        currentStatusEffectResetTask = TheVoidController.pauseVoid();
        break;
    }
  }

  /**
   * Applies either the StatusEffect.FAST or StatusEffect.SLOW status effects
   * @param speedEffect The effect to apply
   */
  private StatusEffectResetTask speedEffect(StatusEffect speedEffect) {
    int speedBoost = speedEffect.getMagnitude(); // Must be smaller than 10

    int statOriginal = (int) entity.getComponent(PlayerActions.class).getSpeed();

    int newSpeed;
    if (speedEffect == StatusEffect.FAST) {
      newSpeed = statOriginal - speedBoost;
    } else {
      newSpeed = -1 * (statOriginal - speedBoost);
    }

    int changedSpeed = entity.getComponent(PlayerActions.class).alterSpeed(newSpeed);

    // Alters the speed back to the original setting after a certain time duration.
    return new StatusEffectResetTask() {
      @Override
      public void run() {
        entity.getComponent(PlayerActions.class).alterSpeed(-changedSpeed);
      }
    };
  }

  /**
   * changes the jump height of the player
   */
  private StatusEffectResetTask jumpBoost() {
    int jumpBoost = StatusEffect.JUMP.getMagnitude();

    int changedJumpHeight = entity.getComponent(PlayerActions.class).alterJumpHeight(jumpBoost);

    // Alters the speed back to the original setting after a certain time duration.
    return new StatusEffectResetTask() {
      @Override
      public void run() {
        entity.getComponent(PlayerActions.class).alterJumpHeight(-changedJumpHeight);
      }
    };
  }

  /**
   * Traps the player in place (immobilises the player)
   */
  private StatusEffectResetTask stuckInMud() {
    int currentSpeed = (int) entity.getComponent(PlayerActions.class).getSpeed();
    int newSpeed = currentSpeed * -1;

    entity.getComponent(PlayerActions.class).alterSpeed(newSpeed);
    /* Play the music when the player stucks in mud*/
    MusicSingleton music = MusicSingleton.getInstance();
    music.playMusicSingleton("sounds/OBSTACLE_Button.mp3", true, 0.7f);
    return new StatusEffectResetTask() {
      @Override
      public void run() {
        entity.getComponent(PlayerActions.class).alterSpeed(currentSpeed);
        MusicSingleton music = MusicSingleton.getInstance();
        music.pauseMusicSingleton("sounds/OBSTACLE_Button.mp3");
      }
    };
  }

  /** Interface used for scheduling effect resets */
  public abstract static class StatusEffectResetTask {
    public abstract void run();
  }
}
