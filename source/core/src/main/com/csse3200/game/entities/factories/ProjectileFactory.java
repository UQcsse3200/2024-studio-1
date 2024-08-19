package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class ProjectileFactory {

    private static final ProjectileConfig stats =
            FileLoader.readClass(ProjectileConfig.class, "configs/Projecitle.json"); //needs a real file path


    public Entity createProjectile() {

        Entity projectile =
                new Entity()
                        .addComponent(new TextureRenderComponent(stats.projectileTexturePath))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(stats.Layer))
                        .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
                        .addComponent(new TouchAttackComponent(stats.Layer));

        PhysicsUtils.setScaledCollider(projectile, stats.scaleX, stats.scaleY);
        projectile.getComponent(ColliderComponent.class).setDensity(1.5f);
        projectile.getComponent(TextureRenderComponent.class).scaleEntity();

        return projectile;
    }

    private void shootProjectile(Vector2 startPosition, Vector2 direction, Entity projectile) {
        projectile.getComponent(TouchAttackComponent.class).create();
        projectile.getEvents().trigger("shootProjectile", startPosition, direction);
        //projectile.addComponent(MovementTask()) - move the projectile
        projectile.getEvents().addListener("collisionStart", this::cleanUp);
    }


    // this method occurs following the collision
    public void cleanUp(Fixture me, Fixture other) {
        //
    }

}




