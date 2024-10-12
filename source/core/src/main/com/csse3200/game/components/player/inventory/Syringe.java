package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

public class Syringe extends BuffItem {
    private static final int syringeBoost = 50;

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
        return "Syringe";
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
        return "syringe";
    }


    /**
     * Return texture related with Syringe item
     *
     * @return texture representing icon of Syringe item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/syringe.png");
    }

    /**
     * Get mystery box icon for this specific item
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_green.png");
    }

    /**
     * Applies the Syringe to an entity, increasing the health by an instant boost,
     * calls the syringeBoost(entity) method
     *
     * @param entity to which Syringe item effect is applied to.
     */
    @Override
    public void effect(Entity entity) {
        syringeBoost(entity);
    }


    /**
     * Increases health by using entity's CombatStatsComponent to add Health
     *
     * @param entity whose health is increased.
     */
    public void syringeBoost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        combatStats.addHealth(syringeBoost);
    }

}

