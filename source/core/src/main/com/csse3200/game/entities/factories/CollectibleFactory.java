package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.HealthComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * A factory that creates a collectible from a specification.
 */
public class CollectibleFactory {
    private static final WeaponFactory weaponFactory = new WeaponFactory();
    private static final ItemFactory itemFactory = new ItemFactory();

    /**
     * Create a collectible from a specification.
     * @param specification the specification to follow.
     * @return the created collectible.
     */
    public static Collectible create(String specification) {
        String[] split = specification.split(":", 2);

        return switch (split[0]) {
            case "melee" -> weaponFactory.create(Collectible.Type.MELEE_WEAPON, split[1]);
            case "ranged" -> weaponFactory.create(Collectible.Type.RANGED_WEAPON, split[1]);
            case "item" -> itemFactory.create(split[1]);
            default -> throw new IllegalStateException("Unexpected value: " + split[0]);
        };
    }

    /**
     * Convert a collectible item into a collectible entity.
     * @param collectible the item to convert
     * @return the final entity containing the collectible.
     */
    public static Entity createCollectibleEntity(Collectible collectible) {
        Entity collectibleEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new HealthComponent(100))
                .addComponent(new ItemComponent(collectible))
                .addComponent(new InventoryComponent()) //Not sure about this
                .addComponent(new TextureRenderComponent(collectible.getIcon()));

        collectibleEntity.getComponent(TextureRenderComponent.class).scaleEntity();
        return collectibleEntity;
    }
}

