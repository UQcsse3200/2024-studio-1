package com.csse3200.game.components.player.inventory;

import com.csse3200.game.entities.Entity;

public abstract class MeleeWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    @Override
    public void pickup(Entity entity) {
        entity.getComponent(InventoryComponent.class).setMelee(this);
        entity.getEvents().addListener("melee", this::attack); // Do nothing
    }

    @Override
    public void drop(Entity entity) {
        entity.getComponent(InventoryComponent.class).resetMelee();
    }

    /**
     * Swing this weapon
     */
    public abstract void attack();
}
