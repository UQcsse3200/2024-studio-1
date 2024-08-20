package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * NOTE - this component does not override create() this component is triggered by an owner event.
 *        Event will "autoCreate".
 *        This allows many projectiles to be triggered at once like a shotgun or a pack of animals.
 */

public class ProjectileAttackComponent extends Component {

    private final short targetLayer;
    private final int owner;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;


    /**
     * when this Entity is made it listens to be shot by its owner.
     * @param layer The physics layer of the target's collider.
     * @param ownerId The owner entity id via entity.getId() only this entity can shoot this projectile.
     *
     */
    public ProjectileAttackComponent(int ownerId, short layer) {
        owner = ownerId;
        targetLayer = layer;
        entity.getEvents().addListener("shootProjectile", this::autoCreate);

    }



    private void autoCreate(int callerId, Vector2 position, Vector2 direction) {

        if (owner != callerId) {
           // is this not my owner.
            return;
        }

        // Move to the owner.
        entity.setPosition(position);

        // Render.
        entity.getComponent(TextureRenderComponent.class).create();

        // Move in a line.
        entity.getComponent(PhysicsMovementComponent.class).setEntity(entity);
        entity.getComponent(PhysicsMovementComponent.class).setTarget(direction);
        entity.getComponent(PhysicsMovementComponent.class).setMoving(true);

        // Listen on collisions.
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }


    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by not my hotbox, ignore.
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }


        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        // Try to attack target.
        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        // ########################## clean up the projectile. - unsure about first two dispose()

        entity.getComponent(TextureRenderComponent.class).dispose();
        entity.getComponent(PhysicsMovementComponent.class).dispose();
        entity.dispose();
    }


}
