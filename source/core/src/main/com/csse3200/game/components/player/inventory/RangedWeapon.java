package com.csse3200.game.components.player.inventory;

import com.csse3200.game.entities.Entity;

public abstract class RangedWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public void onPickup(Entity entity) {
        entity.getComponent(InventoryComponent.class).setRanged(this);
    }
}
