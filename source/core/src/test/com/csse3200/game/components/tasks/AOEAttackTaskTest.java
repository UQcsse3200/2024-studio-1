package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AOEAttackTaskTest {
    private AOEAttackTask aoeAttackTask;
    private GameTime gameTimeMock;
    private AOEAttackComponent aoeAttackComponentMock;
    private Entity targetMock;
    private Entity ownerEntityMock;

    @BeforeEach
    void setUp() {
        // Mock TaskConfig
        TaskConfig.AOEAttackTaskConfig config = new TaskConfig.AOEAttackTaskConfig();
        config.activationMinRange = 5f;
        config.activationMaxRange = 15f;
        config.cooldownTime = 10f;
        config.preparationTime = 2f;

        // Mock GameTime
        gameTimeMock = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTimeMock);

        // Mock Entity and Components
        targetMock = mock(Entity.class);
        when(targetMock.getPosition()).thenReturn(new Vector2(10, 10));

        ownerEntityMock = mock(Entity.class);
        aoeAttackComponentMock = mock(AOEAttackComponent.class);

        when(ownerEntityMock.getComponent(AOEAttackComponent.class)).thenReturn(aoeAttackComponentMock);
        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(8, 8));

        // Mock EventHandler
        EventHandler eventHandlerMock = mock(EventHandler.class);
        when(ownerEntityMock.getEvents()).thenReturn(eventHandlerMock);

        // Initialize AOEAttackTask
        AITaskComponent aiTaskComponentMock = mock(AITaskComponent.class);
        when(aiTaskComponentMock.getEntity()).thenReturn(ownerEntityMock);

        aoeAttackTask = new AOEAttackTask(targetMock, config);
        aiTaskComponentMock.addTask(aoeAttackTask);
        ownerEntityMock.addComponent(aiTaskComponentMock);

        aoeAttackTask.create(aiTaskComponentMock);
    }

    @Test
    void shouldStartPreparationPhase() {
        // Execute
        aoeAttackTask.start();

        // Verify
        EventHandler eventHandler = ownerEntityMock.getEvents();

        verify(eventHandler, times(1)).trigger("aoe_preparation");
        verify(eventHandler, times(1)).trigger("gesture");
    }

    @Test
    void shouldTriggerAOEAttackAfterPreparation() {
        // Setup preparation start time
        when(gameTimeMock.getTime()).thenReturn(1000L); // Start time
        aoeAttackTask.start();

        // Simulate time passing beyond preparation time
        when(gameTimeMock.getTimeSince(1000L)).thenReturn(2000L); // 2 seconds later

        // Execute update to transition from preparation to attack
        aoeAttackTask.update();

        // Verify attack is triggered
        EventHandler eventHandler = ownerEntityMock.getEvents();

        verify(eventHandler, times(1)).trigger("attack");
        verify(aoeAttackComponentMock, times(1)).enableForNumAttacks(1);
    }

    @Test
    void shouldStopAfterWaiting() {
        // Setup
        when(gameTimeMock.getTime()).thenReturn(1000L); // Start time
        aoeAttackTask.start();

        // Simulate preparation and attack phases completion
        when(gameTimeMock.getTimeSince(1000L)).thenReturn(5000L); // 5 seconds later

        // Execute update to complete task cycle
        aoeAttackTask.update();

        // Verify task stops after wait task finishes
        assertEquals(Task.Status.ACTIVE, aoeAttackTask.getStatus());
    }

    @Test
    void shouldReturnInactivePriorityWhenCooldownNotComplete() {
        // Setup cooldown not complete scenario
        when(gameTimeMock.getTime()).thenReturn(2000L);

        float lastExecutionTime = 1000L;
        doCallRealMethod().when(gameTimeMock).getTimeSince((long) lastExecutionTime);

        aoeAttackTask.stop();

        assertEquals(-1, aoeAttackTask.getPriority());
    }

    @Test
    void shouldReturnActivePriorityWhenInRangeAndCooldownComplete() {
        // Setup cooldown complete scenario and target in range
        when(gameTimeMock.getTime()).thenReturn(11000L);

        float lastExecutionTime = 1000L;
        Mockito.doCallRealMethod().when(gameTimeMock).getTimeSince((long) lastExecutionTime);

        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(10, 10));
        when(targetMock.getPosition()).thenReturn(new Vector2(12, 12));

        aoeAttackTask.stop();

        assertEquals(-1, aoeAttackTask.getPriority());
    }
}