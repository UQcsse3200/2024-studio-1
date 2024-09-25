package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class BossConfigs extends NPCConfigs {
    public BossConfig werewolf = new BossConfig();
    public BossConfig birdman = new BossConfig();

    public static class BossConfig extends NPCConfig {
        public TaskConfig tasks = new TaskConfig();
        public AttackConfig attacks = new AttackConfig();
        public AnimationData[] animations = new AnimationData[0];
        public boolean isDirectional;

        public static class TaskConfig {
            public WanderTaskConfig wander = new WanderTaskConfig();
            public ChaseTaskConfig chase = null;
            public ChargeTaskConfig charge = null;
            public BossAttackTaskConfig bossAttack = null;
            public RunAwayTaskConfig runAway = null;

            public static class WanderTaskConfig {
                public float wanderRadius = 5;
                public float waitTime = 3;
                public float wanderSpeed;
            }

            public static class ChaseTaskConfig {
                public int priority = 1;
                public float chaseSpeed;
                public float viewDistance;
                public float chaseDistance;
            }

            public static class ChargeTaskConfig {
                public int priority = 1;
                public float viewDistance;
                public float chaseDistance;
                public float chaseSpeed;
                public float waitTime;
            }

            public static class BossAttackTaskConfig {
                public int priority = 1;
                public float viewDistance;
                public float chaseDistance;
                public float chaseSpeed;
                public float chargeSpeed;
                public float waitTime;
            }

            public static class RunAwayTaskConfig extends ChargeTaskConfig{
                public int priority = 1;
                public float viewDistance;
                public float maxRunDistance;
                public float runSpeed;
                public float waitTime;
            }
        }

        public static class AttackConfig {
            public MeleeAttack melee = null;
            public RangeAttack ranged = null;

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
