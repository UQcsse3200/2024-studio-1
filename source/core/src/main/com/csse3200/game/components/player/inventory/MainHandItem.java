package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;

/**
 * An item held in the player's main hand, it is typically activated using the arrow keys.
 * These items can be aimed and fired in a particular direction.
 */
public abstract class MainHandItem implements Collectible {

    /**
     * Get the Type of this item.
     * The type determines how it ought to be used by the player.
     *
     * @return MAIN_HAND
     */
    @Override
    public Type getType() {
        return Type.MAIN_HAND;
    }

    @Override
    public String getSpecification() {
        return "ranged:" + getName();
    }

    /**
     * Pick up the main hand weapon and put it in the inventory.
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        inventory.getContainer(MainHandItem.class).add(this);
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.getContainer(MainHandItem.class).remove(this);
    }

    /**
     * Shoot this weapon in the chosen direction.
     *
     * @param direction direction to shoot in.
     */
    public abstract void shoot(Vector2 direction);
}
