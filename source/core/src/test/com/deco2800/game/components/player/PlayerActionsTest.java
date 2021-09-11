package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
    @Test
    void shouldNotWalk() {
        PlayerActions playerActions = new PlayerActions();
        playerActions.stopWalking();

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

    Entity createPlayer(short playerLayer) {

        Entity entity =
                new Entity()
                        .addComponent(new ColliderComponent())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(playerLayer))
                        .addComponent(new PlayerActions());


        entity.create();
        return entity;
    }
    */
}
