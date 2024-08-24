package com.csse3200.game.entities.configs;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileConfigTest {
    @Test
    void getSpeed()
    {
        ProjectileConfig projectileConfig = new ProjectileConfig();
        assertEquals(new Vector2(3f, 3f), projectileConfig.getSpeed());

    }

    @Test
    void getScale()
    {
        ProjectileConfig projectileConfig = new ProjectileConfig();
        assertEquals(0.6f, projectileConfig.getScaleX());
        assertEquals(0.3f, projectileConfig.getScaleY());
    }
}