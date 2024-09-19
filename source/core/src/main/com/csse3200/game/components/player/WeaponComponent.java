package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class WeaponComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(WeaponComponent.class);
    private Collectible.Type weaponType; // type of weapon
    private final ProjectileFactory projectileFactory = new ProjectileFactory();

    // Ranged --------------------------------------------
    private int damage; // weapon damage
    private float range; // range of weapon
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
    private long lastAttack; // Time of last ranged weapon activation, in seconds
    private long attackInterval; // Interval between ranged weapon activation, in seconds


    // Melee ---------------------------------------------
    private int swingDamage; // Damage of each swing for melee weapon
    private float swingRange; // Range of melee weapon
    private int swingRate; // swing rate for melee weapon (swing per second


    // Tracking weapon state
    private long lastSwing; // Time of last melee weapon activation, in seconds
    private long swingInterval; // Interval between melee weapon activation, in seconds

    Vector2 lastPos; // last position of the weapon entity, used to determine direction

    Entity rangedItemEntity; // the ranged weapon entity
    Entity meleeItemEntity; // the melee weapon entity

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
    public WeaponComponent(Sprite weaponSprite, Collectible.Type weaponType, int damage, float range,
                           int fireRate, int ammo, int maxAmmo, int reloadTime) {
//        System.out.println("WeaponComponent created");
        if (weaponType == Collectible.Type.MELEE_WEAPON) {
            this.swingDamage = damage;
            this.swingRate = fireRate;
            this.swingRange = range;
            this.weaponSprite = weaponSprite;

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

        this.rangedItemEntity = null;
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
            new WeaponComponent(weaponSprite, weaponType, 600, 3f, 1, 0, 0, 0);
        } else {
            new WeaponComponent(weaponSprite, weaponType, 600, 3f, 1, 1, 1, 1);
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
    public float getRange() {
        return range;
    }

    /**
     * Set the range of weapon to new value (shotgun only)
     *
     * @param range the new ranged to set the weapon to.
     */
    public void setRange(float range) {
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
     * Update the weapon with new ranged weapon
     * @param rangedWeapon new ranged weapon
     */
    public void updateWeapon(RangedWeapon rangedWeapon) {
        logger.debug("Updating weapon - no item entity");
        this.damage = rangedWeapon.getDamage();
        this.range = rangedWeapon.getRange();
        this.fireRate = rangedWeapon.getFireRate();
        this.ammo = rangedWeapon.getAmmo();
        this.maxAmmo = rangedWeapon.getMaxAmmo();
        this.reloadTime = rangedWeapon.getReloadTime();
        this.lastAttack = 0L;
        this.getEntity().getComponent(CombatStatsComponent.class).setBaseAttack(600);
        if (this.fireRate == 0) {
            this.attackInterval = 0L;
        } else {
            this.attackInterval = (1000L / this.fireRate);
        }
    }

    /**
     * Update the weapon with new values (range only) and the weapon entity
     *
     * @param rangedWeapon new melee weapon
     * @param itemEntity new ranged weapon entity
     */
    public void updateWeapon(RangedWeapon rangedWeapon, Entity itemEntity) {
        logger.info("Updating weapon - with item entity");
        this.damage = rangedWeapon.getDamage();
        this.range = rangedWeapon.getRange();
        this.fireRate = rangedWeapon.getFireRate();
        this.ammo = rangedWeapon.getAmmo();
        this.maxAmmo = rangedWeapon.getMaxAmmo();
        this.reloadTime = rangedWeapon.getReloadTime();
        this.lastAttack = 0L;
        this.getEntity().getComponent(CombatStatsComponent.class).setBaseAttack(this.damage);
        if (this.fireRate == 0) {
            this.attackInterval = 0L;
        } else {
            this.attackInterval = (1000L / this.fireRate);
        }
        this.rangedItemEntity = itemEntity;
        this.rangedItemEntity.getComponent(WeaponAnimationController.class).updateHost(this.entity);
    }

    /**
     * Update the weapon with new values (melee only)
     *
     * @param meleeWeapon new melee weapon
     */
    public void updateWeapon(MeleeWeapon meleeWeapon) {
        this.swingDamage = meleeWeapon.getDamage();
        this.swingRange = meleeWeapon.getRange();
        this.swingRate = meleeWeapon.getFireRate();
        this.lastSwing = 0L; // 1000 means 1 second ago
        if (this.swingRate == 0) {
            this.swingInterval = 0L;
        } else {
            this.swingInterval = (1000L / this.swingRate);
        }
    }

    /**
     * Update the weapon with new values (melee only) and the weapon entity
     *
     * @param meleeWeapon new melee weapon
     * @param itemEntity new melee weapon entity
     */
    public void updateWeapon(MeleeWeapon meleeWeapon, Entity itemEntity) {
        this.swingDamage = meleeWeapon.getDamage();
        this.swingRange = meleeWeapon.getRange();
        this.swingRate = meleeWeapon.getFireRate();
        this.lastSwing = 0L;
        if (this.swingRate == 0) {
            this.swingInterval = 0L;
        } else {
            this.swingInterval = (1000L / this.swingRate);
        }
        this.meleeItemEntity = itemEntity;
//        getEntity().getComponent(RangeDetectionComponent.class).updateWeaponEntity(itemEntity);
        getEntity().getComponent(CombatStatsComponent.class).setBaseAttack(this.swingDamage);
    }

    @Override
    public void update() {
        if (this.entity != null) {
            if (this.lastPos == null) {
                this.lastPos = entity.getPosition();
            }
            Vector2 newPos = this.entity.getPosition();
            float dx = newPos.x - this.lastPos.x;
            float dy = newPos.y - this.lastPos.y;
            if (this.meleeItemEntity != null) {
                this.meleeItemEntity.setPosition(this.entity.getPosition());
            }
            if (this.rangedItemEntity != null) {
                if (dx == 0 && dy > 0) {
                    logger.info("Range weapon point up");
                }
                else if (dx == 0 && dy < 0) {
                    logger.info("Range weapon point down");
                }
                else if (dx > 0 && dy == 0) {
                    logger.info("Range weapon point right");
                }
                else if (dx < 0 && dy == 0) {
                    logger.info("Range weapon point left");
                }
                this.rangedItemEntity.setPosition(this.entity.getPosition());
                this.lastPos = newPos;
            }
        }
    }

    /**
     * Drop range weapon, set all related properties to default
     */
    public void dropRangeWeapon() {
        // Update range weapon to bare hand
        this.damage = 1;
        this.range = 1;
        this.fireRate = 1;
        this.ammo = -1; // -1 means no ammo
        this.maxAmmo = -1; // -1 means no ammo
        this.reloadTime = -1; // -1 means no reload time
    }
    /**
     * Drop melee weapon, set all related properties to default
     */
    public void dropMeleeWeapon() {
        // Update melee weapon to bare hand
        this.swingDamage = 1;
        this.swingRange = 1;
        this.swingRate = 1;
    }

    /**
     * Use the melee weapon in the walk direction of the player
     * The weapon will only activate if the time from last activation is longer than
     * specified
     *
     */
    public void attack() {
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
            ServiceLocator.getResourceService()
                    .getAsset("sounds/sword1.ogg", Sound.class)
                    .play();
            logger.info("Melee weapon attack");
            ArrayList<Entity> entities = this.getEntity().getComponent(RangeDetectionComponent.class).getEntities();
            for (Entity e : entities) {
                CombatStatsComponent targetStats = e.getComponent(CombatStatsComponent.class);
                if (targetStats != null) {
                    targetStats.hit(this.getEntity().getComponent(CombatStatsComponent.class));
                }
            }
        } else {
            logger.info("No melee weapon");
        }
    }

    /**
     * Shoot the ranged weapon in the given direction
     * The weapon will only shoot if it has ammo left and the time from last shoot is longer than
     * specified
     *
     * @param direction direction to shoot
     */
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

                logger.info("Ranged weapon reloading");
                entity.getEvents().trigger("RELOAD");
            } else {
                // Shooting
                this.setAmmo(-1);

//                Entity projectile = projectileFactory.createProjectile(this.bulletConfig, direction, this.getEntity().getPosition());
                projectileFactory.createShotGunProjectile(this.bulletConfig, direction, this.getEntity().getPosition());
                logger.info("Ranged weapon shoot");
                entity.getEvents().trigger("RANGED_ATTACK");

            }
            // Reset last Attack time
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
