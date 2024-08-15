package com.csse3200.game.components.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.csse3200.game.components.Component;

public class WeaponComponent extends Component{

    private static final Logger logger = LoggerFactory.getLogger(Component.class);

   
    private WeaponType weaponType; // weapon type
    private int damage; // damage of weapon
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
     * @param weaponSprite sprite of weapon (complulsory)
     * @param weaponType type of weapon (complulsory)
     * @param damage damage of weapon
     * @param range range of weapon
     * @param fireRate fire rate of weapon
     * @param ammo current ammo for shotgun only
     * @param maxAmmo max ammo for shotgun only
     * @param reloadTime reload time for shotgun only
     */
    public WeaponComponent(Sprite weaponSprite, WeaponType weaponType, int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime, int reloadTimer) {
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
     * @param weaponSprite sprite of weapon (complulsory)
     * @param weaponType type of weapon (complulsory)
     */
    public WeaponComponent(Sprite weaponSprite, WeaponType weaponType) {
        new WeaponComponent(weaponSprite, weaponType, 0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Get the current damage of weapon
     * @return damage of weapon
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
     * Get the reload time of weapon (both shotgun and melee)
     * @return reload time of weapon
     */
    public int getReloadTime() {
        return reloadTime;
    }

    /**
     * Set the damage of weapon to new value (upgradable, buff,...)
     * @param damage new damage of weapon
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
     * Set the reload time of weapon to new value (shotgun only)
     * @param reloadTime new reload time of weapon
     */
    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

}

/**
 * Enum for WeaponType
 */
enum WeaponType {
    MELEE,
    SHOTGUN
}