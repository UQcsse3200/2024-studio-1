package com.csse3200.game.components.player.inventory.buffs;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.BuffItem;
import com.csse3200.game.entities.Entity;

public class Heart extends BuffItem {

    private static final int MAX_HEALTH_LIMIT = 50;
    private static final int MAX_HEALTH_INCREASE = 150;

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
     * Applies effect of increasing player's maximum health
     * If player's current health max health is below the limit, it increases by a set amount
     *
     * @param entity The player entity
     */
    @Override
    public void effect(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);

        if (combatStats != null) {
            int currentMaxHealth = combatStats.getMaxHealth();
            if (currentMaxHealth < MAX_HEALTH_LIMIT) {
                combatStats.setMaxHealth(Math.min(currentMaxHealth + MAX_HEALTH_INCREASE, MAX_HEALTH_LIMIT));
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
