package com.csse3200.game.components.player.inventory;

/**
 * An item held in the player's Offhand, it is typically activated using the space bar.
 * These items are not separately aimed.
 */
public abstract class OffHandItem implements Collectible {
    /**
     * Get the Type of this item.
     * The type determines how it ought to be used by the player.
     *
     * @return OFF_HAND
     */
    @Override
    public Type getType() {
        return Type.OFF_HAND;
    }


    @Override
    public String getSpecification() {
        return "melee:" + getName();
    }

    /**
     * Pick up the offhand weapon and put it in the inventory.
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        inventory.getContainer(OffHandItem.class).add(this);
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.getContainer(OffHandItem.class).remove(this);
    }

    /**
     * Activate this melee weapon in the walk direction of the player
     */
    public abstract void attack();
}
