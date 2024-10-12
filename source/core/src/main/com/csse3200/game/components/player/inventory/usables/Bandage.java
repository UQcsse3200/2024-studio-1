package com.csse3200.game.components.player.inventory.usables;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.Entity;

/**
 * The bandage item class that can be used by player to increase health,
 * by a small health boost of 20.
 */
public class Bandage extends UsableItem {
    public static final int Small_Health_Boost = 20;

    @Override
    public String getItemSpecification() {
        return "bandage";
    }

    /**
     * Applies the bandage to an entity, increasing its health by a small amount,
     * calls the increaseSmallBoost(entity) method
     *
     * @param entity to which Bandage item effect is applied to.
     */
    @Override
    public void apply(Entity entity) {
        increaseSmallBoost(entity);
    }

    /**
     * Returns name of item
     *
     * @return the item name
     */
    @Override
    public String getName() {
        return "Bandage";
    }

    /**
     * Return texture related with Bandage item
     *
     * @return texture representing icon of Bandage item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/bandage.png");
    }

    /**
     * Get mystery box icon for this specific item
     *
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_green.png");
    }

    /**
     * Increases health by using entity's CombatStatsComponent to add Health
     *
     * @param entity whose health is increased.
     */
    public void increaseSmallBoost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        int currentHealth = combatStats.getHealth();
        int newHealth = Math.min(currentHealth + Small_Health_Boost, combatStats.getMaxHealth());
        combatStats.setHealth(newHealth);
    }
}
