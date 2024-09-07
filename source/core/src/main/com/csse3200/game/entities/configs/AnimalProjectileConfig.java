package com.csse3200.game.entities.configs;

import com.csse3200.game.physics.PhysicsLayer;

public class AnimalProjectileConfig extends ProjectileConfig {
    public AnimalProjectileConfig () {
        Layer = PhysicsLayer.PLAYER;
    }
}
