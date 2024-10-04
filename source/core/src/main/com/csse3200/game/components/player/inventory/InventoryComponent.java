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
     * Remove an item to your inventory.
     *
     * @param item The item to remove from your inventory.
     */
    public void drop(Collectible item) {
        item.drop(inventory);
        getEntity().getEvents().trigger("updateInventory");
    }

    /**
     * Get a list of Pets this player has.
     *
     * @return the pets.
     */
    public Array<Pet> getPets() {
        return inventory.getContainer(Pet.class).get();
    }

    /**
     * Get a list of Usable Items this player has.
     *
     * @return the items.
     */
    public Array<UsableItem> getItems() {
        return inventory.getContainer(UsableItem.class).get();
    }

    /**
     * Get a list of buffs this player has.
     *
     * @return the buffs.
     */
    public Array<BuffItem> getBuffs() {
        return inventory.getContainer(BuffItem.class).get();
    }

    /**
     * Get this player's offhand weapon.
     *
     * @return the offhand weapon, if it exists
     */
    public Optional<OffHandItem> getOffhand() {
        Array<OffHandItem> offHandItems = inventory.getContainer(OffHandItem.class).get();
        return offHandItems.size > 0 ? Optional.of(offHandItems.get(0)) : Optional.empty();
    }

    /**
     * Get this player's main weapon.
     *
     * @return the main weapon, if it exists
     */
    public Optional<MainHandItem> getMainWeapon() {
        Array<MainHandItem> mainWeapons = inventory.getContainer(MainHandItem.class).get();
        return mainWeapons.size > 0 ? Optional.of(mainWeapons.get(0)) : Optional.empty();
    }
}