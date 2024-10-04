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
     * Get this player's melee weapon.
     *
     * @return the ranged weapon, if it exists
     */
    public Optional<MeleeWeapon> getMelee() {
        Array<MeleeWeapon> meleeWeapons = inventory.getContainer(MeleeWeapon.class).get();
        return meleeWeapons.size > 0 ? Optional.of(meleeWeapons.get(0)) : Optional.empty();
    }

    /**
     * Get this player's ranged weapon.
     *
     * @return the ranged weapon, if it exists
     */
    public Optional<RangedWeapon> getRanged() {
        Array<RangedWeapon> rangedWeapons = inventory.getContainer(RangedWeapon.class).get();
        return rangedWeapons.size > 0 ? Optional.of(rangedWeapons.get(0)) : Optional.empty();
    }
}