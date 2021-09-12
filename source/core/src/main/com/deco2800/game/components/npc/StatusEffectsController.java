package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.statuseffects.StatusEffectEnum;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.utils.math.Vector2Utils;

import java.util.ArrayList;

/** This component is intended to track the status effects that the player has picked up during their run. */
public class StatusEffectsController extends Component {
    private ArrayList<String> statusEffects; // List of status effects.
    // private StatusEffect statusEffects;

    private PhysicsComponent physicsComponent;
    private AnimationRenderComponent animator;
    private Body body;
    private Entity player;

    public StatusEffectsController(Entity target) {
        /** Create a new array list for the status effects. */
        statusEffects = new ArrayList<String>();
        this.player = target;
    }

    /**
     * Creates a number of listeners for events to be triggered in the TheVoidTasks class
     */
    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        //physicsComponent = entity.getComponent(PhysicsComponent.class);
        //physicsComponent.getBody().setGravityScale(0); This removes gravity

        entity.getEvents().addListener("StatusEffectAnimate", this::animate);
        entity.getEvents().addListener("StatusEffectRemove", this::remove);
        //entity.getEvents().addListener("TheVoidMove", this::move);  COULD IMPLEMENT LATER

        this.body = physicsComponent.getBody();
    }

    /**
     * Starts the void's animation
     */
    void animate(){
        animator.startAnimation("Debuff_Bomb");
    }

//    /**
//     * Makes the StatusEffect move (if called repeatedly the void will move at a constant speed)
//     */
//    void move(){
//        this.body.applyForceToCenter(Vector2Utils.RIGHT.cpy().scl(SPEED), true);
//    }

    /**
     * Returns the distance between the player and the void as a float
     *
     * @return the distance between the player and the void
     */
    public float getPlayerDistance(){
        float distance_x;
        float statusEffect_length = this.entity.getScale().x;
        Vector2 statusEffect_pos = this.entity.getPosition();
        distance_x = player.getPosition().sub(statusEffect_pos).x - statusEffect_length;
        return distance_x;
    }

    public void remove() {
        float distance_from_player = getPlayerDistance();

        if (distance_from_player == 0) {
            entity.dispose();
        }
    }

    /** Return the list of current status effects */
    public ArrayList<String> getStatusEffects() {
        return statusEffects;
    }

    /** Add status effect to the list of current status effects */
    public void addStatusEffect(StatusEffectEnum statusEffect) {
        statusEffects.add(statusEffect.getType());
    }

    /** Removes the status effect from the list of current status effects */
    public void removeStatusEffect(StatusEffectEnum statusEffect) {
        statusEffects.remove(statusEffect.getType());
    }
}
