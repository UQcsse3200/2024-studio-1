package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task for an NPC to perform an AOE attack with a preparation phase (charge-up animation).
 */
public class AOEAttackTask extends DefaultTask implements PriorityTask {
    private static final int ACTIVE_PRIORITY = 9;
    private static final int INACTIVE_PRIORITY = 7;
    private final Entity target;
    private final float activationMinRange;
    private final float activationMaxRange;
    private final float cooldownTime;
    private final float preparationTime;
    private GameTime gameTime;
    private long lastExecutionTime;
    private long preparationStartTime;
    private boolean isPreparing = false;
    private AOEAttackComponent aoeAttackComponent;
    private WaitTask waitTask;

    public AOEAttackTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.AOEAttackTaskConfig config) {
        this.target = target;
        this.activationMinRange = config.activationMinRange;
        this.activationMaxRange = config.activationMaxRange;
        this.cooldownTime = config.cooldownTime;
        this.preparationTime = config.preparationTime;
        gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        aoeAttackComponent = owner.getEntity().getComponent(AOEAttackComponent.class);
        waitTask = new WaitTask(3);
        isPreparing = true;
        preparationStartTime = gameTime.getTime();

        // Trigger the charge up animation
        owner.getEntity().getEvents().trigger("aoe_preparation");
        owner.getEntity().getEvents().trigger("gesture");
    }

    @Override
    public void update() {
        if (isPreparing) {
            long elapsedTime = gameTime.getTimeSince(preparationStartTime);
            if (elapsedTime >= preparationTime * 1000) {
                isPreparing = false;
                owner.getEntity().getEvents().trigger("attack");
                aoeAttackComponent.enableForNumAttacks(1);
                waitTask.start();
            }
        } else if (waitTask.getStatus() == Status.ACTIVE) {
            waitTask.update();
        } else if (waitTask.getStatus() == Status.FINISHED) {
            this.stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        lastExecutionTime = gameTime.getTime();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return ACTIVE_PRIORITY;
        }
        float dst = owner.getEntity().getPosition().dst(target.getPosition());
        if (isCooldownComplete() && dst >= activationMinRange && dst <= activationMaxRange) {
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