package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.components.npc.attackeffects.Effect;
import com.csse3200.game.components.npc.attackeffects.KnockbackEffect;
import com.csse3200.game.components.npc.attackeffects.PoisonEffect;
import com.csse3200.game.components.npc.attackeffects.StunEffect;

public class EffectFactory {
    public static Effect createEffect(NPCConfigs.NPCConfig.EffectConfig config, Entity sourceEntity) {
        return switch (config.type.toLowerCase()) {
            case "knockback" -> new KnockbackEffect(sourceEntity, config.force);
            case "poison" -> new PoisonEffect(config.damagePerSecond, config.duration);
            case "stun" -> new StunEffect(config.duration);
            default -> throw new IllegalArgumentException("Unknown effect type: " + config.type);
        };
    }
}