package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;

/**
 * A usable item yjsy can be applied to another item to give the player a chance at obtaining a different item
 */
public class Reroll extends UsableItem {
    /**
     * Returns name of item
     *
     * @return the item name
     */
    @Override
    public String getName() {
        return "Reroll";
    }

    /**
     * Return texture related with Bandage item
     *
     * @return texture representing icon of Bandage item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/reroll_item.png");
    }

    /**
     * Gets the specification associated with this item
     * @return string representation of this item's specification
     */
    @Override
    public String getItemSpecification() {
        return "reroll";
    }

    /**
     * Applies the effects of the reroll item to a specified entity.
     *
     * @param entity the entity that triggers an event to signal the reroll item usage
     */
    @Override
    public void apply(Entity entity) {
        entity.getEvents().trigger("rerollUsed");
        //Do all the respawning stuff here
    }

}
