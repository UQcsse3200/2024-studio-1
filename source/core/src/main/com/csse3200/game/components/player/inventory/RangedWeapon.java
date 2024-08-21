package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;

public abstract class RangedWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.setRanged(this);
        inventory.getEntity().getEvents().addListener("shoot", this::shoot);

        // Add a Weapon Component
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetRanged();
    }

    @Override
    public String getSpecification() {
        return "ranged:";
    }

    /**
     * Fire this weapon.
     *
     * @param direction The direction to fire it.
     */
    public abstract void shoot(Vector2 direction);
}
