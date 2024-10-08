package com.csse3200.game.entities.configs;

import com.csse3200.game.components.effects.EffectType;

/**
 * Defines the configuration for NPC attacks.
 */
public class AttackConfig {
    public MeleeAttack melee = null;
    public RangeAttack ranged = null;
    public AOEAttack aoe = null;

    /**
     * Configuration for a melee attack.
     */
    public static class MeleeAttack {
        public float range;
        public float rate;
        public EffectConfig[] effects = new EffectConfig[0];
    }

    /**
     * Configuration for a ranged attack.
     */
    public static class RangeAttack {
        public float range;
        public float rate;
        public int type;
        public EffectConfig[] effects = new EffectConfig[0];
    }

    /**
     * Configuration for an AOE attack.
     */
    public static class AOEAttack {
        public float range;
        public float rate;
        public EffectConfig[] effects = new EffectConfig[0];
    }

    /**
     * Configuration for an attack's effect.
     */
    public static class EffectConfig {
        public String type;
        public float force; // For knockback
        public float duration; // For stun or poison
        public int damagePerSecond; // For poison

        public EffectType getEffectType() {
            return switch (type.toLowerCase()) {
                case "knockback" -> EffectType.KNOCKBACK;
                case "poison" -> EffectType.POISON;
                case "stun" -> EffectType.STUN;
                default -> throw new IllegalArgumentException("Unknown effect type: " + type);
            };
        }
    }
}
