package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;


public class projectileAttackComponent extends Component {

    private final short targetLayer;
    private final int owner;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;



    /**
     * when this Entity is made it listens to be shot by its owner, when shot
     * @param layer The physics layer of the target's collider.
     * @param ownerId The owner entity id via entity.getId() only this entity can shoot this projectile.
     *
     */
    public projectileAttackComponent(int ownerId, short layer) {
        owner = ownerId;
        targetLayer = layer;
        entity.getEvents().addListener("shootProjectile", this::autoCreate);

    }



    private void autoCreate(int callerId, Vector2 position, Vector2 direction) {

        if (owner != callerId) {
           // is this not my owner.
            return;
        }

        // #################### render and shoot the projectile - here.

        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }


    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by not my hotbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        // ########################## clean up the entity.

        entity.dispose();
    }


}
