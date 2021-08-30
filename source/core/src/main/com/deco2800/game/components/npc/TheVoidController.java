package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.utils.math.Vector2Utils;


/**
 * This class listens to events relevant to the void entity and calls a method
 * when an event is triggered
 */
public class TheVoidController extends Component {
    private static final Vector2 ACCELERATION = new Vector2(8f, 0f);

    private PhysicsComponent physicsComponent;
    private AnimationRenderComponent animator;
    private Body body;
    private Entity player;

    public TheVoidController(Entity target){
        player = target;
    }

    /**
     * Creates a number of listeners for events to be triggered in the TheVoidTasks class
     *
     */
    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.getBody().setGravityScale(0);

        entity.getEvents().addListener("TheVoidAnimate", this::theVoidAnimate);
        entity.getEvents().addListener("TheVoidMove", this::theVoidMove);
        entity.getEvents().addListener("UpdateSound", this::updateSound);

        this.body = physicsComponent.getBody();
    }

    /**
     * Starts the void's animation
     *
     */
     void theVoidAnimate(){
        animator.startAnimation("void");
    }

    /**
     * Makes the void move (if called repeatedly the void will move at a constant speed)
     */
     void theVoidMove(){
        this.body.applyForceToCenter(Vector2Utils.RIGHT.cpy().scl(ACCELERATION), true);
    }

    /**
     * Returns the distance between the player and the void as a float
     *
     * @return the distance between the player and the void
     */
     private float getPlayerDistance(){
        float distance_x;
        float void_length = this.entity.getScale().x;
        Vector2 void_pos = this.entity.getPosition();
        distance_x = player.getPosition().sub(void_pos).x - void_length;
        return distance_x;
    }


     void updateSound(){
         float distance_from_player = getPlayerDistance();

    }








}
