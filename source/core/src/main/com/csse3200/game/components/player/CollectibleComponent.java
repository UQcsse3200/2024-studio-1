package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.Collectible;

/**
 * A Component that manages an entity being collectible.
 */
public class CollectibleComponent extends Component {
    private final Collectible collectible;

    /**
     * Construct a new Component that holds a collectible.
     *
     * @param collectible the collectible that this component holds.
     */
    public CollectibleComponent(Collectible collectible) {
        this.collectible = collectible;
    }

    /**
     * Get the collectible this component holds.
     *
     * @return the collectible this component holds.
     */
    public Collectible getCollectible() {
        return collectible;
    }
}
