package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 ACCELERATION = new Vector2(10f, 0f);  // Force of acceleration, in Newtons (kg.m.s^2)
  private static final float NORMAL_FRICTION = 0.1f;                 // Coefficient of friction for normal movement
  private static final float SLIDING_FRICTION = 0.02f;               // Coefficient of friction when sliding
  private static final float AIR_FRICTION = 0.03f;                   // Coefficient of friction when in air

  private PlayerState playerState = PlayerState.STOPPED;  // Movement state of the player, see PlayerState
  private PhysicsComponent physicsComponent;              
  private Vector2 walkDirection = Vector2.Zero.cpy();     // The direction the player is walking in, set by keypress.
  private Body body;                                      // The player physics body.

  private boolean canJump = false; // Whether the player can jump

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("jump", this::jump);
    entity.getEvents().addListener("togglePlayerJumping", this::togglePlayerJumping);

    this.body = physicsComponent.getBody();
  }

  @Override
  public void update() {
    if (playerState != PlayerState.STOPPED) {
      updateSpeed();
      applyFriction();
    }
  }

  /**
   * Updates the player's movement speed by adding their desired direction to their vector.
   * This function antagonistcally competetes with updateSpeed() in order to determine a 
   * speed limit.
   */
  private void updateSpeed() {
    // Scale the walk direction by the acceleration, and apply that as a force
    this.body.applyForceToCenter(walkDirection.cpy().scl(ACCELERATION), true);
  }

  /**
   * Applies friction to the player, as determined by the X_FRICTION constants and their
   * current movement speed. This function antagonistcally competetes with updateSpeed()
   * in order to determine a speed limit.
   */
  private void applyFriction() {
    Vector2 friction;
    Vector2 velocity = body.getLinearVelocity();
    
    // Determine friction applied for each direction
    switch (playerState) {
      case AIR:
        // Fall through for now

      case SLIDING:
        // Fall through for now

      default:
        friction = new Vector2(-NORMAL_FRICTION * velocity.x, -NORMAL_FRICTION * velocity.y);
        break;
    }

    // Apply friction
    this.body.applyForceToCenter(friction, true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    this.playerState = PlayerState.MOVING;
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    //currentVelocity = new Vector2(-0.2f, -0.2f);
    //updateSpeed();
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }

  /**
   * Makes the player jump upwards
   */
  void jump() {
    System.out.println("trying to jump and " + canJump + "state is " + playerState); // Testing print

    if (playerState != PlayerState.AIR && canJump) {

      System.out.println("in air"); // More testing prints

      this.playerState = PlayerState.AIR;
      body.applyForceToCenter(new Vector2(0f, 300f), true);
      canJump = false;
    }
  }

  /**
   * Makes the player slide.
   */
  void slide() {
    this.playerState = PlayerState.SLIDING;
    body.applyForceToCenter(new Vector2(300f, 0f), true);
  }


  /**
   * Allows the player to jump after colliding with the ground.
   */
  public void togglePlayerJumping() {
      // Allows the player to jump and sets their state back to moving
      // Can't make this private, not sure if that is bad or not
      this.canJump = true;
      this.playerState = PlayerState.MOVING;
  }

  /**
   * Get the player's state.
   * @return Current player's state.
   */
  public PlayerState getPlayerState() {
    return this.playerState;
  }

  /**
   * Get whether the player can jump.
   * @return Whether the player can jump. False if the player is in the air.
   */
  public Boolean getCanJump() {
    return this.canJump;
  }
}
