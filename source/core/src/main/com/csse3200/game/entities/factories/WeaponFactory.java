package com.csse3200.game.entities.factories;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.NPCDamageHandlerComponent;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory {

    public static final Logger logger = LoggerFactory.getLogger(WeaponFactory.class);
    private MeleeWeapon createMelee(String specification) {
        return switch (specification) {
            case "knife" -> new Knife();
            case "pickaxe" -> new Pickaxe();
            default -> new ConcreteMeleeWeapon(specification);
        };
    }

    private RangedWeapon createRanged(String specification){
        // specification format: "ranged:<Ranged Weapon>,<pathtoicon>,<damage>,<range>,<fireRate>,<ammo>,<maxAmmo>,<reloadTime>"
        if (specification.equals("shotgun")) {
            return new Shotgun();
        }
        return new ConcreteRangedWeapon(specification);
    }

    /**
     * Create a new weapon from a specification.
     * @param type the type of the weapon (melee or ranged)
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
    public static Entity createCollectibleEntity(Collectible collectible) {
        Entity collectibleEntity = new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new TextureRenderComponent(collectible.getIcon()));

        PhysicsUtils.setScaledCollider(collectibleEntity, -1f, -1f); //  this affect player movement!!!
        // set the collider to 0
        collectibleEntity.getComponent(ColliderComponent.class).setSensor(false);
        collectibleEntity.getComponent(TextureRenderComponent.class).scaleEntity();
        collectibleEntity.getComponent(HitboxComponent.class).setSize(new Vector2(3f, 3f));
        logger.info("Created collectible entity: " + collectible);
        return collectibleEntity;
    }

}