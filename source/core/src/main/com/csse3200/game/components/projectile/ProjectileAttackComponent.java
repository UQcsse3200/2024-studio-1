package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * when this entity is made it moves in a flight till collision when the render is removed,
 * attack is attempted and entity is removed.
 * params - Flight path info. Start position and flight direction.
 * requires entity to have a
 *      - Hit-boxComponent - track collisions
 *      - CombatStatsComponent - apply damage
 *      - RenderComponent - remove rendering
 * NOTE - this Entity is unregistered after the flight.
 */

public class ProjectileAttackComponent extends Component implements TaskRunner{

    private final short targetLayer;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;



    public ProjectileAttackComponent(Vector2 speed, Vector2 direction) {

        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        targetLayer = hitboxComponent.getLayer();

        // hey Yash - This is the movement section.
        // NOTE -  there is a speed var unused at the moment.
        MovementTask move = new MovementTask(direction);
        move.create(this);
        move.start();

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /**
     * Event driven - triggered by PhysicsContactListener in physics/components
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by not my hotbox, ignore.
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore.
            return;
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        // Try to attack target.
        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        //remove this and this component is not depending on textureComponent
        entity.getComponent(TextureRenderComponent.class).dispose();

        //pretty sure this unregisters render also.
        entity.dispose();
    }


}
