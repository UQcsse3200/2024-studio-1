package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.GameAreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class FollowTaskTest {
    private Entity entity;
    private TaskConfig.FollowTaskConfig config;

    @BeforeEach
    void beforeEach() {

        ServiceLocator.registerRenderService(mock(RenderService.class));
        ServiceLocator.registerTimeSource(mock(GameTime.class));


        PhysicsEngine physicsEngine = new PhysicsEngine();
        ServiceLocator.registerPhysicsService(new PhysicsService(physicsEngine));

        // Initialize and register GameAreaService
        GameAreaService gameAreaService = mock(GameAreaService.class);
        GameController gameController = mock(GameController.class);
        Entity player = new Entity(); // Create a new player entity
        when(gameController.getPlayer()).thenReturn(player);
        when(gameAreaService.getGameController()).thenReturn(gameController);
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Create an entity with necessary components
        entity = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());

        // Define configuration for FollowTask
        config = new TaskConfig.FollowTaskConfig();
        config.followRadius = 5f;
        config.followSpeed = 10f;
        config.waitTime = 2f;
    }

    @Test
    void shouldSwitchBetweenTasks() {
        FollowTask followTask = new FollowTask(config);
        followTask.create(() -> entity);

        // Start the task and verify initial state
        followTask.start();
        Task initialTask = getCurrentTask(followTask);
        assertEquals(followTask.movementTask, initialTask);

        // Simulate update cycle to switch tasks
        followTask.update();

        // Check if it switches to wait task after movement task completes
        Task afterMoveTask = getCurrentTask(followTask);

        assertEquals(followTask.movementTask, afterMoveTask);


        followTask.update();
        Task afterWaitTask = getCurrentTask(followTask);
        assertEquals(followTask.movementTask, afterWaitTask);
    }

    @Test
    void shouldHaveLowPriority() {
        FollowTask followTask = new FollowTask(config);
        followTask.create(() -> entity);

        // Verify that the priority is always low
        assertEquals(1, followTask.getPriority());
    }

    // Helper method to access the private field currentTask using reflection
    private Task getCurrentTask(FollowTask followTask) {
        try {
            java.lang.reflect.Field field = FollowTask.class.getDeclaredField("currentTask");
            field.setAccessible(true);
            return (Task) field.get(followTask);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
