package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.GridPoint2;
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
    public void pickup(Inventory inventory) {

        inventory.addItem(this);

        //Apply method just for testing purpose of Health items(Sprint 1)
//        apply(inventory.getEntity());

        //FIXME This currently "uses" every item in the inventory at once.
        //inventory.getEntity().getEvents().addListener("use", () -> this.apply(inventory.getEntity()));
        // Add anything needed to add to the user upon pickup.
    }
    @Override
    public void pickup(Inventory inventory, Entity entity) {

        pickup(inventory);
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.removeItem(this);
    }

    @Override
    public String getSpecification() {
        return "item:" + getItemSpecification();
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
