package com.csse3200.game.components.player;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {

  @Test
  public void baseTest(){
    InventoryComponent inventoryComponent = new InventoryComponent();
    assertTrue(inventoryComponent.getItems().isEmpty());
    assertEquals(inventoryComponent.getMelee(), null);
    assertEquals(inventoryComponent.getRanged(), null);
  }

  @Test
  public void baseTest(){
    InventoryComponent inventoryComponent = new InventoryComponent();
    assertTrue(inventoryComponent.getItems().isEmpty());
    assertEquals(inventoryComponent.getMelee(), null);
    assertEquals(inventoryComponent.getRanged(), null);
  }

}
