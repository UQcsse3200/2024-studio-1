package com.csse3200.game.components.Projectile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class ProjectileActionsTest {

    @BeforeEach
    void beforeEach() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerTimeSource(gameTime);

        //load in the current default texture.
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(new String []{new ProjectileConfig().projectileTexturePath});
        while (!resourceService.loadForMillis(10)) {
            // wait for assets to load.
        }

    }

    @org.junit.jupiter.api.Test
    void shouldMove() {

        Entity projectile = createProjectile(Vector2Utils.LEFT);
        projectile.create();
        projectile.setPosition(0f, 0f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectile.earlyUpdate();
            projectile.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        float distance = projectile.getPosition().dst(0f,0f);
        assertTrue(distance > 0);
    }

    @org.junit.jupiter.api.Test
    void shouldMoveLeft() {

        Entity projectile = createProjectile(Vector2Utils.LEFT);
        projectile.create();
        projectile.setPosition(0f, 0f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectile.earlyUpdate();
            projectile.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float yChange = projectile.getPosition().y;
        float xChange = projectile.getPosition().x;
        assertEquals(0f, yChange);
        assertTrue(xChange < 0f);
    }

    @org.junit.jupiter.api.Test
    void shouldMoveRight() {

        Entity projectile = createProjectile(Vector2Utils.RIGHT);
        projectile.create();
        projectile.setPosition(0f, 0f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectile.earlyUpdate();
            projectile.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float yChange = projectile.getPosition().y;
        float xChange = projectile.getPosition().x;
        assertEquals(0f, yChange);
        assertTrue(xChange > 0f);
    }

    @org.junit.jupiter.api.Test
    void shouldMoveUp() {

        Entity projectile = createProjectile(Vector2Utils.UP);
        projectile.create();
        projectile.setPosition(0f, 0f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectile.earlyUpdate();
            projectile.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float yChange = projectile.getPosition().y;
        float xChange = projectile.getPosition().x;
        assertEquals(0f, xChange);
        assertTrue(yChange > 0f);
    }

    @org.junit.jupiter.api.Test
    void shouldMoveDown() {

        Entity projectile = createProjectile(Vector2Utils.DOWN);
        projectile.create();
        projectile.setPosition(0f, 0f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectile.earlyUpdate();
            projectile.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float yChange = projectile.getPosition().y;
        float xChange = projectile.getPosition().x;
        assertEquals(0f, xChange);
        assertTrue(yChange < 0f);
    }


    @org.junit.jupiter.api.Test
    void shouldMoveMultiple() {

        Entity projectileDown = createProjectile(Vector2Utils.DOWN);
        Entity projectileUp = createProjectile(Vector2Utils.UP);
        projectileDown.create();
        projectileUp.create();
        projectileDown.setPosition(0f, 0f);
        projectileUp.setPosition(2f, 0f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectileDown.earlyUpdate();
            projectileUp.earlyUpdate();
            projectileDown.update();
            projectileUp.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float upChange = projectileUp.getPosition().dst(1f,0f);;
        float downChange = projectileDown.getPosition().dst(0f,0f);;
        assertTrue(upChange > 0);
        assertTrue(downChange > 0);
    }

    @org.junit.jupiter.api.Test
    void shouldMoveAtSpeed() {

        ProjectileConfig fastStats = new ProjectileConfig();
        fastStats.speed = new Vector2(10,10);
        Entity projectileFast = ProjectileFactory.createProjectile(fastStats, Vector2Utils.LEFT);
        projectileFast.create();

        ProjectileConfig slowStats = new ProjectileConfig();
        slowStats.speed = new Vector2(3,3);
        Entity projectileSlow = ProjectileFactory.createProjectile(slowStats, Vector2Utils.LEFT);
        projectileSlow.create();


        projectileFast.setPosition(0f, 0f);
        projectileSlow.setPosition(0f, 2f);

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            projectileFast.earlyUpdate();
            projectileFast.update();

            projectileSlow.earlyUpdate();
            projectileSlow.update();

            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        float slowChange = projectileSlow.getPosition().dst(0f,2f);
        float fastChange = projectileFast.getPosition().dst(0f,0);
        assertTrue(slowChange < fastChange);
    }



    //Vector2Utils.LEFT
    Entity createProjectile(Vector2 direction) {
        Entity projectile = ProjectileFactory.createProjectile(new ProjectileConfig(), direction);
        projectile.create();
        return projectile;
    }


}