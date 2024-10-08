package com.csse3200.game.entities.factories;

import com.csse3200.game.components.effects.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;

/**
 * Factory to create Effect instances based on configuration.
 */
public class EffectFactory {

    /**
     * Creates an Effect instance based on the provided configuration and source entity.
     *
     * @param config        The effect configuration.
     * @param sourceEntity  The entity applying the effect.
     * @return An instance of Effect.
     */
    public static Effect createEffect(AttackConfig.EffectConfig config, Entity sourceEntity) {
        EffectType effectType = config.getEffectType();

        return switch (effectType) {
            case STUN -> new StunEffect(config.duration);
            case POISON -> new PoisonEffect(config.damagePerSecond, config.duration);
            case KNOCKBACK -> new KnockbackEffect(sourceEntity, config.force);
        };
    }
}