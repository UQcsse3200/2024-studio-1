package com.csse3200.game.entities.factories;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.WeaponAnimationController;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
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

    private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);

    /**
     * Create a collectible melee weapon from specification
     *
     * @param specification the specification of the weapon.
     * @return MeleeWeapon the newly created collectible of type melee weapon.
     * @throws IllegalArgumentException if the specification is invalid.
     */
    private MeleeWeapon createMelee(String specification) throws IllegalArgumentException {
        return switch (specification) {
            case "knife" -> new Knife();
            case "pickaxe" -> new Pickaxe();
            default -> throw new IllegalArgumentException("Invalid melee weapon specification: " + specification);
        };
    }

    /**
     * Create a collectible range weapon from specification
     *
     * @param specification the specification of the weapon.
     * @return RangedWeapon the newly created collectible of type ranged weapon.
     * @throws IllegalArgumentException if the specification is invalid.
     */
    private RangedWeapon createRanged(String specification) throws IllegalArgumentException {
        return switch (specification) {
            case "shotgun" -> new Shotgun();
            default -> throw new IllegalArgumentException("Invalid ranged weapon specification: " + specification);
        };
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
            case MELEE_WEAPON -> createMelee(specification);
            case RANGED_WEAPON -> createRanged(specification);
            default -> throw new IllegalArgumentException("invalid weapon type: " + type);
        };
    }


    /**
     * Convert a collectible item into a collectible entity.
     *
     * @param collectible the item to convert
     * @return the final entity containing the collectible.
     * @throws IllegalArgumentException if the collectible is invalid.
     */
    public Entity createWeaponEntity(Collectible collectible) throws IllegalArgumentException {
        try {
            if (collectible.getType() == Collectible.Type.MELEE_WEAPON) {
                return createMeleeEntity((MeleeWeapon) collectible);
            }
            return createRangeEntity((RangedWeapon) collectible);
        }
        catch (Exception e) {
            logger.error("Failed to create weapon entity");
            throw new IllegalArgumentException("Invalid collectible");
        }
    }

    /**
     * Convert a collectible melee weapon into a melee weapon entity.
     *
     * @param collectible the weapon to convert
     * @return the final entity containing the weapon.
     */
    private Entity createMeleeEntity(MeleeWeapon collectible) throws IllegalArgumentException {

        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(new TextureAtlas("images/Weapons/sword1.atlas"));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("slash", 0.2f, Animation.PlayMode.LOOP);

        Entity meleeEntity = new Entity()
                .addComponent(new NameComponent("Melee: " + collectible.getName()))
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(new WeaponAnimationController());

        PhysicsUtils.setScaledCollider(meleeEntity, -1f, -1f); //  this affect player movement!!!
        // set the collider to 0
        meleeEntity.getComponent(ColliderComponent.class).setSensor(false);
        meleeEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        meleeEntity.getComponent(HitboxComponent.class).setSize(new Vector2(1f, 1f));

        logger.info("Created melee weapon entity: " + collectible);

        return meleeEntity;
    }

    /**
     * Convert a collectible melee weapon into a melee weapon entity.
     *
     * @param collectible the weapon to convert
     * @return the final entity containing the weapon.
     */
    private Entity createRangeEntity(RangedWeapon collectible) {

        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(new TextureAtlas("images/Weapons/shotgun4.atlas"));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("left", 0.2f, Animation.PlayMode.LOOP);

        Entity rangedEntity = new Entity()
                .addComponent(new NameComponent("Ranged: " + collectible.getName()))
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(new WeaponAnimationController());

        PhysicsUtils.setScaledCollider(rangedEntity, -1f, -1f); //  this affect player movement!!!
        // set the collider to 0
        rangedEntity.getComponent(ColliderComponent.class).setSensor(true);
        rangedEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        rangedEntity.getComponent(HitboxComponent.class).setSize(new Vector2(1f, 1f));
        logger.info("Created range weapon entity: " + collectible);

        return rangedEntity;
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
                "images/Weapons/shotgun4.png"
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
                "images/Weapons/shotgun4.atlas"
        };
    }
}
