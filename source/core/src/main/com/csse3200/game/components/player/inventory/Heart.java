package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

public class Heart extends BuffItem{

    private static final int maxHealthIncrease = 50;
    private static final int maxHealthLimit = 150;


    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
        effect(inventory.getEntity());
    }

    @Override
    public String getName() {
        return "Heart";
    }

    @Override
    public void drop(Inventory inventory) {
    }

    @Override
    public String getBuffSpecification() {
        return "Heart";
    }


    @Override
    public Texture getIcon() {
        return new Texture("images/items/syringe.png");
    }


    @Override
    public void effect(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);

        if (combatStats != null) {
            int currentMaxHealth = combatStats.getMaxHealth();
            if (currentMaxHealth < maxHealthLimit) {
                combatStats.setMaxHealth(Math.min(currentMaxHealth + maxHealthIncrease, maxHealthLimit));
            }
        }
    }
}
