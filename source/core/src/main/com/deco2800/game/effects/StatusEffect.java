package com.deco2800.game.effects;

public enum StatusEffect {
  BOMB("powerups/Pick_Ups.atlas", "Debuff_Bomb", "powerup-ui-spritesheets/bomb_item.atlas", "bomb-item", 4, 0),
  JUMP("powerups/Pick_Ups.atlas", "Buff_Jump", "powerup-ui-spritesheets/jump_boost.atlas", "jump-boost", 6, 200),
  FAST("powerups/Pick_Ups.atlas", "Buff_Speed", "powerup-ui-spritesheets/lightning.atlas", "lightning", 4, 5),
  SLOW("powerups/Pick_Ups.atlas", "Debuff_Speed", "powerup-ui-spritesheets/speed_decrease.atlas", "speed-decrease", 4, 5),
  STUCK("powerups/Pick_Ups.atlas", "Debuff_Stuck", "powerup-ui-spritesheets/stuck_lock.atlas", "stuck-lock", 2, 0),
  TIME_STOP("powerups/Pick_Ups.atlas", "Buff_Time_Stop", "powerup-ui-spritesheets/time_stop.atlas", "time-stop", 2, 0);

  private final String groundAnimationAtlas;
  private final String groundAnimationName;
  private final String uiAnimationAtlas;
  private final String uiAnimationName;

  /** Duration of the effect in seconds */
  private final int duration;

  /** The magnitude of this effect, exact meaning may differ based on effect */
  private final int magnitude;

  StatusEffect(String groundAnimationAtlas, String groundAnimationName,
               String uiAnimationAtlas, String uiAnimationName,
               int duration, int magnitude) {
    this.groundAnimationAtlas = groundAnimationAtlas;
    this.groundAnimationName = groundAnimationName;
    this.uiAnimationAtlas = uiAnimationAtlas;
    this.uiAnimationName = uiAnimationName;
    this.duration = duration;
    this.magnitude = magnitude;
  }

  public String getGroundAnimationAtlas() {
    return groundAnimationAtlas;
  }

  public String getGroundAnimationName() {
    return groundAnimationName;
  }

  public String getUiAnimationAtlas() {
    return uiAnimationAtlas;
  }

  public String getUiAnimationName() {
    return uiAnimationName;
  }

  public int getDuration() {
    return duration;
  }

  public int getMagnitude() {
    return magnitude;
  }
}