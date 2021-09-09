package com.deco2800.game.components.tasks;

import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;

public class PlayerCheckVertical extends DefaultTask implements PriorityTask {

    private PhysicsComponent physicsComponent;
   // private Entity player;
    private Body body;


    public PlayerCheckVertical(){
    }

    @Override
    public int getPriority(){return 10;}

    @Override
    public void start() {
        physicsComponent = this.owner.getEntity().getComponent(PhysicsComponent.class);
        body = physicsComponent.getBody();
    }

    public void update() {
        if(body.getLinearVelocity().y < 0) {
            this.owner.getEntity().getEvents().trigger("isFalling");
        } //else if (body.getLinearVelocity().y > 0) {
         //   this.owner.getEntity().getEvents().trigger("isJumping");
       // }
        if((body.getLinearVelocity().x < 4 && body.getLinearVelocity().x > -4)) {
            this.owner.getEntity().getEvents().trigger("isIdle");
        }
        this.owner.getEntity().getEvents().trigger("isFallingDone");
    }
}
