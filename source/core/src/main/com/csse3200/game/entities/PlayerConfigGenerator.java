package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
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
     * @return returns a player configuration that can be saved
     */
    public PlayerConfig savePlayerState(Entity player) {
        PlayerConfig config = player.getComponent(PlayerConfigComponent.class).getPlayerConfig();

        // obtain the stats and inventory components of the player
        CombatStatsComponent statsComponent = player.getComponent(CombatStatsComponent.class);

        config.health = statsComponent.getHealth();
        config.baseAttack = statsComponent.getBaseAttack();
        config.coins = player.getComponent(CoinsComponent.class).getCoins();
        config.maxHealth = statsComponent.getMaxHealth();
        config.armour = statsComponent.getArmor();
        config.buff = statsComponent.getDamageBuff();
        config.canCrit = statsComponent.getCanCrit();
        config.critChance = statsComponent.getCritChance();
        // store the string representation of items player has collected
        InventoryComponent inventoryComponent = player.getComponent(InventoryComponent.class);
        config.items = collectibleToString(new Array<>(inventoryComponent.getItems()));
        config.pets = collectibleToString(new Array<>(inventoryComponent.getPets()));
        config.buffs = collectibleToString(new Array<>(inventoryComponent.getBuffs()));

        // obtain the specification of melee of player, if any
        config.melee = inventoryComponent.getOffhand()
                .map(OffHandItem::getSpecification)
                .orElse("");

        //obtain the specification of ranged weapon of player, if any
        config.ranged = inventoryComponent.getMainWeapon()
                .map(MainHandItem::getSpecification)
                .orElse("");

        return config;
    }


    /**
     * A helper method to convert Array of Collectibles to their string format,
     * storing their details
     *
     * @param collectibles array of items to store
     * @return an array of strings containing specification of all items
     */
    private String[] collectibleToString(Array<Collectible> collectibles) {
        String[] allItems = new String[collectibles.size];
        for (int i = 0; i < collectibles.size; i++) {
            allItems[i] = collectibles.get(i).getSpecification();
        }
        return allItems;
    }
}

