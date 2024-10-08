package com.csse3200.game.entities.factories;

import com.csse3200.game.components.effects.Effect;
import com.csse3200.game.components.effects.KnockbackEffect;
import com.csse3200.game.components.effects.PoisonEffect;
import com.csse3200.game.components.effects.StunEffect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;

public class EffectFactory {
    public static Effect createEffect(AttackConfig.EffectConfig config, Entity sourceEntity) {
        return switch (config.type.toLowerCase()) {
            case "knockback" -> new KnockbackEffect(sourceEntity, config.force);
            case "poison" -> new PoisonEffect(config.damagePerSecond, config.duration);
            case "stun" -> new StunEffect(config.duration);
            default -> throw new IllegalArgumentException("Unknown effect type: " + config.type);
        };
    }
}