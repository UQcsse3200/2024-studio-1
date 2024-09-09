package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerStatsDisplay;


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
        int currDamage = entity.getComponent(CombatStatsComponent.class).getBaseAttack();
        int buffedDamage = currDamage + buff;
        int maxDamage = entity.getComponent(CombatStatsComponent.class).getMaxDamage();
        if (buffedDamage >= maxDamage) {
            //Damage is maxed out
            //Need to update UI and cap
            buffedDamage = maxDamage;
            entity.getComponent(CombatStatsComponent.class).setBaseAttack(maxDamage);
        } else {
            entity.getComponent(CombatStatsComponent.class).addAttack(buff);
        }
        entity.getEvents().trigger("updateDamageBuff", buffedDamage);
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

    @Override
    public String getMysteryIcon() {return ("images/items/mystery_box_red.png");}
}
