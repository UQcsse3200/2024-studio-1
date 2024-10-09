package com.csse3200.game.components.effects;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.player.PlayerActions;

/**
 * Stun effect that can be applied to an entity.
 */
public class StunEffect implements Effect {
    private float duration;
    private float timeElapsed;

    public StunEffect(float duration) {
        this.duration = duration;
        this.timeElapsed = 0;
    }

    @Override
    public void apply(Entity target) {
        target.getEvents().trigger("stunned");
        target.getComponent(PlayerActions.class).setEnabled(false);
    }

    @Override
    public void update(Entity target, float deltaTime) {
        timeElapsed += deltaTime;
        if (timeElapsed >= duration) {
            remove(target);
        }
    }

    @Override
    public void refresh(Effect newEffect) {
        if (newEffect instanceof StunEffect stun) {
            this.duration = Math.max(this.duration - this.timeElapsed, stun.duration);
            this.timeElapsed = 0;
        }
    }

    @Override
    public void remove(Entity target) {
        target.getEvents().trigger("stunExpired");
        target.getComponent(PlayerActions.class).setEnabled(true);
    }

    @Override
    public boolean isExpired() {
        return timeElapsed >= duration;
    }

    @Override
    public EffectType getType() {
        return EffectType.STUN;
    }

    @Override
    public float getDuration() {
        return duration;
    }
}
