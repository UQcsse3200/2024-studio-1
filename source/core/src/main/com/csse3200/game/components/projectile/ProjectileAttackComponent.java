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
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 *  This component is triggered by an owner event "shootProjectile".
 *  This allows many entities to trigger the same projectile.
 *  Projectile is disposed after colliding being triggered once.
 */

public class ProjectileAttackComponent extends Component implements TaskRunner{

    private final short targetLayer;
    private final int owner;
    private boolean hasShot = false;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;
    private String renderPath;



    /**
     * when this Entity is made it listens to be shot by its owner.
     * @param layer The physics layer of the target's collider.
     * @param ownerId The owner entity id via entity.getId() only this entity can shoot this projectile.
     *
     */
    public ProjectileAttackComponent(int ownerId, short layer, String renderPath) {
        this.renderPath = renderPath;
        owner = ownerId;
        targetLayer = layer;
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        entity.getEvents().addListener("shootProjectile", this::autoCreate);

    }


    private void autoCreate(int callerId, Vector2 position, Vector2 direction) {

        if (hasShot || owner != callerId) {
            // I'm already in flight, or it wasn't my owner's event.
            return;
        }

        hasShot = true;

        entity.setPosition(position);
        entity.addComponent(new TextureRenderComponent(renderPath));

        MovementTask move = new MovementTask(direction);
        move.create(this);
        move.start();

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /**
     * Event driven. Attacks then cleans up.
     * @param me - The entity doing the hit.
     * @param other - The entity being hit.
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


        entity.getComponent(TextureRenderComponent.class).dispose();

        entity.dispose();
    }


}
