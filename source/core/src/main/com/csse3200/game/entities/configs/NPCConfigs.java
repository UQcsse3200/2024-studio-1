package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class NPCConfigs {
  public NPCConfig rat = new NPCConfig();
  public NPCConfig bear = new NPCConfig();
  public NPCConfig bat = new NPCConfig();
  public NPCConfig dog = new NPCConfig();
  public NPCConfig snake = new NPCConfig();
  public NPCConfig dino = new NPCConfig();
  public NPCConfig minotaur = new NPCConfig();
  public NPCConfig dragon = new NPCConfig();
  public NPCConfig werewolf = new NPCConfig();
  public NPCConfig birdman = new NPCConfig();
  public NPCConfig kitsune = new NPCConfig();
  public NPCConfig ringFire = new NPCConfig();

  public static class NPCConfig extends BaseEntityConfig {

    public NPCConfig() {
    }

    public int strength;

    public int getStrength() {
      return strength;
    }


    public TaskConfig tasks = new TaskConfig();
    public AttackConfig attacks = new AttackConfig();
    public AnimationData[] animations = new AnimationData[0];
    public boolean isDirectional;


    public static class TaskConfig {
      public WanderTaskConfig wander = null;
      public FollowTaskConfig follow = null;
      public StraightWanderTaskConfig straightWander = null;
      public ChaseTaskConfig chase = null;
      public ChargeTaskConfig charge = null;
      public JumpTaskConfig jump = null;
      public RunAwayTaskConfig runAway = null;
      public RangeAttackTaskConfig rangeAttack = null;
      public RangeAttackTaskConfig spreadRangeAttack = null;
      public AOEAttackTaskConfig aoeAttack = null;

      public static class WanderTaskConfig {
        public float wanderRadius;
        public float waitTime;
        public float wanderSpeed;
      }

      public static class FollowTaskConfig{
        public float followRadius;
        public float waitTime;
        public float followDistance;
        public float followSpeed;
      }

      public static class StraightWanderTaskConfig {
        public float wanderSpeed;
      }

      public static class ChaseTaskConfig {
        public float chaseSpeed;
        public float viewDistance;
        public float chaseDistance;
        public float maxTime;
      }

      public static class ChargeTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float chaseSpeed;
        public float distanceMultiplier = 1;
        public float waitTime;
        public float cooldownTime;
      }

      public static class JumpTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float jumpDuration;
        public float waitTime;
        public float cooldownTime;
      }

      public static class RunAwayTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public float activationHealth;
        public float runSpeed;
        public float stopDistance;
        public float maxRunTime;
        public float cooldownTime;
      }

      public static class RangeAttackTaskConfig {
        public float activationMinRange;
        public float activationMaxRange;
        public int attackNum;
        public float cooldownTime;
      }

      public static class AOEAttackTaskConfig {
          public float activationMinRange;
          public float activationMaxRange;
          public float preparationTime;
          public float cooldownTime;
      }
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
        public float radius;
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
