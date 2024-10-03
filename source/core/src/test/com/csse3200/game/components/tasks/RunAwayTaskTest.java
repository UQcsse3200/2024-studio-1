package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class RunAwayTaskTest {
    private GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, and game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldStartRunningAway() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        // Use a configuration object for RunAwayTask
        TaskConfig.RunAwayTaskConfig config = new TaskConfig.RunAwayTaskConfig();
        config.runSpeed = 3;
        config.stopDistance = 5;
        config.maxRunTime = 10;
        config.activationMinRange = 1;
        config.activationMaxRange = 10;
        config.activationHealth = 1f;
        config.cooldownTime = 5;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));

        Entity entity = makeEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Ensure the task is active and running away from the target
        float initialDistance = entity.getPosition().dst(target.getPosition());

        // Increase iterations to give more time for the entity to move
        for (int i = 0; i < 10; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Ensure the entity has moved away from the target
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance > initialDistance);
    }

    @Test
    void shouldStopRunningAway() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        // Use a configuration object for RunAwayTask
        TaskConfig.RunAwayTaskConfig config = new TaskConfig.RunAwayTaskConfig();
        config.runSpeed = 3;
        config.stopDistance = 5;
        config.maxRunTime = 10;
        config.activationMinRange = 1;
        config.activationMaxRange = 10;
        config.activationHealth = 1f;
        config.cooldownTime = 5;

        RunAwayTask runAwayTask = new RunAwayTask(target, config);
        AITaskComponent ai = new AITaskComponent().addTask(runAwayTask);
        Entity entity = makeEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Simulate entity running for a few updates
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Capture the entity's current position
        Vector2 stoppedPosition = entity.getPosition().cpy();
        entity.earlyUpdate();
        entity.update();
        ServiceLocator.getPhysicsService().getPhysics().update();

        // Check that the entity has stopped moving after triggering stopRunAway
        float tolerance = 0.05f;
        assertEquals(stoppedPosition.x, entity.getPosition().x, tolerance);
        assertEquals(stoppedPosition.y, entity.getPosition().y, tolerance);
    }

    @Test
    void shouldNotRunAwayIfNotInRange() {
        Entity target = new Entity();
        target.setPosition(20f, 20f); // Target out of view distance

        // Use a configuration object for RunAwayTask
        TaskConfig.RunAwayTaskConfig config = new TaskConfig.RunAwayTaskConfig();
        config.runSpeed = 3;
        config.stopDistance = 5;
        config.maxRunTime = 10;
        config.activationMinRange = 1;
        config.activationMaxRange = 10;
        config.activationHealth = 0.5f;
        config.cooldownTime = 5;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));
        Entity entity = makeEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Run a few update cycles and check that the entity has not moved
        Vector2 initialPosition = entity.getPosition().cpy();
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        assertEquals(initialPosition, entity.getPosition());
    }

    @Test
    void shouldStopRunningAwayWhenFarFromTarget() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        // Use a configuration object for RunAwayTask
        TaskConfig.RunAwayTaskConfig config = new TaskConfig.RunAwayTaskConfig();
        config.runSpeed = 20;
        config.stopDistance = 5;
        config.maxRunTime = 10;
        config.activationMinRange = 1;
        config.activationMaxRange = 4;
        config.activationHealth = 1f;
        config.cooldownTime = 5;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));
        Entity entity = makeEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Simulate running away until entity exceeds the stopDistance
        for (int i = 0; i < 10; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Ensure that the entity has stopped moving after exceeding maxChaseDistance
        float distance = entity.getPosition().dst(target.getPosition());
        assertTrue(distance >= 5); // Should be beyond stopDistance and stopped running
    }

    @Test
    void shouldRespectCooldownAfterRunningAway() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        // Use a configuration object for RunAwayTask
        TaskConfig.RunAwayTaskConfig config = new TaskConfig.RunAwayTaskConfig();
        config.runSpeed = 3;
        config.stopDistance = 5;
        config.maxRunTime = 10;
        config.activationMinRange = 1;
        config.activationMaxRange = 10;
        config.activationHealth = 1f;
        config.cooldownTime = 5;

        RunAwayTask runAwayTask = new RunAwayTask(target, config);
        AITaskComponent ai = new AITaskComponent().addTask(runAwayTask);
        Entity entity = makeEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Simulate the task running once
        when(gameTime.getTime()).thenReturn(1000L); // After 1 second
        entity.earlyUpdate();
        entity.update();
        runAwayTask.stop();

        // Simulate time passing (less than cooldown)
        when(gameTime.getTime()).thenReturn(2000L);  // 1 second later
        assertEquals(-1, runAwayTask.getPriority(), "Should not run again during cooldown");

        // Simulate time passing (cooldown complete)
        when(gameTime.getTime()).thenReturn(6000L);  // 5 seconds later
        assertEquals(9, runAwayTask.getPriority(), "RunAwayTask should be ready after cooldown");
    }

    private Entity makeEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new CombatStatsComponent(100,10));
    }
}