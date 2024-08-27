package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.player.WeaponComponent;

public abstract class MeleeWeapon implements Collectible {

    private int damage;
    private int range;
    private int fireRate;

    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.setMelee(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this); // update existing weapon
        }
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();

        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(); // remove weapon
        }
    }

    @Override
    public String getSpecification() {
        return "melee:" + getMeleeSpecification();
    }

    /**
     * Get the specification of this melee weapon.
     *
     * @return the string representation of this melee weapon.
     */
    public abstract String getMeleeSpecification();

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }
}
