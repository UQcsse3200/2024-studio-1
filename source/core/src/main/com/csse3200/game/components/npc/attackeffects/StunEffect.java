package com.csse3200.game.components.npc.attackeffects;

import com.csse3200.game.entities.Entity;

public class StunEffect implements Effect {
    private float duration;
    private float timeElapsed;

    public StunEffect(float duration) {
        this.duration = duration;
        this.timeElapsed = 0;
    }

    @Override
    public void apply(Entity target) {
        // Apply the stun effect
    }

    @Override
    public void update(Entity target, float deltaTime) {
        // Update effect
        timeElapsed += deltaTime;
        if (timeElapsed >= duration) {
            remove(target);
        }
    }

    @Override
    public void remove(Entity target) {
        // Remove the stun effect from the target
    }
}
