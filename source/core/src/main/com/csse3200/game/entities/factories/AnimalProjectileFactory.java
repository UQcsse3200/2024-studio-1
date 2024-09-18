package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.components.projectile.ProjectileAnimationController;
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
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> Factory for producing entities with a projectile themed component configuration.
 */
public class AnimalProjectileFactory extends ProjectileFactory {

    private static final Logger logger = LoggerFactory.getLogger(AnimalProjectileFactory.class);

    /**
     * Makes a new Entity with projectile components.
     *
     * @param stats     Contains all the re-usable projectile configurations. See ProjectileConfig.
     * @param direction  the Vector2 where the projectile is launched 
     * @param  parentPosition the origin point for the projectile 
     * @return Entity the returned projectile Entity
     */
    @Override
    public Entity createProjectile(ProjectileConfig stats, Vector2 direction, Vector2 parentPosition) {

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/npc/dragon/dragon.atlas", TextureAtlas.class));
        animator.addAnimation("fire_attack_left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("fire_attack_right", 0.1f, Animation.PlayMode.LOOP);

        Entity projectile =
                new Entity()
                        .addComponent(new NameComponent("Animal Projectile"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(stats.Layer))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(stats.Layer, direction, stats.speed, parentPosition))
                        .addComponent(new ProjectileActions())
                        .addComponent(new DirectionalNPCComponent(true))
                        .addComponent(new ProjectileAnimationController())
                        .addComponent(animator);

        projectile.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        //projectile.setScale(stats.scaleX, stats.scaleY);
        projectile.getComponent(AnimationRenderComponent.class).scaleEntity();


        return projectile;
    }

    @Override
    protected String[] getTextureAtlasFilepaths() {
        return new String[] {
                "images/npc/dragon/dragon.atlas"
        };
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/npc/dragon/dragon.png"
        };
    }
}









