package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.BuyableComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.CollectibleHitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory that creates a collectible from a specification.
 */
public class CollectibleFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(CollectibleFactory.class);
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
        logger.info("Creating collectible with specification: {}", specification);
        String[] split = specification.split(":", 2);

        return switch (split[0]) {
            case "melee" -> weaponFactory.create(Collectible.Type.OFF_HAND, split[1]);
            case "ranged" -> weaponFactory.create(Collectible.Type.MAIN_HAND, split[1]);
            case "item", "buff", "pet" -> itemFactory.create(split[1]);
            default -> throw new IllegalStateException("Unexpected value: " + split[0]);
        };
    }

    /**
     * Generate a list of all valid specifications this factory can generate.
     *
     * @return the specifications as a List.
     */
    public List<String> getAllSpecs() {
        List<String> specs = new ArrayList<>();

        specs.addAll(itemFactory.getAllSpecs());
        specs.addAll(weaponFactory.getAllSpecs());

        return specs;
    }

    /**
     * Convert a collectible item into a collectible entity.
     *
     * @param collectible the item to convert
     * @return the final entity containing the collectible.
     */
    public Entity createCollectibleEntity(String specification, Collectible collectible) {
        Entity collectibleEntity = new Entity()
                .addComponent(new NameComponent("Collectible: " + collectible.getName()))
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new CollectibleHitboxComponent())
                .addComponent(new PhysicsComponent());

        Texture texture = collectible.getIcon();
        if (specification.contains("mystery") && collectible.getMysteryIcon() != null) {
            texture = collectible.getMysteryIcon();
        }
        if (specification.contains("buyable")) {
            collectibleEntity.addComponent(new BuyableComponent(10));
        }

        collectibleEntity.addComponent(new TextureRenderComponent(texture));

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
        return createCollectibleEntity(specification, collectible);
    }
}

