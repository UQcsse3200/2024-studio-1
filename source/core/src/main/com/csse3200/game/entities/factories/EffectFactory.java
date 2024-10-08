package com.csse3200.game.entities.factories;

import com.csse3200.game.components.effects.Effect;
import com.csse3200.game.components.effects.KnockbackEffect;
import com.csse3200.game.components.effects.PoisonEffect;
import com.csse3200.game.components.effects.StunEffect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EffectConfig;

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
    public static Effect createEffect(EffectConfig config, Entity sourceEntity) {

        return switch (config.type) {
            case STUN -> new StunEffect(config.duration);
            case POISON -> new PoisonEffect(config.damagePerSecond, config.duration);
            case KNOCKBACK -> new KnockbackEffect(sourceEntity, config.force);
        };
    }
}