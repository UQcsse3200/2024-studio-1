package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.configs.PlayerConfig;

/**
 * Generates a PlayerConfig object based on current state of player’s entity by
 * extracting combat stats, inventory and equipped weapons. This converts them
 * into a configuration format that can be saved and loaded as needed.
 */
public class PlayerConfigGenerator {
    /**
     * Generates a player config object based on current state of given entity.
     *
     * @param player the entity player whose state needs to be saved
     *
     * @return returns a player configuration that can be saved
     */
    public PlayerConfig savePlayerState(Entity player) {
        PlayerConfig config = player.getComponent(PlayerConfigComponent.class).getPlayerConfig();

        // obtain the stats and inventory components of the player
        CombatStatsComponent statsComponent = player.getComponent(CombatStatsComponent.class);

        config.health = statsComponent.getHealth();
        config.baseAttack = statsComponent.getBaseAttack();
        config.coins = player.getComponent(CoinsComponent.class).getCoins();
        config.speed = player.getComponent(PlayerActions.class).getCurrSpeed();

        // store the string representation of items player has collected
        InventoryComponent inventoryComponent = player.getComponent(InventoryComponent.class);
        config.items = itemsToString(inventoryComponent.getInventory().getItems());

        // obtain the specification of melee of player, if any
        config.melee = inventoryComponent.getInventory().getMelee()
                .map(MeleeWeapon::getSpecification)
                .orElse("");

        //obtain the specification of ranged weapon of player, if any
        config.ranged = inventoryComponent.getInventory().getRanged()
                .map(RangedWeapon::getSpecification)
                .orElse("");

        return config;
    }


    /**
     * A helper method to convert Array of Collectibles to their string format,
     * storing their details
     *
     * @param items array of items to store
     *
     * @return an array of strings containing specification of all items
     */
    private String[] itemsToString(Array<Collectible> items) {
        String[] allItems = new String[items.size];
        for (int i = 0; i < items.size; i++) {
            allItems[i] = items.get(i).getSpecification();
        }
        return allItems;
    }
}

