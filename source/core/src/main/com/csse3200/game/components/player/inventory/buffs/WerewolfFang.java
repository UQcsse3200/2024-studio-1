package com.csse3200.game.components.player.inventory.buffs;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.BuffItem;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.entities.Entity;

/**
 * The bleed item reduces 5% health of enemies per second for 5 seconds.
 * The bleed item is a boss drop after killing the werewolf
 * The weapons on which the bleed effect is available is axe and knife
 */
public class WerewolfFang extends BuffItem {

  // Percentage of health to reduce per second
  private final int bleedDamage = 5;

  /**
   * Returns the name of the item.
   *
   * @return the name of the item, "Bleed Buff".
   */
  @Override
  public String getName() {
    return "Bleed Buff";
  }

  /**
   * Returns the icon representing the BleedBuff item.
   *
   * @return a Texture object for the bleed icon.
   */
  @Override
  public Texture getIcon() {
    return new Texture("images/items/werewolf_fang.png");
  }

  /**
   * Drops the BleedBuff item from the player's inventory.
   *
   * @param inventory The inventory from which the item is dropped.
   */

  @Override
  public void drop(Inventory inventory) {}

  /**
   * Returns a string that specifies the type of buff.
   *
   * @return the buff specification as a string, "bleedbuff".
   */
  @Override
  public String getBuffSpecification() {
    return "bleedbuff";
  }

  /**
   * Returns the percentage of health reduced per second by the bleed effect.
   *
   * @return the health reduction percentage (5%).
   */
  public int getBleedDamage() {
    return bleedDamage;
  }

  /**
   * Applies the bleed effect to an entity. The player must hit an enemy with a weapon
   * that can apply the bleed effect. The method listens for the
   * "BleedWeaponHit" event to apply the effect.
   *
   * @param entity The player entity that will apply the bleed effect.
   */

  @Override
  public void effect(Entity entity) {
    entity.getComponent(CombatStatsComponent.class).updateBleedStatus();
    entity.getComponent(CombatStatsComponent.class).updateBleedDamage(bleedDamage);
  }

}
