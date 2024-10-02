package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

public class Heart extends BuffItem{

    private static final int maxHealthIncrease = 50;
    private static final int maxHealthLimit = 150;

    /**
     * Triggers effect of the item when player picks up
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
        effect(inventory.getEntity());
    }

    /**
     * Return name of the item
     * @return string representing the name of item.
     */
    @Override
    public String getName() {
        return "Heart";
    }

    /**
     * Does nothing when heart is dropped out of inventory
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {
    }

    /**
     * Provides buff specification for the item
     * @return string representing buff specification
     */
    @Override
    public String getBuffSpecification() {
        return "Heart";
    }

    /**
     * Returns texture associated with the item
     * @return texture representing the heart icon
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/heart_organ.png");
    }

    /**
     * Applies effect of increasing player's maximum health
     * If player's current health max health is below the limit, it increases by a set amount
     * @param entity The player entity
     */
    @Override
    public void effect(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);

        if (combatStats != null) {
            int currentMaxHealth = combatStats.getMaxHealth();
            if (currentMaxHealth < maxHealthLimit) {
                combatStats.setMaxHealth(Math.min(currentMaxHealth + maxHealthIncrease, maxHealthLimit));
                combatStats.setHealth(combatStats.getMaxHealth());
            }
        }
    }
}
