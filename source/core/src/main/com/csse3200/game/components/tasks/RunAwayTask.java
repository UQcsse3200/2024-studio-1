package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunAwayTask extends ChargeTask {
    private static final Logger logger = LoggerFactory.getLogger(RunAwayTask.class);
    private boolean isRunningAway = false;

    public RunAwayTask(Entity target, int priority, float viewDistance, float maxRunDistance, float runSpeed, float waitTime) {
        super(target, priority, viewDistance, maxRunDistance, runSpeed, waitTime);
    }

    @Override
    public void start() {
        super.start();
        this.owner.getEntity().getEvents().addListener("runAway", this::startRunningAway);
        this.owner.getEntity().getEvents().addListener("stopRunAway", this::stopRunningAway);
    }

    @Override
    protected void startMoving() {
        logger.debug("Starting running away from {}", target.getPosition());
        Vector2 runAwayDirection = calculateRunAwayDirection();
        movementTask.setTarget(runAwayDirection);
        movementTask.start();
    }

    private Vector2 calculateRunAwayDirection() {
        Vector2 currentPosition = owner.getEntity().getPosition();
        Vector2 targetPosition = target.getPosition();
        Vector2 awayVector = currentPosition.cpy().sub(targetPosition).nor();
        return currentPosition.cpy().add(awayVector.scl(maxChaseDistance));
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }
        return getInactivePriority();
    }

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

    @Override
    protected int getInactivePriority() {
        return isRunningAway ? priority : -1;
    }

    private void startRunningAway() {
        isRunningAway = true;
        logger.debug("Started running away");
        this.start();
    }

    private void stopRunningAway() {
        isRunningAway = false;
        logger.debug("Stopped running away");
        this.stop();
    }
}