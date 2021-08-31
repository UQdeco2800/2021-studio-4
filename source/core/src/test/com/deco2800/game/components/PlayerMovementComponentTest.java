package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerState;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.JumpableComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class PlayerMovementComponentTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void canJump() {
        short playerLayer = (1 << 1);
        short obstacleLayer = (1 << 2);
        Entity entity = createPlayer(playerLayer, obstacleLayer);
        Entity obstacle = createObstacle(obstacleLayer);

        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = obstacle.getComponent(HitboxComponent.class).getFixture();
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(true, entity.getComponent(PlayerActions.class).getCanJump());
    }

    @Test
    void canNotJump() {
        short playerLayer = (1 << 1);
        short obstacleLayer = (0); // Default physics layer ---> Not ground
        Entity entity = createPlayer(playerLayer, obstacleLayer);
        Entity obstacle = createObstacle(obstacleLayer);

        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = obstacle.getComponent(HitboxComponent.class).getFixture();
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(false, entity.getComponent(PlayerActions.class).getCanJump());
    }

    Entity createPlayer(short playerLayer, short layer) {
        Entity entity =
                new Entity()
                        .addComponent(new PlayerMovementComponent(layer))
                        .addComponent(new ColliderComponent())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(playerLayer))
                        .addComponent(new PlayerActions());
        entity.create();
        return entity;
    }

    Entity createObstacle(short obstacleLayer) {
        Entity target =
                new Entity()
                        .addComponent(new ColliderComponent())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(obstacleLayer))
                        .addComponent(new JumpableComponent());
        target.create();
        return target;
    }
 }
