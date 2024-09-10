package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.CollectibleFactory;

/**
 * A service to manage the creation of collectibles based on a specification.
 */
public class CollectibleFactoryService {

    private final CollectibleFactory collectibleFactory;

    /**
     * Constructs a new CollectibleFactoryService. Initialises the internal CollectibleFactory
     * which is used to create collectible entities.
     */
    public CollectibleFactoryService() {
        this.collectibleFactory = new CollectibleFactory();
    }

    /**
     * Create a collectible as a collectible entity
     * @param specification the item to create
     * @return the entity containing the collectible
     */
    public Entity createCollectibleEntity(String specification) {
        return collectibleFactory.createCollectibleEntity(specification);
    }
}
