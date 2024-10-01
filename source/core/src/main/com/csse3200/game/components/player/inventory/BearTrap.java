package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;

public class BearTrap extends UsableItem {

    /**
     * Applies bear trap item that can be used by entity
     * @param entity the entity to apply it to.
     */
    @Override
    public void apply(Entity entity) {
        spawnBearTrap(entity);
    }

    /**
     * Spawns bear trap at entity's current position
     * @param entity whose position is used
     */
    private void spawnBearTrap(Entity entity) {
        Entity bearTrap = new DeployableItemFactory().createBearTrap();

        // Spawn it at the player's current position
        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(bearTrap, new GridPoint2(xPos, yPos), true, true);
    }

    /**
     * Returns name of the item
     * @return string telling item's name
     */
    @Override
    public String getName() {
        return "Bear Trap";
    }

    /**
     * Return texture related with Bear trap item
     *
     * @return texture representing icon of Bear trap item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/trap_open.png");
    }

    /**
     * Get the specification of this item.
     *
     * @return the string representation of this item.
     */
    @Override
    public String getItemSpecification() {
        return "beartrap";
    }
}
