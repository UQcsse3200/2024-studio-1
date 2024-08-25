package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

public class Bandage extends UsableItem {
    private static final int Small_Health_Boost = 20;

    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    @Override
    public void drop(Inventory inventory) {
    }

    @Override
    public String getName() {
        return "Bandage";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/bandage.png");
    }

    @Override
    public void apply(Entity entity) {
        increaseSmallBoost(entity);
    }

    public void increaseSmallBoost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        combatStats.addHealth(Small_Health_Boost);
    }
}
