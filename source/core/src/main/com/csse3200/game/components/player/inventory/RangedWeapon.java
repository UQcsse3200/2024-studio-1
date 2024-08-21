package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.WeaponComponent;

public abstract class RangedWeapon implements Collectible {

    private int damage;
    private int range;
    private int fireRate;
    private int ammo; // current ammo left
    private int maxAmmo;
    private int reloadTime;

    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.setRanged(this);
        inventory.getEntity().getEvents().addListener("shoot", this::shoot);

        // Add a Weapon Component
        inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this);
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

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }
}
