package com.csse3200.game.entities.configs;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  //public LinkedHashMap<String, WeaponType> slotTypeMap;
  public String equipped;
  public int gold = 1;
  public int baseAttack = 10;
  public String favouriteColour = "none";
  public String[] items;

  public int health = 100;
  public String melee;
  public String ranged;


  public PlayerConfig(Entity player) {
    CombatStatsComponent statsComponent = player.getComponent(CombatStatsComponent.class);
    InventoryComponent inventoryComponent = player.getComponent(InventoryComponent.class);

    this.health = statsComponent.getHealth();
    this.baseAttack = statsComponent.getBaseAttack();
    this.items = new String[inventoryComponent.getInventory().getItems().size];
    this.items = itemsToString(inventoryComponent.getInventory().getItems());
    this.melee = "";

  }

  private String[] itemsToString(Array<Collectible> items) {
    String[] itemNames = new String[items.size];
    for (int i = 0; i < items.size; i++) {
      itemNames[i] = items.get(i).getSpecification();
    }
    return itemNames;
  }


  public PlayerConfig(){
    //slotTypeMap = new LinkedHashMap<>("melee", WeaponType.MELEE);
    this.equipped = "melee";
  }

}
