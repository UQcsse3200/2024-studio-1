package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.areas.EnemyRoom;

public abstract class BigRedButton extends UsableItem {
    @Override
    public void pickup(Inventory inventory) {
        super.pickup(inventory);
    }

    @Override
    public String getItemSpecification() {
        return "BigRedButton";
    }

    /**
     * Handles the dropping of item from player's inventory after being used
     *
     * @param inventory The inventory to be dropped out of.
     */
    @Override
    public void drop(Inventory inventory) {
        super.drop(inventory);
    }

    /**
     * Returns name of item
     *
     * @return the item name
     */
    @Override
    public String getName() {
        return "BigRedButton";
    }

    /**
     * Return texture related with Bandage item
     *
     * @return texture representing icon of Bandage item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/big_red_button.png");
    }

    /**
     * Get mystery box icon for this specific item
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_red.png");
    }

    public void apply(MainGameArea gameArea, EnemyRoom Eroom) {
        if (gameArea.getCurrentRoom() instanceof EnemyRoom) {
            Eroom.isAllAnimalDead();
        }
    }
}
