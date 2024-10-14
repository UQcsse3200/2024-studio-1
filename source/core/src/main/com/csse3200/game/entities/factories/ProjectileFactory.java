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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Factory for producing entities with a projectile themed component configuration.
 */
public class ProjectileFactory extends LoadedFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProjectileFactory.class);
    private final ProjectileConfigs configs = loadConfigs();

    public static ProjectileConfigs loadConfigs() {
        Path path = Paths.get("configs/projectiles.json");
        ProjectileConfigs configs = FileLoader.readClass(ProjectileConfigs.class, "configs/projectiles.json");
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

    /**
     * Simple create function for external use
     * @param specification name of projectile
     * @param direction direction of movement
     * @param parentPosition position of shooting entity
     * @return a projectile entity with given spec
     */
    public Entity create(String specification, Vector2 direction, Vector2 parentPosition) {
        return switch (specification) {
            case "dragonProjectile" -> this.createDragonProjectile(direction, parentPosition);
            case "kitsuneProjectile1" -> this.createKitsuneProjectile1(direction, parentPosition);
            case "kitsuneProjectile2" -> this.createKitsuneProjectile2(direction, parentPosition);
            case "kitsuneProjectile" -> this.createKitsuneProjectile(direction, parentPosition);
            case "cthuluProjectile" -> this.createCthuluProjectile(direction, parentPosition);
            default -> throw new IllegalArgumentException("Unknown animal: " + specification);
        };
    }

    /**
     * Create a dragon projectile with predefined components and animations.
     * @param direction direction of movement
     * @param parentPosition position of Dragon
     * @return a dragon projectile entity with given spec
     */
    public Entity createDragonProjectile(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.dragonProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/dragon/dragon.atlas", config.animations);
        Entity dragonProjectile = createProjectile("Dragon Projectile", config, direction,
                parentPosition, animator);

        return dragonProjectile;
    }

    /**
     * Create a Cthulu projectile with predefined components and animations.
     * @param direction direction of movement
     * @param parentPosition position of Cthulu
     * @return a dragon projectile entity with given spec
     */
    public Entity createCthuluProjectile(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.cthuluProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/cthulu/cthulu_bullet.atlas", config.animations);
        Entity cthuluProjectile = createProjectile("Cthulu Projectile", config, direction,
                parentPosition, animator);

        return cthuluProjectile;
    }

    /**
     * Create a kitsune projectile type 1 with predefined components and animations.
     * @param direction direction of movement
     * @param parentPosition position of Kitsune
     * @return a kitsune projectile type 1 entity with given spec
     */
    public Entity createKitsuneProjectile1(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.kitsuneProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/kitsune/fire1.atlas", config.animations);
        Entity kitsuneProjectile = createProjectile("Kitsune Projectile1", config, direction,
                parentPosition, animator);

        return kitsuneProjectile;
    }

    /**
     * Create a kitsune projectile with predefined components and animations.
     * @param direction direction of movement
     * @param parentPosition position of Kitsune
     * @return a kitsune projectile type 1 entity with given spec
     */
    public Entity createKitsuneProjectile(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.kitsuneProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/kitsune/kitsune_bullet.atlas", config.animations);
        Entity kitsuneProjectile = createProjectile("Kitsune Projectile", config, direction,
                parentPosition, animator);
        kitsuneProjectile.setScale(0.3f, 0.3f);
        return kitsuneProjectile;
    }

    /**
     * Create a kitsune projectile type 2 with predefined components and animations.
     * @param direction direction of movement
     * @param parentPosition position of Kitsune
     * @return a kitsune projectile type 2 entity with given spec
     */
    public Entity createKitsuneProjectile2(Vector2 direction, Vector2 parentPosition) {
        ProjectileConfigs.BaseProjectileConfig config = configs.kitsuneProjectile;
        AnimationRenderComponent animator = createAnimator("images/npc/kitsune/fire2.atlas", config.animations);
        Entity kitsuneProjectile = createProjectile("Kitsune Projectile2", config, direction,
                parentPosition, animator);

        return kitsuneProjectile;
    }

    /**
     * Makes a new Entity with projectile components. This is the newer version that support directional animations,
     *      custom projectile configs loaded from json files.
     * @param name name of projectile
     * @param stats projectile's stats
     * @param direction direction of movement
     * @param parentPosition position of shooting entity
     * @param animator The animator component for the projectile
     * @return the created projectile entity
     */
    public Entity createProjectile(String name, ProjectileConfigs.BaseProjectileConfig stats,
                                   Vector2 direction, Vector2 parentPosition,
                                   AnimationRenderComponent animator) {

        Entity projectile = new Entity()
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
        PhysicsUtils.setScaledCollider(projectile, 0.5f, 0.5f);
        projectile.getComponent(AnimationRenderComponent.class).scaleEntity();
        projectile.setScale(stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(0.5f);


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
        return projectile;
    }

    /**This projectile is a shotgun effect.
     * Four total projectiles with spread of +-0.07 RAD and Lag speed of 90%.
     * Shot in format:
     *                         (+0.07)
     *  gun(->)      (Lag)     (Norm)
     *                         (-0.7)
     * NOTE - Magic numbers should not be tested.
     **/
     public List<Entity> createShotGunProjectile (ProjectileConfig stats, Vector2 direction,
                                       Vector2 parentPosition) {
        float followSpeed = 0.9F;
        float plusMinus = 0.07F;

        Vector2 rectCordMore = direction.cpy().rotateRad(plusMinus);
        Vector2 rectCordLess = direction.cpy().rotateRad(-plusMinus);
        Vector2 follower = direction.cpy().setLength(followSpeed);

        List<Vector2> directions = Arrays.asList(rectCordMore, direction, rectCordLess, follower);
        List<Entity> projectiles = new ArrayList<>();
        for (Vector2 dir : directions) {
            projectiles.add(createProjectile(stats, dir, parentPosition));
        }
        return projectiles;
    }


    @Override
    protected String[] getTextureAtlasFilepaths() {
        return new String[] {
                "images/Projectiles/GreenShoot.atlas",
                "images/npc/dragon/dragon.atlas",
                "images/npc/kitsune/fire1.atlas",
                "images/npc/kitsune/fire2.atlas",
                "images/npc/cthulu/cthulu_bullet.atlas",
                "images/npc/kitsune/kitsune_bullet.atlas"
        };
    }

    @Override
    protected String[] getTextureFilepaths() {
        return new String[]{
                "images/Projectiles/GreenShoot.png",
                "images/npc/dragon/dragon.png",
                "images/npc/kitsune/fire1.png",
                "images/npc/kitsune/fire2.png",
                "images/npc/cthulu/cthulu_bullet.png",
                "images/npc/kitsune/kitsune_bullet.png"
        };
    }


}









