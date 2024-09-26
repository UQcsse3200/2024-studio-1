package com.csse3200.game.entities.configs;

public class BossConfig extends NPCConfigs.NPCConfig {
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
        this.tasks.runAway.viewDistance = 20.0f;
        this.tasks.runAway.chaseDistance = 20.0f;
        this.tasks.runAway.waitTime = 1f;
    }
}
