package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.weapons.ConcreteMeleeWeapon;
import com.csse3200.game.components.player.inventory.weapons.ConcreteRangedWeapon;
import com.csse3200.game.components.player.inventory.weapons.MeleeWeapon;
import com.csse3200.game.components.player.inventory.weapons.RangedWeapon;
import com.csse3200.game.components.weapon.FiringController;
import com.csse3200.game.components.weapon.PositionTracker;
import com.csse3200.game.components.weapon.WeaponAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory extends LoadedFactory {

    private static final Logger logger = LoggerFactory.getLogger(WeaponFactory.class);

    public WeaponFactory() {
        super(logger);
    }

    /**
     * Create a collectible melee weapon from specification
     *
     * @param specification the specification of the weapon.
     * @return MeleeWeapon the newly created collectible of type melee weapon.
     * @throws IllegalArgumentException if the specification is invalid.
     */
    private MeleeWeapon createMelee(String specification) throws IllegalArgumentException {
        WeaponConfig.WeaponData weaponData = WeaponConfig.getWeaponData(specification);
        if (weaponData == null) {
            throw new IllegalArgumentException("Invalid melee weapon specification: " + specification);
        }

        ConcreteMeleeWeapon meleeWeapon = new ConcreteMeleeWeapon(weaponData.getName(), weaponData.getIconPath(),
                weaponData.getDamage(), weaponData.getRange(), weaponData.getFireRate());
        meleeWeapon.setWeaponEntity(createWeaponEntity(meleeWeapon));
        return meleeWeapon;
    }

    /**
     * Create a collectible range weapon from specification
     *
     * @param specification the specification of the weapon.
     * @return RangedWeapon the newly created collectible of type ranged weapon.
     * @throws IllegalArgumentException if the specification is invalid.
     */
    private RangedWeapon createRanged(String specification) throws IllegalArgumentException {
        WeaponConfig.WeaponData weaponData = WeaponConfig.getWeaponData(specification);
        if (weaponData == null) {
            throw new IllegalArgumentException("Invalid ranged weapon specification: " + specification);
        }

        ConcreteRangedWeapon rangedWeapon = new ConcreteRangedWeapon(weaponData.getName(), weaponData.getIconPath(),
                weaponData.getDamage(), weaponData.getRange(), weaponData.getFireRate(),
                weaponData.getAmmo(), weaponData.getMaxAmmo(), weaponData.getReloadTime());
        rangedWeapon.setWeaponEntity(createWeaponEntity(rangedWeapon));
        return rangedWeapon;
    }

    /**
     * Create a new collectible weapon from a specification.
     *
     * @param type          the type of the weapon (melee or ranged)
     * @param specification the specification of the weapon.
     * @return The newly constructed collectible.
     * @throws IllegalArgumentException if the weapon type is invalid.
     */
    public Collectible create(Collectible.Type type, String specification) throws IllegalArgumentException {
        return switch (type) {
            case OFF_HAND -> createMelee(specification);
            case MAIN_HAND -> createRanged(specification);
            default -> throw new IllegalArgumentException("invalid weapon type: " + type);
        };
    }

    /**
     * Create weapon entity for player to use. Should only be invoke from WeaponComponent
     *
     * @param collectible the weapon to convert
     * @return the final entity containing the weapon.
     * @throws IllegalArgumentException with invalid input collectible
     */
    public Entity createWeaponEntity(Collectible collectible) throws IllegalArgumentException {
        try {
            if (collectible.getType() == Collectible.Type.OFF_HAND) {
                return createMeleeEntity((MeleeWeapon) collectible);
            } else if (collectible.getType() == Collectible.Type.MAIN_HAND) {
                return createRangeEntity((RangedWeapon) collectible);
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.error("Failed to create weapon entity:{}", e.toString());
            throw new IllegalArgumentException("Invalid collectible");
        }
    }

    /**
     * Create melee for the player to use. This weapon entity will not have the collectible
     * component
     *
     * @param collectible the weapon to convert
     * @return the final entity containing the weapon.
     * @throws IllegalArgumentException with invalid input collectible
     */
    private Entity createMeleeEntity(MeleeWeapon collectible) throws IllegalArgumentException {

        WeaponAnimationRenderComponent animator = createAnimator(collectible);

        Entity meleeEntity = new Entity()
                .addComponent(new NameComponent("Melee"))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(new FiringController(collectible))
                .addComponent(new CombatStatsComponent(1, collectible.getDamage(), true, 0, 0))
                .addComponent(new PositionTracker())
                .addComponent(new WeaponAnimationController());

        // set the collider to 0
        meleeEntity.getComponent(ColliderComponent.class).setSensor(true);
        meleeEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        meleeEntity.getComponent(ColliderComponent.class).setAsBox(new Vector2(1, 1));
        meleeEntity.setScale(2.0f, 2.0f);

        logger.info("Created melee weapon entity: {}", collectible);

        return meleeEntity;
    }

    /**
     * Create range weapon for the player to use. This weapon entity will not have the collectible
     * component
     *
     * @param collectible the weapon to convert
     * @return the final entity containing the weapon.
     * @throws IllegalArgumentException with invalid input collectible
     */
    private Entity createRangeEntity(RangedWeapon collectible) {

        // Load atlas and animation
        WeaponAnimationRenderComponent animator = createAnimator(collectible);
        ProjectileConfig projectileConfig = new ProjectileConfig();
        projectileConfig.baseAttack = collectible.getDamage();

        Entity rangedEntity = new Entity()
                .addComponent(new NameComponent("Ranged"))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(new FiringController(collectible, projectileConfig))
                .addComponent(new PositionTracker())
                .addComponent(new WeaponAnimationController());

        rangedEntity.getComponent(ColliderComponent.class).setSensor(true);
        rangedEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        rangedEntity.getComponent(HitboxComponent.class).setSize(new Vector2(3f, 3f));
        logger.info("Created range weapon entity: {}", collectible);
        return rangedEntity;
    }

    private WeaponAnimationRenderComponent createAnimator(Collectible weapon) {
        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(new TextureAtlas("images/Weapons/" +
                        weapon.getName() + ".atlas"));

        animator.addAnimation("idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("shootUp", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootDown", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootLeft", 0.05f, Animation.PlayMode.NORMAL);
        animator.addAnimation("shootRight", 0.05f, Animation.PlayMode.NORMAL);
        // Only for shotguns
        if (weapon.getType() == Collectible.Type.MAIN_HAND) {
            animator.addAnimation("left", 0.04f, Animation.PlayMode.LOOP);
            animator.addAnimation("up", 0.04f, Animation.PlayMode.LOOP);
            animator.addAnimation("down", 0.04f, Animation.PlayMode.LOOP);
        } else if (weapon.getType() == Collectible.Type.OFF_HAND) {
            // Only for swords
            animator.addAnimation("item", 0.05f, Animation.PlayMode.NORMAL);
        } else {
            logger.warn("Invalid collectible passed");
        }
        return animator;
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
    protected String[] getTextureAtlasFilepaths() {
        return new String[]{
                "images/Weapons/knife.atlas",
                "images/Weapons/axe.atlas",
                "images/Weapons/shotgun.atlas",
                "images/Weapons/fnscar.atlas",
                "images/Weapons/supersoaker.atlas",
                "images/Weapons/pistol.atlas",
                "images/Weapons/plasmablaster.atlas"
        };
    }

    public Collection<String> getAllSpecs() {
        return List.of(
                "ranged:shotgun",
                "ranged:plasmablaster",
                "ranged:supersoaker",
                "melee:knife",
                "melee:axe"
        );
    }
}
