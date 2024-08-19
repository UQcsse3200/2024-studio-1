package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A component intended to be used by the player to track their inventory.
 * <p>
 * Currently only stores the gold amount but can be extended for more advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
    private final Inventory inventory;

    public InventoryComponent() {
        super();
        this.inventory = new Inventory(this.entity);
    }

    /**
     * Add an item to your inventory.
     *
     * @param item The item to add to your inventory
     */
    public void pickup(Collectible item) {
        item.pickup(inventory);
    }

    /**
     * Remove an item to your inventory.
     *
     * @param item The item to remove from your inventory
     */
    public void drop(Collectible item) {
        item.drop(inventory);
    }
}
