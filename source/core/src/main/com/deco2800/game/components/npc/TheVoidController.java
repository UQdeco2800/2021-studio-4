package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.utils.math.Vector2Utils;

public class TheVoidController extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(1f, 0); // Metres per second
    private static final Vector2 ACCELERATION = new Vector2(5f, 0f);

    private PhysicsComponent physicsComponent;
    AnimationRenderComponent animator;
    Body body;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.getBody().setGravityScale(0);

        entity.getEvents().addListener("TheVoidAnimate", this::theVoidAnimate);
        entity.getEvents().addListener("TheVoidMove", this::theVoidMove);

        this.body = physicsComponent.getBody();
    }


    void theVoidAnimate(){
        animator.startAnimation("void");
    }

    void theVoidMove(){
        this.body.applyForceToCenter(Vector2Utils.RIGHT.cpy().scl(ACCELERATION), true);
    }




}
