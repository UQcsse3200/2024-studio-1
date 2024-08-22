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
        inventory.getEntity().getEvents().addListener("melee", this::attack);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this);
        }
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();
    }

    @Override
    public String getSpecification() {
        return "melee:";
    }

    /**
     * Swing this weapon
     */
    public abstract void attack();

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
