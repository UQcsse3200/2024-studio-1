package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.Map;
import java.util.Set;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class NPCConfigs {
  private final Map<String, NPCConfig> npcConfigs;

  public NPCConfigs(Map<String, NPCConfig> npcConfigs) {
    this.npcConfigs = npcConfigs;
  }

  public NPCConfig getConfig(String npcType) {
    return npcConfigs.get(npcType.toLowerCase());
  }

  public Set<String> getNpcTypes() {
    return npcConfigs.keySet();
  }

  public static class NPCConfig extends BaseEntityConfig {

    public NPCConfig() {
    }

    public TaskConfig tasks = new TaskConfig();
    public AttackConfig attacks = new AttackConfig();
    public AnimationData[] animations = new AnimationData[0];
    public String name;
    public boolean isBoss = false;
    public boolean isDirectional;
    public int strength;

    public int getStrength() {
      return strength;
    }

    public static class AttackConfig {
      public MeleeAttack melee = null;
      public RangeAttack ranged = null;
      public AOEAttack aoe = null;

      public static class MeleeAttack {
        public float range;
        public float rate;
        public EffectConfig[] effects = new EffectConfig[0];
      }

      public static class RangeAttack {
        public float range;
        public float rate;
        public int type;
        public EffectConfig[] effects = new EffectConfig[0];
      }
      public static class AOEAttack {
        public float range;
        public float rate;
        public EffectConfig[] effects = new EffectConfig[0];
      }
    }

    public static class EffectConfig {
      public String type;
      public float force; // For knockback
      public float duration; // For stun or poison
      public int damagePerSecond; // For poison
    }

    public static class AnimationData {
      public String name;
      public float frameDuration;
      public Animation.PlayMode playMode;
    }
  }
}
