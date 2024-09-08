package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

import java.util.Objects;

/**
 * An energy drink item that immediately affects the player's speed upon pickup
 */
public class EnergyDrink extends BuffItem {

    /**
     * A variable that represents the speed boost value
     */
    String speedType;

    Vector2 speed;

    float speedPercentage;

    public EnergyDrink(String speedType) {
        this.speedType = speedType;
        setScalar(speedType);
    }

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
        Vector2 currSpeed = entity.getComponent(PlayerActions.class).getCurrSpeed();
        Vector2 updatedSpeed = currSpeed.add(getSpeed());
        float currSpeedPercentage = entity.getComponent(PlayerActions.class).getCurrSpeedPercentage();
        float newSpeedPercentage = currSpeedPercentage + getSpeedPercentage();
        entity.getComponent(PlayerActions.class).setSpeedPercentage(newSpeedPercentage);
        entity.getComponent(PlayerActions.class).setSpeed(updatedSpeed);
        entity.getEvents().trigger("updateSpeedPercentage", newSpeedPercentage);
    }

    /**
     * Get the speed value of this item
     *
     * @return the speed value
     */
    public Vector2 getSpeed() {
        return this.speed;
    }

    public float getSpeedPercentage() {
        return this.speedPercentage;
    }

    public void setScalar(String speedType) {
        Vector2 baseSpeed = new Vector2(3f, 3f); //Improvement: actually get the default speed somehow
        if (speedType.equals("Low")) {
            this.speed = baseSpeed.scl(0.3f);
            this.speedPercentage = 0.3f;
        }
        else if (speedType.equals("Medium")) {
            this.speed = baseSpeed.scl(0.5f);
            this.speedPercentage = 0.5f;
        }
        else if (speedType.equals("High")) {
            this.speed = baseSpeed.scl(0.6f);
            this.speedPercentage = 0.6f;
        }
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

    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_blue.png");
    }
}
