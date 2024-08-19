package com.csse3200.game.entities.configs;

/**
 * Defines crocodile's properties stored in entities config files to be loaded by Entity Factories.
 */
public class CrocodileConfig extends BaseEntityConfig {
    public int health = 50;
    public int speed = 5;
    public int baseAttack = 100;
    public int attackPersecond = 1;
    public int burnAttack = 0;
    public int burnDuration = 0;
}
