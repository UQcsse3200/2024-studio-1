package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for an NPC to run away from a target at a given speed. The task will stop
 * when the entity is far enough from the target or when the specified time has passed.
 */
public class RunAwayTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(RunAwayTask.class);
    private static final int ACTIVE_PRIORITY = 10;
    private static final int INACTIVE_PRIORITY = 9;
    private final Entity target;
    private final float activationMinRange;
    private final float activationMaxRange;
    private final float activationHealthPercentage;
    private final float runSpeed;  // Speed of movement
    private final float stopDistance;  // Distance to stop running
    private final float maxRunTime;  // Maximum time to run for
    private final float cooldownTime;
    private MovementTask movementTask;
    private GameTime gameTime;
    private long startTime;
    private long lastExecutionTime;

    /**
     * Creates a RunAwayTask.
     *
     * @param target Entity to run away from.
     * @param config Configuration for the runaway task.
     */
    public RunAwayTask(Entity target, TaskConfig.RunAwayTaskConfig config) {
        this.target = target;
        this.activationMinRange = config.activationMinRange;
        this.activationMaxRange = config.activationMaxRange;
        this.activationHealthPercentage = config.activationHealth;
        this.runSpeed = config.runSpeed;
        this.stopDistance = config.stopDistance;
        this.maxRunTime = config.maxRunTime;
        this.cooldownTime = config.cooldownTime;
        gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        logger.debug("Starting to run away from {}", target);
        super.start();
        startTime = gameTime.getTime();

        // Calculate the direction away from the target
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 directionAway = currentPos.cpy().sub(target.getPosition()).nor();
        Vector2 targetPos = currentPos.add(directionAway.scl(stopDistance));

        // Initialise a MovementTask to move the entity away
        movementTask = new MovementTask(targetPos);
        movementTask.create(owner);
        movementTask.start();
        movementTask.setVelocity(runSpeed);

        // Trigger the run animation
        owner.getEntity().getEvents().trigger("run");
    }

    @Override
    public void update() {
        movementTask.update();
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition());
        long elapsedTime = gameTime.getTimeSince(startTime);

        // Stop if the distance is greater than or equal to the stop distance, or if the time has passed
        if (distanceToTarget >= stopDistance || elapsedTime >= maxRunTime * 1000) {
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
        lastExecutionTime = gameTime.getTime();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return ACTIVE_PRIORITY;
        }
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition());
        int currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        int maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        float healthPercentage = (float) currentHealth / maxHealth;
        if (isCooldownComplete() && distanceToTarget >= activationMinRange && distanceToTarget <= activationMaxRange
                && healthPercentage <= activationHealthPercentage) {
            return INACTIVE_PRIORITY;
        }
        return -1;
    }

    private boolean isCooldownComplete() {
        if (lastExecutionTime == 0) {
            return true;
        }
        long currentTime = gameTime.getTime();
        return (currentTime - lastExecutionTime) >= (cooldownTime * 1000);
    }
}