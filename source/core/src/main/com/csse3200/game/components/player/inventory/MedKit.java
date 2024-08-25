package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.entities.Entity;

/**
 * The Medkit item class can be used by player to increase health,
 * by a large health boost of 100.
 */
public class MedKit extends UsableItem{

    private static final int Large_Health_Boost = 100;

    /**
     * The pickup function handles the pickup of Medkit item into player's inventory.
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    /**
     * Handles the dropping of item from player's inventory after being used
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {
    }

    /**
     * Returns name of item
     * @return the item name
     */
    @Override
    public String getName() {
        return "Medical Kit";
    }

    /**
     * Return texture related with Medkit item
     * @return texture representing icon of Medkit item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/med_kit.png");
    }

    /**
     * Applies the Medkit to an entity, increasing its health by a large amount,
     * calls the increaseLargeBoost(entity) method
     * @param entity to which Medkit item effect is applied to.
     */
    @Override
    public void apply(Entity entity) {
        increaseLargeBoost(entity);
    }

    /**
     * Increases health by using entity's CombatStatsComponent to add Health
     * @param entity whose health is increased.
     */
    public void increaseLargeBoost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        combatStats.addHealth(Large_Health_Boost);
    }
}