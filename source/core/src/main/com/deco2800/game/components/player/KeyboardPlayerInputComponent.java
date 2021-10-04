package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  public static boolean paused = false;

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    if (paused) {
      walkDirection.set(Vector2.Zero.cpy());
      triggerWalkEvent();
      return false;
    }
    switch (keycode) {
      case Keys.W:
      case Keys.SPACE:
        entity.getEvents().trigger("jump");
        return true;
      case Keys.A:
        entity.getEvents().trigger("keyPressed");
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        entity.getEvents().trigger("setPreviousWalkDirection", Vector2Utils.LEFT);
        return true;
      case Keys.S:
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        entity.getEvents().trigger("keyPressed");
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        entity.getEvents().trigger("setPreviousWalkDirection", Vector2Utils.RIGHT);
        return true;
      case Keys.SHIFT_RIGHT:
        entity.getEvents().trigger("slide");
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    if (paused) {
      walkDirection.set(Vector2.Zero.cpy());
      triggerWalkEvent();
      return false;
    }
    switch (keycode) {
      case Keys.A:
        entity.getEvents().trigger("keyReleased");
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        entity.getEvents().trigger("keyReleased");
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}
