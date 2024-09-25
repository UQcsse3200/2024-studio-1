package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.WeaponAnimationController;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory extends LoadedFactory {

    private static final Logger logger = LoggerFactory.getLogger(WeaponFactory.class);

    private MeleeWeapon createMelee(String specification) throws IllegalArgumentException {
        WeaponConfig.WeaponData weaponData = WeaponConfig.getWeaponData(specification);
        if (weaponData == null) {
            throw new IllegalArgumentException("Invalid melee weapon specification: " + specification);
        }

        MeleeWeapon meleeWeapon = new MeleeWeapon(weaponData.getName(), weaponData.getIconPath(),
                weaponData.getDamage(), weaponData.getRange(), weaponData.getFireRate());
        return meleeWeapon;
    }

    private RangedWeapon createRanged(String specification) throws IllegalArgumentException {
        WeaponConfig.WeaponData weaponData = WeaponConfig.getWeaponData(specification);
        if (weaponData == null) {
            throw new IllegalArgumentException("Invalid ranged weapon specification: " + specification);
        }

        RangedWeapon rangedWeapon = new RangedWeapon(weaponData.getName(), weaponData.getIconPath(),
                weaponData.getDamage(), weaponData.getRange(), weaponData.getFireRate(),
                weaponData.getAmmo(), weaponData.getMaxAmmo(), weaponData.getReloadTime());
        return rangedWeapon;
    }

    public Collectible create(Collectible.Type type, String specification) throws IllegalArgumentException {
        return switch (type) {
            case MELEE_WEAPON -> createMelee(specification);
            case RANGED_WEAPON -> createRanged(specification);
            default -> throw new IllegalArgumentException("Invalid weapon type: " + type);
        };
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
        meleeEntity.getComponent(ColliderComponent.class).setAsBox(new Vector2(0f, 0f));
        //meleeEntity.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);

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


    public Entity createWeaponEntity(Collectible collectible) throws IllegalArgumentException {
        try {
            if (collectible.getType() == Collectible.Type.MELEE_WEAPON) {
                return createMeleeEntity((MeleeWeapon) collectible);
            }
            return createRangeEntity((RangedWeapon) collectible);
        } catch (Exception e) {
            logger.error("Failed to create weapon entity: {}", e.toString());
            throw new IllegalArgumentException("Invalid collectible");
        }
    }
    /**
     * Get all the filepath to textures needed by this Factory
     *
     * @return the filepath needed.
     */
    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Weapons/sword1.png",
                "images/Weapons/shotgun4.png",
                "images/Weapons/shotgun_1.png",
                "images/Weapons/shotgun_2.png",
                "images/Weapons/winchester.png"
        };
    }

    /**
     * Get all the filepath to sounds needed by this Factory
     *
     * @return the filepath needed.
     */
    @Override
    protected String[] getSoundFilepaths() {
        return new String[]{
                "sounds/shotgun1_f.ogg",
                "sounds/shotgun1_r.ogg",
                "sounds/sword1.ogg"
        };
    }

    /**
     * Get all the filepath to texture atlases needed by this Factory
     *
     * @return the filepath needed.
     */
    @Override
    protected String[] getTextureAtlasFilepaths(){
        return new String[]{
                "images/Weapons/sword1.atlas",
                "images/Weapons/shotgun4.atlas",
                "images/Weapons/shotgun_1.atlas",
                "images/Weapons/shotgun_2.atlas",
                "images/Weapons/shotgun_3.atlas",
                "images/Weapons/50_cal.atlas",
                "images/Weapons/ak.atlas",
                "images/Weapons/fn_scar.atlas",
                "images/Weapons/super_soaker.atlas",
                "images/Weapons/winchester.atlas"
        };
    }
}
