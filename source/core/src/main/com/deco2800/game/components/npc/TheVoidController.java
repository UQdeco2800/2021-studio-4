package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

public class TheVoidController extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(1f, 0); // Metres per second

    private PhysicsComponent physicsComponent;
    AnimationRenderComponent animator;

    @Override
    public void create() {
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.getBody().setGravityScale(0);

        entity.getEvents().addListener("TheVoidAnimate", this::theVoidAnimate);
    }


    void theVoidAnimate(){
        animator.startAnimation("void");
    }



}
