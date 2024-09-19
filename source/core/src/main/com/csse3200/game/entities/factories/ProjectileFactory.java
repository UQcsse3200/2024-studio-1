package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.*;

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
        animator.addAnimation("GreenShoot", 0.1f, Animation.PlayMode.LOOP);

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

        projectile.getComponent(ProjectileAttackComponent.class).create();
        projectile.getComponent(AnimationRenderComponent.class).startAnimation("GreenShoot");
        projectile.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.setScale(stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9,9), true, true);
        return projectile;
    }

    public void createShotGunProjectile (ProjectileConfig stats, Vector2 direction, Vector2 parentPosition) {
        Double polarAngle = atan(direction.y / direction.x);
        float followSpeed = 0.9F;
        float scale = 1;
        if (direction.x < 0) {
             scale = -1;
        }
        Double plusMinus = 0.07;
        Vector2 rectCordMore = new Vector2(scale * (float) (cos(polarAngle + plusMinus)), (float) ( sin(polarAngle + plusMinus)));
        Vector2 rectCordLess = new Vector2(scale * (float)  (cos(polarAngle - plusMinus)), (float) ( sin(polarAngle - plusMinus)));
        Vector2 follower = new Vector2(followSpeed * direction.x, followSpeed * direction.y);
        List<Vector2> directions = Arrays.asList(rectCordMore, direction, rectCordLess, follower);
        for (Vector2 dir : directions) {
            createProjectile(stats, dir, parentPosition);
        }
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









