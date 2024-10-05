package com.csse3200.game.entities.configs;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.physics.PhysicsLayer;


public class ProjectileConfig {
    public int health = 0;
    public int baseAttack = 10;
    public Vector2 speed =  new Vector2(10f, 10f);
    public float scaleX = 0.5f;
    public float scaleY = 0.5f;
    public String projectileAtlasPath = "images/Projectiles/GreenShoot.atlas";
    public short Layer = PhysicsLayer.NPC;

}