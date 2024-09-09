package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for handling a boss's attack behavior. The boss chases and charges toward a target,
 * with defined view distance and movement speeds. Switches between different tasks such as
 * chasing, charging, and waiting.
 */
public class BossAttackTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(BossAttackTask.class);
    private final Entity target; // The entity the boss is attacking.
    private final int priority; // Priority of this task compared to other tasks.
    private final float viewDistance; // Distance within which the boss can detect the target.
    private final float maxChaseDistance; // Maximum chase distance before giving up.
    private final float chaseSpeed; // Speed while chasing the target.
    private final float chargeSpeed; // Speed during a charge attack.
    private final float waitTime; // Time to wait after an attack.

    private PriorityTask chaseTask; // Task for chasing the target.
    private PriorityTask chargeTask; // Task for charging at the target.
    private PriorityTask waitTask; // Task for waiting after an attack.
    protected PriorityTask currentTask; // The current active task.

    /**
     * Constructor for BossAttackTask.
     *
     * @param target The target entity to attack.
     * @param priority The priority of this task.
     * @param viewDistance Distance within which the boss can detect and chase the target.
     * @param maxChaseDistance Maximum distance from the target while chasing before giving up.
     * @param chaseSpeed Speed for chasing the target.
     * @param chargeSpeed Speed for charging towards the target.
     * @param waitTime How long to wait after the charge is completed.
     */
    public BossAttackTask(Entity target, int priority, float viewDistance, float maxChaseDistance,
                          float chaseSpeed, float chargeSpeed, float waitTime) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        this.chaseSpeed = chaseSpeed;
        this.chargeSpeed = chargeSpeed;
        this.waitTime = waitTime;
    }

    @Override
    public void start() {
        super.start();

        // Initialize the chase, charge, and wait tasks.
        chaseTask = new ChaseTask(target, priority, viewDistance, maxChaseDistance, chaseSpeed);
        chargeTask = new ChargeTask(target, priority + 1, viewDistance / 2, maxChaseDistance, chargeSpeed, 0);
        waitTask = new WaitTask(waitTime, priority + 2);

        // Create tasks and set the chase task as the initial task.
        chaseTask.create(owner);
        chargeTask.create(owner);
        waitTask.create(owner);

        currentTask = chaseTask;
        currentTask.start(); // Start with chasing the target.
    }

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

    protected void switchToTask(PriorityTask newTask) {
        currentTask.stop(); // Stop the current task before switching.
        currentTask = newTask;
        currentTask.start(); // Start the new task.
    }

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
