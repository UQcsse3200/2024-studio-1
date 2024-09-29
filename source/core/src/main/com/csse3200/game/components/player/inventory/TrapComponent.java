package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.CollectibleComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.services.ServiceLocator;

public class TrapComponent extends Component {


    public void create(){
        entity.getEvents().addListener("collisionStart",this::onCollisionStart);
    }

    public void onCollisionStart(Fixture me, Fixture other){

        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        if (!isEnemy(otherEntity)) {
            return; // Not a collectible or already picked up
        }

        Entity trap = ((BodyUserData) me.getBody().getUserData()).entity;

        CombatStatsComponent enemyStats = otherEntity.getComponent(CombatStatsComponent.class);
        CombatStatsComponent trapStats = trap.getComponent(CombatStatsComponent.class);
        enemyStats.hit(trapStats);
        markEntityForRemoval(trap);
    }


    public boolean isEnemy(Entity enemy){
        return enemy.getComponent(AITaskComponent.class) != null;
    }

    private void markEntityForRemoval(Entity trap) {
        ServiceLocator.getEntityService().markEntityForRemoval(trap);
    }

}
