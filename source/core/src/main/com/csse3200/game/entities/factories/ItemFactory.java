package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.components.player.inventory.ShieldPotion;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.function.Consumer;

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

    private Collectible create(String texturePath, String name, Consumer<Entity> apply){
        return new UsableItem() {
            @Override
            public void apply(Entity entity) {
                apply.accept(entity);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Texture getIcon() {
                return ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
            }

            @Override
            public void drop(Inventory inventory) {
                // do nothing
            }
        };
    }

    /**
     * Creates a MedKit that restores players health
     * @return medKit
     */
    private Collectible createMedKit() {
//        HealthItemConfig config = configs.medKit;

        return create("images/items/med_kit.png", "MedKit", (entity) -> {});
    }

    /**
     * Creates a Bandage that provides minimal health restoration to player
     * @return Bandage
     */
    private Collectible createBandage() {
//        Entity bandage = createBaseItem();
//        HealthItemConfig config = configs.bandage;

        return create("images/items/bandage.png", "Bandage", (entity) -> {});
    }

    /**
     * Creates an EnergyDrink that increases the players movement speed
     * @return energyDrink
     */
    private Collectible createEnergyDrink() {
//        Entity energyDrink = createBaseItem();
//        SpeedBoostConfig config = configs.energyDrink;

        return create("images/items/energy_drink.png", "Energy Drink", (entity) -> {});
    }

    /**
     * Creates a ShieldPotion that provides the player with immunity,
     * negating the next two hits the player takes
     * @return shieldPotion entity
     */
    private Collectible createShieldPotion() {
//        Entity shieldPotion = createBaseItem();
//        ShieldItemConfig config = configs.shieldPotion;

        return new ShieldPotion();
    }
}
