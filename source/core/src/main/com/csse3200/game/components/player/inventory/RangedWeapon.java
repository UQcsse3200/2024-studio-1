package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;

public abstract class RangedWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public void pickup(Entity entity) {
        entity.getComponent(InventoryComponent.class).setRanged(this);
        entity.getEvents().addListener("shoot", this::shoot);
    }

    @Override
    public void drop(Entity entity) {
        entity.getComponent(InventoryComponent.class).resetRanged();
    }

    /**
     * Fire this weapon.
     * @param direction The direction to fire it.
     */
    public abstract void shoot(Vector2 direction);
}
