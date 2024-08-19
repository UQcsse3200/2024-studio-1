package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

  @Test
  public void baseTest(){
    InventoryComponent inventoryComponent = new InventoryComponent();
    Entity entity = new Entity().addComponent(inventoryComponent);
    MeleeWeapon meleeWeapon = new MeleeWeapon() {
      @Override
      public void attack() {
        System.out.println("whack!");
      }

      @Override
      public String getName() {
        return "whacker";
      }

      @Override
      public Texture getIcon() {
        return new Texture("./light.png");
      }
    };

    inventoryComponent.pickup(meleeWeapon);
    assertEquals(meleeWeapon, inventoryComponent.getInventory().getMelee());

    inventoryComponent.getInventory().resetMelee();
    assertNull(inventoryComponent.getInventory().getMelee());
  }

}
