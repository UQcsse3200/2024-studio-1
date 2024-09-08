package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BossAttackTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(BossAttackTask.class);
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private final float chaseSpeed;
    private final float chargeSpeed;
    private final float waitTime;

    private PriorityTask chaseTask;
    private PriorityTask chargeTask;
    private PriorityTask waitTask;
    private PriorityTask currentTask;

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
        chaseTask = new ChaseTask(target, priority, viewDistance, maxChaseDistance, chaseSpeed);
        chargeTask = new ChargeTask(target, priority + 1, viewDistance / 2, maxChaseDistance, chargeSpeed, 0);
        waitTask = new WaitTask(waitTime, priority + 2);

        chaseTask.create(owner);
        chargeTask.create(owner);
        waitTask.create(owner);

        currentTask = chaseTask;
        currentTask.start();
    }

    @Override
    public void update() {
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition());

        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == chaseTask && distanceToTarget <= viewDistance / 2) {
                logger.debug("Switching to charge task");
                switchToTask(chargeTask);
            } else if (currentTask == chargeTask) {
                logger.debug("Switching to wait task");
                switchToTask(waitTask);
            } else if (currentTask == waitTask) {
                logger.debug("Switching to chase task");
                switchToTask(chaseTask);
            }
        }
        currentTask.update();
    }

    private void switchToTask(PriorityTask newTask) {
        currentTask.stop();
        currentTask = newTask;
        currentTask.start();
    }

    @Override
    public void stop() {
        super.stop();
        if (currentTask != null) {
            currentTask.stop();
        }
    }

    @Override
    public int getPriority() {
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition());
        if (distanceToTarget <= maxChaseDistance) {
            return priority;
        }
        return -1;
    }
}