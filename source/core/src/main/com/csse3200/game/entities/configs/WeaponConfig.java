package com.csse3200.game.entities.configs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.WeaponAnimationController;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.components.player.inventory.RangedWeapon;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WeaponConfig {

    private static final Logger logger = LoggerFactory.getLogger(WeaponConfig.class);

    // Store weapon data in a map
    private static final Map<String, WeaponData> weaponConfigs = new HashMap<>();

    static {
        // Load weapon data (can be from JSON or hardcoded for now)
        weaponConfigs.put("shotgun", new WeaponData(30, 5, 5, 20, 20, 3, "images/Weapons/Shotgun.png"));
        weaponConfigs.put("knife", new WeaponData(10, 1, 2, "images/Weapons/Knife.png"));
        weaponConfigs.put("pickaxe", new WeaponData(50,0,3,"images/Weapons/pickaxe.png"));

    }

    // Method to fetch weapon data by specification
    public static WeaponData getWeaponData(String specification) {
        return weaponConfigs.get(specification);
    }

    public static Entity createMeleeEntity(MeleeWeapon collectible) throws IllegalArgumentException {
        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(new TextureAtlas("images/Weapons/slash_1.atlas"));
        animator.addAnimation("idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("shootUp", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootDown", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootLeft", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootRight", 0.05f, Animation.PlayMode.NORMAL);

        Entity meleeEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(new WeaponAnimationController());

        PhysicsUtils.setScaledCollider(meleeEntity, -1f, -1f);
        meleeEntity.getComponent(ColliderComponent.class).setSensor(true);
        meleeEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        meleeEntity.getComponent(ColliderComponent.class).setAsBox(new Vector2(1f, 1f));
        meleeEntity.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);

        logger.info("Created melee weapon entity: " + collectible);
        return meleeEntity;
    }

    public static Entity createRangeEntity(RangedWeapon collectible) {
        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(new TextureAtlas("images/Weapons/plasma_blaster.atlas"));
        animator.addAnimation("idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("left", 0.04f, Animation.PlayMode.LOOP);
        animator.addAnimation("up", 0.04f, Animation.PlayMode.LOOP);
        animator.addAnimation("down", 0.04f, Animation.PlayMode.LOOP);
        animator.addAnimation("shootUp", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootDown", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootLeft", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootRight", 0.05f, Animation.PlayMode.NORMAL);

        Entity rangedEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(new WeaponAnimationController());

        PhysicsUtils.setScaledCollider(rangedEntity, -1f, -1f);
        rangedEntity.getComponent(ColliderComponent.class).setSensor(true);
        rangedEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        rangedEntity.getComponent(HitboxComponent.class).setSize(new Vector2(3f, 3f));
        logger.info("Created range weapon entity: " + collectible);
        return rangedEntity;
    }

    // Define the WeaponData class
    public static class WeaponData {
        private final int damage;
        private final int range;
        private final int fireRate;
        private final int ammo;
        private final int maxAmmo;
        private final int reloadTime;
        private final String iconPath;

        // Constructor for ranged weapons
        public WeaponData(int damage, int range, int fireRate, int ammo, int maxAmmo, int reloadTime, String iconPath) {
            this.damage = damage;
            this.range = range;
            this.fireRate = fireRate;
            this.ammo = ammo;
            this.maxAmmo = maxAmmo;
            this.reloadTime = reloadTime;
            this.iconPath = iconPath;
        }

        // Constructor for melee weapons
        public WeaponData(int damage, int range, int fireRate, String iconPath) {
            this(damage, range, fireRate, 0, 0, 0, iconPath);
        }

        public int getDamage() {
            return damage;
        }

        public int getRange() {
            return range;
        }

        public int getFireRate() {
            return fireRate;
        }

        public int getAmmo() {
            return ammo;
        }

        public int getMaxAmmo() {
            return maxAmmo;
        }

        public int getReloadTime() {
            return reloadTime;
        }

        public String getIconPath() {
            return iconPath;
        }
    }
}
