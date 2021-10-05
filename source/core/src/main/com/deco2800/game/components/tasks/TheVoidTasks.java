package com.deco2800.game.components.tasks;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.npc.TheVoidController;

/**
 * Starts the void's animation when the game starts and makes the void constantly move as
 * well as updating the void's sound volume. The methods triggered are defined in
 * TheVoidController class.
 */
public class TheVoidTasks extends DefaultTask implements PriorityTask {
    public static boolean paused = false;

    public TheVoidTasks(){}

    @Override
    public int getPriority(){return 10;}

    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().trigger("TheVoidAnimate");
    }

    public void update() {
        if (!paused) {
            this.owner.getEntity().getEvents().trigger("TheVoidMove");
            this.owner.getEntity().getEvents().trigger("UpdateSound");
            this.owner.getEntity().getEvents().trigger("StopVoidIfPlayerDead");
            this.owner.getEntity().getEvents().trigger("RestartVoidOnRestart");
        }
    }

    public void stopVoid() {
        this.owner.getEntity().getEvents().trigger("StopVoidIfPlayerDead");
    }
}
