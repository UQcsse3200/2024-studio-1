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

public class ProjectileAttackComponent extends Component {

    private final short targetLayer;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;
    private Vector2 speed;
    private Vector2 direction;

    public ProjectileAttackComponent(Vector2 direction, Vector2 speed) {
        this.targetLayer = PhysicsLayer.NPC; // Default target layer, update as needed
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