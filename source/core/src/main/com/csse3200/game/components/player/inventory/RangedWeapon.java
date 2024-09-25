package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.entities.Entity;

import java.util.logging.Logger;

public class RangedWeapon implements Collectible {

    private static final Logger logger = Logger.getLogger(RangedWeapon.class.getName());

    private String name;
    private String iconPath;
    private int damage;
    private int range;
    private int fireRate;
    private int ammo; // current ammo left
    private int maxAmmo;
    private int reloadTime;

    /**
     * Constructor for ranged weapons.
     * @param name The name of the weapon.
     * @param iconPath The path to the icon of the weapon.
     * @param damage The damage of the weapon.
     * @param range The range of the weapon.
     * @param fireRate The fire rate of the weapon.
     * @param ammo The current ammo of the weapon.
     * @param maxAmmo The maximum ammo of the weapon.
     * @param reloadTime The reload time of the weapon.
     */
    public RangedWeapon(String name, String iconPath, int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime) {
        this.name = name;
        this.iconPath = iconPath;
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.ammo = ammo;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;
    }

    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
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
        return "ranged:" + getName();
    }

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

    @Override
    public Texture getIcon() {
        return new Texture(iconPath);
    }

    public void shoot(Vector2 direction) {
        if (getAmmo() > 0) {
            System.out.println(name + " fired in direction: " + direction);
            setAmmo(getAmmo() - 1);
        } else {
            System.out.println("Out of ammo! Need to reload.");
        }
    }
}
