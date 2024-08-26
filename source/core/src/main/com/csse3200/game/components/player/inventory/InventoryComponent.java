package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their inventory.
 * <p>
 * Currently only stores the gold amount but can be extended for more advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
    private final Inventory inventory;
    private int totalWeaponCount = 0;

    /**
     * Construct a new empty inventory component
     */
    public InventoryComponent() {
        super();
        this.inventory = new Inventory(this);
    }

    /**
     * Add an item to your inventory.
     *
     * @param item The item to add to your inventory
     */
    public void pickup(Collectible item) {
        if(item instanceof RangedWeapon || item instanceof MeleeWeapon){
            totalWeaponCount = totalWeaponCount + 1;
            System.out.println("Weapon picked up! Total weapons: " + totalWeaponCount);

            // Notify listeners about the updated weapon count
            entity.getEvents().trigger("updateWeaponCount", totalWeaponCount);
        }
        item.pickup(inventory);
    }

    /**
     * Remove an item to your inventory.
     *
     * @param item The item to remove from your inventory.
     */
    public void drop(Collectible item) {
        item.drop(inventory);
    }

    /**
     * Get the underlying inventory model.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

}