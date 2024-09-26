package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;

public class BearTrap extends UsableItem {

    @Override
    public void apply(Entity entity) {
        spawnBearTrap(entity);
    }

    private void spawnBearTrap(Entity entity) {
        Entity bearTrap = new DeployableItemFactory().createBearTrap();

        // Spawn it at the player's current position
        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(bearTrap, new GridPoint2(xPos, yPos), true, true);
    }

    @Override
    public String getName() {
        return "Bear Trap";
    }

    @Override
    public Texture getIcon() {
        return new Texture("images/items/armor.png");
    }

    @Override
    public String getItemSpecification() {
        return "bearTrap";
    }
}
