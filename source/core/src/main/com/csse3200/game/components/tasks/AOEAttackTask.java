package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task for an NPC to perform an Area of Effect (AOE) attack with a preparation phase (charge-up animation).
 * This task includes handling of cooldowns and activation based on the target's proximity.
 */
public class AOEAttackTask extends DefaultTask implements PriorityTask {
    private static final int ACTIVE_PRIORITY = 10;
    private static final int INACTIVE_PRIORITY = 6;
    private final Entity target;
    private final float activationMinRange;
    private final float activationMaxRange;
    private final float cooldownTime;
    private final float preparationTime;
    private final GameTime gameTime;
    private long lastExecutionTime;
    private long preparationStartTime;
    private boolean isPreparing = false;
    private AOEAttackComponent aoeAttackComponent;
    private WaitTask waitTask;

    /**
     * Creates a new AOEAttackTask.
     *
     * @param target the target entity for the AOE attack
     * @param config the configuration for the AOE attack task, including range, cooldown, and preparation times
     */
    public AOEAttackTask(Entity target, TaskConfig.AOEAttackTaskConfig config) {
        this.target = target;
        this.activationMinRange = config.activationMinRange;
        this.activationMaxRange = config.activationMaxRange;
        this.cooldownTime = config.cooldownTime;
        this.preparationTime = config.preparationTime;
        gameTime = ServiceLocator.getTimeSource();
    }

    /**
     * Starts the AOE attack task. This initializes the preparation phase and triggers the charge-up animation.
     */
    @Override
    public void start() {
        super.start();
        aoeAttackComponent = owner.getEntity().getComponent(AOEAttackComponent.class);
        waitTask = new WaitTask(3);
        isPreparing = true;
        preparationStartTime = gameTime.getTime();

        // Trigger the charge-up animation and gesture for the AOE attack.
        owner.getEntity().getEvents().trigger("aoe_preparation");
        owner.getEntity().getEvents().trigger("gesture");
    }

    /**
     * Updates the task, managing the transition between the preparation phase and the actual attack.
     * Once the preparation time is complete, the AOE attack is triggered.
     */
    @Override
    public void update() {
        if (isPreparing) {
            long elapsedTime = gameTime.getTimeSince(preparationStartTime);
            if (elapsedTime >= preparationTime * 1000) {
                isPreparing = false;
                owner.getEntity().getEvents().trigger("attack");
                aoeAttackComponent.enableForNumAttacks(1);
                waitTask.start();
            }
        } else if (waitTask.getStatus() == Status.ACTIVE) {
            waitTask.update();
        } else if (waitTask.getStatus() == Status.FINISHED) {
            this.stop();
        }
    }

    /**
     * Stops the AOE attack task, marking the last execution time.
     */
    @Override
    public void stop() {
        super.stop();
        lastExecutionTime = gameTime.getTime();
    }

    /**
     * Gets the priority of the task. The task will have a high priority when active,
     * and a lower priority if the target is within the defined range and the cooldown has completed.
     *
     * @return the priority of the task, or -1 if it should not be executed
     */
    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return ACTIVE_PRIORITY;
        }
        float dst = owner.getEntity().getPosition().dst(target.getPosition());
        if (isCooldownComplete() && dst >= activationMinRange && dst <= activationMaxRange) {
            return INACTIVE_PRIORITY;
        }
        return -1;
    }

    /**
     * Checks if the cooldown time since the last execution has completed, allowing a new attack.
     *
     * @return true if the cooldown is complete, false otherwise
     */
    private boolean isCooldownComplete() {
        if (lastExecutionTime == 0) {
            return true;
        }
        long currentTime = gameTime.getTime();
        return (currentTime - lastExecutionTime) >= (cooldownTime * 1000);
    }
}
