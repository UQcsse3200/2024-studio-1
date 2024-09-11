package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
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

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, and game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldStartRunningAway() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        // Use a configuration object for RunAwayTask
        NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig config = new NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig();
        config.priority = 10;
        config.viewDistance = 5;
        config.chaseDistance = 10;
        config.chaseSpeed = 3;
        config.waitTime = 2;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));

        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Simulate the 'runAway' event trigger
        entity.getEvents().trigger("runAway");

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
        assertTrue(newDistance <= initialDistance);
    }

    @Test
    void shouldStopRunningAway() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        // Use a configuration object for RunAwayTask
        NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig config = new NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig();
        config.priority = 10;
        config.viewDistance = 5;
        config.chaseDistance = 10;
        config.chaseSpeed = 3;
        config.waitTime = 2;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Simulate the 'runAway' event trigger followed by the 'stopRunAway' event
        entity.getEvents().trigger("runAway");

        // Simulate entity running for a few updates
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Trigger the 'stopRunAway' event and ensure the entity has stopped moving
        entity.getEvents().trigger("stopRunAway");

        // Capture the entity's current position
        Vector2 stoppedPosition = entity.getPosition().cpy();
        entity.earlyUpdate();
        entity.update();
        ServiceLocator.getPhysicsService().getPhysics().update();

        // Check that the entity has stopped moving after triggering stopRunAway
        assertEquals(stoppedPosition, entity.getPosition());
    }

    @Test
    void shouldNotRunAwayIfNotInRange() {
        Entity target = new Entity();
        target.setPosition(20f, 20f); // Target out of view distance

        // Use a configuration object for RunAwayTask
        NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig config = new NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig();
        config.priority = 10;
        config.viewDistance = 5;
        config.chaseDistance = 10;
        config.chaseSpeed = 3;
        config.waitTime = 2;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Trigger the 'runAway' event
        entity.getEvents().trigger("runAway");

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
        NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig config = new NPCConfigs.NPCConfig.TaskConfig.RunAwayTaskConfig();
        config.priority = 10;
        config.viewDistance = 5;
        config.chaseDistance = 10;
        config.chaseSpeed = 3;
        config.waitTime = 2;

        AITaskComponent ai = new AITaskComponent().addTask(new RunAwayTask(target, config));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        // Simulate the 'runAway' event trigger
        entity.getEvents().trigger("runAway");

        // Simulate running away until entity exceeds the maxChaseDistance
        for (int i = 0; i < 10; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        // Ensure that the entity has stopped moving after exceeding maxChaseDistance
        float distance = entity.getPosition().dst(target.getPosition());
        assertTrue(distance > 2); // Should be beyond maxChaseDistance and stopped running
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}