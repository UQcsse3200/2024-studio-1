package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;

/**
 * An item that can be collected.
 */
public interface Collectible {

    /**
     * Get the Type of this item. The type determines how it ought to be used by the player.
     *
     * @return the Type of this item
     */
    Type getType();

    /**
     * Get the name of this item.
     *
     * @return The name of this item
     */
    String getName();

    /**
     * Get the texture for this item's icon.
     *
     * @return this item's icon.
     */
    Texture getIcon();

    /**
     * Return a string representation of this collectible that can be parsed by CollectibleFactory
     *
     * @return the string representation of this collectible.
     */
    String getSpecification();

    /**
     * Make the entity pick us up, and apply any effects to them.
     *
     * @param inventory The inventory to be put in.
     */
    void pickup(Inventory inventory);

    /**
     * Remove this collectible from the entity
     *
     * @param inventory The inventory to be dropped out of.
     */
    void drop(Inventory inventory);

    /**
     * Get the mystery box icon representation of this item
     *
     * @return the mystery box icon representation of this item
     */
    default Texture getMysteryIcon() {
        return null;
    }

    /**
     * Each of the different types of collectible.
     */
    enum Type {
        ITEM(UsableItem.class),
        MELEE_WEAPON(MeleeWeapon.class),
        RANGED_WEAPON(RangedWeapon.class),
        BUFF_ITEM(BuffItem.class),
        PET(Pet.class),
        NONE(null);

        private final Class<? extends Collectible> type;

        Type(Class<? extends Collectible> type) {
            this.type = type;
        }

        /**
         * Get the associated Collectible type from the class.
         *
         * @param type the class of the collectible.
         * @return the type.
         */
        public static Type getByClass(Class<? extends Collectible> type) {
            for (Type t : Type.values()) {
                if (t.type == type) {
                    return t;
                }
            }
            return NONE;
        }

        public Class<? extends Collectible> getType() {
            return type;
        }
    }
}
