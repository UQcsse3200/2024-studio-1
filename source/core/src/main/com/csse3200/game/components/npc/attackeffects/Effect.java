package com.csse3200.game.components.npc.attackeffects;

import com.csse3200.game.entities.Entity;

/**
 * Interface for effects that can be applied to an entity.
 */
public interface Effect {
    void apply(Entity target);
    void update(Entity target, float deltaTime);
    void remove(Entity target);
}
