package com.deco2800.game.components.tasks;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

/**
 * Starts the void's animation when the game starts and makes the void constantly move as
 * well as updating the void's sound volume. The methods triggered are defined in
 * TheVoidController class.
 */
public class StatusEffectTasks extends DefaultTask implements PriorityTask {

    Entity target;

    /**
     * Initiliser
     */
    public StatusEffectTasks(Entity target) {
        this.target = target;
    }

    /**
     * Returns a Priority that is always true
     * @return 10
     */
    @Override
    public int getPriority() {
        if (getDistanceToTarget() == 0) {
            return 10;
        }
        else return -1;
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }


    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().trigger("StatusEffectAnimation");
    }

    /**
     * Called to remove the StatusBar apon
     */
    public void update() {
        this.owner.getEntity().getEvents().trigger("StatusEffectRemoval");
    }
}
