package com.csse3200.game.entities.configs;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.player.inventory.Collectible;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int gold = 1;
  public String favouriteColour = "none";
  public Array<Collectible> items = new Array<>();
  public int health = 0;

}
