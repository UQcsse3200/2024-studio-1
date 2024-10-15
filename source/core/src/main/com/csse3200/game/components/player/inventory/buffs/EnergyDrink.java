package com.csse3200.game.components.player.inventory.buffs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.inventory.BuffItem;
import com.csse3200.game.components.player.inventory.Inventory;
import com.csse3200.game.entities.Entity;

import java.util.List;

/**
 * An energy drink item that immediately affects the player's speed upon pickup. An energy drink has three different
 * types, which can be chosen by changing the specification of this item to either "Low", "Medium" or "High". Each
 * of the three types have a different speed boost associated with it
 */
public class EnergyDrink extends BuffItem {

    /**
     * A string representing the type of energy drink
     */
    String speedType;
    /**
     * The speed associated with this energy drink type
     */
    Vector2 speed;
    /**
     * The maximum speed that the energy drink effect can sum to
     */
    Vector2 maxSpeed = new Vector2(4.5f, 4.5f);
    /**
     * The speed associated with this energy drink type. This variable is used
     * to update the UI of the speed percentage stats
     */
    float speedPercentage;
    /**
     * The icon of this energy drink type
     */
    Texture energyDrinkIcon;

    /**
     * A constructor to initialise an EnergyDrink item
     *
     * @param speedType
     */
    public EnergyDrink(String speedType) {
        this.speedType = speedType;
        if (!List.of("High", "Medium", "Low").contains(speedType)) {
            throw new IllegalArgumentException("Invalid speedType: " + speedType);
        }
//        setScalar(speedType);
        setIcon(speedType);
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
        return this.energyDrinkIcon;
    }

    /**
     * Sets the correct icon depending on what type of energy drink this is
     *
     * @param speedType the string identification representing the type of energy drink
     */
    public void setIcon(String speedType) {
        switch (speedType) {
            case "Low" -> this.energyDrinkIcon = new Texture("images/items/energy_drink_blue.png");
            case "Medium" -> this.energyDrinkIcon = new Texture("images/items/energy_drink_purple.png");
            case "High" -> this.energyDrinkIcon = new Texture("images/items/energy_drink_red.png");
        }
    }

    /**
     * Get the mystery box icon representation of this item
     *
     * @return the mystery box icon representation of this item
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_blue.png");
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
     * Return a string representation of this collectible that can be parsed by CollectibleFactory
     *
     * @return the string representation of this collectible.
     */
    @Override
    public String getBuffSpecification() {
        return "energydrink:" + this.speedType;
    }

    /**
     * A method that applies the effect of this specific energy drink to the player. This method handles the
     * effect upon the player's speed, as well as updates to the UI of the speed percentage stats
     *
     * @param entity the player entity
     */
    @Override
    public void effect(Entity entity) {
        setScalar(speedType, entity);
        Vector2 currSpeed = entity.getComponent(PlayerActions.class).getCurrPlayerSpeed();
        //Add the current speed with the boost (vector) associated with this energy drink
        Vector2 updatedSpeed = new Vector2(currSpeed.x + getSpeed().x, currSpeed.y + getSpeed().y);
        Vector2 baseSpeed = entity.getComponent(PlayerActions.class).getBaseSpeed();
        //Get the new proportion of the progress bar for the UI
        float newSpeedPercentage = entity.getComponent(PlayerActions.class).getSpeedProgressBarProportion(updatedSpeed, baseSpeed);
        float maxBoost = entity.getComponent(PlayerActions.class).getMaxTotalSpeedBoost();

        //Check that picking up the item will not result in the speed going above the maximum
        if (newSpeedPercentage >= maxBoost) {
            Vector2 maxSpeed = entity.getComponent(PlayerActions.class).getMaxPlayerSpeed();
            entity.getComponent(PlayerActions.class).setSpeed(maxSpeed); //Cap it at the max speed
            newSpeedPercentage = maxBoost;
            //Update the UI
            entity.getEvents().trigger("updateSpeedPercentage", newSpeedPercentage, speedType);
        } else {
            entity.getComponent(PlayerActions.class).setSpeed(updatedSpeed);
            //Update the UI
            entity.getEvents().trigger("updateSpeedPercentage", newSpeedPercentage, speedType);
        }
    }

    /**
     * Get the vector representing the speed boost of this energy drink type
     *
     * @return the speed boost (vector) of this energy drink type
     */
    public Vector2 getSpeed() {
        return this.speed;
    }

    /**
     * Get the float representing the speed boost of this energy drink type
     *
     * @return the speed boost (float) of this energy drink type
     */
    public float getSpeedPercentage() {
        return this.speedPercentage;
    }

    /**
     * Sets the correct speed boost depending on what type of energy drink this is. The speed boost will be
     * a percentage of the original "base speed"
     *
     * @param speedType the string identification representing the type of energy drink
     */
    public void setScalar(String speedType, Entity entity) {
//        Vector2 baseSpeed = new Vector2(3f, 3f); //Improvement: actually get the default speed somehow
        Vector2 baseSpeed = entity.getComponent(PlayerActions.class).getBaseSpeed();
        switch (speedType) {
            case "Low" -> {
                this.speed = baseSpeed.scl(0.03f); //0.03% of the base speed
                this.speedPercentage = 0.03f; //% Increase
            }
            case "Medium" -> {
                this.speed = baseSpeed.scl(0.05f);
                this.speedPercentage = 0.05f;
            }
            case "High" -> {
                this.speed = baseSpeed.scl(0.1f);
                this.speedPercentage = 0.1f;
            }
            default -> throw new IllegalStateException("Unexpected value: " + speedType);
        }
    }
}
