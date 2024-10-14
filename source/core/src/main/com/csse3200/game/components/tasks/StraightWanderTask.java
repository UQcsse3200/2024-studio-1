package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.configs.TaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving in a random direction. On collision, the entity changes direction randomly.
 * Requires an entity with a PhysicsMovementComponent.
 */
public class StraightWanderTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(StraightWanderTask.class);
    private static final int PRIORITY = 1;
    private static final float MAX_WANDER_LIMIT = 50; // Max distance to wander in a single direction
    private static final float EXCLUSION_RANGE = 20; // Angles in degrees to exclude when generating a new direction
    private final float wanderSpeed;
    private MovementTask movementTask;
    private float currentDirectionAngle;

    /**
     * @param wanderSpeed The speed an entity wanders at.
     */
    public StraightWanderTask(float wanderSpeed) {
        this.wanderSpeed = wanderSpeed;
    }

    /**
     * @param config The configuration for the task.
     */
    public StraightWanderTask(TaskConfig.StraightWanderTaskConfig config) {
        this.wanderSpeed = config.wanderSpeed;
    }

    @Override
    public int getPriority() {
        return PRIORITY; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startMoving();
        owner.getEntity().getEvents().trigger("walk");
        owner.getEntity().getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    @Override
    public void update() {
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            startMoving();
        }
    }

    private void startMoving() {
        logger.debug("Starting moving");
        owner.getEntity().getEvents().trigger("walk");
        Vector2 currentPosition = owner.getEntity().getPosition();
        Vector2 randomDirection = getRandomDirectionVector();
        Vector2 targetPosition = currentPosition.cpy().add(randomDirection);
        if (movementTask == null) {
            movementTask = new MovementTask(targetPosition);
            movementTask.create(owner);
        } else {
            movementTask.setTarget(targetPosition);
        }
        movementTask.start();
        movementTask.setVelocity(wanderSpeed);
    }

    @Override
    public void stop() {
        logger.debug("Stopping wander task");
        super.stop();
        movementTask.stop();
    }

    /**
     * Generates a random direction vector ensuring it's not near the current or opposite direction.
     *
     * @return A valid random direction vector.
     */
    private Vector2 getRandomDirectionVector() {
        float newAngle = MathUtils.random(0, 360);
        int attempts = 0;
        while (!isValidDirection(newAngle)) {
            newAngle = MathUtils.random(0, 360);
            attempts++;
            if (attempts > 15) {
                // Fallback to any random direction after 15 failed attempts
                logger.warn("Failed to find a valid direction after 15 attempts. Using any random direction.");
                break;
            }
        };

        currentDirectionAngle = newAngle;
        return new Vector2(1, 0).setAngleDeg(currentDirectionAngle).scl(MAX_WANDER_LIMIT);
    }


    /**
     * Checks if the new angle is outside the excluded ranges.
     *
     * @param angle The angle to check.
     * @return True if valid, false otherwise.
     */
    private boolean isValidDirection(float angle) {
        float lowerBound1 = normalizeAngle(currentDirectionAngle - EXCLUSION_RANGE);
        float upperBound1 = normalizeAngle(currentDirectionAngle + EXCLUSION_RANGE);
        float oppositeDirection = normalizeAngle(currentDirectionAngle + 180f);
        float lowerBound2 = normalizeAngle(oppositeDirection - EXCLUSION_RANGE);
        float upperBound2 = normalizeAngle(oppositeDirection + EXCLUSION_RANGE);

        return !isWithinRange(angle, lowerBound1, upperBound1) && !isWithinRange(angle, lowerBound2, upperBound2);
    }

    /**
     * Normalises an angle to be within [0, 360) degrees.
     *
     * @param angle The angle to normalize.
     * @return The normalized angle.
     */
    private float normalizeAngle(float angle) {
        return (angle % 360 + 360) % 360;
    }

    /**
     * Checks if a given angle is within a specified range, accounting for angle wrapping.
     *
     * @param angle The angle to check.
     * @param lower The lower bound of the range.
     * @param upper The upper bound of the range.
     * @return True if within range, false otherwise.
     */
    private boolean isWithinRange(float angle, float lower, float upper) {
        if (lower < upper) {
            return angle >= lower && angle <= upper;
        } else {
            // Range wraps around 360
            return angle >= lower || angle <= upper;
        }
    }

    private void onCollisionStart(Object fixtureA, Object fixtureB) {
        if (movementTask.getStatus() == Status.ACTIVE) {
            logger.debug("Collision detected, changing direction");
            startMoving();
        }
    }
}
