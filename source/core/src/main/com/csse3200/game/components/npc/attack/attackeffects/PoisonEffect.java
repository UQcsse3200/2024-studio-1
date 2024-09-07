package com.csse3200.game.components.npc.attack.attackeffects;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;

public class PoisonEffect implements Effect {
    private int damagePerSecond;
    private float duration;
    private float timeElapsed;

    public PoisonEffect(int damagePerSecond, float duration) {
        this.damagePerSecond = damagePerSecond;
        this.duration = duration;
        this.timeElapsed = 0;
    }

    @Override
    public void apply(Entity target) {
    }

    @Override
    public void update(Entity target, float deltaTime) {
        // Apply damage over time
        timeElapsed += deltaTime;
        if (timeElapsed >= 1) {
            target.getComponent(CombatStatsComponent.class).takeDamage(damagePerSecond);
            timeElapsed = 0;
        }
        duration -= deltaTime;
    }

    @Override
    public void remove(Entity entity) {
        // Clean up any visuals or status markers
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}