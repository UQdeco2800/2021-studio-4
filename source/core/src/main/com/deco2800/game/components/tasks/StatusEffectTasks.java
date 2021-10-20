package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;

/**
 * Starts the void's animation when the game starts and makes the void constantly move as
 * well as updating the void's sound volume. The methods triggered are defined in
 * TheVoidController class.
 */
public class StatusEffectTasks extends DefaultTask implements PriorityTask {

    /**
     * Initiliser
     */
    public StatusEffectTasks() {}

    /**
     * Returns a Priority that is always true
     * @return 10
     */
    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().trigger("StatusEffectAnimate");
    }
}
