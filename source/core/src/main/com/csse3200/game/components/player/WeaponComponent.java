package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponComponent extends Component{

    /**
     * Enum for weapon type (you can add more weapon types if needed)
     */
    public enum WeaponType {
        MELEE,
        SHOTGUN,
        NONE // barehanded
    }; 

    private static final Logger logger = LoggerFactory.getLogger(Component.class);

    private WeaponType weaponType; // type of weapon
    private int damage; // weapon damage
    private int range; // range of weapon
    private int fireRate; // fire rate of weapon
    private int ammo; // current ammo for shotgun only
    private int maxAmmo; // max ammo for shotgun only
    private int reloadTime; // reload time for shotgun only

    // variable for storing sprite of weapon
    // Note: this should have default sprite (not holding weapon) and sprite for each weapon type with hand holding it
    private Sprite weaponSprite;

    /**
     * Constructor for WeaponComponent
     * @param weaponSprite sprite of weapon (compulsory)
     * @param weaponType type of weapon (compulsory)
     * @param damage weapon damage
     * @param range range of weapon
     * @param fireRate fire rate of weapon
     * @param ammo current ammo for shotgun only
     * @param maxAmmo max ammo for shotgun only
     * @param reloadTime reload time for shotgun only
     */
    public WeaponComponent(Sprite weaponSprite, WeaponType weaponType, int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime) {
        System.out.println("WeaponComponent created");
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.ammo = ammo;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;
        this.weaponSprite = weaponSprite;
        this.weaponType = weaponType;
    }

    /**
     * Constructor for WeaponComponent with default values
     * Note: This is acceptable but not recommended
     * @param weaponSprite sprite of weapon (compulsory)
     * @param weaponType type of weapon (compulsory)
     */
    public WeaponComponent(Sprite weaponSprite, WeaponType weaponType) {
        new WeaponComponent(weaponSprite, weaponType, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Get the type of weapon
     * @return type of weapon
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Set the type of weapon to new value
     * @param weaponType new type of weapon
     * @return type of weapon
     */
    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    /**
     * Get the current weapon damage
     * @return weapon damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get the current range of weapon
     * @return range of weapon
     */
    public int getRange() {
        return range;
    }

    /**
     * Get the current fire rate of weapon (shots per second)
     * @return fire rate of weapon
     */
    public int getFireRate() {
        return fireRate;
    }

    /**
     * Get the current ammo of weapon (shotgun only)
     * @return ammo of weapon
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Get the max ammo of weapon (shotgun only)
     * @return max ammo of weapon
     */
    public int getMaxAmmo() {
        return maxAmmo;
    }

    /**
     * Get reloading time of weapon (both shotgun and melee)
     * @return reload time of weapon
     */
    public int getReloadTime() {
        return reloadTime;
    }

    /**
     * Set the weapon damage to new value (upgradable, buff,...)
     * @param damage new weapon damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Set the range of weapon to new value (shotgun only)
     * @param range
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Set the fire rate of weapon to new value (shotgun only)
     * @param fireRate new fire rate of weapon
     */
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Set the current ammo of weapon to new value (shotgun only)
     * Note: - leave ammo = -1 will automatically set ammo to (current ammo - 1)
     *       - leave ammo = -2 will automatically set ammo to max ammo
     * @param ammo new ammo of weapon
     */
    public void setAmmo(int ammo) {
        if (ammo == -1) {
            this.ammo -= 1;
        } else if (ammo == -2) {
            this.ammo = maxAmmo;
        } else {
            this.ammo = ammo;
        }
    }

    /**
     * Set the max ammo of weapon to new value (shotgun only)
     * @param maxAmmo new max ammo of weapon
     */
    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    /**
     * Set reloading time of weapon to new value (shotgun only)
     * @param reloadTime new reload time of weapon
     */
    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

}