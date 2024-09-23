package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.NPCHealthBarComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class Scarecrow extends UsableItem {

    @Override
    public void apply(Entity entity) {
        spawnScarecrow(entity);
    }

    private void spawnScarecrow(Entity entity) {
        Entity scarecrow  = new Entity();
        scarecrow.addComponent(new HitboxComponent());
        scarecrow.addComponent(new ColliderComponent());
        scarecrow.addComponent(new CombatStatsComponent(100,15));
        scarecrow.addComponent(new NPCHealthBarComponent());
        scarecrow.addComponent(new TextureRenderComponent(getIcon()));

        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;

        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(scarecrow, new GridPoint2(xPos, yPos), true, true);
    }

    @Override
    public String getName() {
        return "Scarecrow";
    }

    @Override
    public Texture getIcon() {
        return new Texture("/images/items/shield_potion.png");
    }

    @Override
    public String getItemSpecification() {
        return "scarecrow";
    }

}
