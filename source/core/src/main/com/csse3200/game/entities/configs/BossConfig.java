package com.csse3200.game.entities.configs;

/**
 * Defines boss's properties stored in entities config files to be loaded by Entity Factories.
 */
public class BossConfig extends BaseEntityConfig {
    public int health = 100;
    public int speed = 15;
    public int baseAttack = 25;
    public int attackPersecond = 1;
    public int burnAttack = 3;
    public int burnDuration = 3;
}
