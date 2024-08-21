package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 * Need `attackComponent` to replace `movementComponent`
 */
public class AttackTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

    private final GameTime gameTime;
    private Vector2 target;
    private long lastTimeAttacked;
    private float attackRange = 0.5f;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;

    public AttackTask(Vector2 target) {
        this.target = target;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    public AttackTask(Vector2 target, int attackRange) {
        this(target);
        this.attackRange = attackRange;
    }

    @Override
    public void start() {
        super.start();
        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        movementComponent.setTarget(target);
        movementComponent.setMoving(true);
        logger.debug("Starting attacking towards {}", target);
        lastTimeAttacked = gameTime.getTime();
        lastPos = owner.getEntity().getPosition();
    }

    @Override
    public void update() {
        if (isAtTarget()) {
            movementComponent.setMoving(false);
            status = Status.FINISHED;
            logger.debug("Finished attacking {}", target);
        } else {
            checkIfStuck();
        }
    }

    public void setTarget(Vector2 target) {
        this.target = target;
        movementComponent.setTarget(target);
    }

    public void setDamage(float damage) {
        // Require further changes from movement to attack
        Vector2 dam = new Vector2(damage,damage);
        logger.debug("Setting velocity to {}", damage);
        movementComponent.setVelocity(dam);
    }

    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.debug("Stopping movement");
    }

    private boolean isAtTarget() {
        return owner.getEntity().getPosition().dst(target) <= attackRange;
    }

    private void checkIfStuck() {
        if (didMove()) {
            lastTimeAttacked = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
        } else if (gameTime.getTimeSince(lastTimeAttacked) > 500L) {
            movementComponent.setMoving(false);
            status = Status.FAILED;
            logger.debug("Got stuck! Failing movement task");
        }
    }

    private boolean didMove() {
        return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
    }
}
