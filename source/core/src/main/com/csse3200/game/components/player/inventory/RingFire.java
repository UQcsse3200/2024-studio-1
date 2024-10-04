package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.factories.PetFactory;

public class RingFire extends BuffItem {
    @Override
    public String getName() {
        return "Ring of Fire";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/Ring_Of_Fire.png");
    }

    @Override
    public void drop(Inventory inventory) {
        // do nothing for now.
    }

    @Override
    public Texture getMysteryIcon() {
        return super.getMysteryIcon();
    }

    @Override
    public String getBuffSpecification() {
        return "ringfire";
    }

    @Override
    public void effect(Entity entity) {
        Entity ringFire = new PetFactory().createRingFire();
        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;
        entity.getComponent(InventoryComponent.class).getInventory().addPet(ringFire);
        ServiceLocator.getEntityService().register(ringFire);
        ringFire.setPosition(xPos, yPos);
    }
}
