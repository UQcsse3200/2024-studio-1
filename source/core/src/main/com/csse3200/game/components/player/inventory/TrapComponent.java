package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;

/**
 * Component to manage the trap implementation
 */
public class TrapComponent extends Component {

    /**
     * Register a collision event listener and method is
     * called when trap is initialized.
     */
    public void create(){
        entity.getEvents().addListener("collisionStart",this::onCollisionStart);
    }

    /**
     * Handles collision detection when other entity collides with the trap
     * If animal collides, it does damage to the animal.
     * @param me represents the trap
     * @param other represents the animals (other entity) in the game
     */
    public void onCollisionStart(Fixture me, Fixture other){

        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        if (!isEnemy(otherEntity)) {
            return; //Not an enemy entity
        }

        Entity trap = ((BodyUserData) me.getBody().getUserData()).entity;

        //Apply damage to the enemy using combat stats component
        CombatStatsComponent enemyStats = otherEntity.getComponent(CombatStatsComponent.class);
        CombatStatsComponent trapStats = trap.getComponent(CombatStatsComponent.class);
        enemyStats.hit(trapStats);

        //Remove the trap after use
        markEntityForRemoval(trap);
    }

    /**
     * Checks whether entity is an enemy
     * @param enemy entity to check
     * @return true if entity is an enemy
     */
    public boolean isEnemy(Entity enemy){
        return enemy.getComponent(AITaskComponent.class) != null;
    }

    /**
     * Marks entity for removal after it is used
     * @param trap the item to be removed.
     */
    private void markEntityForRemoval(Entity trap) {
        ServiceLocator.getEntityService().markEntityForRemoval(trap);
    }

}
