package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

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
    NPCConfig config = npcConfigs.get(npcType);
    config.name = npcType;
    return config;
  }

  public Set<String> getNpcTypes() {
    return npcConfigs.keySet();
  }

  /**
   * Configuration for an NPC entity.
   */
  public static class NPCConfig extends BaseEntityConfig {
    public String name;
    public int strength;
    public boolean isBoss = false;
    public boolean isDirectional;
    public boolean isScaled = false;
    public Vector2 scale;
    public boolean variableDensity = false;
    public float density;
    public TaskConfig tasks = new TaskConfig();
    public AttackConfig attacks = new AttackConfig();
    public AnimationData[] animations = new AnimationData[0];

    /**
     * Configuration for an animation.
     */
    public static class AnimationData {

      public String name;
      public float frameDuration;
      public Animation.PlayMode playMode;
    }

    /**
     * Configuration for an attack.
     */
    public int getStrength() {
      return strength;
    }
  }
}
