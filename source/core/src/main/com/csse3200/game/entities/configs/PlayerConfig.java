package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int gold = 1;
  public String favouriteColour = "none";
  public String[] items = new String[0];
  public int health = 100;

}
