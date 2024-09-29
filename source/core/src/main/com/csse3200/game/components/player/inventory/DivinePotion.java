package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

public class DivinePotion extends BuffItem{
    private static final int potionBoost = 30;

    /**
     * Make the entity pick the item up, and apply effects immediately instead of being added to the inventory
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        inventory.addItem(this); // FIXME
        effect(inventory.getEntity());
    }

    /**
     * Get the name of this item
     *
     * @return the String representation of the name of this item
     */
    @Override
    public String getName() {
        return "Divine Potion";
    }


    /**
     * Remove this collectible from the entity
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {
    }


    /**
     * Return a string representation of this collectible that can be parsed by CollectibleFactory
     *
     * @return the string representation of this collectible.
     */
    @Override
    public String getBuffSpecification() {
        return "Divine potion";
    }


    /**
     * Return texture related with divine potion item
     *
     * @return texture representing icon of divine potion item.
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/divine_potion.png");
    }

    /**
     * Applies the divine potion to an entity
     * calls the Boost(entity) method
     *
     * @param entity to which divine potion item effect is applied to.
     */
    @Override
    public void effect(Entity entity) {
        Boost(entity);
    }


    /**
     * Increases health by using entity's CombatStatsComponent to set Health
     *
     * @param entity whose health is increased.
     */
    public void Boost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        int currentHealth = combatStats.getHealth();
        int newHealth = Math.min(currentHealth + potionBoost,combatStats.getMaxHealth());
        combatStats.setHealth(newHealth);
    }
}
