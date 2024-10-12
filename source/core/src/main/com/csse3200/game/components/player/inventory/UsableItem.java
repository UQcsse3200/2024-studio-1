package com.csse3200.game.components.player.inventory;

import com.csse3200.game.entities.Entity;

/**
 * An item that can be used at the player's leisure.
 */
public abstract class UsableItem implements Collectible {
    @Override
    public Type getType() {
        return Type.ITEM;
    }

    @Override
    public String getSpecification() {
        return "item:" + getItemSpecification();
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.getContainer(UsableItem.class).add(this);
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.getContainer(UsableItem.class).remove(this);
    }

    /**
     * Get the specification of this item.
     *
     * @return the string representation of this item.
     */
    public abstract String getItemSpecification();

    /**
     * Apply any effects this item does upon use.
     *
     * @param entity the entity to apply it to.
     */
    public abstract void apply(Entity entity);

}
