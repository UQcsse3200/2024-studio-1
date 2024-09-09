package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

/**
 * Represents an armor item which increases the players armour statistics,
 * and hence overall reduces the damage taken by the player.
 */
public class Armor extends BuffItem {

    private final int armor = 10;

    /**
     * Applies the armor effect to the player which increases the armor value.
     *
     * @param entity The player entity
     */
    @Override
    public void effect(Entity entity) {
        // entity.getComponent(CombatStatsComponent.class).increaseArmor(armor);
    }

    /**
     * Returns the armor value of this item.
     *
     * @return the armor value
     */
    public int getArmorValue() {
        return armor;
    }

    /**
     * Returns the buff specification for this item, identifying it as an "armor" type.
     *
     * @return a string that identifies the type of buff
     */
    @Override
    public String getBuffSpecification() {
        return "armor";
    }

    /**
     * Returns the name of this item.
     *
     * @return the name of the item, "Armor"
     */
    @Override
    public String getName() {
        return "Armor";
    }

    /**
     * Provides the texture associated with this armor item.
     *
     * @return the Texture object for this item's icon
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/armor.png");
    }

    /**
     * Removes the collectible item from the entity.
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {

    }
}
