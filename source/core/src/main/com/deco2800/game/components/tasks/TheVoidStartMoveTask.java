package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

public class TheVoidStartMoveTask extends DefaultTask implements PriorityTask {


    public TheVoidStartMoveTask(){}

    @Override
    public int getPriority(){return 10;}

    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().trigger("TheVoidMove");
    }
    public void update() {
        this.owner.getEntity().getEvents().trigger("TheVoidMove");
    }
}
