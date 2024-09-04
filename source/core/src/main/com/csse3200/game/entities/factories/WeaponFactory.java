package com.csse3200.game.entities.factories;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * A factory that creates weapons.
 */
public class WeaponFactory {
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
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent(collectible.getIcon()));

        collectibleEntity.getComponent(TextureRenderComponent.class).scaleEntity();
        return collectibleEntity;
    }

}