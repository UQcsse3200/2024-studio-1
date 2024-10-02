package com.csse3200.game.components.player.inventory;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class BigRedButton extends UsableItem {
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
     * Return texture related with Big Red Button item
     *
     * @return texture representing icon of Button item
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/Big_red_button.png");
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
     * Activates an item which damages all surrounding enemies
     *
     *
     * @param entity Deals damage to all enemy entities within the current room
     */
    @Override
    public void apply(Entity entity) {
        if (ServiceLocator.getGameAreaService().getGameArea().getCurrentRoom() instanceof EnemyRoom enemyRoom) {
            List<Entity> entities = enemyRoom.getEnemies();
            for (Entity enemy : entities) {
                CombatStatsComponent combatStatsComponent = enemy.getComponent(CombatStatsComponent.class);
                if (combatStatsComponent != null) {
                    combatStatsComponent.setHealth(1);
                    combatStatsComponent.hit(combatStatsComponent);
                }
            }
        }
    }

}
