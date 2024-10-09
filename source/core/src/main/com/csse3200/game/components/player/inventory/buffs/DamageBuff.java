package com.csse3200.game.components.player.inventory.buffs;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.BuffItem;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.entities.Entity;

/**
 * An item that boosts your damage.
 */
public class DamageBuff extends BuffItem {
    private final int buff = 15;

    /**
     * get the amount the damage is buffed by.
     *
     * @return the amount the damages is buff by.
     */
    public int getBuff() {
        return buff;
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
        return new Texture("images/items/damage_buff.png");
    }

    /**
     * Removes the item from inventory
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {

    }

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
        int currDamage = entity.getComponent(CombatStatsComponent.class).getBaseAttack();
        int buffedDamage = currDamage + buff;
        int maxDamage = entity.getComponent(CombatStatsComponent.class).getMaxDamage();
        if (buffedDamage >= maxDamage) {
            //Damage is maxed out
            //Need to update UI and cap
            buffedDamage = maxDamage;
            entity.getComponent(CombatStatsComponent.class).setBuff(buffedDamage);
        } else {
            entity.getComponent(CombatStatsComponent.class).addAttack(buff);
        }
        entity.getEvents().trigger("updateDamageBuff", buffedDamage);
    }
}
