package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

public class TheVoidTasks extends DefaultTask implements PriorityTask {
    private Vector2 startPos;
    private MovementTask movementTask;


    public TheVoidTasks(){}

    @Override
    public int getPriority(){return 10;}

    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().trigger("TheVoidAnimate");
    }

    public void update() {
        startPos = owner.getEntity().getPosition();
        movementTask = new MovementTask(startPos.add(5,0));
        movementTask.create(owner);
        movementTask.start();
    }
}
