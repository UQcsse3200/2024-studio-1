package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.ShieldComponent;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ShieldPotion class represents a shield potion item within the game.
 * When used, it provides the player with a temporary immunity against damage.
 * Specifically, the next two hits against the player are negated. After two hits
 * shield breaks and the player loses immunity.
 */
public class ShieldPotion extends UsableItem {


    private static final Logger log = LoggerFactory.getLogger(ShieldPotion.class);
    /**
     * The number of hits the shield can negate before being depleted (initially inactive).
     */
    private int charges = 0;

    /**
     * Applies the effects of the shield potion to a specified entity.
     *
     * @param entity the entity to which the shield potion effects are applied to.
     */
    @Override
    public void apply(Entity entity) {
        charges = 2; // Activate the shield with full charges
        entity.getEvents().trigger("shieldActivated");
         entity.getEvents().addListener("hit", () -> negateHit(entity));
        entity.getComponent(ShieldComponent.class).activateShield();
    }

    /**
     * Negates a hit against the entity, reducing the number of charges by one.
     * If charges reach zero, the shield is considered depleted and removed.
     *
     * @param entity the entity that the hit is negated against
     */
    public void negateHit(Entity entity) {
        if (charges > 0) {
            charges--;
            log.info("Negated a hit! Remaining charges: {}", charges);
            if (charges == 0) {
                log.info("Shield depleted");
                entity.getEvents().trigger("shieldDeactivated");
                entity.getComponent(ShieldComponent.class).deactivateShield();
            }
        }
    }

    /**
     * Gets the number of charges that the shield currently has
     *
     * @return the charges
     */
    public int getCharges() {
        return charges;
    }

    /**
     * Handles the pickup of the shield potion by adding it to the player's inventory.
     *
     * @param inventory The inventory to be put in.
     */
    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    @Override
    public void pickup(Inventory inventory, Entity itemEntity) {
        super.pickup(inventory);
    }
    /**
     * Handles dropping the shield potion from the player's inventory after being used.
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }

    /**
     * Returns the texture associated with the shield potion item.
     *
     * @return A Texture representing the icon of the shield potion.
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/shield_potion.png");
    }

    /**
     * Get mystery box icon for this specific item
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_red.png");
    }
    
    /**
     * Returns the name of the item.
     *
     * @return the item name.
     */
    @Override
    public String getName() {
        return "Shield Potion";
    }

    @Override
    public String getItemSpecification() {
        return "shieldpotion";
    }

    /**
     * Removes the shield from the entity.
     *
     * @param entity the entity from which the shield is removed.
     */
    public void removeShield(Entity entity) {
        entity.getEvents().trigger("shieldDeactivated");
    }
}

