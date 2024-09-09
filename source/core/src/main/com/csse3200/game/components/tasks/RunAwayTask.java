package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for managing the runaway behavior of an entity. The entity runs away from the target
 * when it is too close. The entity will stop running away when it is far enough from the target.
 */
public class RunAwayTask extends ChargeTask {
    private static final Logger logger = LoggerFactory.getLogger(RunAwayTask.class);
    private boolean isRunningAway = false;

    /**
     * Constructs the RunAwayTask using the target entity and a configuration object
     * that defines movement parameters like speed and distance.
     *
     * @param target The target entity for the entity to run away from.
     * @param config The configuration for the entity's run away behavior.
     */
    public RunAwayTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig config) {
        super(target, config);
    }

    /**
     * Starts the run-away task and sets up event listeners to handle the "runAway" and "stopRunAway" events.
     */
    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().addListener("runAway", this::startRunningAway);
        this.owner.getEntity().getEvents().addListener("stopRunAway", this::stopRunningAway);
    }

    /**
     * Sets the target position in the opposite direction from the target entity, causing the owner to run away.
     */
    @Override
    protected void startMoving() {
        logger.debug("Starting running away from {}", target.getPosition());
        Vector2 runAwayDirection = calculateRunAwayDirection();
        movementTask.setTarget(runAwayDirection);
        movementTask.start();
    }

    /**
     * Calculates the direction to move away from the target.
     *
     * @return A Vector2 representing the direction to run away.
     */
    private Vector2 calculateRunAwayDirection() {
        Vector2 currentPosition = owner.getEntity().getPosition();
        Vector2 targetPosition = target.getPosition();
        Vector2 awayVector = currentPosition.cpy().sub(targetPosition).nor();
        return currentPosition.cpy().add(awayVector.scl(maxChaseDistance));
    }

    /**
     * Returns the priority of the run-away task. It depends on whether the entity is actively running away.
     *
     * @return The task priority if running away, otherwise -1.
     */
    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }
        return getInactivePriority();
    }

    /**
     * Returns the active priority level when the entity is running away.
     *
     * @return The priority level, or -1 if the entity should stop running away.
     */
    @Override
    protected int getActivePriority() {
        if (!isRunningAway) {
            return -1;
        }
        float distance = getDistanceToTarget();
        if (distance > maxChaseDistance) {
            stopRunningAway();
            return -1;
        }
        return priority;
    }

    /**
     * Returns the priority level when the task is inactive.
     *
     * @return The inactive priority level, or -1 if not running away.
     */
    @Override
    protected int getInactivePriority() {
        return isRunningAway ? priority : -1;
    }

    /**
     * Trigger the start of running away.
     */
    private void startRunningAway() {
        isRunningAway = true;
        logger.debug("Started running away");
        this.start();
    }

    /**
     * Trigger the stop of running away.
     */
    private void stopRunningAway() {
        isRunningAway = false;
        logger.debug("Stopped running away");
        this.stop();
    }
}