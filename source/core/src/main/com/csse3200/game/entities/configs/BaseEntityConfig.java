package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 1;
    public int baseAttack = 0;
    public float wanderSpeed = 1f;
    public float chaseSpeed = 1.5f;
    public float viewDistance = 5f;
    public float chaseDistance = 5f;
    public TaskConfig tasks = new TaskConfig();
    public AnimationData[] animations = new AnimationData[0];

    public static class TaskConfig {
        public float wanderSpeed = 1f;
        public float chaseSpeed = 1.5f;
        public float viewDistance = 5f;
        public float chaseDistance = 5f;
        public float attackRange = 1f;
        public float attackInterval = 1f;
    }

    public static class AnimationData {
        public String name;
        public float frameDuration;
        public Animation.PlayMode playMode;
    }
}
