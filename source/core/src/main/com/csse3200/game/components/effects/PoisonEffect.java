package com.csse3200.game.components.effects;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.CombatStatsComponent;

/**
 * Poison effect that can be applied to an entity.
 */
public class PoisonEffect implements Effect {
    private final int damagePerSecond;
    private float duration;
    private float timeElapsed;

    public PoisonEffect(int damagePerSecond, float duration) {
        this.damagePerSecond = damagePerSecond;
        this.duration = duration;
        this.timeElapsed = 0;
    }

    @Override
    public void apply(Entity target) {
        target.getEvents().trigger("poisoned");
    }

    @Override
    public void update(Entity target, float deltaTime) {
        timeElapsed += deltaTime;
        CombatStatsComponent stats = target.getComponent(CombatStatsComponent.class);
        if (stats != null) {
            // Apply damage every second
            if (timeElapsed >= 1.0f) {
                stats.addHealth(-damagePerSecond);
                target.getEvents().trigger("poisonDamage", damagePerSecond);
                timeElapsed -= 1.0f;
            }
        }
        if (duration > 0) {
            duration -= deltaTime;
        }
    }

    @Override
    public void refresh(Effect newEffect) {
        if (newEffect instanceof PoisonEffect poison) {
            this.duration = Math.max(this.duration, poison.duration);
            this.timeElapsed = 0;
        }
    }

    @Override
    public void remove(Entity target) {
        target.getEvents().trigger("poisonExpired");
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public EffectType getType() {
        return EffectType.POISON;
    }

    @Override
    public float getDuration() {
        return duration;
    }
}