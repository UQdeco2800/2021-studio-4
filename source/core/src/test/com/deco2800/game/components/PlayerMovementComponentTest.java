package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class PlayerMovementComponentTest {

    Map<ObstacleEntity, ObstacleEntity> mapInteractables = new HashMap<>();

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        mapInteractables.clear();
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

    /*
    @Test
    void opensDoor() {
        short playerLayer = (1 << 1);
        short obstacleLayer = (1 << 2);
        ObstacleEntity button = (ObstacleEntity) createButton();
        ObstacleEntity door = (ObstacleEntity) createDoor(1);

        mapInteractables.put(button, door);

        Entity entity = createPlayer(playerLayer, obstacleLayer);

        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = button.getComponent(HitboxComponent.class).getFixture();
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(null, door.getComponent(HitboxComponent.class));
        assertEquals(null, door.getComponent(ColliderComponent.class));
    }

    @Test
    void extendsBridge() {
        short playerLayer = (1 << 1);
        short obstacleLayer = (1 << 2);
        ObstacleEntity button = (ObstacleEntity) createButton();
        ObstacleEntity bridge = (ObstacleEntity) createBridge(1);

        mapInteractables.put(button, bridge);

        Entity entity = createPlayer(playerLayer, obstacleLayer);

        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = button.getComponent(HitboxComponent.class).getFixture();
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        boolean created = false;

        if (bridge.getComponent(ColliderComponent.class).getFixture() != null
                && bridge.getComponent(ColliderComponent.class).getFixture() != null) {
            created = true;
        }

        assertEquals(true, created);
    }
     */

    Entity createPlayer(short playerLayer, short layer) {

        AnimationRenderComponent animationRenderComponent;
        animationRenderComponent = Mockito.mock(AnimationRenderComponent.class);

        Entity entity =
                new Entity()
                        .addComponent(new PlayerMovementComponent(layer))
                        .addComponent(new ColliderComponent())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(playerLayer))
                        .addComponent(new PlayerActions("some level string"))
                        .addComponent(animationRenderComponent);
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


    Entity createButton() {
        ObstacleEntity button =
                new ObstacleEntity(ObstacleDefinition.BUTTON,1)
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new InteractableComponent());


        button.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        button.create();
        return button;
    }

    Entity createBridge(int width) {
        ObstacleEntity bridge =
                new ObstacleEntity(ObstacleDefinition.BRIDGE,width)
                        .addComponent(new PhysicsComponent())
                        .addComponent(new JumpableComponent()) //Added for jump functionality
                        .addComponent(new SubInteractableComponent());

        bridge.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        bridge.create();
        return bridge;
    }

    Entity createDoor(int height) {
        ObstacleEntity door =
                new ObstacleEntity(ObstacleDefinition.DOOR,height)
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new SubInteractableComponent());

        door.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        door.create();
        return door;
    }

 }
