package com.csse3200.game.entities.configs;

public class TaskConfig {
    public WanderTaskConfig wander = null;
    public FollowTaskConfig follow = null;
    public StraightWanderTaskConfig straightWander = null;
    public ChaseTaskConfig chase = null;
    public ChargeTaskConfig charge = null;
    public JumpTaskConfig jump = null;
    public RunAwayTaskConfig runAway = null;
    public RangeAttackTaskConfig rangeAttack = null;
    public RangeAttackTaskConfig spreadRangeAttack = null;
    public AOEAttackTaskConfig aoeAttack = null;

    public static class WanderTaskConfig {
        public float wanderRadius;
        public float waitTime;
        public float wanderSpeed;
    }

    public static class FollowTaskConfig {
        public float followRadius;
        public float waitTime;
        public float followDistance;
        public float followSpeed;
    }

    public static class StraightWanderTaskConfig {
        public float wanderSpeed;
    }

    public static class ChaseTaskConfig {
        public float chaseSpeed;
        public float viewDistance;
        public float chaseDistance;
        public float maxTime;
    }

    public static class ChargeTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float chaseSpeed;
        public float distanceMultiplier = 1;
        public float waitTime;
        public float cooldownTime;
    }

    public static class JumpTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float jumpDuration;
        public float waitTime;
        public float cooldownTime;
    }

    public static class RunAwayTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float activationHealth;
        public float runSpeed;
        public float stopDistance;
        public float maxRunTime;
        public float cooldownTime;
    }

    public static class RangeAttackTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public int attackNum;
        public float cooldownTime;
    }

    public static class AOEAttackTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float preparationTime;
        public float cooldownTime;
    }
}