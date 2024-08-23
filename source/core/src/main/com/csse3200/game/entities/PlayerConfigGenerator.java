package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;

public class PlayerConfigGenerator {

    public PlayerConfig savePlayerState(Entity player) {
        PlayerConfig config = new PlayerConfig();
        CombatStatsComponent statsComponent = player.getComponent(CombatStatsComponent.class);
        InventoryComponent inventoryComponent = player.getComponent(InventoryComponent.class);

        config.health = statsComponent.getHealth();
        config.baseAttack = statsComponent.getBaseAttack();
        config.items = new String[inventoryComponent.getInventory().getItems().size];
        config.items = itemsToString(inventoryComponent.getInventory().getItems());
        return config;
    }


    private String[] itemsToString(Array<Collectible> items) {
        String[] itemNames = new String[items.size];
        for (int i = 0; i < items.size; i++) {
            itemNames[i] = items.get(i).getSpecification();
        }
        return itemNames;
    }
}
