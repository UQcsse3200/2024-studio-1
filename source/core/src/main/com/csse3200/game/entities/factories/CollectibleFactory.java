package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.components.player.inventory.Knife;
import com.csse3200.game.components.player.inventory.MeleeWeapon;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class CollectibleFactory {
    private final WeaponFactory weaponFactory = new WeaponFactory();
    private final ItemFactory itemFactory = new ItemFactory();


    public Entity create(String specification) {
        String[] split = specification.split(":");

        return switch (split[0]) {
            case "melee" -> createCollectible(weaponFactory.create(Collectible.Type.MELEE_WEAPON, split[1]));
            case "ranged" -> createCollectible(weaponFactory.create(Collectible.Type.RANGED_WEAPON, split[1]));
            case "item" -> itemFactory.create(split[1]);
            default -> throw new IllegalStateException("Unexpected value: " + split[0]);
        };
    }

    public Entity createCollectible(Collectible collectible) {
        return new Entity()
                .addComponent(new CollectibleComponent(collectible))
                .addComponent(new HitboxComponent())
                .addComponent(new TextureRenderComponent(collectible.getIcon()));
    }
}
