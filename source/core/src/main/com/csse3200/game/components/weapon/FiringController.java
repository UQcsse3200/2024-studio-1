package com.csse3200.game.components.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.weapons.*;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * Controller for firing weapons (predecessor: WeaponComponent)
 */
public class FiringController extends Component {

    /**
     * Logger for debugging purposes.
     */
    private static final Logger logger = LoggerFactory.getLogger(FiringController.class);

    private ProjectileFactory projectileFactory;

    private int damage;                                 // weapon damage
    private float range;                                // range of weapon
    private int fireRate;                               // fire rate of weapon (round per second)

    private final ProjectileConfig projectileConfig;          // Config file for this weapon's projectile
    private int ammo;                                   // current ammo for ranged only
    private int maxAmmo;                                // max ammo for ranged only
    private int reloadTime;                             // reload time for ranged only

    // variable for storing sprite of weapon
    // Note: this should have default sprite (not holding weapon) and sprite for each
    // weapon type with hand holding it
    private Sprite weaponSprite;
    // Tracking weapon state
    private final long activationInterval;              // Interval between ranged weapon
    // activation, in miliseconds

    private Entity player;
    private short targetLayer;                          // Layer to target for melee weapon
    private final boolean isMelee;
    private boolean isReady = true;

    /**
     * Initialize the firing controller for ranged weapon
     *
     * @param collectible      the weapon to get the specification from
     * @param projectileConfig the projectile configuration for this weapon
     */
    public FiringController(RangedWeapon collectible, ProjectileConfig projectileConfig) {
        this.isMelee = false;

        //setup projectile config for values from the collectable value
        this.damage = collectible.getDamage();
        this.range = collectible.getRange();
        projectileConfig.baseAttack = damage;
        projectileConfig.range = range;
        this.projectileConfig = projectileConfig;

        this.fireRate = collectible.getFireRate();

        // Setup variables to track weapon state
        this.activationInterval = this.fireRate == 0 ? 0 : (1000L / this.fireRate);
        this.ammo = collectible.getAmmo();
        this.maxAmmo = collectible.getMaxAmmo();
        this.reloadTime = collectible.getReloadTime();
    }

    /**
     * Initialize the firing controller for melee weapon
     *
     * @param collectible the weapon to get the specification from
     */
    public FiringController(MeleeWeapon collectible) {
        this.isMelee = true;
        this.damage = collectible.getDamage();
        this.range = collectible.getRange();
        this.fireRate = collectible.getFireRate();
        // Setup variables to track weapon state
        this.activationInterval = this.fireRate == 0 ? 0 : (1000L / this.fireRate);
        this.ammo = 0;
        this.maxAmmo = 0;
        this.reloadTime = 0;

        // Type of projectile
        this.projectileConfig = null;
    }

    /**
     * Create the projectile factory
     */
    @Override
    public void create() {
        // No action by default.
        this.projectileFactory = new ProjectileFactory();
    }

    /**
     * Store the player that are using this weapon
     *
     * @param player the Player entity that is holing the weapon
     */
    public void connectPlayer(Entity player) {
        this.player = player;
        this.targetLayer = PhysicsLayer.NPC;
    }

    /**
     * Remove player from this weapon
     */
    public void disconnectPlayer() {
        this.player = null;
    }

    /**
     * Activate weapon on the direction specified
     * If the weapon is ranged, it will shoot in the direction specified (direction not null)
     * If the weapon is melee, it will attack in the direction the player is walking
     *
     * @param direction direction to shoot in, set to null for melees
     * @return message to indicate the weapon attack
     */
    public String activate(Vector2 direction) {
        if (this.isMelee) {
            try {
                attackMelee();
            } catch (Exception e) {
                logger.info(e.getMessage());
                return "Melee weapon attack failed";
            }
            return "Melee weapon attack triggered";
        } else {
            try {
                if (direction == null) {
                    throw new NullPointerException();
                }
                shoot(direction);
            } catch (NullPointerException e) {
                logger.info("No direction specified for ranged weapon");
                return "No direction specified for ranged weapon";
            }
            return "Ranged weapon attack triggered";
        }
    }

    /**
     * Shoot the ranged weapon in the given direction
     * The weapon will only shoot if it has ammo left and the time from last shoot is longer than
     * specified
     *
     * @param direction direction to shoot
     */
    private void shoot(Vector2 direction) {
        logger.info("Ranged weapon attack triggered");
        if (this.player == null) {
            logger.info("Player not connected");
            return;
        }
        if (this.getEntity() != null) {
            if (!isReady) {
                // Weapon not ready
                logger.info("Ranged weapon not ready");
                return;
            }
            // Shooting
            this.setAmmo(-1);
            // Create projectiles
            spawnProjectile(direction);
            // Trigger sound
            ServiceLocator.getResourceService().playSound("sounds/shotgun1_f.ogg");
            // Trigger event for animation controller
            triggerEvent(direction);
            logger.info("Ranged weapon shoot");
            // Check if the weapon needs to be reloaded or not
            checkState();
        } else {
            logger.info("No ranged weapon");
        }
    }

    /**
     * Use the melee weapon in the walk direction of the player
     * The weapon will only activate if the time from last activation is longer than
     * specified
     */
    private void attackMelee() {
        logger.info("Melee weapon attack triggered");
        if (this.player == null) {
            logger.info("Player not connected");
            return;
        }
        if (this.getEntity() != null) {
            if (!isReady) {
                // Weapon not ready
                logger.info("Melee weapon not ready");
                return;
            }
            // Set ready state
            isReady = false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isReady = true;
                }
            }, (float) this.activationInterval / 1000L);
            ServiceLocator.getResourceService().playSound("sounds/sword1.ogg");
            logger.info("Melee weapon attack");
            triggerEvent(this.player.getComponent(PlayerActions.class).getWalkDirection());
            // get all NPC entities in a map by accessing game area
            List<Entity> mapEntities = ServiceLocator.getGameAreaService()
                    .getGameArea().getListOfEntities();
            //logger.info("Entities in map: " + mapEntities);
            logger.info("Entities in map: {0}", mapEntities);
            applyFilteredDamage(mapEntities);
        } else {
            logger.info("No melee weapon");
        }
    }

    /**
     * Apply damage to entities in the map such that
     * - The entity is not the current entity
     * - The entity is within the swing range
     * - The entity has a hitbox component
     * - The entity is in the target layer
     * - The entity has combat stats component
     *
     * @param mapEntities list of entities in the map
     */
    private void applyFilteredDamage(List<Entity> mapEntities) {
        // if player still survive
        if (this.player.getComponent(CombatStatsComponent.class).getHealth() <= 0) {
            return;
        }

        for (Entity e : mapEntities) {
            if (e == null || e.getId() == this.player.getId()) {
                continue; // Skip null entities and the current entity
            }

            float distance = e.getPosition().dst(this.player.getPosition());
            if (distance > this.range) {
                continue; // Skip entities outside swing range
            }

            HitboxComponent hitbox = e.getComponent(HitboxComponent.class);
            if (hitbox == null || hitbox.getLayer() != targetLayer) {
                continue; // Skip if no hitbox or wrong layer
            }

            CombatStatsComponent targetStats = e.getComponent(CombatStatsComponent.class);
            if (targetStats != null) {
                targetStats.hit(this.entity.getComponent(CombatStatsComponent.class));
            }
        }

    }

    /**
     * Trigger shoot event base on shooting direction or walking direction of the player if this
     * is called for melee weapons.
     *
     * @param direction The direction to shoot in
     */
    private void triggerEvent(Vector2 direction) {
        if (direction.x == 0.0) {
            if (direction.y > 0.0) {
                this.entity.getEvents().trigger("shootUp");
            } else {
                this.entity.getEvents().trigger("shootDown");
            }
        } else if (direction.x > 0) {
            this.entity.getEvents().trigger("shootRight");
        } else if (direction.x < 0) {
            this.entity.getEvents().trigger("shootLeft");
        }
        // Trigger event for weapon UIW
        if (this.player != null && !this.isMelee) {

            this.player.getEvents().trigger("ranged_activate", this.ammo);
        }
    }

    /**
     * Set weapon to not ready.
     * If the weapon still have ammo, create timer to set the weapon to ready after activation
     * interval.
     * Otherwise, create timer to set isReady to true after reload time.
     */
    private void checkState() {
        // Check for reloading
        isReady = false;
        if (this.getAmmo() != 0) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isReady = true;
                }
            }, (float) this.activationInterval / 1000L);
        }
        else {
            logger.info("Ranged weapon reloading");
            ServiceLocator.getResourceService().playSound("sounds/shotgun1_r.ogg");
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    setAmmo(-2);
                    getPlayer().getEvents().trigger("ranged_activate", getMaxAmmo());
                    isReady = true;
                }
            }, this.reloadTime);
        }
    }

    /**
     * Spawn projectile(s) for this weapon.
     * Will spawn multiple projectiles (shotgun spray) for weapons with range > 0.
     * Otherwise, will spawn single projectile with unlimited range.
     * @param direction direction to shoot the projectile(s)
     */
    private void spawnProjectile(Vector2 direction) {
        if (this.range == 0) {
            // Not a shotgun
            Entity projectile = projectileFactory.createProjectile(this.projectileConfig, direction,
                    entity.getPosition());
            ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile,
                    new GridPoint2(9, 9), true, true);
            return;
        }
        // Is a shotgun
        for (Entity e : projectileFactory.createShotGunProjectile(this.projectileConfig,
                direction, this.getEntity().getPosition())) {
            ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(e,
                    new GridPoint2(9, 9), true, true);
        }
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
        if (this.isMelee) {
            return 0;
        } else {
            return ammo;
        }
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
            this.ammo = this.getMaxAmmo();
        } else if (ammo < this.maxAmmo && ammo > 0) {
            // If value is not expected, default to max ammo
            this.ammo = ammo;
        } else {
            this.ammo = getMaxAmmo();
        }
    }

    /**
     * Get the max ammo of weapon (shotgun only)
     *
     * @return max ammo of weapon
     */
    public int getMaxAmmo() {
        if (this.isMelee) {
            return 0;
        } else {
            return maxAmmo;
        }
    }

    /**
     * Set the max ammo of weapon to new value (shotgun only)
     *
     * @param maxAmmo new max ammo of weapon
     */
    public void setMaxAmmo(int maxAmmo) {
        if (!this.isMelee) {
            this.maxAmmo = maxAmmo;
        }
    }

    /**
     * Get reloading time of weapon (both shotgun and melee)
     *
     * @return reload time of weapon
     */
    public int getReloadTime() {
        if (this.isMelee) {
            return 0;
        } else {
            return reloadTime;
        }
    }

    /**
     * Set reloading time of weapon to new value (shotgun only)
     *
     * @param reloadTime new reload time of weapon
     */
    public void setReloadTime(int reloadTime) {
        if (reloadTime < 0 && !this.isMelee) {
            throw new IllegalArgumentException("Reload time must be greater than 0");
        } else {
            this.reloadTime = reloadTime;
        }
    }

    /**
     * Get the sprite of the weapon
     *
     * @return the sprite of the weapon
     */
    public Sprite getWeaponSprite() {
        return weaponSprite;
    }

    /**
     * Set the sprite of the weapon
     *
     * @param sprite the sprite to set
     */
    public void setWeaponSprite(Sprite sprite) {
        this.weaponSprite = sprite;
    }

    public ProjectileFactory getProjectileFactory() {
        // return the projectile factory
        return this.projectileFactory;
    }

    /**
     * Get the player that is using this weapon
     *
     * @return the player entity
     */
    public Entity getPlayer() {
        return this.player;
    }

    /**
     * Get the target layer of the weapon
     *
     * @return the target layer
     */
    public short getTargetLayer() {
        return this.targetLayer;
    }
}
