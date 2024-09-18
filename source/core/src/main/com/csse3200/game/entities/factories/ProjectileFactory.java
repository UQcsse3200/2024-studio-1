package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.components.projectile.ProjectileActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for producing entities with a projectile themed component configuration.
 */
public class ProjectileFactory extends LoadedFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);
    public ProjectileFactory() {
        super(logger);
    }


    /**
     * Makes a new Entity with projectile components.
     *
     * @param stats     Contains all the re-usable projectile configurations. See ProjectileConfig.
     * @param direction Direction of shot projectile.
     */
    public Entity createProjectile(ProjectileConfig stats, Vector2 direction, Vector2 parentPosition) {

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(stats.projectileAtlasPath, TextureAtlas.class));
        animator.addAnimation("Shoot", 0.1f, Animation.PlayMode.LOOP);

        Entity projectile =
                new Entity()
                        .addComponent(new NameComponent("projectile"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(stats.Layer, direction, stats.speed, parentPosition))
                        .addComponent(new ProjectileActions())
                        .addComponent(animator);

        projectile.getComponent(AnimationRenderComponent.class).startAnimation("Shoot");
        projectile.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.setScale(stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);


        return projectile;
    }


    @Override
    protected String[] getTextureAtlasFilepaths() {
        return new String[] {
                "images/Projectiles/GreenShoot.atlas",
        };
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Projectiles/GreenShoot.png",
        };
    }
}









