package com.deco2800.game.areas;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class LevelGameAreaTest {
    List<ObstacleEntity> obstacleEntities = new ArrayList<>();
    Map<Integer, Integer> mapInteractables = new HashMap<>();

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        obstacleEntities.clear();
        mapInteractables.clear();
    }

    @Test
    void onlyInteractables() {
        List<ObstacleEntity> newObstacleEntities = new ArrayList<>();

        ObstacleEntity buttonOne = (ObstacleEntity) createButton();
        ObstacleEntity buttonTwo = (ObstacleEntity) createButton();
        ObstacleEntity buttonThree = (ObstacleEntity) createButton();
        obstacleEntities.add(buttonOne);
        obstacleEntities.add(buttonTwo);
        obstacleEntities.add(buttonThree);
        newObstacleEntities.add(buttonOne);
        newObstacleEntities.add(buttonTwo);
        newObstacleEntities.add(buttonThree);


        ObstacleEntity doorOne = (ObstacleEntity) createDoor(1);
        ObstacleEntity doorTwo = (ObstacleEntity) createDoor(1);
        obstacleEntities.add(doorOne);
        obstacleEntities.add(doorTwo);
        newObstacleEntities.add(doorOne);
        newObstacleEntities.add(doorTwo);

        ObstacleEntity bridgeOne = (ObstacleEntity) createBridge(1);
        obstacleEntities.add(bridgeOne);
        newObstacleEntities.add(bridgeOne);

        mapInteractables();

        Map<Integer, Integer> newMapInteractables = new HashMap<>();
        newMapInteractables.put(buttonOne.getId(), doorOne.getId());
        newMapInteractables.put(buttonTwo.getId(), doorTwo.getId());
        newMapInteractables.put(buttonThree.getId(), bridgeOne.getId());

        assertEquals(newObstacleEntities.toString(), obstacleEntities.toString());
        assertEquals(newMapInteractables.toString(), this.mapInteractables.toString());
    }

    @Test
    void notOnlyInteractables() {
        ObstacleEntity platformOne = (ObstacleEntity) createPlatform(1);
        ObstacleEntity platformTwo = (ObstacleEntity) createPlatform(1);
        ObstacleEntity platformThree = (ObstacleEntity) createPlatform(1);
        ObstacleEntity platformFour = (ObstacleEntity) createPlatform(1);

        ObstacleEntity buttonOne = (ObstacleEntity) createButton();
        ObstacleEntity buttonTwo = (ObstacleEntity) createButton();
        ObstacleEntity buttonThree = (ObstacleEntity) createButton();

        obstacleEntities.add(buttonOne);
        obstacleEntities.add(platformOne); // platform
        obstacleEntities.add(buttonTwo);
        obstacleEntities.add(buttonThree);
        obstacleEntities.add(platformTwo); // platform


        ObstacleEntity doorOne = (ObstacleEntity) createDoor(1);
        ObstacleEntity doorTwo = (ObstacleEntity) createDoor(1);

        obstacleEntities.add(doorOne);
        obstacleEntities.add(platformThree); // platform
        obstacleEntities.add(doorTwo);


        ObstacleEntity bridgeOne = (ObstacleEntity) createBridge(1);
        obstacleEntities.add(bridgeOne);
        obstacleEntities.add(platformFour); // platform

        mapInteractables();

        Map<Integer, Integer> newMapInteractables = new HashMap<>();
        newMapInteractables.put(buttonOne.getId(), doorOne.getId());
        newMapInteractables.put(buttonTwo.getId(), doorTwo.getId());
        newMapInteractables.put(buttonThree.getId(), bridgeOne.getId());

        assertEquals(newMapInteractables.toString(), this.mapInteractables.toString());
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
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
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
                        .addComponent(new SubInteractableComponent());

        door.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        door.create();
        return door;
    }

    public static Entity createPlatform(int width) {
        ObstacleEntity platform =
                new ObstacleEntity(ObstacleDefinition.PLATFORM,width)
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new JumpableComponent()); //Added for jump functionality

        platform.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        platform.create();
        return platform;
    }

    void mapInteractables() {
        ArrayList<ObstacleEntity> buttons = new ArrayList<>();

        ArrayList<ObstacleEntity> subInteractables = new ArrayList<>();

        for (int i = 0; i < obstacleEntities.size(); i++) {
            ObstacleEntity obstacle = obstacleEntities.get(i);
            InteractableComponent interactable = obstacle.getComponent(InteractableComponent.class);
            SubInteractableComponent subInteractable = obstacle.getComponent(SubInteractableComponent.class);

            if (subInteractable != null) {
                subInteractables.add(obstacle);
            } else if (interactable != null) {
                buttons.add(obstacle);
            }
        }

        if (buttons.size() > 0 && subInteractables.size() > 0) {
            for (int j = 0; j < buttons.size(); j++) {
                mapInteractables.put(buttons.get(j).getId(), subInteractables.get(j).getId());
            }
        }
    }
}
