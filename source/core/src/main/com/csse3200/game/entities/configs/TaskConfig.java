package com.csse3200.game.entities.configs;

import com.csse3200.game.components.tasks.TaskType;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines all task configurations for the NPC Factory.
 */
public class TaskConfig {
    public WanderTaskConfig wander = null;
    public StraightWanderTaskConfig straightWander = null;
    public FollowTaskConfig follow = null;
    public ChaseTaskConfig chase = null;
    public ChargeTaskConfig charge = null;
    public JumpTaskConfig jump = null;
    public RunAwayTaskConfig runAway = null;
    public RangeAttackTaskConfig rangeAttack = null;
    public RangeAttackTaskConfig spreadRangeAttack = null;
    public AOEAttackTaskConfig aoeAttack = null;

    /**
     * Get the task configurations for the NPC.
     *
     * @return The task configurations.
     */
    public Map<TaskType, Object> getTaskConfigs() {
        Map<TaskType, Object> configs = new HashMap<>();
        if (wander != null) configs.put(TaskType.WANDER, wander);
        if (straightWander != null) configs.put(TaskType.STRAIGHT_WANDER, straightWander);
        if (follow != null) configs.put(TaskType.FOLLOW, follow);
        if (chase != null) configs.put(TaskType.CHASE, chase);
        if (charge != null) configs.put(TaskType.CHARGE, charge);
        if (jump != null) configs.put(TaskType.JUMP, jump);
        if (runAway != null) configs.put(TaskType.RUN_AWAY, runAway);
        if (rangeAttack != null) configs.put(TaskType.RANGE_ATTACK, rangeAttack);
        if (spreadRangeAttack != null) configs.put(TaskType.SPREAD_RANGE_ATTACK, spreadRangeAttack);
        if (aoeAttack != null) configs.put(TaskType.AOE_ATTACK, aoeAttack);
        return configs;
    }

    /**
     * Wander task configurations for the NPC.
     *
     */
    public static class WanderTaskConfig {
        public float wanderRadius;
        public float waitTime;
        public float wanderSpeed;
    }

    /**
     * Follow task configurations for the NPC.
     *
     */
    public static class FollowTaskConfig {
        public float followRadius;
        public float waitTime;
        public float followDistance;
        public float followSpeed;
    }

    /**
     * Straight wander task configurations for the NPC.
     *
     */
    public static class StraightWanderTaskConfig {
        public float wanderSpeed;
    }

    /**
     * Chase task configurations for the NPC.
     *
     */
    public static class ChaseTaskConfig {
        public float chaseSpeed;
        public float viewDistance;
        public float chaseDistance;
        public float maxTime;
    }

    /**
     * Charge task configurations for the NPC.
     *
     */
    public static class ChargeTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float chaseSpeed;
        public float distanceMultiplier = 1;
        public float waitTime;
        public float cooldownTime;
    }

    /**
     * Jump task configurations for the NPC.
     *
     */
    public static class JumpTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float jumpDuration;
        public float waitTime;
        public float cooldownTime;
    }

    /**
     * Run away task configurations for the NPC.
     *
     */
    public static class RunAwayTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float activationHealth;
        public float runSpeed;
        public float stopDistance;
        public float maxRunTime;
        public float cooldownTime;
    }

    /**
     * Range attack task configurations for the NPC.
     *
     */
    public static class RangeAttackTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public int attackNum;
        public float cooldownTime;
    }

    /**
     * AOE attack task configurations for the NPC.
     *
     */
    public static class AOEAttackTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float preparationTime;
        public float cooldownTime;
    }
}