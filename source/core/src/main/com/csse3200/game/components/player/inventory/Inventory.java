package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;

/**
 * A component intended to be used by the player to track their inventory.
 * <p>
 * Currently only stores the gold amount but can be extended for more advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class Inventory {
    private final InventoryComponent component;

    private final Container<MeleeWeapon> meleeWeapons = new Container<>(1);
    private final Container<RangedWeapon> rangedWeapons = new Container<>(1);
    private final Container<UsableItem> usableItems = new Container<>(9);
    private final Container<BuffItem> buffs = new Container<>();
    private final Container<Pet> pets = new Container<>();

    /**
     * Construct an inventory for an inventory component
     *
     * @param component the component this inventory is attached to.
     */
    public Inventory(InventoryComponent component) {
        super();
        this.component = component;
    }

    /**
     * Get the entity this inventory is attached to.
     *
     * @return the entity this Inventory is attached to.
     */
    public Entity getEntity() {
        return component.getEntity();
    }

    /**
     * Get one of the various Inventory containers.
     *
     * @param type the class the container holds.
     * @param <T>  the class the container holds.
     * @return the container that holds that type
     */
    public <T extends Collectible> Container<T> getContainer(Class<T> type) {
        var container = switch (Collectible.Type.getByClass(type)) {
            case ITEM -> usableItems;
            case MELEE_WEAPON -> meleeWeapons;
            case RANGED_WEAPON -> rangedWeapons;
            case BUFF_ITEM -> buffs;
            case PET -> pets;
            default -> throw new IllegalStateException("Unexpected value: " + type.getSimpleName());
        };

        //noinspection unchecked
        return (Container<T>) container;
    }

    /**
     * The storage interface for a collection of inventory items of a shared type.
     *
     * @param <T> the type of object held by this container
     */
    public final class Container<T extends Collectible> {
        private static final int NO_LIMIT = -1;
        private final Inventory inventory = Inventory.this;

        private final int limit;
        private final Array<T> collectibles;

        private Container(int limit) {
            this.collectibles = new Array<>(Math.max(0, limit));
            this.limit = limit;
        }

        private Container() {
            this(NO_LIMIT);
        }

        /**
         * Add a collectible to this container.
         *
         * @param collectible the collectible to add.
         */
        public void add(T collectible) {
            if (limit > NO_LIMIT && collectibles.size >= limit) {
                component.drop(collectibles.get(0));
            }
            collectibles.add(collectible);
        }

        /**
         * Remove a collectible from this container.
         *
         * @param collectible the collectible to remove.
         */
        public void remove(T collectible) {
            collectibles.removeValue(collectible, true);
        }

        /**
         * Get a read-only copy of the collectibles in this container.
         *
         * @return the collectibles.
         */
        public Array<T> get() {
            return new Array<>(collectibles);
        }
    }
}
