package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.utils.math.Vector2Utils;

public class TheVoidController extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(1f, 0); // Metres per second

    private PhysicsComponent physicsComponent;
    private Vector2 moveDirection = Vector2Utils.RIGHT.cpy();
    AnimationRenderComponent animator;

    @Override
    public void create() {
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("TheVoidMove", this::theVoidMove);
        entity.getEvents().addListener("TheVoidAnimate", this::theVoidAnimate);
    }

    void theVoidMove() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = moveDirection.cpy().scl(MAX_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    void theVoidAnimate(){
        animator.startAnimation("void");
    }


}
