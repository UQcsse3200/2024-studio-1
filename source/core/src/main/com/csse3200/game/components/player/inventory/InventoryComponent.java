package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
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
    private int rangedWeaponCount = 0;
    private int meleeWeaponCount = 0;

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
        item.pickup(inventory);
        getEntity().getEvents().trigger("updateInventory");
    }

    /**
     * Add a weapon collectible and its entity wrapper to your inventory.
     *
     * @param item The item to add to your inventory
     */
    public void pickup(Collectible item, Entity itemEntity) {
        if(item instanceof MeleeWeapon){
            meleeWeaponCount = 1;
            System.out.println("Melee Weapon picked up. Total Melee weapons are: " + meleeWeaponCount);
            entity.getEvents().trigger("updateMeleeWeaponCount", meleeWeaponCount);
            item.pickup(inventory, itemEntity);
        }
        if(item instanceof RangedWeapon){
            rangedWeaponCount = 1;
            System.out.println("Ranged Weapon picked up. Total Ranged weapons are: " + rangedWeaponCount);
            entity.getEvents().trigger("updateRangedWeaponCount", rangedWeaponCount);
            item.pickup(inventory, itemEntity);
        }
        else {
            // Should never go here
            item.pickup(inventory);
        }
        getEntity().getEvents().trigger("updateInventory");
    }

    /**
     * Remove an item to your inventory.
     *
     * @param item The item to remove from your inventory.
     */
    public void drop(Collectible item) {
        item.drop(inventory);
        getEntity().getEvents().trigger("updateInventory");
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