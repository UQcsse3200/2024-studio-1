package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;


/**
 * ProjectileAttackComponent acts out the lifecycle of a projectile.
 * params
 *  layer - physics layer that the projectile shot on. Provided by ProjectileConfig.
 *  direction - Direction being shot ie; Vector2Utils.LEFT.
 *  speed -  Set in the projectileConfig. Vector2(3,3) is 3m\s etc.
 * requires
 *  ProjectileActions - Allows the projectile to be shot.
 *  CombatStatsComponent - Used to hit() on collision.
 *  HitboxComponent - triggers event on collisions.
 *
 *
 *
 */
public class ProjectileAttackComponent extends Component {

    private short targetLayer;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;
    private Vector2 speed;
    private Vector2 direction;

    public ProjectileAttackComponent(short layer, Vector2 direction, Vector2 speed) {
        this.targetLayer = layer;
        this.speed = speed;
        this.direction = direction;
    }

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getComponent(ProjectileActions.class).shoot(direction, speed);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            return; // Not our hitbox
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            return; // Not our target
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        entity.getComponent(ProjectileActions.class).stopShoot();

        //soft dispose - It's still there and it will still collide
        entity.getComponent(TextureRenderComponent.class).dispose();

    }
}