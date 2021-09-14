package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(GameExtension.class)
public class PlayerActionsTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldWalkLeft() {
        PlayerActions playerActions = new PlayerActions();
        playerActions.walk(Vector2Utils.LEFT);

        assertEquals(Vector2Utils.LEFT, playerActions.getWalkDirection());
        assertEquals(PlayerState.MOVING, playerActions.getPlayerState());
    }

    @Test
    void shouldWalkRight() {
        PlayerActions playerActions = new PlayerActions();
        playerActions.walk(Vector2Utils.RIGHT);

        assertEquals(Vector2Utils.RIGHT, playerActions.getWalkDirection());
        assertEquals(PlayerState.MOVING, playerActions.getPlayerState());
    }


    @Test
    void shouldNotWalk() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        assertEquals(Vector2.Zero.cpy(), playerActions.getWalkDirection());
    }

    @Test
    void shouldJump() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.togglePlayerJumping();
        playerActions.jump();

        assertEquals(PlayerState.AIR, playerActions.getPlayerState());
        assertEquals(false, playerActions.getCanJump());
    }

    @Test
    void shouldSlide() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);playerActions.slide();
        assertEquals(PlayerState.SLIDING, playerActions.getPlayerState());
    }
//__________________________________________________________________________________

    //test that the setCanJump() function sets the canJump variable properly
    @Test
    void setCanJump(){
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(true);
        assertEquals(true , playerActions.getCanJump());
    }


    //test that the setIsJumping() function sets the player's animation to jumping
   @Test
   void setIsJumping() {
       short playerLayer = (1 << 1);
       Entity entity = createPlayer(playerLayer);

       PlayerActions playerActions = entity.getComponent(PlayerActions.class);
       playerActions.setIsJumping();
       assertEquals("Jumping" , playerActions.getCurrentMovement());
   }

    //test that the setIsSliding() function sets the player's animation to Sliding
    @Test
    void setIsSliding() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setIsSliding();
        assertEquals("Sliding" , playerActions.getCurrentMovement());
    }


   //test that the isFalling() function sets the player's animation to falling
   @Test
   void setIsFallingSetsMovementToFalling() {
       short playerLayer = (1 << 1);
       Entity entity = createPlayer(playerLayer);

       PlayerActions playerActions = entity.getComponent(PlayerActions.class);
       playerActions.setIsFalling();
       assertEquals("Falling" , playerActions.getCurrentMovement());
   }

    //test that the isFalling() function sets canJump to false
    @Test
    void setIsFallingSetsCanJumpToFalse() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setIsFalling();
        assertEquals(false , playerActions.getCanJump());
    }


    //test that the jump() function sets the player's movement to Jumping if
    // the player is touching the ground
    @Test
    void jumpSetsMovementToJumpingIfValid() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(true);
        //set player state to something other than AIR
        playerActions.walk(Vector2Utils.RIGHT);
        playerActions.jump();
        assertEquals("Jumping" , playerActions.getCurrentMovement());
    }

    //test that the jump() function does not change the player's movement to
    // Jumping if the player is not touching the ground
    @Test
    void jumpDoesNotSetMovementToJumpingIfInvalid() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(false);
        playerActions.jump();
        assertNotEquals("Jumping" , playerActions.getCurrentMovement());
    }


    //test that the slide() function sets the player's movement to Sliding if
    // they are touching the ground
    @Test
    void slideSetsMovementToSlidingIfValid() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(true);
        playerActions.slide();
        assertEquals("Sliding" , playerActions.getCurrentMovement());
    }

    //test that the slide() function does not change the player's movement to
    // Sliding if they are not touching the ground
    @Test
    void slideDoesNotSetMovementToSlidingIfInvalid() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(false);
        playerActions.slide();
        assertNotEquals("Sliding" , playerActions.getCurrentMovement());
    }


    //test that the walk() function sets the player's movement to Running if
    // they are touching the ground
    @Test
    void walkSetsMovementToRunningIfValid() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(true);
        //Any direction will do
        playerActions.walk(Vector2Utils.RIGHT);
        assertEquals("Running" , playerActions.getCurrentMovement());
    }


    //test that the walk() function does not change the player's movement to
    // Running if they are not touching the ground
    @Test
    void walkDoesNotSetMovementToRunningIfInvalid() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.setCanJump(false);
        playerActions.walk(Vector2Utils.RIGHT);
        assertNotEquals("Running" , playerActions.getCurrentMovement());
    }

    //test that the walk() function sets the player's direction to Right If
    // they walk(Right)
    @Test
    void walkSetsDirectionToRight() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.walk(Vector2Utils.RIGHT);
        assertEquals("Right" , playerActions.getCurrentDirection());
    }

    //test that the walk() function sets the player's direction to Left if
    // they walk(Left)
    @Test
    void walkSetsDirectionToLeft() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        playerActions.walk(Vector2Utils.LEFT);
        assertEquals("Left" , playerActions.getCurrentDirection());
    }



    //test that the checkIfFallingIsDone() function does not change the
    // player's animation if the player's animation is set to Falling and they
    // can not jump
    @Test
    void checkIfFallingStaysFalling() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are falling
        playerActions.setIsFalling();
        //they can not jump
        playerActions.setCanJump(false);
        playerActions.checkIfFallingIsDone();
        assertEquals("Falling" , playerActions.getCurrentMovement());
    }


    //Test that the checkIfFallingDone() function sets the player's animation to
    // Idle if the player's animation is set to Falling, they can jump and no keys
    // are being pressed
    @Test
    void checkIfFallingSetsIdleIfNoKeysPressed() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are falling
        playerActions.setIsFalling();
        //they can jump
        playerActions.setCanJump(true);
        //keys pressed defaults to zero
        playerActions.checkIfFallingIsDone();
        assertEquals("Idle" , playerActions.getCurrentMovement());
    }



    //Test that the checkIfFallingIsDone() function sets the player's animation to
    // Idle if the player's animation is set to Falling, they can jump and they
    // are not moving
    @Test
    void checkIfFallingSetsIdleIfNotMoving() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are falling
        playerActions.setIsFalling();
        //they can jump
        playerActions.setCanJump(true);
        //sets the players current movement speed to zero
        playerActions.stopWalking();
        playerActions.checkIfFallingIsDone();
        assertEquals("Idle" , playerActions.getCurrentMovement());
    }



    //Test that the checkIfFallingIsDone() function sets the player's animation to
    // Running if the player's animation is set to Falling, they can jump, they are
    // moving horizontally and keys are being pressed
    @Test
    void checkIfFallingStaysFallingIfNoKeysAreBeingPressedAndCanNotJump() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are falling
        playerActions.setIsFalling();
        //they can jump
        playerActions.setCanJump(true);
        //they are moving horizontally
        playerActions.walk(Vector2Utils.RIGHT);
        //keys a being pressed
        playerActions.keyWasPressed();
        playerActions.checkIfFallingIsDone();
        assertEquals("Running" , playerActions.getCurrentMovement());
    }

    //Test that the checkIfSlidingFunction() function does not change the player's animation
    // if they are sliding and they can not jump
    @Test
    void checkIfSlidingStaySlidingIfSlidingAndCanNotJump() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are sliding
        playerActions.setIsSliding();
        //they can not jump
        playerActions.setCanJump(false);
        playerActions.checkIfSlidingIsDone();
        assertEquals("Sliding" , playerActions.getCurrentMovement());
    }

    //Test that the checkIfSlidingFunction() function sets the player's animation
    // to Idle if the player's animation is set to Sliding, they can jump and
    // they are not moving
    @Test
    void checkIfSlidingSetsIdleIfSlidingAndCanJumpAndNotMoving() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are sliding
        playerActions.setIsSliding();
        //they can jump
        playerActions.setCanJump(true);
        //they are not moving
        entity.getComponent(PhysicsComponent.class).getBody().applyForceToCenter(Vector2Utils.RIGHT.scl(0f,0f), true);
        playerActions.checkIfSlidingIsDone();
        assertEquals("Idle" , playerActions.getCurrentMovement());
    }

    //Test that the checkIfSlidingFunction() function sets the player's animation
    // to Running if the player's animation is set to Sliding, they can jump,
    // keys are being pressed and they are moving slower than 7m/s horizontally
    @Test
    void checkIfSlidingSetsRunningIfSlidingAndCanJumpAndSlowerThan7() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are sliding
        playerActions.setIsSliding();
        //they can jump
        playerActions.setCanJump(true);
        //keys are being pressed
        playerActions.keyWasPressed();
        //they are moving slower than 7m/s
        entity.getComponent(PhysicsComponent.class).getBody().setLinearVelocity(6f, 0f);
        playerActions.checkIfSlidingIsDone();
        assertEquals("Running" , playerActions.getCurrentMovement());
    }

    //Test that the checkIfSlidingFunction() function does not change the player's animation
    // to Running if the player's animation is set to Sliding, they can jump and
    // they are moving slower than 7m/s horizontally
    @Test
    void checkIfSlidingStaysSlidingIfSlidingAndCanJumpAndFasterThan7() {
        short playerLayer = (1 << 1);
        Entity entity = createPlayer(playerLayer);

        PlayerActions playerActions = entity.getComponent(PlayerActions.class);
        //they are sliding
        playerActions.setIsSliding();
        //they can jump
        playerActions.setCanJump(true);
        //they are moving faster than 7m/s
        entity.getComponent(PhysicsComponent.class).getBody().setLinearVelocity(8f, 0f);
        playerActions.checkIfSlidingIsDone();
        assertEquals("Sliding" , playerActions.getCurrentMovement());
    }











    Entity createPlayer(short playerLayer) {

        AnimationRenderComponent animationRenderComponent;
        animationRenderComponent = Mockito.mock(AnimationRenderComponent.class);

        Entity entity =
                new Entity()
                        .addComponent(new ColliderComponent())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(playerLayer))
                        .addComponent(new PlayerActions())
                        .addComponent(animationRenderComponent);


        entity.create();
        return entity;
    }

}
