package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.entities.Entity;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class BossAttackTaskTest {
    private Entity target;
    private Entity boss;
    private BossAttackTask bossAttackTask;
    private static final float VIEW_DISTANCE = 10f;
    private static final float MAX_CHASE_DISTANCE = 15f;
    private static final float CHASE_SPEED = 2f;
    private static final float CHARGE_SPEED = 5f;
    private static final float WAIT_TIME = 1f;

    @BeforeEach
    void setUp() {
        // Mock rendering and physics
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Mock game time
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);

        // Set up target entity
        target = new Entity();
        target.setPosition(5f, 5f);

        // Set up boss entity and its task component
        AITaskComponent ai = new AITaskComponent();
        boss = makePhysicsEntity()
                .addComponent(ai)
                .addComponent(new DirectionalNPCComponent(true)); // Add DirectionalNPCComponent
        boss.setPosition(0f, 0f);

        // Create the BossAttackTask
        bossAttackTask = new BossAttackTask(target, 10, VIEW_DISTANCE, MAX_CHASE_DISTANCE, CHASE_SPEED, CHARGE_SPEED, WAIT_TIME);
        ai.addTask(bossAttackTask);
        boss.create();
    }

    @Test
    void shouldStartWithChaseTask() {
        bossAttackTask.start();
        assertInstanceOf(ChaseTask.class, bossAttackTask.currentTask, "Initial task should be ChaseTask");
    }

    @Test
    void shouldStopChasingWhenOutOfRange() {
        bossAttackTask.start();
        boss.setPosition(0f, 0f); // far away from the target
        target.setPosition(20f, 20f); // move target out of range
        bossAttackTask.update();

        assertEquals(-1, bossAttackTask.getPriority(), "Should return low priority when target is out of chase range");
    }

    @Test
    void shouldMaintainPriorityWhenInRange() {
        bossAttackTask.start();
        boss.setPosition(0f, 0f); // within max chase distance
        bossAttackTask.update();

        assertEquals(10, bossAttackTask.getPriority(), "Should maintain priority when target is within chase range");
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}
