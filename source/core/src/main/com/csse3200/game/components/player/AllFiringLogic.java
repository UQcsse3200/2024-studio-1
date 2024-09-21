package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;



public class AllFiringLogic extends Component {
    private Entity entity;
    private ProjectileFactory projectileFactory;
    private ProjectileConfig bulletConfig;

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
                return; // Weapon not ready
            }
            if (ammo == 0) {
                reload(); // Handle reloading
                return;
            } else {
                // Shooting logic
                ammo--;
                Entity projectile = projectileFactory.createProjectile(bulletConfig, direction, entity.getPosition());
                projectile.getComponent(ProjectileAttackComponent.class).create();
                ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9, 9), true, true);
                entity.getEvents().trigger("RANGED_ATTACK");
                ServiceLocator.getResourceService().getAsset("sounds/shoot.ogg", Sound.class).play();
            }
            lastAttack = currentTime;
        }


    public void attack() {
        Entity entity = this.getEntity();
        long currentTime = System.currentTimeMillis();
            // Check if the melee weapon is ready
            if (currentTime - lastSwing <= swingInterval) {
                return; // Weapon not ready
            }

            lastSwing = currentTime;
            ServiceLocator.getResourceService()
                    .getAsset("sounds/sword1.ogg", Sound.class)
                    .play();
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
