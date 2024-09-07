package com.csse3200.game.components.npc.attack.attackeffects;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;

public class PoisonEffect implements Effect {
    private int damagePerSecond;
    private float duration;
    private float timeElapsed;
    private CombatStatsComponent dummyAttacker;

    public PoisonEffect(int damagePerSecond, float duration) {
        this.damagePerSecond = damagePerSecond;
        this.duration = duration;
        this.timeElapsed = 0;
        this.dummyAttacker = new CombatStatsComponent(1, damagePerSecond);
    }

    @Override
    public void apply(Entity target) {
        // Initial application logic, if needed
    }

    @Override
    public void update(Entity target, float deltaTime) {
        timeElapsed += deltaTime;
        if (timeElapsed >= 1) {
            CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
            if (targetStats != null) {
                targetStats.hit(dummyAttacker);
            }
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