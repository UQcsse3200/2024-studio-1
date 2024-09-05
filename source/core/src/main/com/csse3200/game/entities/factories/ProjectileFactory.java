package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for producing entities with a projectile themed component configuration.
 */
public class ProjectileFactory {

    private static final String[] projectileAtlas = {
            "images/Projectiles/GreenShoot.atlas",
    };

    private static final String[] projectileTextures = {
            "images/Projectiles/GreenShoot.png",
    };

    private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);

    /**
     * Makes a new Entity with projectile components.
     * @param stats Contains all the re-usable projectile configurations. See ProjectileConfig.
     * @param direction Direction of shot projectile.
     */
    public static Entity createProjectile(ProjectileConfig stats, Vector2 direction) {

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(stats.projectileAtlasPath, TextureAtlas.class));
        animator.addAnimation("GreenShoot", 0.1f, Animation.PlayMode.LOOP);

        Entity projectile =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(stats.Layer))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(stats.Layer, direction, stats.speed))
                        .addComponent(new ProjectileActions())
                        .addComponent(animator);



        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.setScale(stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);



        return projectile;
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(projectileTextures);
        resourceService.loadTextureAtlases(projectileAtlas);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    public ProjectileFactory() {
        loadAssets();
    }

}







