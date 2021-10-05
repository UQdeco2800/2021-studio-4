package com.deco2800.game.areas;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.levels.LevelDefinition;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class LevelGameAreaTest {
    List<ObstacleEntity> obstacleEntities = new ArrayList<>();
    List<ObstacleEntity> interactableEntities = new ArrayList<>();


    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        obstacleEntities.clear();
        interactableEntities.clear();
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

        ArrayList<ObstacleEntity> buttonEntities = new ArrayList<>();
        buttonEntities.add(buttonOne);
        buttonEntities.add(buttonTwo);
        buttonEntities.add(buttonThree);

        ArrayList<ObstacleEntity> buttonOneMapped = new ArrayList<>();
        buttonOneMapped.add(doorOne);

        ArrayList<ObstacleEntity> buttonTwoMapped = new ArrayList<>();
        buttonTwoMapped.add(doorTwo);

        ArrayList<ObstacleEntity> buttonThreeMapped = new ArrayList<>();
        buttonThreeMapped.add(bridgeOne);

        assertEquals(newObstacleEntities.toString(), obstacleEntities.toString());
        assertEquals(buttonEntities.toString(), this.interactableEntities.toString());

        assertEquals(buttonOneMapped.toString(), buttonOne.getComponent(InteractableComponent.class).getMapped().toString());
        assertEquals(buttonTwoMapped.toString(), buttonTwo.getComponent(InteractableComponent.class).getMapped().toString());
        assertEquals(buttonThreeMapped.toString(), buttonThree.getComponent(InteractableComponent.class).getMapped().toString());
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

        ArrayList<ObstacleEntity> buttonEntities = new ArrayList<>();
        buttonEntities.add(buttonOne);
        buttonEntities.add(buttonTwo);
        buttonEntities.add(buttonThree);

        ArrayList<ObstacleEntity> buttonOneMapped = new ArrayList<>();
        buttonOneMapped.add(doorOne);

        ArrayList<ObstacleEntity> buttonTwoMapped = new ArrayList<>();
        buttonTwoMapped.add(doorTwo);

        ArrayList<ObstacleEntity> buttonThreeMapped = new ArrayList<>();
        buttonThreeMapped.add(bridgeOne);

        assertEquals(buttonEntities.toString(), this.interactableEntities.toString());

        assertEquals(buttonOneMapped.toString(), buttonOne.getComponent(InteractableComponent.class).getMapped().toString());
        assertEquals(buttonTwoMapped.toString(), buttonTwo.getComponent(InteractableComponent.class).getMapped().toString());
        assertEquals(buttonThreeMapped.toString(), buttonThree.getComponent(InteractableComponent.class).getMapped().toString());
    }

    // @Test
    // void saveToFile() {
    //     TerrainFactory terrainFactory = mock(TerrainFactory.class);
    //     LevelDefinition levelDefinition = mock(LevelDefinition.class);
    //     LevelGameArea area = new LevelGameArea(terrainFactory, levelDefinition);

    //     when(levelDefinition.getLevelFileName()).thenReturn("TEST");
        
    //     area.spawnPlatform(1, 12, 2);
    //     area.writeAll();


    // }

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

    public void mapInteractables() {
        // list of all buttons in order of creation
        ArrayList<ObstacleEntity> buttons = new ArrayList<>();

        //list of all doors and bridges in order of creation
        ArrayList<ObstacleEntity> subInteractables = new ArrayList<>();

        for (int i = 0; i < obstacleEntities.size(); i++) {
            ObstacleEntity obstacle = obstacleEntities.get(i);
            InteractableComponent interactable = obstacle.getComponent(InteractableComponent.class); // button
            SubInteractableComponent subInteractable = obstacle.getComponent(SubInteractableComponent.class); //door or bridge

            if (subInteractable != null) { // if bridge or door
                subInteractables.add(obstacle);
            } else if (interactable != null) { //if button
                buttons.add(obstacle);
            }
        }

        if (buttons.size() > 0 && subInteractables.size() > 0) {
            for (int j = 0; j < buttons.size(); j++) {
                InteractableComponent interactable = buttons.get(j).getComponent(InteractableComponent.class);
                interactable.addSubInteractable(subInteractables.get(j));
                interactableEntities.add(buttons.get(j));
            }
        }
    }

}
