package com.csse3200.game.entities.configs;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerConfigTest {


    @Test
    public void testInit(){
        PlayerConfig playerConfig = new PlayerConfig();
        assertEquals(playerConfig.baseAttack, 10);
    }

    @Test
    public void testFromEntity(){
        CombatStatsComponent statsComponent = new CombatStatsComponent(100, 30);
        InventoryComponent inventoryComponent = new InventoryComponent();
        Entity entity = new Entity().addComponent(statsComponent)
                .addComponent(inventoryComponent);

        PlayerConfig actual = new PlayerConfig(entity);
        assertEquals(100, actual.health);
        assertEquals(30, actual.baseAttack);
    }
}