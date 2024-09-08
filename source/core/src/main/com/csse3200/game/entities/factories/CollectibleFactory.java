package com.csse3200.game.entities.factories;

import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * A factory that creates a collectible from a specification.
 */
public class CollectibleFactory extends LoadedFactory {
    private final WeaponFactory weaponFactory = new WeaponFactory();
    private final ItemFactory itemFactory = new ItemFactory();

    /**
     * Create a collectible from a specification.
     * <p>
     * The specification format is one of the following,
     * "melee:"
     * "ranged:"
     * "item:"
     * "buff:"
     * followed by a specific specification for those item's respective factories.
     *
     * @param specification the specification to follow.
     * @return the created collectible.
     */
    public Collectible create(String specification) {
        String[] split = specification.split(":", 2);
        return switch (split[0]) {
            case "melee" -> weaponFactory.create(Collectible.Type.MELEE_WEAPON, split[1]);
            case "ranged" -> weaponFactory.create(Collectible.Type.RANGED_WEAPON, split[1]);
            case "item", "buff" -> itemFactory.create(split[1]);
            default -> throw new IllegalStateException("Unexpected value: " + split[0]);
        };
    }

    /**
     * Convert a collectible item into a collectible entity.
     *
     * @param collectible the item to convert
     * @return the final entity containing the collectible.
     */
    public Entity createCollectibleEntity(Collectible collectible) {
        Entity collectibleEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent(collectible.getIcon()));
        //Commented out due to "asset not loaded" issues
//        if (collectible.getSpecification().contains("-mystery")) {
//            collectibleEntity.getComponent(TextureRenderComponent.class).setTexture(collectible.getMysteryIcon());
//        }

        collectibleEntity.getComponent(TextureRenderComponent.class).scaleEntity();
        return collectibleEntity;
    }

    /**
     * Create a collectible item as a collectible entity.
     *
     * @param specification the item to create
     * @return the final entity containing the collectible.
     */
    public Entity createCollectibleEntity(String specification) {
        Collectible collectible = create(specification);
        return createCollectibleEntity(collectible);
    }
}

