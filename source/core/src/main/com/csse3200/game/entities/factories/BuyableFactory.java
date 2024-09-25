package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.components.player.inventory.BuyableComponent;
import com.csse3200.game.components.player.inventory.Collectible;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.CollectibleHitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class BuyableFactory {
    private final CollectibleFactory collectibleFactory = new CollectibleFactory();
    public Entity createBuyable(String specification, int cost) {
        Collectible collectible =  collectibleFactory.create(specification);
        Texture collectibleTexture = collectible.getIcon();
        Entity buyableEntity = new Entity()
                .addComponent(new NameComponent("Buyable: " + collectible.getName()))
                .addComponent(new CollectibleHitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new TextureRenderComponent(collectibleTexture))
                .addComponent(new BuyableComponent(cost, collectible));
        return buyableEntity;
    }

}


