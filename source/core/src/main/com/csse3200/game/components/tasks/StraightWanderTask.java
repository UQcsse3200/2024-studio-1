package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving in a random direction. On collision, the entity changes direction randomly.
 * Requires an entity with a PhysicsMovementComponent.
 */
public class StraightWanderTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(StraightWanderTask.class);
    private static final float MAX_WANDER_LIMIT = 50;
    private final float wanderSpeed;
    private MovementTask movementTask;

    /**
     * @param wanderSpeed The speed an entity wanders at.
     */
    public StraightWanderTask(float wanderSpeed) {
        this.wanderSpeed = wanderSpeed;
    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
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

    private Vector2 getRandomDirectionVector() {
        float angle = MathUtils.random(0, 360);
        return new Vector2(1, 0).setAngleDeg(angle).scl(MAX_WANDER_LIMIT);
    }

    private void onCollisionStart(Object fixtureA, Object fixtureB) {
        logger.debug("Collision detected, changing direction");
        startMoving();
    }
}
