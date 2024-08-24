package com.csse3200.game.entities.configs;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import com.csse3200.game.entities.Entity;

import java.util.*;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public String equipped;
  public int gold = 1;
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
  /** The specification of player's equiped ranged weapon */
  public String ranged;


  public PlayerConfig(){
    this.equipped = "melee";
  }

  /**
   * Checks if two players are teh same based on their attributes
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
    return gold == config.gold &&
            baseAttack == config.baseAttack &&
            health == config.health &&
            Objects.equals(equipped, config.equipped) &&
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
    int result = Objects.hashCode(equipped);
    result = 31 * result + gold;
    result = 31 * result + baseAttack;
    result = 31 * result + Objects.hashCode(favouriteColour);
    result = 31 * result + Arrays.hashCode(items);
    result = 31 * result + health;
    result = 31 * result + Objects.hashCode(melee);
    result = 31 * result + Objects.hashCode(ranged);
    return result;
  }
}