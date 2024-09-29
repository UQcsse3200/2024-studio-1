package com.csse3200.game.entities.configs;

public class BossConfig extends NPCConfigs.NPCConfig {
    public float waitTime;
    public float chaseTime;
    public float wanderMinRange;
    public float chaseMinRange;
    public float chaseMaxRange;
    public float chargeMinRange;
    public float chargeMaxRange;
    public float retreatMinRange;
    public float retreatMaxRange;
    public float jumpMinRange;
    public float jumpMaxRange;
    public float aoeMinRange;
    public float aoeMaxRange;
    public float rangedMinRange;
    public float rangedMaxRange;
    public int rangedAttackNum;
    public float ranged2MinRange;
    public float ranged2MaxRange;
    public int ranged2AttackNum;
    public String fallbackState;

    public void setDefaults() {
        // Default task values specific to bosses
        this.tasks.wander.wanderRadius = 3.0f;
        this.tasks.wander.waitTime = 2.0f;
        this.tasks.wander.wanderSpeed = 1.0f;

        this.tasks.chase.priority = 10;
        this.tasks.chase.viewDistance = 20.0f;
        this.tasks.chase.chaseDistance = 20.0f;

        this.tasks.charge.priority = 10;
        this.tasks.charge.viewDistance = 20.0f;
        this.tasks.charge.chaseDistance = 20.0f;
        this.tasks.charge.waitTime = 1f;

        this.tasks.runAway.priority = 10;
        this.tasks.runAway.stopDistance = 4.0f;
        this.tasks.runAway.maxRunTime = 5.0f;
    }
}
