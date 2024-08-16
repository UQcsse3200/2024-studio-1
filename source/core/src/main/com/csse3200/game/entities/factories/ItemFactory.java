package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.HealthItemConfig;
import com.csse3200.game.entities.configs.ItemConfigs;
import com.csse3200.game.entities.configs.SpeedBoostConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.ColliderComponent;
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

        //medKit.addComponent(new TextureRenderComponent("images/tree.png"));
        return medKit;
    }

    /**
     * Creates a bandAid entity
     * @return bandAid entity
     */
    public static Entity createBandAid() {
        Entity bandAid = createBaseItem();
        HealthItemConfig config = configs.bandAid;

        return bandAid;
    }

    /**
     * Creates an energyDrink entity
     * @return energyDrink entity
     */
    public static Entity createEnergyDrink() {
        Entity energyDrink = createBaseItem();
        SpeedBoostConfig config = configs.energyDrink;

        return energyDrink;
    }


    /**
     * Creates a generic Item to be used as a base entity by more specific Item creation methods.
     * NB: This is where we add more components
     * @return item entity
     */
    private static Entity createBaseItem() {
        Entity item =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent());
                // .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
        return item;

    }

}
