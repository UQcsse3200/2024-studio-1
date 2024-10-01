package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;

public class DivinePotion extends BuffItem{
    private static final int potionBoost = 30;
    Vector2 maxSpeed = new Vector2(6f, 6f);

    /**
     * Make the entity pick the item up, and apply effects immediately instead of being added to the inventory
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        inventory.addItem(this);
        effect(inventory.getEntity());
    }

    /**
     * Get the name of this item
     *
     * @return the String representation of the name of this item
     */
    @Override
    public String getName() {
        return "Divine Potion";
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
        return "Divine potion";
    }


    /**
     * Return texture related with divine potion item
     *
     * @return texture representing icon of divine potion item.
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/divine_potion.png");
    }

    /**
     * Applies the divine potion to an entity
     * calls the Boost(entity) method
     *
     * @param entity to which divine potion item effect is applied to.
     */
    @Override
    public void effect(Entity entity) {
        Boost(entity);
        speed(entity);
    }


    /**
     * Increases health by using entity's CombatStatsComponent to set Health
     *
     * @param entity whose health is increased.
     */
    public void Boost(Entity entity) {
        CombatStatsComponent combatStats = entity.getComponent(CombatStatsComponent.class);
        int currentHealth = combatStats.getHealth();
        int newHealth = Math.min(currentHealth + potionBoost,combatStats.getMaxHealth());
        combatStats.setHealth(newHealth);
    }

    public void speed(Entity entity) {
        PlayerActions playerActions = entity.getComponent(PlayerActions.class);

        // Get the current speed and speed limit
        float currSpeedPercentage = playerActions.getCurrSpeedPercentage();
        float speedLimit = playerActions.getMaxSpeed();
        float speedBoost = .25f; // Fixed speed boost

        // Add the fixed speed boost to the current percentage
        float newSpeedPercentage = currSpeedPercentage + speedBoost;
        // Ensure the new speed doesn't exceed the maximum allowed speed limit
        if (newSpeedPercentage >= speedLimit) {
            // Cap speed to the max allowed speed (6f, 6f)
            playerActions.setSpeed(this.maxSpeed); // Use maxSpeed limit
            playerActions.setSpeedPercentage(speedLimit); // Set UI to max speed percentage
        } else {
            // Add the fixed speed boost (0.1) to both x and y components of the current speed
            Vector2 currSpeed = playerActions.getCurrSpeed();
            Vector2 updatedSpeed = currSpeed.add(new Vector2(.25f, .25f)); // Fixed speed boost
            playerActions.setSpeed(updatedSpeed); // Update the actual speed
            playerActions.setSpeedPercentage(newSpeedPercentage); // Update UI percentage
        }

        // Trigger event to update the speed percentage in the UI
        entity.getEvents().trigger("updateSpeedUI", newSpeedPercentage);
    }

}


