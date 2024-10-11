package com.csse3200.game.components.player.inventory.usables;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.Entity;

public class TeleporterItem extends UsableItem {
    @Override
    public String getItemSpecification() {
        return "teleporter";
    }

    /**
     * Applies the bandage to an entity, increasing its health by a small amount,
     * calls the increaseSmallBoost(entity) method
     *
     * @param entity to which Bandage item effect is applied to.
     */
    @Override
    public void apply(Entity entity) {
        entity.getEvents().trigger("teleportToBoss");
    }

    /**
     * Returns name of item
     *
     * @return the item name
     */
    @Override
    public String getName() {
        return "Teleporter";
    }

    /**
     * Return texture related with Bandage item
     *
     * @return texture representing icon of Bandage item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/big_purple_button.png");
    }

    /**
     * Get mystery box icon for this specific item
     *
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_red.png");
    }

}
