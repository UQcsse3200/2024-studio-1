package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.components.CombatStatsComponent;


public class DamageBuff extends BuffItem{
    private final int buff = 5;

    /**
     * Returns the string of the buff item
     *
     * @return string of the item
     */
    @Override
    public String getBuffSpecification() {
        return "damagebuff";
    }

    /**
     * Applies the damage buff to the player for each weapon
     *
     * @param entity The player entity
     */
    @Override
    public void effect(Entity entity) {
        entity.getComponent(CombatStatsComponent.class).addAttack(buff);
    }

    public int getBuff() {return buff;}

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
        return new Texture("damage_buff.png");
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
