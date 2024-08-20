package com.csse3200.game.entities.factories;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * For weapon - Design a projectile config for your gun, held in stats arg.
 *              Every projectile has an owner entity - check out config.
 *              One projectile is shot once - shooting removes the projectile.
 *              Projectiles are made listening for a "shootProjectile" with args including owner id.
 *              Projectile is unregistered after collision.
 * For map - Consider physics layers if you want to contain a projectile.
 * For health players - damage is acted by projectileAttackComponent.
 *
 */
public class ProjectileFactory {


    /**
     * makes a new Entity with projectile components.
     *
     * @param stats - defines a projectile, will be reused often.
     * @param owner - the entity.getId() that would like to trigger this projectile to shoot.
     *
     * @return Entity projectile
     */
    public Entity createProjectile(ProjectileConfig stats, int owner) {



        Entity projectile =
                new Entity()
                        .addComponent(new TextureRenderComponent(stats.projectileTexturePath))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(stats.Layer))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(owner, stats.Layer))
                        .addComponent(new PhysicsMovementComponent());



        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);
        projectile.getComponent(TextureRenderComponent.class).scaleEntity();


        return projectile;
    }

    private ProjectileFactory() {throw new IllegalStateException("Instantiating static util class");}

}







