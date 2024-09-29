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
        //North
        int NxPos = (int) entity.getPosition().x;
        int NyPos = (int) entity.getPosition().y + 2;
//
//        //South
//        int SxPos = (int) entity.getPosition().x;
//        int SyPos = (int) entity.getPosition().y - 2;
//
//        //East
//        int ExPos = (int) entity.getPosition().x + 2;
//        int EyPos = (int) entity.getPosition().y;
//
//        //West
//        int WxPos = (int) entity.getPosition().x - 2;
//        int WyPos = (int) entity.getPosition().y;

        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(ringFire, new GridPoint2(NxPos, NyPos), true, true);
//        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(ringFire, new GridPoint2(SxPos, SyPos), true, true);
//        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(ringFire, new GridPoint2(ExPos, EyPos), true, true);
//        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(ringFire, new GridPoint2(WxPos, WyPos), true, true);
    }

    @Override
    public String getName() {
        return "Ring of Fire";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/Ring_Of_Fire.png");
    }

    @Override
    public String getItemSpecification() {
        return "ringfire";
    }
}
