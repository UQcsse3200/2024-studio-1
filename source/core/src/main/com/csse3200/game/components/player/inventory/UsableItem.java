package com.csse3200.game.components.player.inventory;

import com.csse3200.game.entities.Entity;

public abstract class UsableItem implements Collectible {
    @Override
    public Type getType() {
        return Type.ITEM;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.addItem(this);
        //FIXME This currently "uses" every item in the inventory at once.
        inventory.getEntity().getEvents().addListener("use", () -> this.apply(inventory.getEntity()));
        // Add anything needed to add to the user upon pickup.
    }

    @Override
    public String getSpecification() {
        return "item:";
    }

    /**
     * Apply any effects this item does upon use.
     */
    public abstract void apply(Entity entity);
}
