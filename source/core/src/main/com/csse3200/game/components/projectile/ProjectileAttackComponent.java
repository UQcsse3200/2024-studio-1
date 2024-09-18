package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


/**
 * This component acts out a projectile life cycle.
 * It's built, shot, waits for collision, attempts to attack, then it is removed.
 * requires
 *  ProjectileActions - Allows the projectile to be shot.
 *  CombatStatsComponent - Used to hit() on collision.
 *  HitboxComponent - triggers event on collisions.
 */
public class ProjectileAttackComponent extends Component {

    private final short targetLayer;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;
    private final Vector2 speed;
    private final Vector2 direction;
    private final Vector2 parentPosition;

    /**
     *  Sets up vars for a projectile attack.
     *  @param layer physics layer that the projectile shot on. Provided by ProjectileConfig.
     *  @param direction Direction being shot. Example - Vector2Utils. LEFT shoots left.
     *  @param speed Set in the projectileConfig. Example - Vector2(3,3) is 3m\s etc.
    */
    public ProjectileAttackComponent(short layer, Vector2 direction, Vector2 speed, Vector2 parentPosition) {
        this.targetLayer = layer;
        this.speed = speed;
        this.direction = direction;
        this.parentPosition = parentPosition;
    }

    /**
     * On create a collision listener is started and the ProjectileAction shoot is started.
     * The collisionStart event is triggered by PhysicsContactListener, based on HitboxComponent.
     */
    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getComponent(ProjectileActions.class).shoot(direction, speed, parentPosition);
    }

    /**
     * Method is called by the Event listener in Create(), args are provided by PhysicsContactListener.
     * @param me Fixture to test if belongs to this entity.
     * @param other Fixture belonging to the hit entity.
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            return; // Not our hit-box.
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            if (!PhysicsLayer.contains(PhysicsLayer.OBSTACLE, other.getFilterData().categoryBits)) {
                return;
            }
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        //ServiceLocator.getGameAreaService().getGameArea().disposeEntity(entity);
        ServiceLocator.getEntityService().markEntityForRemoval(entity);
    }
}