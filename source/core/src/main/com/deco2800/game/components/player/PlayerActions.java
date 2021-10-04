package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

import java.util.Random;
import java.util.function.DoubleToIntFunction;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */


public class PlayerActions extends Component {

    private String gameLevel;

    public PlayerActions(String currentLevel) {
        this.gameLevel = currentLevel;
    }

//enum consisting of the possible movement of the player
  private enum Movement {
    Running,
    Idle,
    Falling,
    Sliding,
    Jump,
    Walk
  }
  //direction the player is moving
  private enum MovingDirection {
    Left,
    Right
  }

  private MovingDirection movingDirection;
  private String currentPowerUp;
  private Movement currentMovement;
  private String previousAnimation;
  private boolean canPlayerMove;
  private double spawnAnimationToUse;
  private String spawnAnimation;

  private static Vector2 ACCELERATION = new Vector2(10f, 0f);;  // Force of acceleration, in Newtons (kg.m.s^2)
  private static final float NORMAL_FRICTION = 0.1f;                 // Coefficient of friction for normal movement

  private PlayerState playerState = PlayerState.STOPPED;        // Movement state of the player, see PlayerState
  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();           // The direction the player is walking in, set by keypress.
  private Vector2 previousWalkDirection = Vector2.Zero.cpy();   // The direction the player was moving in last.

  private Body body;// The player physics body.
  private int keysPressed; //stores number of keys being pressed that affect the plaer
  AnimationRenderComponent animator;

  private Vector2 jumpSpeed = new Vector2(0f, 300f);
  private Vector2 jumpPadSpeed = new Vector2(0f, 600f);
  private boolean canJump = false; // Whether the player can jump


  @Override
  public void create() {
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("jump", this::jump);
    entity.getEvents().addListener("togglePlayerJumping", this::togglePlayerJumping);
    entity.getEvents().addListener("slide", this::slide);
    entity.getEvents().addListener("setPreviousWalkDirection", this::setPreviousWalkDirection);
    entity.getEvents().addListener("playerIsFalling", this::setIsFalling);
    entity.getEvents().addListener("isJumping", this::setIsJumping);
    entity.getEvents().addListener("isFallingDone", this::checkIfFallingIsDone);
    entity.getEvents().addListener("isSlidingDone", this::checkIfSlidingIsDone);
    entity.getEvents().addListener("keyPressed", this::keyWasPressed);
    entity.getEvents().addListener("keyReleased", this::keyWasReleased);
    entity.getEvents().addListener("setPowerUpAnimation", this::setCurrentPowerUp);
    entity.getEvents().addListener("playerIsDead", this::playerIsDead);


    movingDirection = MovingDirection.Right;
    currentMovement = Movement.Idle;
    currentPowerUp = "Default";
    keysPressed = 0;

    this.body = physicsComponent.getBody();
    previousAnimation = getAnimation();


    animator.startAnimation(getAnimation());
    canPlayerMove = true;
    //animator.startAnimation("spawn_level1");
/*
    hasSpawnAnimationFinished = false;
    if(gameLevel == "LEVEL_4") {
        System.out.println("set animation to slidingRight");
        spawnAnimation = "levelOneSpawn";
    } else {
        spawnAnimationToUse = (Math.random() * (2 - 1)) + 1;
        if (spawnAnimationToUse == 1) {
            spawnAnimation = "spawnOne";
        } else {
            spawnAnimation = "spawnTwo";
        }
    }

 */
  }

  @Override
  public void update() {
      /*
      if(!hasSpawnAnimationFinished && animator.getCurrentAnimation() == null) {
          animator.startAnimation(spawnAnimation);
          System.out.println("started" + animator.getCurrentAnimation() + "animation");
      } else if (animator.isFinished() && !hasSpawnAnimationFinished) {
          System.out.println("has finished");
          this.hasSpawnAnimationFinished = true;
          animator.startAnimation(getAnimation());
      }
      */
    if (playerState != PlayerState.STOPPED) {
      updateSpeed();
      ServiceLocator.getCamera().getEntity().setPosition(entity.getCenterPosition());
      applyFriction();
    }
    if (this.body.getPosition().y < -5) {
      playerIsDead();
    }
  }

  private void playerIsDead() {
      canPlayerMove = false;
      this.entity.getComponent(PlayerStatsDisplay.class).playerIsDead();
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

  public float getSpeed() {
    return ACCELERATION.x;
  }

  public float getJumpHeight() {
    return jumpSpeed.y;
  }

  /**
   * Updates the player's movement speed by adding their desired direction to their vector.
   * This function increases the altered speed
   * speed limit.
   */
  public int alterSpeed(int newSpeed) {

    // increase or decrease the players movement
    ACCELERATION.add(newSpeed, 0);
    //return (int) ACCELERATION.x;
    return newSpeed;
  }

  public int alterJumpHeight(int newJump) {

    // increase or decrease the players movement
    jumpSpeed.add(0, newJump);
    return newJump;
  }

  public void setCanPlayerMove(boolean value) {
      this.canPlayerMove = value;
  }



  /**
   * Sets the animation of the player to the powerUp entered as the parameter value.
   * Default will set the player back to its original animation. The String value is
   * case sensitive and should begin with a capital letter. This should be called when
   * a player hits a powerUp and when their powerUp runs out.
   *
   * @param powerUp the string name of the power up animation, these are the options:
   *              Default, SpeedUp, SpeedDown, Stuck
   */
  //This is currently commented out since i have not made any placeholder sprites for powerUps
  // so I can't try and load in an animation i havent defined
private void setCurrentPowerUp(String powerUp) {
  currentPowerUp = powerUp;
  setMovementAnimation(currentMovement);
}

public String getCurrentPowerUp() {
  return currentPowerUp;
}

  /**
   * Sets the movementAnimation of the player to the animation corresponding
   * to the parameter value. The String value is case sensitive and should begin
   * with a capital letter.
   *
   * @param value the movement the player is doing one of the following:
   *              Running, Idle, Falling, Jumping, Sliding
   */
  private void setMovementAnimation(Movement value){
      /**
    if(value == Movement.Walk && getCurrentPowerUp() == "SpeedUp") {
      value = Movement.Running;
    }

    if(currentPowerUp == "Stuck") {
      value = Movement.Idle;
    }
       */

    if(!(previousAnimation.equals(getAnimation())) || value != currentMovement){
      //System.out.println(value);
      currentMovement = value;
      previousAnimation = getAnimation();
      animator.startAnimation(getAnimation());
    }
  }

  /**
   * Sets the movingDirection of the player to either Left or Right
   *
   * @param value enum value Left or Right
   */
  private void setMovingDirection(MovingDirection value){
    movingDirection = value;
  }

  /**
   * Returns a string that represents the current state of the player, this
   * includes the movement they are doing, the direction they are moving/looking
   * and the current power up they have
   *
   * @returns String containing currentMovement + movingDirection + powerUp
   */
  private String getAnimation(){
    return  getCurrentMovement() + getCurrentDirection();
  }

  String getCurrentMovement() {
    return currentMovement.toString();
  }

  String getCurrentDirection() {
    return movingDirection.toString();
  }


  /**
   * sets the players animation to falling
   */
  void setIsFalling(){
      if (canPlayerMove) {
          setCanJump(false);
          setMovementAnimation(Movement.Falling);
      }
  }

  void setIsJumping(){
    setMovementAnimation(Movement.Jump);
  }

  void setIsSliding() {
    setMovementAnimation(Movement.Sliding);
  }

  /**
   * This checks if the animation of the player is set to falling when it should not
   * be and sets the player to the correct animation
   */
  void checkIfFallingIsDone(){
    if((currentMovement == Movement.Falling | currentMovement == Movement.Jump) & canJump){
        if(body.getLinearVelocity().x == 0 | keysPressed == 0){
          setMovementAnimation(Movement.Idle);
        } else {
          setMovementAnimation(Movement.Walk);
        }
    }
  }

  void checkIfSlidingIsDone() {
    if (currentMovement == Movement.Sliding && canJump) {
        if (body.getLinearVelocity().x == 0) {
          setMovementAnimation(Movement.Idle);
        } else if (keysPressed > 0 && (body.getLinearVelocity().x < 7 && body.getLinearVelocity().x > 0 ||
                body.getLinearVelocity().x > -7 && body.getLinearVelocity().x < 7)) {
          setMovementAnimation(Movement.Walk);
        }
      }
  }

  /**
   * Increments the variable storing number of keys being pressed
   */
  void keyWasPressed(){
      keysPressed++;
  }

  /**
   * Decrements the variable storing number of keys being pressed
   */
  private void keyWasReleased(){
    keysPressed--;
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
      if (canPlayerMove) {
          if (direction.x == 1.0) {
              setMovingDirection(MovingDirection.Right);
          } else {
              setMovingDirection(MovingDirection.Left);
          }

          if (canJump) {
              setMovementAnimation(Movement.Walk);
          }

          this.walkDirection = direction;
          this.playerState = PlayerState.MOVING;
      }
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
      if (canPlayerMove) {
          this.walkDirection = Vector2.Zero.cpy();
          if (currentMovement != Movement.Falling) {
              setMovementAnimation(Movement.Idle);
          }
          //currentVelocity = new Vector2(-0.2f, -0.2f);
          //updateSpeed();
      }
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
      if (canPlayerMove) {
          //System.out.println("trying to jump and " + canJump + "state is " + playerState); // Testing print
          if (playerState != PlayerState.AIR && canJump) {
              //System.out.println("in air"); // More testing prints
              setIsJumping();

              this.playerState = PlayerState.AIR;
              body.applyForceToCenter(jumpSpeed, true);
              canJump = false;
          }
      }
  }

  /**
   * Makes the player slide if they are touching the ground and not currently sliding
   */
  void slide() {
      if(canPlayerMove && !getCurrentMovement().equals("Sliding")) {
          this.playerState = PlayerState.SLIDING;
          if (getCanJump()) {
              setIsSliding();
              if (previousWalkDirection.epsilonEquals(Vector2Utils.LEFT)) {
                  body.applyForceToCenter(new Vector2(-300f, 0f), true);
              } else {
                  body.applyForceToCenter(new Vector2(300f, 0f), true);
              }
          }
      }
  }

  /**
   * Tracks the last direction the player was moving in.
   * @param previousWalkDirection The direction the player was last moving in.
   */
  void setPreviousWalkDirection(Vector2 previousWalkDirection) {
    this.previousWalkDirection = previousWalkDirection;
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

  public void setCanJump(Boolean value) {
    canJump = value;
  }


  /**
   * Thrusts the player up at the defined jump pad speed.
   */
  public void jumpPad() {
    body.applyForceToCenter(jumpPadSpeed, true);
  }


  /**
   * Get the player's state.
   * @return Current player's state.
   */
  public PlayerState getPlayerState() {
    return this.playerState;
  }


  /**
   * Get the player's walking direction.
   * @return Current player's walking direction.
   */
  public Vector2 getWalkDirection() {
    return this.walkDirection;
  }

  /**
   * Get whether the player can jump.
   * @return Whether the player can jump. False if the player is in the air.
   */
  public Boolean getCanJump() {
    return this.canJump;
  }
}
