package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

/**
 * The bandage item class that can be used by player to increase health,
 * by a small health boost of 20.
 */
public class Bandage extends UsableItem {
    public static final int Small_Health_Boost = 20;

    /**
     * The pickup function handles the pickup of Bandage item into player's inventory.
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
        return "Bandage";
    }

    /**
     * Return texture related with Bandage item
     * @return texture representing icon of Bandage item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/bandage.png");
    }

    /**
     * Applies the bandage to an entity, increasing its health by a small amount,
     * calls the increaseSmallBoost(entity) method
     * @param entity to which Bandage item effect is applied to.
     */
    @Override
    public void apply(Entity entity) {
        increaseSmallBoost(entity);
    }

    /**
     * Increases health by using entity's CombatStatsComponent to add Health
     * @param entity whose health is increased.
     */
    public void increaseSmallBoost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        combatStats.addHealth(Small_Health_Boost);
    }
}
