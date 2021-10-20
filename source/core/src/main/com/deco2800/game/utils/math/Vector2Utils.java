package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Contains additional utility constants and functions for common Vector2 operations.
 */
public class Vector2Utils {
  public static final Vector2 LEFT = new Vector2(-1f, 0f);
  public static final Vector2 RIGHT = new Vector2(1f, 0f);
  public static final Vector2 UP = new Vector2(0f, 1f);
  public static final Vector2 DOWN = new Vector2(0f, -1f);

  public static final Vector2 ONE = new Vector2(1f, 1f);
  public static final Vector2 MAX = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
  public static final Vector2 MIN = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);

  private Vector2Utils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
