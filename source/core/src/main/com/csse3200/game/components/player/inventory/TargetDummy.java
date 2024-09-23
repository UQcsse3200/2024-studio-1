package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.NPCHealthBarComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class TargetDummy extends UsableItem {

    @Override
    public void apply(Entity entity) {
        spawnTargetDummy(entity);
    }

    private void spawnTargetDummy(Entity entity) {
        Entity targetDummy  = new Entity()
            .addComponent(new HitboxComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new CombatStatsComponent(100,15))
            .addComponent(new NPCHealthBarComponent())
            .addComponent(new TextureRenderComponent(getIcon()))
            .addComponent(new PhysicsComponent());

        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;

        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(targetDummy, new GridPoint2(xPos, yPos), true, true);
    }

    @Override
    public String getName() {
        return "Target Dummy";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/armor.png");

    }

    @Override
    public String getItemSpecification() {
        return "targetdummy";
    }

}
