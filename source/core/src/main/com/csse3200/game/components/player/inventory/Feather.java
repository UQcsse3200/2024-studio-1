package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

/**
 * The Feather class represents an item that grants the player a 20% chance to perform a critical hit, dealing double damage.
 * This item is a buff that is applied to the entities combat stats.
 */
public class Feather extends BuffItem {

    /**
     * Applies the critical hit chance to the player
     * @param entity The player entity
     */
    @Override
    public void effect(Entity entity) {
        entity.getComponent(CombatStatsComponent.class).updateCritAbility();
        entity.getComponent(CombatStatsComponent.class).updateCritChance(0.2);
    }

    /**
     * Returns a string that specifies the type of buff
     * @return the buff specification
     */
    @Override
    public String getBuffSpecification() {
        return "feather";
    }

    /**
     * Returns the name of the item
     * @return the name of the item
     */
    @Override
    public String getName() {
        return "Feather";
    }

    /**
     * Returns the icon representing the feather item
     * @return a texture object for the feather icon
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/feather.png");
    }

    /**
     * Get mystery box icon for this specific item
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_red.png");
    }

    @Override
    public void drop(Inventory inventory) {
    }

}
