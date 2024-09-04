package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(WeaponComponent.class);
    private Collectible.Type weaponType; // type of weapon

    // Ranged
    private int damage; // weapon damage
    private int range; // range of weapon
    private int fireRate; // fire rate of weapon (round per second)


    private ProjectileConfig bulletConfig; // Config file for this weapon's projectile
    private int ammo; // current ammo for shotgun only
    private int maxAmmo; // max ammo for shotgun only
    private int reloadTime; // reload time for shotgun only
    // variable for storing sprite of weapon
    // Note: this should have default sprite (not holding weapon) and sprite for each
    // weapon type with hand holding it
    private Sprite weaponSprite;

    // Tracking weapon state
    private long lastAttack;
    private long attackInterval;

    // Melee
    private int swingDamge; // Damage of each swing for melee weapon
    private int swingRange; // Range of melee weapon
    private int swingRate; // swing rate for melee weapon (swing per second

    private Sprite meleeSprite;

    // Tracking weapon state
    private long lastSwing;
    private long swingInterval;

    /**
     * Constructor for WeaponComponent
     *
     * @param weaponSprite sprite of weapon (compulsory)
     * @param weaponType   type of weapon (compulsory)
     * @param damage       weapon damage
     * @param range        range of weapon
     * @param fireRate     fire rate of weapon
     * @param ammo         current ammo for shotgun only
     * @param maxAmmo      max ammo for shotgun only
     * @param reloadTime   reload time for shotgun only
     */
    public WeaponComponent(Sprite weaponSprite, Collectible.Type weaponType, int damage, int range,
                           int fireRate, int ammo, int maxAmmo, int reloadTime) {
//        System.out.println("WeaponComponent created");
        if (weaponType == Collectible.Type.MELEE_WEAPON) {
            this.swingDamge = damage;
            this.swingRate = fireRate;
            this.swingRange = range;
            this.meleeSprite = weaponSprite;

            // Setup variables to track weapon state
            this.lastAttack = 0L;
            if (this.swingRate == 0) {
                this.swingInterval = 0L;
            } else {
                this.swingInterval = (1000L / this.swingRate);
            }
        }
        else if (weaponType == Collectible.Type.RANGED_WEAPON) {
            this.damage = damage;
            this.range = range;
            this.fireRate = fireRate;

            this.weaponSprite = weaponSprite;

            // Setup variables to track weapon state
            this.lastAttack = 0L;
            if (this.fireRate == 0) {
                this.attackInterval = 0L;
            } else {
                this.attackInterval = (1000L / this.fireRate);
            }
        }
        this.ammo = ammo;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;

        // Currently has only 1 projectile config
        this.bulletConfig = new ProjectileConfig();

    }

    /**
     * Constructor for WeaponComponent with default values
     * Note: This is acceptable but not recommended
     *
     * @param weaponSprite sprite of weapon (compulsory)
     * @param weaponType   type of weapon (compulsory)
     */
    public WeaponComponent(Sprite weaponSprite, Collectible.Type weaponType) {
        if (weaponType == Collectible.Type.MELEE_WEAPON) {
            new WeaponComponent(weaponSprite, weaponType, 1, 1, 1, 0, 0, 0);
        } else {
            new WeaponComponent(weaponSprite, weaponType, 1, 1, 1, 1, 1, 1);
        }
    }

    /**
     * Get the type of weapon
     *
     * @return type of weapon
     */
    public Collectible.Type getWeaponType() {
        return weaponType;
    }

    /**
     * Set the type of weapon to new value
     *
     * @param weaponType new type of weapon
     */
    public void setWeaponType(Collectible.Type weaponType) {
        this.weaponType = weaponType;
    }

    /**
     * Get the current weapon damage
     *
     * @return weapon damage
     */
    public int getDamage() {
        return damage;
    }


    /**
     * Set the weapon damage to new value (upgradable, buff,...)
     *
     * @param damage new weapon damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Get the current range of weapon
     *
     * @return range of weapon
     */
    public int getRange() {
        return range;
    }

    /**
     * Set the range of weapon to new value (shotgun only)
     *
     * @param range the new ranged to set the weapon to.
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Get the current fire rate of weapon (shots per second)
     *
     * @return fire rate of weapon
     */
    public int getFireRate() {
        return fireRate;
    }

    /**
     * Set the fire rate of weapon to new value (shotgun only)
     *
     * @param fireRate new fire rate of weapon
     */
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Get the current ammo of weapon (shotgun only)
     *
     * @return ammo of weapon
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Set the current ammo of weapon to new value (shotgun only)
     * Note: - leave ammo = -1 will automatically set ammo to (current ammo - 1)
     * - leave ammo = -2 will automatically set ammo to max ammo
     *
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
     * Get the max ammo of weapon (shotgun only)
     *
     * @return max ammo of weapon
     */
    public int getMaxAmmo() {
        return maxAmmo;
    }

    /**
     * Set the max ammo of weapon to new value (shotgun only)
     *
     * @param maxAmmo new max ammo of weapon
     */
    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    /**
     * Get reloading time of weapon (both shotgun and melee)
     *
     * @return reload time of weapon
     */
    public int getReloadTime() {
        return reloadTime;
    }

    /**
     * Set reloading time of weapon to new value (shotgun only)
     *
     * @param reloadTime new reload time of weapon
     */
    public void setReloadTime(int reloadTime) {
        if (reloadTime < 0) {
            throw new IllegalArgumentException("Reload time must be greater than 0");
        } else {
            this.reloadTime = reloadTime;
        }
    }

    /**
     * Update the weapon with new values (ranged only)
     *
     * @param rangedWeapon ranged weapon
     */
    public void updateWeapon(RangedWeapon rangedWeapon) {
        this.damage = rangedWeapon.getDamage();
        this.range = rangedWeapon.getRange();
        this.fireRate = rangedWeapon.getFireRate();
        this.ammo = rangedWeapon.getAmmo();
        this.maxAmmo = rangedWeapon.getMaxAmmo();
        this.reloadTime = rangedWeapon.getReloadTime();
        this.lastAttack = 0L;
        if (this.fireRate == 0) {
            this.attackInterval = 0L;
        } else {
            this.attackInterval = (1000L / this.fireRate);
        }
    }

    /**
     * Update the weapon with new values (melee only)
     *
     * @param meleeWeapon new melee weapon
     */
    public void updateWeapon(MeleeWeapon meleeWeapon) {
        this.swingDamge = meleeWeapon.getDamage();
        this.swingRange = meleeWeapon.getRange();
        this.swingRate = meleeWeapon.getFireRate();
        this.lastSwing = 0L;
        if (this.swingRate == 0) {
            this.swingInterval = 0L;
        } else {
            this.swingInterval = (1000L / this.swingRate);
        }
    }

    public void dropRangeWeapon() {
        // Update range weapon to bare hand
        this.damage = 1;
        this.range = 1;
        this.fireRate = 1;
        this.ammo = -1; // -1 means no ammo
        this.maxAmmo = -1; // -1 means no ammo
        this.reloadTime = -1; // -1 means no reload time
    }
    public void dropMeleeWeapon() {
        // Update melee weapon to bare hand
        this.swingDamge = 1;
        this.swingRange = 1;
        this.swingRate = 1;
    }

    public void attack() {
//        logger.info("WeaponComponent attack");
        Entity entity = this.getEntity();
        long currentTime = System.currentTimeMillis();
        if (entity != null) {
            if (currentTime - this.lastSwing <= this.swingInterval) {
                // Weapon not ready
                logger.info("Melee weapon not ready");
                return;
            }
            // Render attack here using
            this.lastSwing = currentTime;
            ServiceLocator.getResourceService().playSound("sounds/Impact4.ogg");
            logger.info("Melee weapon attack");
        } else {
            logger.info("No melee weapon");
        }
    }

    public void shoot(Vector2 direction) {
        Entity entity = this.getEntity();
        long currentTime = System.currentTimeMillis();
        if (entity != null) {
            if (currentTime - this.lastAttack <= this.attackInterval) {
                // Weapon not ready
                logger.info("Ranged weapon not ready");
                return;
            }
            if (this.getAmmo() == 0) {
                // Reloading
                this.setAmmo(-2);
                // Offset time so that the weapon must wait extra long for reload time
                currentTime += this.getReloadTime() * 1000L - this.attackInterval;
                ServiceLocator.getResourceService().playSound("sounds/shotgun1_r.ogg");
                logger.info("Ranged weapon reloading");
            } else {
                // Shooting
                this.setAmmo(-1);
                // Spawn projectile
                ServiceLocator.getResourceService().playSound("sounds/shotgun1_f.ogg");
                ProjectileFactory.createProjectile(this.bulletConfig, direction);
                logger.info("Ranged weapon shoot");
            }
            // Reset lastAtttack time
            this.lastAttack = currentTime;
        } else {
            logger.info("No ranged weapon");
        }
    }

    public Sprite getWeaponSprite() {
        return weaponSprite;
    }

    public void setWeaponSprite(Sprite sprite) {
        this.weaponSprite = sprite;
    }
}
