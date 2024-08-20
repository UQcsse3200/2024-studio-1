package com.csse3200.game.entities.factories;

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

    private static final ItemConfigs configs =
            FileLoader.readClass(ItemConfigs.class, "configs/items.json");


    /**
     * Creates a medKit entity
     * @return medKit entity
     */
    public static Entity createMedKit() {
        Entity medKit = createBaseItem();
        HealthItemConfig config = configs.medKit;

        medKit.addComponent(new TextureRenderComponent("images/items/med_kit.png"));
        return medKit;
    }

    /**
     * Creates a bandAid entity
     * @return bandAid entity
     */
    public static Entity createBandage() {
        Entity bandage = createBaseItem();
        HealthItemConfig config = configs.bandage;

        bandage.addComponent(new TextureRenderComponent("images/items/bandage.png"));
        return bandage;
    }

    /**
     * Creates an energyDrink entity
     * @return energyDrink entity
     */
    public static Entity createEnergyDrink() {
        Entity energyDrink = createBaseItem();
        SpeedBoostConfig config = configs.energyDrink;

        energyDrink.addComponent(new TextureRenderComponent("images/items/energy_drink.png"));
        return energyDrink;
    }

    /**
     * Creates a shieldPotion entity
     * @return shieldPotion entity
     */
    public static Entity createShieldPotion() {
        Entity shieldPotion = createBaseItem();
        ShieldItemConfig config = configs.shieldPotion;

        shieldPotion.addComponent(new TextureRenderComponent("images/items/shield_potion.png"));
        return shieldPotion;
    }


    /**
     * Creates a generic Item to be used as a base entity by more specific Item creation methods.
     * NB: This is where we add more components
     * @return item entity
     */
    private static Entity createBaseItem() {
        Entity item = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT));

        return item;
    }

}
