package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.tasks.FollowTask;
import com.csse3200.game.entities.factories.PetFactory;

public class RingFire extends UsableItem {
    @Override
    public void apply(Entity entity) {
        spawnRingFire(entity);
    }

    private void spawnRingFire(Entity entity) {
        Entity ringFire = new PetFactory().createRingFire();
        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;
        entity.getComponent(InventoryComponent.class).getInventory().addPet(ringFire);
        ServiceLocator.getEntityService().register(ringFire);
        ringFire.setPosition(xPos,yPos);
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
