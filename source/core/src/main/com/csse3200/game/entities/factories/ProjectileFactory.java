package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.components.projectile.ProjectileActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory for producing entities with a projectile themed component configuration.
 */
public class ProjectileFactory {


    /**
     * Makes a new Entity with projectile components.
     * @param stats Contains all the re-usable projectile configurations. See ProjectileConfig.
     * @param direction Direction of shot projectile.
     */
    public static Entity createProjectile(ProjectileConfig stats, Vector2 direction) {


        Entity projectile =
                new Entity()
                        .addComponent(new TextureRenderComponent(stats.projectileTexturePath))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(stats.Layer))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(stats.Layer, direction, stats.speed))
                        .addComponent(new ProjectileActions());



        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);
        projectile.getComponent(TextureRenderComponent.class).scaleEntity();


        return projectile;
    }

    /**
     * not to be initialised.
     */
    private ProjectileFactory() {throw new IllegalStateException("Instantiating static util class");}

}







