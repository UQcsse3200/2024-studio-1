package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
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
    private final NPCConfigs.NPCConfig.TaskConfig.ChaseTaskConfig chaseConfig;
    private final NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig chargeConfig;

    private PriorityTask chaseTask;
    private PriorityTask chargeTask;
    private PriorityTask waitTask;
    private PriorityTask currentTask;

    public BossAttackTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.BossAttackTaskConfig config) {
        this.target = target;
        this.priority = config.priority;
        this.viewDistance = config.viewDistance;
        this.maxChaseDistance = config.chaseDistance;
        this.chaseSpeed = config.chaseSpeed;
        this.chargeSpeed = config.chargeSpeed;
        this.waitTime = config.waitTime;
        this.chaseConfig = new NPCConfigs.NPCConfig.TaskConfig.ChaseTaskConfig();
        chaseConfig.priority = priority;
        chaseConfig.viewDistance = viewDistance;
        chaseConfig.chaseDistance = maxChaseDistance;
        chaseConfig.chaseSpeed = chaseSpeed;
        this.chargeConfig = new NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig();
        chargeConfig.priority = priority + 1;
        chargeConfig.viewDistance = viewDistance / 2;
        chargeConfig.chaseDistance = maxChaseDistance;
        chargeConfig.chaseSpeed = chargeSpeed;
        chargeConfig.waitTime = 0;
    }

    @Override
    public void start() {
        super.start();
        chaseTask = new ChaseTask(target, chaseConfig);
        chargeTask = new ChargeTask(target, chargeConfig);
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