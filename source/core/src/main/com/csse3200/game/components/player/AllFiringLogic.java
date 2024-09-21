package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.WeaponComponent;
import com.csse3200.game.components.player.RangeDetectionComponent;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;



public class AllFiringLogic extends Component {
    private Entity entity;
    private ProjectileFactory projectileFactory;
    private ProjectileConfig bulletConfig;
    private static final Logger logger = LoggerFactory.getLogger(WeaponComponent.class);
    private RangeDetectionComponent rangeDetection;

    // Ranged Weapon Variables
    private int damage;
    private float range;
    private int fireRate;
    private int ammo;
    private int reloadTime;

    // Melee Weapon Variables
    private int swingDamage;
    private int swingRate;

    private long lastAttack;
    private long attackInterval;
    private long lastSwing;
    private long swingInterval;

    public AllFiringLogic(Entity entity, ProjectileFactory projectileFactory, ProjectileConfig bulletConfig) {
        this.entity = entity;
        this.projectileFactory = projectileFactory;
        this.bulletConfig = bulletConfig;
    }

    public void configureRangedWeapon(int damage, float range, int fireRate, int ammo, int reloadTime) {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.ammo = ammo;
        this.reloadTime = reloadTime;

        this.attackInterval = fireRate == 0 ? 0 : (1000L / fireRate);
    }

    public void configureMeleeWeapon(int swingDamage, int swingRate) {
        this.swingDamage = swingDamage;
        this.swingRate = swingRate;

        this.swingInterval = swingRate == 0 ? 0 : (1000L / swingRate);
    }

    public void shoot(Vector2 direction) {
        long currentTime = System.currentTimeMillis();
            if (currentTime - lastAttack <= attackInterval) {
                logger.info("Ranged weapon not ready");
                return; // Weapon not ready
            }
            if (ammo == 0) {
                logger.info("Out of ammo, reloading...");
                reload(); // Handle reloading
                return;
            } else {
                // Shooting logic
                ammo--;
                Entity projectile = projectileFactory.createProjectile(bulletConfig, direction, entity.getPosition());
                projectile.getComponent(ProjectileAttackComponent.class).create();
                ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9, 9), true, true);
                logger.info("Ranged weapon shoot");
                entity.getEvents().trigger("RANGED_ATTACK");
                ServiceLocator.getResourceService().getAsset("sounds/shoot.ogg", Sound.class).play();
            }
            lastAttack = currentTime;
        }


    public void attack() {
        long currentTime = System.currentTimeMillis();
            // Check if the melee weapon is ready
            if (currentTime - lastSwing <= swingInterval) {
                logger.info("Melee weapon not ready");
                return; // Weapon not ready
            }

            lastSwing = currentTime;
            ServiceLocator.getResourceService()
                    .getAsset("sounds/sword1.ogg", Sound.class)
                    .play();
            logger.info("Melee weapon attack");
            rangeDetection = entity.getComponent(RangeDetectionComponent.class);
            ArrayList<Entity> entitiesInRange = rangeDetection.getEntities();
                // Apply damage to each entity in range
                for (Entity target : entitiesInRange) {
                    CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
                    if (targetStats != null) {
                        // Apply melee damage
                        targetStats.hit(target.getComponent(CombatStatsComponent.class)); // Apply the swing damage directly
                    }
                }
    }


    private void reload() {
        ammo = reloadTime; // Reload logic
        entity.getEvents().trigger("RELOAD");
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
}
