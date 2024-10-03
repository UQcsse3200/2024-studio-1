package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class ChargeTaskTest {
    // Entities and components
    private Entity target;
    private Entity chargingEntity;
    private ChargeTask chargeTask;
    private AITaskComponent ai;
    private GameTime gameTime;
    private static final float ACTIVATION_MIN_RANGE = 2f;
    private static final float ACTIVATION_MAX_RANGE = 10f;
    private static final float CHASE_SPEED = 5f;
    private static final float WAIT_TIME = 1f;
    private static final float DISTANCE_MULTIPLIER = 1.2f;
    private static final float COOLDOWN_TIME = 5f;

    @BeforeEach
    void setUp() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);

        target = new Entity();
        target.setPosition(5f, 5f);

        ai = new AITaskComponent();
        chargingEntity = makePhysicsEntity()
                .addComponent(ai)
                .addComponent(new DirectionalNPCComponent(true));
        chargingEntity.setPosition(0f, 0f);

        // Configuring ChargeTask
        TaskConfig.ChargeTaskConfig config = new TaskConfig.ChargeTaskConfig();
        config.activationMinRange = ACTIVATION_MIN_RANGE;
        config.activationMaxRange = ACTIVATION_MAX_RANGE;
        config.chaseSpeed = CHASE_SPEED;
        config.waitTime = WAIT_TIME;
        config.distanceMultiplier = DISTANCE_MULTIPLIER;
        config.cooldownTime = COOLDOWN_TIME;

        chargeTask = new ChargeTask(target, config);
        ai.addTask(chargeTask);
        chargingEntity.create();
    }

    @Test
    void shouldStartCharging() {    // To check if charge task is starting correctly
        chargeTask.start();
        assertEquals(Task.Status.ACTIVE, chargeTask.getStatus(), "ChargeTask should be active after starting");
    }

    @Test
    void shouldNotChargeWhenOutOfRange() {
        target.setPosition(20f, 20f);
        ai.update();

        assertEquals(-1, chargeTask.getPriority(), "Should return low priority when target is out of charge range");
    }

    @Test
    void shouldMaintainPriorityWhenInRange() {
        target.setPosition(5f, 5f);
        ai.update();

        assertEquals(10, chargeTask.getPriority(), "Should maintain priority when target is within charge range");
    }

    @Test
    void shouldChargeOnceAndWait() {
        chargeTask.start();
        chargeTask.update(); // Simulate charge in progress
        chargeTask.stop();

        // After charging once, task should switch to inactive
        assertEquals(Task.Status.INACTIVE, chargeTask.getStatus(), "ChargeTask should be inactive after one charge");
    }

    @Test
    void shouldRespectCooldownBetweenCharges() {
        when(gameTime.getTime()).thenReturn(1000L); // After 1 second
        chargeTask.start();
        chargeTask.update();
        chargeTask.stop();

        // Simulate time passing (less than cooldown)
        when(gameTime.getTime()).thenReturn(2000L);  // 1 second later
        assertEquals(-1, chargeTask.getPriority(), "Should not be able to charge during cooldown");

        // Simulate time passing (cooldown complete)
        when(gameTime.getTime()).thenReturn(6000L);  // 5 seconds later
        assertEquals(8, chargeTask.getPriority(), "ChargeTask should be ready after cooldown");
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}