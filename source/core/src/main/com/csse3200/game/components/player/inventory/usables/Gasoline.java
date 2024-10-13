package com.csse3200.game.components.player.inventory.usables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;


public class Gasoline extends UsableItem {
    @Override
    public void apply(Entity entity) {
        int[] offsetx = {-3, -2, -1, 0, 1, 2, 3, 2, 1, 0, -1, -2};
        int[] offsety = {0, 1, 2, 3, 2, 1, 0, -1, -2, -3, -2, -1};
        for (int i = 0; i < 12; i++) {

            spawnRingFire(entity, offsetx[i], offsety[i]);
        }
    }

    private void spawnRingFire(Entity entity, int offsetX, int offsetY) {
        Entity ringFire = new DeployableItemFactory().createRingFire();
        int xPos = (int) entity.getPosition().x + offsetX;
        int yPos = (int) entity.getPosition().y + offsetY;
        if (ServiceLocator.getGameAreaService().getGameController().getCurrentRoom() instanceof EnemyRoom room) {
            room.SpawnDeployable(ringFire, new GridPoint2(xPos, yPos), true, true);
        }
    }

    @Override
    public String getName() {
        return "Gasoline";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/Fire.png");
    }

    @Override
    public String getItemSpecification() {
        return "gasoline";
    }
}
