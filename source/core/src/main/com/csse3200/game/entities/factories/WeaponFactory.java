package com.csse3200.game.entities.factories;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.WeaponAnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory extends LoadedFactory {

    private static final Logger logger = LoggerFactory.getLogger(PlayerFactory.class);

    private MeleeWeapon createMelee(String specification) {
        return switch (specification) {
            case "knife" -> new Knife();
            case "pickaxe" -> new Pickaxe();
            default -> new ConcreteMeleeWeapon(specification);
        };
    }

    /**
     * Create a range weapon from specification
     *
     * @param specification
     * @return RangedWeapon
     */
    private RangedWeapon createRanged(String specification) {
        // specification format: "ranged:<Ranged Weapon>,<pathtoicon>,<damage>,<range>,<fireRate>,<ammo>,<maxAmmo>,<reloadTime>"
        if (specification.equals("shotgun")) {
            return new Shotgun();
        }
        return new ConcreteRangedWeapon(specification);
    }

    /**
     * Create a new weapon from a specification.
     *
     * @param type          the type of the weapon (melee or ranged)
     * @param specification the specification of the weapon.
     * @return The newly constructed collectible.
     */
    public Collectible create(Collectible.Type type, String specification) {
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
     */
    public Entity createWeaponEntity(Collectible collectible) {
        if (collectible.getType() == Collectible.Type.MELEE_WEAPON) {
            return createMeleeEntity((MeleeWeapon) collectible);
        }

        return createRangeEntity((RangedWeapon) collectible);
    }

    /**
     * Convert a collectible melee weapon into a melee weapon entity.
     *
     * @param collectible the weapon to convert
     * @return the final entity containing the weapon.
     */
    private Entity createMeleeEntity(MeleeWeapon collectible) {

        TextureAtlas atlas = new TextureAtlas(getTextureAtlasFilepaths()[0]);
        TextureRegion defaultTexture = atlas.findRegion("idle");

        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/Weapons/sword1.atlas",
                                TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("slash", 0.2f, Animation.PlayMode.LOOP);

        Entity meleeEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator);

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

        TextureAtlas atlas = new TextureAtlas(this.getTextureAtlasFilepaths()[1]);
        TextureRegion defaultTexture = atlas.findRegion("idle");

        WeaponAnimationRenderComponent animator =
                new WeaponAnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/Weapons/shotgun4" +
                                        ".atlas",
                                TextureAtlas.class));
        animator.addAnimation("idle", 0.2f, Animation.PlayMode.LOOP);

        Entity meleeEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(animator);

        PhysicsUtils.setScaledCollider(meleeEntity, -1f, -1f); //  this affect player movement!!!
        // set the collider to 0
        meleeEntity.getComponent(ColliderComponent.class).setSensor(true);
        meleeEntity.getComponent(WeaponAnimationRenderComponent.class).startAnimation("idle");
        meleeEntity.getComponent(HitboxComponent.class).setSize(new Vector2(1f, 1f));
        logger.info("Created range weapon entity: " + collectible);

        return meleeEntity;
    }
    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Weapons/sword1.png",
                "images/Weapons/shotgun4.png"
        };
    }
    @Override
    protected String[] getSoundFilepaths() {
        return new String[]{
                "sounds/shotgun1_f.ogg",
                "sounds/shotgun1_r.ogg",
                "sounds/sword1.ogg"
        };
    }

    @Override
    protected String[] getTextureAtlasFilepaths(){
        return new String[]{
                "images/Weapons/sword1.atlas",
                "images/Weapons/shotgun4.atlas"
        };
    }
}
