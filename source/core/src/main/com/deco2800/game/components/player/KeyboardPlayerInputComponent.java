package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean paused = false;

  private List<Integer> illegalKeydowns = new ArrayList<>();
  private List<Integer> keydowns = new ArrayList<>();

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
    keydowns.add(keycode);
    if (paused) {
      illegalKeydowns.add(keycode);
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
    keydowns.remove(Integer.valueOf(keycode));
    if (illegalKeydowns.contains(keycode)) {
      illegalKeydowns.remove(Integer.valueOf(keycode));
      return false;
    }
    switch (keycode) {
      case Keys.A:
        entity.getEvents().trigger("keyReleased");
        walkDirection.sub(Vector2Utils.LEFT);
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

  public void pause() {
    paused = true;
    illegalKeydowns.addAll(keydowns);
    walkDirection.set(Vector2.Zero.cpy());
    triggerWalkEvent();
  }

  public void resume() {
    paused = false;
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}
