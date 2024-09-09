package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.CombatStatsComponent;


public class DamageBuff extends BuffItem{

    /**
     * Returns the string of the buff item
     *
     * @return string of the item
     */
    @Override
    public String getBuffSpecification() {
        return "damagebuff";
    }

    @Override
    public void effect(Entity entity) {
        int baseAttack = entity.getComponent(CombatStatsComponent.class).getBaseAttack();
        entity.getComponent(CombatStatsComponent.class).setBaseAttack(35); //placeholder value

    }

    /**
     * Gets the name of the item
     *
     * @return the name of the item
     */
    @Override
    public String getName() {
        return "Damage Buff";
    }

    /**
     * Gets the render for the item
     *
     * @return the items image
     */
    @Override
    public Texture getIcon() {
        return null;
    }

    /**
     * Removes the item from inventory
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {

    }
}
