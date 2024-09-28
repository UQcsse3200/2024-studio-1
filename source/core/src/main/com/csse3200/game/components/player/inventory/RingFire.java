package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;

public class RingFire extends UsableItem {
    @Override
    public void apply(Entity entity) {
        spawnRingFire(entity);
    }

    private void spawnRingFire(Entity entity) {
        Entity ringFire = new DeployableItemFactory().createRingFire();

        // Spawn it at the player's current position
        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(ringFire, new GridPoint2(xPos, yPos), true, true);
    }

    @Override
    public String getName() {
        return "Ring of Fire";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/damagebuff.png");
    }

    @Override
    public String getItemSpecification() {
        return "ringfire";
    }
}
