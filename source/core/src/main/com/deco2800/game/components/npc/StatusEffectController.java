package com.deco2800.game.components.npc;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.statuseffects.StatusEffectTargetComponent;
import com.deco2800.game.effects.StatusEffect;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;

/** This component is intended to track the status effects that the player has picked up during their run. */
public class StatusEffectController extends Component {
    private AnimationRenderComponent animator;
    private StatusEffect effect;
    private HitboxComponent hitboxComponent;
    private boolean used = false;

    public StatusEffectController(StatusEffect effect) {
        this.effect = effect;
    }

    /**
     * Creates a number of listeners for events to be triggered in the TheVoidTasks class
     */
    @Override
    public void create() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);

        entity.getEvents().addListener("StatusEffectAnimate", this::animate);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /**
     * Method used for monitoring collisions. If something collides with a StatusEffectTargetComponent, then
     * an event is triggered
     * @param me The first fixture (player).
     * @param other The second fixture.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (used) {
            return;
        }

        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        // Get both entities
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        StatusEffectTargetComponent statusEffectTargetComponent = target.getComponent(StatusEffectTargetComponent.class);

        if (statusEffectTargetComponent != null) {
            target.getEvents().trigger("StatusEffectTrigger", effect);
        }
        System.out.println(target);
        // Delete effect after being used
        this.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        entity.getComponent(ColliderComponent.class).setSensor(true);
        entity.setScale(-0.01f, -0.01f); // Makes it invisible. However still has origin sized collision box
        used = true;
    }

    /**
     * Starts the effect entity's animation
     */
    void animate(){
        animator.startAnimation(effect.getGroundAnimationName());
    }

    public StatusEffect getEffect() {
        return effect;
    }
}
