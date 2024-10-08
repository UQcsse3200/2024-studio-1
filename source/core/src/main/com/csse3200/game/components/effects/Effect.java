package com.csse3200.game.components.effects;

import com.csse3200.game.entities.Entity;

/**
 * Interface for effects that can be applied to an entity.
 */
public interface Effect {
    /**
     * Applies the effect to the target entity.
     *
     * @param target The entity to apply the effect to.
     */
    void apply(Entity target);

    /**
     * Updates the effect's state.
     *
     * @param target    The entity the effect is applied to.
     * @param deltaTime The time elapsed since the last update.
     */
    void update(Entity target, float deltaTime);

    /**
     * Removes the effect from the target entity.
     *
     * @param target The entity to remove the effect from.
     */
    void remove(Entity target);

    /**
     * Refreshes the effect's state based on a new effect instance.
     *
     * @param newEffect The new effect instance to refresh with.
     */
    void refresh(Effect newEffect);

    /**
     * Checks if the effect has expired.
     *
     * @return True if the effect has expired, false otherwise.
     */
    boolean isExpired();

    /**
     * Returns the type of the effect.
     *
     * @return EffectType enum.
     */
    EffectType getType();
}
