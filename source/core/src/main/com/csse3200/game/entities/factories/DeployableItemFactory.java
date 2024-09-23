package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.NPCHealthBarComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class DeployableItemFactory extends LoadedFactory {

    public Entity createTargetDummy() {

        Entity targetDummy  = new Entity()
                .addComponent(new HitboxComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new CombatStatsComponent(100,0))
                .addComponent(new NPCHealthBarComponent())
                .addComponent(new TextureRenderComponent("images/items/armor.png"))
                .addComponent(new PhysicsComponent());

        targetDummy.getComponent(TextureRenderComponent.class).scaleEntity();

        return targetDummy;
    }

    public Entity createBearTrap() {
        Entity bearTrap = new Entity()
                .addComponent(new HitboxComponent())
                .addComponent(new CombatStatsComponent(100,30))
                .addComponent(new TextureRenderComponent("images/items/damagebuff.png"))
                .addComponent(new PhysicsComponent());

        bearTrap.getComponent(TextureRenderComponent.class).scaleEntity();

        return bearTrap;
    }

}
