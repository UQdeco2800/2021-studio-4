package com.deco2800.game.components.tasks;

import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;

public class PlayerChangeAnimationHelper extends DefaultTask implements PriorityTask {

    private PhysicsComponent physicsComponent;
   // private Entity player;
    private Body body;


    public PlayerChangeAnimationHelper(){
    }

    @Override
    public int getPriority(){return 10;}

    @Override
    public void start() {
        physicsComponent = this.owner.getEntity().getComponent(PhysicsComponent.class);
        body = physicsComponent.getBody();
    }

    public void update() {
        //System.out.println(body.getLinearVelocity());
        if(body.getLinearVelocity().y < 0) {
            this.owner.getEntity().getEvents().trigger("playerIsFalling");
        }
        this.owner.getEntity().getEvents().trigger("isFallingOrSlidingDone");
    }
}
