package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.player.WeaponComponent;

public abstract class MeleeWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.setMelee(this);
        inventory.getEntity().getEvents().addListener("melee", this::attack);

        // Add a Weapon Component
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();
    }

    /**
     * Swing this weapon
     */
    public abstract void attack();
}
