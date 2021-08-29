package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.JumpableComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

public class PlayerMovementComponent extends Component {
    private short targetLayer;
    private String effect;
    private HitboxComponent hitboxComponent;

    /**
     * Create a component which interacts with entities on collision, without effect.
     * @param targetLayer The physics layer of the target's collider.
     */
    public PlayerMovementComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    /**
     * Create a component which interacts with entities on collision, with effect.
     * @param targetLayer The physics layer of the target's collider.
     * @param effect The effect of the collision.
     */
    public PlayerMovementComponent(short targetLayer, String effect) {
        this.targetLayer = targetLayer;
        this.effect = effect;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

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

        //Get the
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class); // probably don't need this
        JumpableComponent jumpableComponent = target.getComponent(JumpableComponent.class);

        PlayerActions playerActions = player.getComponent(PlayerActions.class);

        if (physicsComponent != null && jumpableComponent != null) {
            System.out.println("on ground"); // Test print
            playerActions.togglePlayerJumping();
        }

        // && jumpableComponent != null ---> allows for easier implementation with other map elements
        // && physicsComponent.toString()
        //             .equals("Entity{id=9}.PhysicsComponent") ---> uglier but only allows jumping from floor
    }
}
