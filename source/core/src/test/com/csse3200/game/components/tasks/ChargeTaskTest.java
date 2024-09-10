package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ChargeTaskTest {
    private Entity target;
    private Entity chargingEntity;
    private ChargeTask chargeTask;
    private static final float VIEW_DISTANCE = 10f;
    private static final float MAX_CHASE_DISTANCE = 15f;
    private static final float CHASE_SPEED = 5f;
    private static final float WAIT_TIME = 1f;

    @BeforeEach
    void setUp() {
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);

        target = new Entity();
        target.setPosition(5f, 5f);

        AITaskComponent ai = new AITaskComponent();
        chargingEntity = makePhysicsEntity()
                .addComponent(ai)
                .addComponent(new DirectionalNPCComponent(true));
        chargingEntity.setPosition(0f, 0f);

        NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig config = new NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig();
        config.priority = 10;
        config.viewDistance = VIEW_DISTANCE;
        config.chaseDistance = MAX_CHASE_DISTANCE;
        config.chaseSpeed = CHASE_SPEED;
        config.waitTime = WAIT_TIME;

        chargeTask = new ChargeTask(target, config);
        ai.addTask(chargeTask);
        chargingEntity.create();
    }

    @Test
    void shouldStartCharging() {
        chargeTask.start();
        assertEquals(ChargeTask.Status.ACTIVE, chargeTask.getStatus(), "ChargeTask should be active after starting");
    }

    @Test
    void shouldStopChargingWhenOutOfRange() {
        chargeTask.start();
        chargingEntity.setPosition(0f, 0f);
        target.setPosition(20f, 20f);
        chargeTask.update();

        assertEquals(-1, chargeTask.getPriority(), "Should return low priority when target is out of charge range");
    }

    @Test
    void shouldMaintainPriorityWhenInRange() {
        chargeTask.start();
        chargingEntity.setPosition(0f, 0f);
        chargeTask.update();

        assertEquals(10, chargeTask.getPriority(), "Should maintain priority when target is within charge range");
    }

    @Test
    void shouldTriggerChargeAndEventuallyResetPriority() {
        chargeTask.start();
        int initialPriority = chargeTask.getPriority();

        chargeTask.triggerCharge();
        assertEquals(15, chargeTask.getPriority(), "Priority should be set to trigger priority");

        chargeTask.movementTask.stop();
        chargeTask.update();

        if (chargeTask.waitTask != null) {
            chargeTask.waitTask.stop();
            chargeTask.update();
        }

        assertEquals(initialPriority, chargeTask.getPriority(), "Priority should reset to initial value after movement and wait tasks completion");
    }

    @Test
    void shouldUpdateBehaviorOnMovement() {
        chargeTask.start();
        Vector2 initialPosition = new Vector2(chargingEntity.getPosition());
        chargeTask.update();

        chargingEntity.setPosition(1f, 1f);
        chargeTask.update();

        assertNotEquals(initialPosition, chargingEntity.getPosition(), "Entity position should be updated");
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}