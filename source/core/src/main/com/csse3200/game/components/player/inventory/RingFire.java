package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;

public class RingFire extends UsableItem {
    @Override
    public void apply(Entity entity) {
        if (ServiceLocator.getGameAreaService().getGameArea().getCurrentRoom() instanceof EnemyRoom room) {
            int[] offsetx = {-3, -2, -1, 0, 1, 2, 3, 2, 1, 0, -1, -2};
            int[] offsety = {0, 1, 2, 3, 2, 1, 0, -1, -2, -3, -2, -1};
            for (int i = 0; i < 12; i++) {

                spawnRingFire(entity, offsetx[i], offsety[i]);
            }
        }
    }

    private void spawnRingFire(Entity entity, int offsetX, int offsetY) {
        Entity ringFire = new DeployableItemFactory().createRingFire();
        int xPos = (int) entity.getPosition().x + offsetX;
        int yPos = (int) entity.getPosition().y + offsetY;
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(ringFire, new GridPoint2(xPos, yPos), true, true);

    }

    @Override
    public String getName() {
        return "Ring of Fire";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/Fire.png");
    }

    @Override
    public String getItemSpecification() {
        return "ringfire";
    }
}
