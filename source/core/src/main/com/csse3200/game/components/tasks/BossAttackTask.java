package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for managing the attack behavior of a boss entity. The boss alternates between
 * chasing, charging at a target, and waiting after each attack. Tasks are switched based on
 * the distance to the target and current state of the boss.
 */
public class BossAttackTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(BossAttackTask.class);
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private final float chaseSpeed;
    private final float chargeSpeed;
    private final float waitTime;
    private final NPCConfigs.NPCConfig.TaskConfig.ChaseTaskConfig chaseConfig;
    private final NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig chargeConfig;

    private PriorityTask chaseTask; // Task for chasing the target.
    private PriorityTask chargeTask; // Task for charging at the target.
    private PriorityTask waitTask; // Task for waiting after an attack.
    protected PriorityTask currentTask; // The current active task.

    /**
     * Constructs the BossAttackTask using the target entity and a configuration object
     * that defines movement parameters like speed and distance.
     *
     * @param target The target entity for the boss.
     * @param config The configuration for the boss's attack behavior.
     */
    public BossAttackTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.BossAttackTaskConfig config) {
        this.target = target;
        this.priority = config.priority;
        this.viewDistance = config.viewDistance;
        this.maxChaseDistance = config.chaseDistance;
        this.chaseSpeed = config.chaseSpeed;
        this.chargeSpeed = config.chargeSpeed;
        this.waitTime = config.waitTime;
        this.chaseConfig = new NPCConfigs.NPCConfig.TaskConfig.ChaseTaskConfig();
        chaseConfig.priority = priority;
        chaseConfig.viewDistance = viewDistance;
        chaseConfig.chaseDistance = maxChaseDistance;
        chaseConfig.chaseSpeed = chaseSpeed;
        this.chargeConfig = new NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig();
        chargeConfig.priority = priority + 1;
        chargeConfig.viewDistance = viewDistance / 2;
        chargeConfig.chaseDistance = maxChaseDistance;
        chargeConfig.chaseSpeed = chargeSpeed;
        chargeConfig.waitTime = 0;
    }

    /**
     * Starts the attack task by initializing chase, charge, and wait tasks.
     * Initially, the boss starts by chasing the target.
     */
    @Override
    public void start() {
        super.start();
        chaseTask = new ChaseTask(target, chaseConfig);
        chargeTask = new ChargeTask(target, chargeConfig);
        waitTask = new WaitTask(waitTime, priority + 2);

        // Create tasks and set the chase task as the initial task.
        chaseTask.create(owner);
        chargeTask.create(owner);
        waitTask.create(owner);

        currentTask = chaseTask;
        currentTask.start(); // Start with chasing the target.
    }

    /**
     * Updates the current active task (chase, charge, or wait) based on the boss's
     * distance to the target. Switches tasks when conditions are met.
     */
    @Override
    public void update() {
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition()); // Calculate distance to the target.

        // Switch between tasks based on the boss's distance to the target and current task.
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == chaseTask && distanceToTarget <= viewDistance / 2) {
                logger.debug("Switching to charge task");
                switchToTask(chargeTask); // If within charging range, switch to the charge task.
            } else if (currentTask == chargeTask) {
                logger.debug("Switching to wait task");
                switchToTask(waitTask); // After charging, switch to the wait task.
            } else if (currentTask == waitTask) {
                logger.debug("Switching to chase task");
                switchToTask(chaseTask); // After waiting, return to chasing.
            }
        }
        currentTask.update(); // Continue updating the active task.
    }

    /**
     * Switches to a new task (chase, charge, or wait) by stopping the current task
     * and starting the new one.
     *
     * @param newTask The new task to switch to.
     */
    protected void switchToTask(PriorityTask newTask) {
        currentTask.stop(); // Stop the current task before switching.
        currentTask = newTask;
        currentTask.start(); // Start the new task.
    }

    /**
     * Get the priority of the BossAttackTask. The priority increases as the boss gets closer
     * to the target. Returns -1 if the target is out of range.
     *
     * @return Task priority based on proximity to the target.
     */
    @Override
    public int getPriority() {
        // Return task priority based on proximity to the target.
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition());
        if (distanceToTarget <= maxChaseDistance) {
            return priority; // Active priority if within chase range.
        }
        return -1; // Low priority if out of range.
    }
}
