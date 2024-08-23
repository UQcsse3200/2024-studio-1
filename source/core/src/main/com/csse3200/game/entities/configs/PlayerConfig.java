package com.csse3200.game.entities.configs;

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


  public PlayerConfig(){
    //slotTypeMap = new LinkedHashMap<>("melee", WeaponType.MELEE);
    this.equipped = "melee";
  }

}
