package com.deco2800.game.components.player;

import java.io.Console;

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
  private static final Vector2 MAX_SPEED = new Vector2(5f, 15f);  // Metres per second
  private static final float NORMAL_FRICTION = 1f;                // Coefficient of friction for normal movement
  private static final float SLIDING_FRICTION = 0.2f;             // Coefficient of friction when sliding
  private static final float AIR_FRICTION = 0.3f;                   // Friction when in air

  private Vector2 currentVelocity = new Vector2(0f, 0f);  //Current speed, in metres per second
  private Vector2 acceleration = new Vector2(0.2f, 0.2f); //Rate of speed increase, metres per second per calucation?
  private PlayerState playerState = PlayerState.STOPPED;  // Movement state of the player, see PlayerState

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("jump", this::jump);
  }

  @Override
  public void update() {
    if (playerState != PlayerState.STOPPED) {
      updateSpeed();
      applyFriction();
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();

    //Adds the acceleration value onto the current speed, then checks if the current speed exceeds the imposed limit
    //If this is the case, reduces the current speed to the limit. Current implementation does not protect against BLJ's
    currentVelocity.add(acceleration);
    if (MAX_SPEED.x < currentVelocity.x) {
      currentVelocity.x = MAX_SPEED.x;
    }
    if (MAX_SPEED.y < currentVelocity.y) {
      currentVelocity.y = MAX_SPEED.y;
    }
    Vector2 desiredVelocity = walkDirection.cpy().scl(currentVelocity);

    // impulse = (desiredVel - currentVel) * mass
    //Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyForceToCenter(desiredVelocity, true);
  }

  private void applyFriction() {
    Body body = physicsComponent.getBody();
    Vector2 friction;
    Vector2 velocity = body.getLinearVelocity();
    
    // Determine friction applied for each direction
    switch (playerState) {
      case AIR:
        // Fall through for now

      case SLIDING:
        // Fall through for now

      default:
        friction = new Vector2(-NORMAL_FRICTION*velocity.x, -NORMAL_FRICTION*velocity.y);
        break;
    }

    // Apply friction
    body.applyForceToCenter(friction, true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    playerState = PlayerState.MOVING;
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    currentVelocity = new Vector2(-0.2f, -0.2f);
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
   * 
   * @param height the height to jump.
   */
  void jump() {
    playerState = PlayerState.MOVING;
    this.currentVelocity.add(0f, 15f);
  }
}
