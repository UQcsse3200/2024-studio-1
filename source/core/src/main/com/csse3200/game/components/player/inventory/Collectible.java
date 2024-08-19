package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

/**
 * An item that can be collected.
 */
public interface Collectible {
    enum Type {
        ITEM,
        MELEE_WEAPON,
        RANGED_WEAPON
    }

    /**
     * Get the Type of this item. The type determines how it ought to be used by the player.
     * @return the Type of this item
     */
    Type getType();

    /**
     * Get the name of this item.
     * @return The name of this item
     */
    String getName();

    /**
     * Get the texture for this item's icon.
     * @return this item's icon.
     */
    Texture getIcon();

    /**
     * Apply the effect of this item to a provided character.
     * @param entity The character to apply the effect to.
     */
    void apply(Entity entity);

    /**
     * Remove the effect of this item from a provided character.
     * @param entity The character to remove the effect from.
     */
    void unapply(Entity entity);

    /**
     * Make the entity pick us up, and apply any effects to them.
     * @param inventory The inventory to be put in.
     */
    void pickup(Inventory inventory);

    /**
     * Remove this collectible from the entity
     * @param inventory The inventory to be dropped out of.
     */
    void drop(Inventory inventory);
}
