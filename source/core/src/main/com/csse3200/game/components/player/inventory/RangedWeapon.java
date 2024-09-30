package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.logging.Logger;

public abstract class RangedWeapon implements Collectible {
    protected static final Logger logger = Logger.getLogger(RangedWeapon.class.getName());

    protected String name;        // name of the weapon
    protected String iconPath;    // path to the icon of the weapon
    protected int damage;         // weapon damage
    protected int range;          // range of the weapon
    protected int fireRate;       // fire rate of the weapon
    protected int ammo;           // current ammo left
    protected int maxAmmo;        // maximum ammo
    protected int reloadTime;     // reload time

    /**
     * Get the Type of this item.
     * The type determines how it ought to be used by the player.
     * @return Type.RANGED_WEAPON
     */
    @Override
    public Type getType() {
        return Type.RANGED_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        logger.info("Picking up ranged weapon - no entity");
        inventory.setRanged(this);
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetRanged();
    }

    @Override
    public String getSpecification() {
        return "ranged:" + getName();
    }

    /**
     * Get the name of the weapon.
     * @return The name of the weapon.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the weapon.
     * @param name The name of the weapon.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the ranged weapon icon.
     * @return The ranged weapon icon.
     */
    @Override
    public Texture getIcon() {
        return new Texture(iconPath);
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

    public abstract void shoot(Vector2 direction);
}