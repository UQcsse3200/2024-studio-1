package com.csse3200.game.components.player.inventory.pets;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.Pet;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PetFactory;
import com.csse3200.game.services.ServiceLocator;

public class RingFire extends Pet {
    @Override
    public String getName() {
        return "Ring of Fire";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/Ring_Of_Fire.png");
    }

    @Override
    public Texture getMysteryIcon() {
        return super.getMysteryIcon();
    }

    @Override
    protected String getPetSpecification() {
        return "ringfire";
    }

    @Override
    protected Entity spawn(Entity player) {
        Entity ringFire = new PetFactory().createRingFire();

        int xPos = (int) player.getPosition().x;
        int yPos = (int) player.getPosition().y;
        ServiceLocator.getEntityService().register(ringFire);
        ringFire.setPosition(xPos, yPos);

        return ringFire;
    }
}
