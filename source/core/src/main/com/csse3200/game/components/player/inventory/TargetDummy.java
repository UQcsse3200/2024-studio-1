package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.components.npc.NPCConfigComponent;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.components.tasks.ChargeTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * Represents a deployable Target Dummy item in the game. When applied, this item
 * will spawn a target dummy at the player's current position, and nearby enemies
 * will change their target to the dummy.
 */
public class TargetDummy extends UsableItem {

    /**
     * Applies the Target Dummy by spawning it at the position of the player.
     * This dummy will act as a decoy to attract enemies.
     *
     * @param entity The entity applying the Target Dummy, typically the player.
     */
    @Override
    public void apply(Entity entity) {
        spawnTargetDummy(entity);
    }

    /**
     * Spawns a Target Dummy at the given entity's position and updates the enemies'
     * targets to the dummy. If the entity is located in a room that contains enemies,
     * those enemies will begin to chase or charge the dummy.
     *
     * @param entity the player entity
     */
    private void spawnTargetDummy(Entity entity) {
        Entity targetDummy = new DeployableItemFactory().createTargetDummy();

        int xPos = (int) entity.getPosition().x;
        int yPos = (int) entity.getPosition().y;

        if (ServiceLocator.getGameAreaService().getGameArea().getCurrentRoom() instanceof EnemyRoom room) {
            room.SpawnDeployable(targetDummy, new GridPoint2(xPos, yPos), true, true);
            List<Entity> enemies = room.getEnemies();
            updateEnemyTargets(enemies, targetDummy);
        }
    }

    /**
     * Updates the enemy entities in the base room by adding tasks for the newly specified target.
     * The tasks are determined based on the NPC configuration of each entity.
     * @param target the target entity that enemies will attack
     */
    public void updateEnemyTargets(List<Entity> enemies, Entity target) {
        for (Entity entity : enemies) {
            if (entity.getComponent(AITaskComponent.class) != null) {

                NPCConfigs.NPCConfig config = entity.getComponent(NPCConfigComponent.class).config;
                NPCConfigs.NPCConfig.TaskConfig tasks = config.tasks;

                MeleeAttackComponent meleeAttack = entity.getComponent(MeleeAttackComponent.class);
                RangeAttackComponent rangedAttack = entity.getComponent(RangeAttackComponent.class);

                // Add chase task
                if (tasks.chase != null) {
                    entity.getComponent(AITaskComponent.class).addTask(new ChaseTask(target, tasks.chase));
                }
                // Add charge task
                if (tasks.charge != null) {
                    entity.getComponent(AITaskComponent.class).addTask(new ChargeTask(target, tasks.charge));
                }

                if (meleeAttack != null) {
                    meleeAttack.updateTarget(target);
                }
                if (rangedAttack!= null) {
                    rangedAttack.updateTarget(target);
                }

                entity.getComponent(AITaskComponent.class).update();
            }
        }
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
     * Gets the name if the item
     * @return the name of the item
     */
    @Override
    public String getName() {
        return "Target Dummy";
    }

    /**
     * Gets the item specification
     * @return the item specification
     */
    @Override
    public String getItemSpecification() {
        return "targetdummy";
    }

    /**
     * Gets the icon texture of the TargetDummy item
     * @return a Texture representing the icon of the Target Dummy
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/target_dummy.png");

    }

    /**
     * Get mystery box icon for this specific item
     * @return mystery box icon
     */
    @Override
    public Texture getMysteryIcon() {
        return new Texture("images/items/mystery_box_red.png");
    }

}
