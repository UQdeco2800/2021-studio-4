package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.statuseffects.StatusEffectOperation;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;

import java.util.ArrayList;

/** This component is intended to track the status effects that the player has picked up during their run. */
public class StatusEffectsController extends Component {
    private ArrayList<String> statusEffects; // List of status effects.
    // private StatusEffect statusEffects;

    private PhysicsComponent physicsComponent;
    private AnimationRenderComponent animator;
    private Body body;
    private Entity player;
    private String effect;

    public StatusEffectsController(Entity target, String effect) {
        /** Create a new array list for the status effects. */
        statusEffects = new ArrayList<String>(5);
        for (int i = 0; i < 5; i++) {
            //Initialises each index in the array
            statusEffects.add("");
        }
        this.player = target;
        this.effect = effect;
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
        animator.startAnimation(effect);
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

    /**
     * Checks if the statusEffect is near the player. If so, it removes the statusEffect from the game
     * and adds that effect to the statusEffects array.
     * (At the moment the statusEffect is turning invisible and dropping off of the map)
     */
    public void remove() {
        if (getPlayerDistance() < 0.05) {
            entity.getComponent(ColliderComponent.class).setSensor(true);
            entity.setScale(-0.01f, -0.01f); // Makes it invisible. However still has origin sized collision box

            //adds the effect to the Array
            switch (effect) {
                case "Buff_Jump":
                    statusEffects.add(0, effect);
                    break;
                case "Buff_Time_Stop":
                    statusEffects.add(1, effect);
                    break;
                case "Buff_Speed":
                    statusEffects.add(2, effect);
                    break;
                case "Debuff_Bomb":
                    statusEffects.add(3, effect);
                    break;
                case "Debuff_Speed":
                    statusEffects.add(4, effect);
                    break;
                case "Debuff_Stuck":
                    statusEffects.add(5, effect);
                    break;
                default:
                    break;
            }

            // Changes the players ability based off of the effect given
            StatusEffectOperation statusEffectOperation = new StatusEffectOperation(player, effect, statusEffects);
            statusEffectOperation.inspect();

        }
    }

    /** Return the list of current status effects */
    public ArrayList<String> getStatusEffects() {
        return statusEffects;
    }

    /** Add status effect to the list of current status effects */
    public void addStatusEffect(int index, String statusEffect) {
        statusEffects.add(index, statusEffect);
    }


}
