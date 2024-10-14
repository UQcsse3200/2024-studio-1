package com.csse3200.game.components.player.inventory.usables;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.NPCConfigComponent;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;

import java.util.Arrays;
import java.util.List;

/**
 * Component to manage the trap implementation
 */
public class TrapComponent extends Component {

    /**
     * Register a collision event listener and method is
     * called when trap is initialized.
     */
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /**
     * Handles collision detection when other entity collides with the trap
     * If animal collides, it does damage to the animal.
     *
     * @param me    represents the trap
     * @param other represents the animals (other entity) in the game
     */
    public void onCollisionStart(Fixture me, Fixture other) {

        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        if (!isEnemy(otherEntity)) {
            return; //Not an enemy entity
        }

        Entity trap = ((BodyUserData) me.getBody().getUserData()).entity;

        //Apply damage to the enemy using combat stats component
        CombatStatsComponent enemyStats = otherEntity.getComponent(CombatStatsComponent.class);
        CombatStatsComponent trapStats = trap.getComponent(CombatStatsComponent.class);

        //reduces health of enemy by base attack value
        enemyStats.hit(trapStats);

        //immobilizes enemy for a time duration
        immobilizeEnemies(otherEntity);

        // Remove the trap after use
        markEntityForRemoval(trap);
    }

    /**
     * Checks whether entity is an enemy
     *
=======
     * Checks whether entity is a pet NPC
>>>>>>> de3be37527f3e0fb08e51e802cbf684df539336b:source/core/src/main/com/csse3200/game/components/player/inventory/TrapComponent.java
     * @param enemy entity to check
     * @return true if entity is not a pet
     * @return false if the entity is pet
     */
    public boolean isEnemy(Entity enemy) {
//        return enemy.getComponent(AITaskComponent.class) != null;

        if (enemy.getComponent(AITaskComponent.class) != null) {
            NPCConfigs.NPCConfig config = enemy.getComponent(NPCConfigComponent.class).config;
            NPCConfigs.NPCConfig.TaskConfig tasks = config.tasks;

            if (tasks.follow != null) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Marks entity for removal after it is used
     *
     * @param trap the item to be removed.
     */
    private void markEntityForRemoval(Entity trap) {
        ServiceLocator.getEntityService().markEntityForRemoval(trap);
    }

    public void immobilizeEnemies(Entity enemy){

        List<PriorityTask> tasks = enemy.getComponent(AITaskComponent.class).getTasks();

        enemy.getComponent(AITaskComponent.class).stopAllTasks();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                for (PriorityTask task : tasks) {
                    enemy.getComponent(AITaskComponent.class).addTask(task);
                }
            }
        }, 5);
    }
}
