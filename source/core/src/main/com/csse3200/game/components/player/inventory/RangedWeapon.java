package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.entities.Entity;

import java.util.logging.Logger;

public abstract class RangedWeapon implements Collectible {

    private static final Logger logger = Logger.getLogger(RangedWeapon.class.getName());

    private int damage;
    private int range;
    private int fireRate; // in shots per second
    private int ammo; // current ammo left
    private int maxAmmo;
    private int reloadTime; // in seconds

    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        logger.info("Picking up ranged weapon - no entity");
        inventory.setRanged(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this);
        } else {
            logger.warning("Inventory entity or WeaponComponent is null");
        }
    }

    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        logger.info("Picking up ranged weapon - with entity");
        inventory.setRanged(this);

        // Add a Weapon Component
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            logger.info("Setting ranged weapon in inventory");
            inventory.getEntity().getComponent(WeaponComponent.class).updateWeapon(this, itemEntity);
        } else {
            logger.info("Inventory entity or WeaponComponent is null");
        }
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetRanged();

        // Switch to default weapon (bare hands)
        if (inventory.getEntity() != null && inventory.getEntity().getComponent(WeaponComponent.class) != null) {
            inventory.getEntity().getComponent(WeaponComponent.class).dropRangeWeapon();
        }
    }

    @Override
    public String getSpecification() {
        return "ranged:" + getRangedSpecification();
    }

    /**
     * Get the specification of this ranged weapon.
     *
     * @return the string representation of this ranged weapon.
     */
    public abstract String getRangedSpecification();

    /**
     * Fire this weapon.
     *
     * @param direction The direction to fire it.
     */
    public abstract void shoot(Vector2 direction);

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }
}
