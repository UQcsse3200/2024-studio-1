package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task for an NPC to perform a given number of range attacks on a target entity.
 */
public class RangeAttackTask extends DefaultTask implements PriorityTask {
    private static final int ACTIVE_PRIORITY = 9;
    private static final int SINGLE_INACTIVE_PRIORITY = 5;
    private static final int SPREAD_INACTIVE_PRIORITY = 6;
    private final float activationMinRange;
    private final float activationMaxRange;
    private final int attackNum;
    private final Entity target;
    private final String attackType;
    private RangeAttackComponent rangeAttackComponent;
    private final GameTime gameTime;
    private long lastExecutionTime;
    private final float cooldownTime;

    public RangeAttackTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.RangeAttackTaskConfig config,
                           String attackType) {
        this.target = target;
        this.activationMinRange = config.activationMinRange;
        this.activationMaxRange = config.activationMaxRange;
        this.attackNum = config.attackNum;
        this.cooldownTime = config.cooldownTime;
        this.attackType = attackType;
        gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        rangeAttackComponent = owner.getEntity().getComponent(RangeAttackComponent.class);
        // Configure the range attack component as needed
        rangeAttackComponent.setType(attackType);
        rangeAttackComponent.enableForNumAttacks(attackNum);
        rangeAttackComponent.setEnabled(true);
    }

    @Override
    public void update() {
        // Check if the range attack is complete
        if (!rangeAttackComponent.isEnabled()) {
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        rangeAttackComponent.setEnabled(false);
        lastExecutionTime = gameTime.getTime();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return ACTIVE_PRIORITY;
        }
        float distanceToTarget = owner.getEntity().getPosition().dst(target.getPosition());
        if (isCooldownComplete() && distanceToTarget >= activationMinRange && distanceToTarget <= activationMaxRange) {
            if (attackType.equals("single")) {
                return SINGLE_INACTIVE_PRIORITY;
            } else if (attackType.equals("spread")) {
                return SPREAD_INACTIVE_PRIORITY;
            }
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