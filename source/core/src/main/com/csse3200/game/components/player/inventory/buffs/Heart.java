package com.csse3200.game.components.player.inventory.buffs;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.BuffItem;
import com.csse3200.game.entities.Entity;

public class Heart extends BuffItem {

    private static final int maxHealthIncrease = 50;
    private static final int maxHealthLimit = 250;

    /**
     * Provides buff specification for the item
     *
     * @return string representing buff specification
     */
    @Override
    public String getBuffSpecification() {
        return "heart";
    }

    /**
     Applies the effect of increasing the player's maximum health.
     * This method checks the current maximum health of the player entity. If the player's
     * maximum health is below the defined limit (250), it increases the max health by
     * a specified value (50), up to the limit. After increasing the max health, it
     * sets the player's current health to match the new max health value.
     *
     * @param entity The player entity to which the heart buff is applied.
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

    /**
     * Return name of the item
     *
     * @return string representing the name of item.
     */
    @Override
    public String getName() {
        return "Heart";
    }

    /**
     * Returns texture associated with the item
     *
     * @return texture representing the heart icon
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/heart_organ.png");
    }
}
