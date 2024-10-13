package com.csse3200.game.components.player.inventory.usables;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.UsableItem;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class BigRedButton extends UsableItem {

    @Override
    public String getItemSpecification() {
        return "bigredbutton";
    }

    /**
     * Activates an item which damages all surrounding enemies
     *
     * @param entity Deals damage to all enemy entities within the current room
     */
    @Override
    public void apply(Entity entity) {
        if (ServiceLocator.getGameAreaService().getGameController().getCurrentRoom() instanceof EnemyRoom enemyRoom) {
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
        return new Texture("images/items/big_red_button.png");
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
