package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.endgame.LevelEndComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.ObstacleDefinition;
import com.deco2800.game.entities.ObstacleEntity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.screens.MainGameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerMovementComponent extends Component {
    private short targetLayer;
    private Map<ObstacleEntity, List<ObstacleEntity>> mapInteractables;
    private HitboxComponent hitboxComponent;

    /**
     * Create a component which interacts with entities on collision.
     * @param targetLayer The physics layer of the target's collider.
     */
    public PlayerMovementComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    public PlayerMovementComponent(short targetLayer, Map<ObstacleEntity, List<ObstacleEntity>> mapInteractables) {
        this.targetLayer = targetLayer;
        this.mapInteractables = mapInteractables;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    /**
     * Communicate with the player's action ability through monitoring
     * collision with the ground.
     * @param me The first fixture (player).
     * @param other The second fixture.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Get both entities
        Entity player = ((BodyUserData) me.getBody().getUserData()).entity;
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        // Get the relevant components from the target entity
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class); // probably don't need this
        JumpableComponent jumpableComponent = target.getComponent(JumpableComponent.class);
        LevelEndComponent levelEndComponent = target.getComponent(LevelEndComponent.class);
        JumpPadComponent jumpPadComponent = target.getComponent(JumpPadComponent.class);
        InteractableComponent interactableComponent = target.getComponent(InteractableComponent.class);
        SubInteractableComponent subComponent = target.getComponent(SubInteractableComponent.class);

        // Can control user behaviour with component
        PlayerActions playerActions = player.getComponent(PlayerActions.class);

        if (physicsComponent != null) {
            if (jumpableComponent != null) {
                // Jumping off the ground
                playerActions.togglePlayerJumping();
            }

            if (jumpPadComponent != null) {
                // Colliding with jumppad
                playerActions.jumpPad();
            }

            if (interactableComponent != null && mapInteractables.containsKey(target)) {
                List<ObstacleEntity> subInteractables = mapInteractables.get(target);
                for (ObstacleEntity subInteractable : subInteractables) {
                    ColliderComponent colliderComponent = subInteractable.getComponent(ColliderComponent.class);

                    if (subInteractable.getDefinition() == ObstacleDefinition.DOOR) {
                        // Desired affect on mapped door - disappears
                        colliderComponent.dispose();
                    } else if (subInteractable.getDefinition() == ObstacleDefinition.BRIDGE) {
                        // Desired affect on mapped bridge - appears
                        // may need to create bridges without a collider component at the beginning
                        colliderComponent.create();
                    }
                }
            }
        }
        if (levelEndComponent != null) {
            MainGameScreen.setLevelComplete();
        }

        // && physicsComponent.toString()
        //             .equals("Entity{id=9}.PhysicsComponent") ---> uglier but only allows jumping from floor
    }
}
