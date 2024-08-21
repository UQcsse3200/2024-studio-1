package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

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

    public enum Type {
        ITEM,
        MELEE_WEAPON,
        RANGED_WEAPON,
        NONE
    }
}
