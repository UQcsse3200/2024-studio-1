package com.csse3200.game.entities.factories;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.components.player.inventory.*;

public class WeaponFactory {
    private MeleeWeapon createMelee(String specification) {
        return new Knife();
    }


    private RangedWeapon createRanged(String specification){
        // specification format: "ranged:<Ranged Weapon>,<pathtoicon>,<damage>,<range>,<fireRate>,<ammo>,<maxAmmo>,<reloadTime>"

        return new ConcreteRangedWeapon(specification);
    }

    public Collectible create(Collectible.Type type, String specification) {
        return switch (type) {
            case MELEE_WEAPON -> createMelee(specification);
            case RANGED_WEAPON -> createRanged(specification);
            default -> throw new IllegalArgumentException("invalid weapon type: " + type);
        };
    }

    public static Entity createKnifeEntity() {
        Knife knife = new Knife();
        Entity knifeEntity = new Entity()
                .addComponent(new TextureRenderComponent("images/Weapons/knife.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new CollectibleComponent(knife));

        knifeEntity.getComponent(TextureRenderComponent.class).scaleEntity();
        knifeEntity.scaleHeight(0.9f);
        PhysicsUtils.setScaledCollider(knifeEntity, 0.5f, 0.2f);
        return knifeEntity;
    }
}