package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.components.projectile.ProjectileAnimationController;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.components.projectile.ProjectileActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.configs.ProjectileConfigs;
import com.csse3200.game.files.FileLoader;
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
import java.nio.file.*;

/**
 * Factory for producing entities with a projectile themed component configuration.
 */
public class ProjectileFactory extends LoadedFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);
    private final ProjectileConfigs configs = loadConfigs();

    public static ProjectileConfigs loadConfigs() {
        Path path = Paths.get("configs/projectiles.json");
        ProjectileConfigs configs = FileLoader.readClass(ProjectileConfigs.class, "configs/projectiles.json");
        System.out.println("BRUHHH");
        return configs;
    }

    public ProjectileFactory() {
        super(logger);
    }

    /**
     * Helper method to create an AnimationRenderComponent for an NPC.
     *
     * @param atlasPath The path to the texture atlas for the NPC
     * @param animations An array of animations for the NPC
     * @return The created AnimationRenderComponent
     */
    private static AnimationRenderComponent createAnimator(String atlasPath,
                                                           ProjectileConfigs.BaseProjectileConfig.ProjectileAnimations[] animations) {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class));
        for (ProjectileConfigs.BaseProjectileConfig.ProjectileAnimations animation : animations) {
            animator.addAnimation(animation.name, animation.frameDuration, animation.playMode);
        }
        return animator;
    }

    public Entity create(String specification, Vector2 direction, Vector2 parentPosition) {
        return switch (specification) {
            case "dragonProjectile" -> this.createDragonProjectile(direction, parentPosition);
            case "kitsuneProjectile" -> this.createKitsuneProjectile(direction, parentPosition);
            default -> throw new IllegalArgumentException("Unknown animal: " + specification);
        };
    }

    public Entity createDragonProjectile(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.dragonProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/dragon/dragon.atlas", config.animations);
        Entity dragonProjectile = createProjectile("Dragon Projectile", config, direction,
                parentPosition, animator);

        return dragonProjectile;
    }

    public Entity createKitsuneProjectile(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.kitsuneProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/kitsune/fire1.atlas", config.animations);
        Entity kitsuneProjectile = createProjectile("Kitsune Projectile", config, direction,
                parentPosition, animator);

        return kitsuneProjectile;
    }

    /**
     * Makes a new Entity with projectile components.
     *
     * @param stats     Contains all the re-usable projectile configurations. See ProjectileConfig.
     * @param direction Direction of shot projectile.
     */
    public Entity createProjectile(String name, ProjectileConfigs.BaseProjectileConfig stats,
                                   Vector2 direction, Vector2 parentPosition,
                                   AnimationRenderComponent animator) {

        Entity projectile =
                new Entity()
                        .addComponent(new NameComponent(name))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new ProjectileAttackComponent(stats.Layer, direction, stats.speed, parentPosition))
                        .addComponent(new DirectionalNPCComponent(stats.isDirectional))
                        .addComponent(new ProjectileActions())
                        .addComponent(new ProjectileAnimationController())
                        .addComponent(animator);

        projectile.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.getComponent(AnimationRenderComponent.class).scaleEntity();
        projectile.setScale(stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);


        return projectile;
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
                "images/npc/dragon/dragon.atlas",
                "images/npc/kitsune/fire1.atlas",
                "images/npc/kitsune/fire2.atlas"
        };
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Projectiles/GreenShoot.png",
                "images/npc/dragon/dragon.png",
                "images/npc/kitsune/fire1.png",
                "images/npc/kitsune/fire2.png"
        };
    }


}









