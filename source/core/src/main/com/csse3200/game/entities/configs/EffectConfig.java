package com.csse3200.game.entities.configs;

import com.csse3200.game.components.effects.EffectType;

public class EffectConfig {
    public EffectType type;
    public float force; // For knockback
    public float duration; // For stun or poison
    public int damagePerSecond; // For poison
}
