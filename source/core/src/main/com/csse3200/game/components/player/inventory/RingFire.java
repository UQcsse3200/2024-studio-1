package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PetFactory;
import com.csse3200.game.services.ServiceLocator;

public class RingFire extends UsableItem {
    @Override
    public void apply(Entity entity) {
        spawnRingFire(entity);
    }

    private void spawnRingFire(Entity entity) {
        Entity ringFire = new PetFactory().create("ringfire");
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
