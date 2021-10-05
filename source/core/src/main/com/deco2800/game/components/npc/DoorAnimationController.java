package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an interactable object's state and plays the animation when one
 * of the events is triggered.
 */
public class DoorAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("idle", this::animateIdle);
        entity.getEvents().addListener("activate", this::animateActive);
    }

    void animateIdle() {
        animator.startAnimation("Door_Closed");
    }

    void animateActive() {
        animator.startAnimation("Door");
    }
}
