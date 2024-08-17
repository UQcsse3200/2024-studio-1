package com.csse3200.game.components.player.inventory;

import com.csse3200.game.entities.Entity;

public abstract class MeleeWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    @Override
    public void onPickup(Entity entity) {
        entity.getComponent(InventoryComponent.class).setMelee(this);
    }
}
