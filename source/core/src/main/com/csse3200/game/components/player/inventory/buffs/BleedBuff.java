package com.csse3200.game.components.player.inventory.buffs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.BuffItem;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.entities.Entity;

/**
 * The bleed item reduces 5% health of enemies per second for 5 seconds.
 * The bleed item is a boss drop after killing the werewolf
 * The weapons on which the bleed effect is available is axe and knife
 */
public class BleedBuff extends BuffItem {

    // Percentage of health to reduce per second
    private final int healthReductionPerc = 5;

    /*
         Default constructor for the BleedBuff class.
    */
    public BleedBuff() {
    }

    /**
     * Applies the bleed effect to the enemy. Reduces 5% of the enemy's health per
     * second for 5 seconds. The damage stops if the enemy's health reaches 0.
     *
     * @param enemy The enemy entity to apply the bleed effect to.
     */

    private void applyBleedEffect(Entity enemy) {
        CombatStatsComponent enemyStats = enemy.getComponent(CombatStatsComponent.class);

        if (enemyStats != null) {
            // Schedule bleed damage over 5 seconds
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (enemyStats.getHealth() > 0) {
                        float bleedAmount = enemyStats.getHealth() * (healthReductionPerc / 100f);
                        enemyStats.setHealth((int) (enemyStats.getHealth() - bleedAmount));
                    } else {
                        // Stop applying bleed effect if the enemy is dead
                        cancel();
                    }
                }
            }, 1, 1, 5); // Reduce health for 5 seconds
        }
    }

    /**
     * Returns the name of the item.
     *
     * @return the name of the item, "Bleed Buff".
     */
    @Override
    public String getName() {
        return "Bleed Buff";
    }

    /**
     * Returns the icon representing the BleedBuff item.
     *
     * @return a Texture object for the bleed icon.
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/bleed.png");
    }

    /**
     * Drops the BleedBuff item from the player's inventory.
     *
     * @param inventory The inventory from which the item is dropped.
     */

    @Override
    public void drop(Inventory inventory) {
    }

    /**
     * Returns a string that specifies the type of buff.
     *
     * @return the buff specification as a string, "bleedbuff".
     */
    @Override
    public String getBuffSpecification() {
        return "bleedbuff";
    }

    /**
     * Applies the bleed effect to an entity. The player must hit an enemy with a weapon
     * that can apply the bleed effect. The method listens for the
     * "BleedWeaponHit" event to apply the effect.
     *
     * @param entity The player entity that will apply the bleed effect.
     */

    @Override
    public void effect(Entity entity) {
        // Listen for events when the player hits an enemy with Bleed weapon
        entity.getEvents().addListener("BleedWeaponHit", this::applyBleedEffect);

    }

    /**
     * Returns the percentage of health reduced per second by the bleed effect.
     *
     * @return the health reduction percentage (5%).
     */
    public int getHealthReductionPerc() {
        return healthReductionPerc;
    }
}
