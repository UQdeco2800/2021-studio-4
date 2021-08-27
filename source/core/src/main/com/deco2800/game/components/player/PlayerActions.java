package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  //private static final Vector2 MAX_SPEED = new Vector2(5f, 15f);      // Metres per second
  private static final Vector2 ACCELERATION = new Vector2(10f, 0f);   // Force of acceleration, in Newtons (kg.m.s^2)
  private static final float NORMAL_FRICTION = 0.1f;                 // Coefficient of friction for normal movement
  private static final float SLIDING_FRICTION = 0.02f;               // Coefficient of friction when sliding
  private static final float AIR_FRICTION = 0.03f;                   // Coefficient of friction when in air

  //Rate of speed increase, metres per second per calucation?
  private PlayerState playerState = PlayerState.STOPPED;  // Movement state of the player, see PlayerState
  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private Body body;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("jump", this::jump);

    this.body = physicsComponent.getBody();
  }

  @Override
  public void update() {
    if (playerState != PlayerState.STOPPED) {
      updateSpeed();
<<<<<<< HEAD
      ServiceLocator.getCamera().getEntity().setPosition(entity.getCenterPosition());
=======
      applyFriction();
>>>>>>> 51cd6a1eb62face48a3fa2381238bcf7dd4ec90f
    }
  }

  private void updateSpeed() {
    // Scale the walk direction by the acceleration, and apply that as a force
    this.body.applyForceToCenter(walkDirection.cpy().scl(ACCELERATION), true);
  }

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
        friction = new Vector2(-NORMAL_FRICTION*velocity.x, -NORMAL_FRICTION*velocity.y);
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
    playerState = PlayerState.MOVING;
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
   * 
   * @param height the height to jump.
   */
  void jump() {
    playerState = PlayerState.MOVING;
    body.applyForceToCenter(new Vector2(0f, 300f), true);
  }
}
