package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RatAttackTask extends ChargeTask {
    private static final Logger logger = LoggerFactory.getLogger(RatAttackTask.class);

    public RatAttackTask(Entity target, int priority, float attackDistance, float maxAttackDistance) {
        super(target, priority, attackDistance, maxAttackDistance, 5f);
    }

    @Override
    public void start() {
        super.start();
        initialiseTasks();
        startMoving();
        this.owner.getEntity().getEvents().trigger("attack");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                this.owner.getEntity().getEvents().trigger("attackEnd");
                startWaiting();
            } else {
                this.owner.getEntity().getEvents().trigger("attackStart");
                startMoving();
            }
        }
        currentTask.update();
    }

    private void startMoving() {
        logger.debug("Starting attacking towards {}", target.getPosition());
        movementTask.setTarget(target.getPosition());
        swapTask(movementTask);
    }
}