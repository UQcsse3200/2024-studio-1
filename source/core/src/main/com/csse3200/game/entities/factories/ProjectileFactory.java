package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * For weapon            - stats param - Design a projectile config for a weapon - checkout configs/ProjectileConfig.
 * For map               - All Layers if you want to contain a projectile.
 * For health players    - Damage is acted by via hit() method on Entity.
 *
 * NOTE - Entity is unregistered after collision. Add only to a single use entity - dive-bombing bird IDK.
 */
public class ProjectileFactory {


    /**
     * Makes a new Entity with projectile components.
     */
    public static Entity createProjectile(ProjectileConfig stats, Vector2 position, Vector2 direction) {



        Entity projectile =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(stats.Layer))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(direction, stats.speed))
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new TextureRenderComponent(stats.projectileTexturePath));


        projectile.setPosition(position);
        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);
        projectile.getComponent(TextureRenderComponent.class).scaleEntity();


        return projectile;
    }

    private ProjectileFactory() {throw new IllegalStateException("Instantiating static util class");}

}







