package com.csse3200.game.components.tasks;

import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackTask extends ChargeTask {
    private static final Logger logger = LoggerFactory.getLogger(AttackTask.class);

    public AttackTask(Entity target, int priority, float attackDistance, float maxAttackDistance) {
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