package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.physics.PhysicsLayer;

public class ProjectileConfigs {
    //public ProjectileConfig baseProjectile = new BaseProjectileConfig();
    public BaseProjectileConfig dragonProjectile = new BaseProjectileConfig();
    public BaseProjectileConfig kitsuneProjectile = new BaseProjectileConfig();
    public BaseProjectileConfig cthuluProjectile = new BaseProjectileConfig();

    public static class BaseProjectileConfig extends BaseEntityConfig {
        public short Layer = PhysicsLayer.PLAYER;
        public Vector2 speed =  new Vector2(10f, 10f);
        public float scaleX = 0.5f;
        public float scaleY = 0.5f;
        public ProjectileAnimations[] animations = new ProjectileAnimations[0];
        public boolean isDirectional;

        public static class ProjectileAnimations {
            public String name;
            public float frameDuration;
            public Animation.PlayMode playMode;
        }
    }
}

