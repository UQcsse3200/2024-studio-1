package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.EnergyDrink;
import com.csse3200.game.components.player.inventory.ShieldPotion;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.HealthItemConfig;
import com.csse3200.game.entities.configs.ItemConfigs;
import com.csse3200.game.entities.configs.ShieldItemConfig;
import com.csse3200.game.entities.configs.SpeedBoostConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ItemFactory {

    /**
     * Creates a specified entity
     *
     * @param specification The specification of the item to create.
     * @return the specified entity
     */
    public Collectible create(String specification) {
        return switch (specification){
            case "medkit" -> createMedKit();
            case "bandage" -> createBandage();
            case "energydrink" -> createEnergyDrink();
            case "shieldpotion" -> createShieldPotion();
            default -> throw new IllegalArgumentException("Invalid item specification: " + specification);
        };
    }

    /**
     * Creates a MedKit that restores players health
     * @return medKit
     */
    public UsableItem createMedKit() {
        // return new MedKit();
        return null;
    }

    /**
     * Creates a Bandage that provides minimal health restoration to player
     * @return Bandage
     */
    public UsableItem createBandage() {
        // return new Bandage();
        return null;
    }

    /**
     * Creates an EnergyDrink that increases the players movement speed
     * @return energyDrink
     */
    public Collectible createEnergyDrink() {
        return new EnergyDrink();
    }

    /**
     * Creates a ShieldPotion that provides the player with immunity,
     * negating the next two hits the player takes
     * @return ShieldPotion
     */
    public UsableItem createShieldPotion() {
        return new ShieldPotion();
    }

}
