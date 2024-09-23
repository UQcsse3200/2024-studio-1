package com.csse3200.game.entities.configs;

import java.util.*;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  /** This character's name */
  public String name;
  /** Player's base attack by default*/
  public int baseAttack = 10;
  /** Player's favourite colour by default */
  public String favouriteColour = "none";
  /** The items player has collected/picked up during the game */
  public String[] items;

  /** Player's current health */
  public int health = 100;
  /** The specification of player's equipped melee weapon */
  public String melee;
  /** The specification of player's equipped ranged weapon */
  public String ranged;
  public String speed;

  /** The texture this player uses*/
  public String textureFilename;
  /** The texture atlas this player uses*/
  public String textureAtlasFilename;

  /**
   * Checks if two players are the same based on their attributes
   *
   * @param object The object to compare with the player
   *
   * @return True if two objects are the same, otherwise false
   */
  @Override
  public final boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (!(object instanceof PlayerConfig config)) {
      return false;
    }

    // check if all the attributes are the same
    return baseAttack == config.baseAttack &&
            health == config.health &&
            Objects.equals(favouriteColour, config.favouriteColour) &&
            Arrays.equals(items, config.items) &&
            Objects.equals(melee, config.melee) &&
            Objects.equals(ranged, config.ranged);
  }

  /**
   * Generates a hashcode based on player's attributes
   *
   * @return a hashcode for a player
   */
  @Override
  public int hashCode() {
    int result = Objects.hashCode(baseAttack);
    result = 31 * result + Objects.hashCode(favouriteColour);
    result = 31 * result + Arrays.hashCode(items);
    result = 31 * result + health;
    result = 31 * result + Objects.hashCode(melee);
    result = 31 * result + Objects.hashCode(ranged);
    return result;
  }
}
