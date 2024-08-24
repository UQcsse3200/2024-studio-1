package com.csse3200.game.entities.configs;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.physics.PhysicsLayer;

public class ProjectileConfig extends BaseEntityConfig{
    public Vector2 speed =  new Vector2(3f, 3f);
    public float scaleX = 0.6f;
    public float scaleY = 0.3f;
    public String projectileTexturePath = "images/ghost.atlas";
    public short Layer = PhysicsLayer.PLAYER;
    public int weaponID = 0;

    public Vector2 getSpeed() {return speed.cpy();}
    public float getScaleX() {return scaleX;}
    public float getScaleY() {return scaleY;}

}
