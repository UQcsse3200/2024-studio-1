package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

/**
 * An energy drink item that immediately affects the player's speed upon pickup
 */
public class EnergyDrink extends BuffItem {

    /**
     * A variable that represents the speed boost value
     */
    private static final Vector2 speed = new Vector2(1f, 1f);

    /**
     * Get the name of this item
     *
     * @return the String representation of the name of this item
     */
    @Override
    public String getName() {
        return "Energy drink";
    }

    /**
     * Get the texture for this item's icon.
     *
     * @return this item's icon.
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/energy_drink.png");
    }

    /**
     * Remove this collectible from the entity
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {

    }

    /**
     * A method that applies the effect of the energy drink to the player. Specifically, updates the player's
     * speed
     *
     * @param entity the player entity
     */
    @Override
    public void effect(Entity entity) {
        Vector2 currSpeed = entity.getComponent(PlayerActions.class).getSpeed();
        Vector2 updatedSpeed = currSpeed.add(this.getSpeed());
        entity.getComponent(PlayerActions.class).setSpeed(updatedSpeed);
    }

    /**
     * Get the speed value of this item
     *
     * @return the speed value
     */
    public Vector2 getSpeed() {
        return speed;
    }

    /**
     * Return a string representation of this collectible that can be parsed by CollectibleFactory
     *
     * @return the string representation of this collectible.
     */
    @Override
    public String getBuffSpecification() {
        return "energydrink";
    }
}
