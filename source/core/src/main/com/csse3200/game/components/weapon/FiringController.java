package com.csse3200.game.components.weapon;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FiringController extends Component {
    private static final Logger logger = LoggerFactory.getLogger(FiringController.class);

    private Collectible weaponCollectible;
    private ProjectileFactory projectileFactory;

    // Ranged --------------------------------------------
    private int damage; // weapon damage
    private float range; // range of weapon
    private int fireRate; // fire rate of weapon (round per second)

    private ProjectileConfig projectileConfig; // Config file for this weapon's projectile
    private int ammo; // current ammo for shotgun only
    private int maxAmmo; // max ammo for shotgun only
    private int reloadTime; // reload time for shotgun only

    // variable for storing sprite of weapon
    // Note: this should have default sprite (not holding weapon) and sprite for each
    // weapon type with hand holding it
    private Sprite weaponSprite;
    // Tracking weapon state
    private long lastActivation; // Time of last ranged weapon activation, in seconds
    private final long activationInterval; // Interval between ranged weapon activation, in seconds

    private Entity player;
    private short targetLayer; // Layer to target for melee weapon
    private final boolean isMelee;

    public FiringController(RangedWeapon collectible, ProjectileConfig projectileConfig) {
        this.isMelee = false;
        this.damage = collectible.getDamage();
        this.range = collectible.getRange();
        this.fireRate = collectible.getFireRate();
        // Setup variables to track weapon state
        this.lastActivation = 0L;
        this.activationInterval = this.fireRate == 0?0 : (1000L / this.fireRate);
        this.ammo = collectible.getAmmo();
        this.maxAmmo = collectible.getMaxAmmo();
        this.reloadTime = collectible.getReloadTime();

        // Type of projectile
        this.projectileConfig = projectileConfig;

        this.weaponCollectible = collectible;
    }

    public FiringController(MeleeWeapon collectible) {
        this.isMelee = false;
        this.damage = collectible.getDamage();
        this.range = collectible.getRange();
        this.fireRate = collectible.getFireRate();
        // Setup variables to track weapon state
        this.lastActivation = 0L;
        this.activationInterval = this.fireRate == 0?0 : (1000L / this.fireRate);
        this.ammo = 0;
        this.maxAmmo = 0;
        this.reloadTime = 0;

        // Type of projectile
        this.projectileConfig = null;

        this.weaponCollectible = collectible;
    }

    /**
     * Create the projectile factory and weapon factory
     */
    @Override
    public void create() {
        // No action by default.
        this.projectileFactory = new ProjectileFactory();
    }

    public void connectPlayer(Entity player) {
        this.player = player;
    }
    public void disconnectPlayer() {
        this.player = null;
    }

    /**
     * Activate weapon on the direction
     * @param direction direction to shoot in, set to null for melees
     */
    public void activate(Vector2 direction) {
        if (this.isMelee) {
            shoot(direction);
        } else {
            attackMelee();
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
        Entity entity = this.getEntity();
        long currentTime = System.currentTimeMillis();
        if (entity != null) {
            if (currentTime - this.lastActivation <= this.activationInterval) {
                // Weapon not ready
                logger.info("Ranged weapon not ready");
                return;
            }
            if (this.getAmmo() == 0) {
                // Reloading
                this.setAmmo(-2);
                // Offset time so that the weapon must wait extra long for reload time
                currentTime += this.getReloadTime() * 1000L - this.activationInterval;

                logger.info("Ranged weapon reloading");
                ServiceLocator.getResourceService().playSound("sounds/shotgun1_r.ogg");
                entity.getEvents().trigger("RELOAD");
            } else {
                // Shooting
                this.setAmmo(-1);

                Entity projectile = projectileFactory.createProjectile(this.projectileConfig, direction, this.getEntity().getPosition());
                projectile.getComponent(ProjectileAttackComponent.class).create();
                ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9, 9), true, true);
                ServiceLocator.getResourceService().playSound("sounds/shotgun1_f.ogg");
                // Trigger event for animation controller
                triggerEvent(direction);
                logger.info("Ranged weapon shoot");
                entity.getEvents().trigger("RANGED_ATTACK");
            }
            // Reset last Attack time
            this.lastActivation = currentTime;
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
        Entity entity = this.getEntity();
        long currentTime = System.currentTimeMillis();
        if (entity != null) {
            if (currentTime - this.lastActivation <= this.activationInterval) {
                // Weapon not ready
                logger.info("Melee weapon not ready");
                return;
            }
            // Render attack here using
            this.lastActivation = currentTime;
            ServiceLocator.getResourceService().playSound("sounds/sword1.ogg");
            logger.info("Melee weapon attack");
            triggerEvent(this.player.getComponent(PlayerActions.class).getWalkDirection());
            // get all NPC entities in a map by accessing game area
            List<Entity> mapEntities = ServiceLocator.getGameAreaService()
                    .getGameArea().getListOfEntities();
            logger.info("Entities in map: " + mapEntities);
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
        if (this.entity.getComponent(CombatStatsComponent.class).getHealth() <= 0) {
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
            // if the target in front of the player
            // (1/4 of the circle, ex. if player face right, target in front is fromn 90 to 270 degree)
            Vector2 direction = e.getPosition().sub(this.player.getPosition()).nor();
            Vector2 playerDirection = this.player.getComponent(PlayerActions.class).getWalkDirection();
            if (direction.angleDeg(playerDirection) > 90 || direction.angleDeg(playerDirection) < -90) {
                continue; // Skip entities not in front of the player
            }

            HitboxComponent hitbox = e.getComponent(HitboxComponent.class);
            if (hitbox == null || hitbox.getLayer() != targetLayer) {
                continue; // Skip if no hitbox or wrong layer
            }

            CombatStatsComponent targetStats = e.getComponent(CombatStatsComponent.class);
            if (targetStats != null) {
                targetStats.hit(this.player.getComponent(CombatStatsComponent.class));
            }
        }

    }

    /**
     * Trigger shoot event base on shooting direction or walking direction of the player if this
     * is called for melee weapons
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
        } else if (direction.x == 1.0) {
            this.entity.getEvents().trigger("shootRight");
        } else if (direction.x == -1.0) {
            this.entity.getEvents().trigger("shootLeft");
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

}
