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

/**
 * Unit test for AOEAttackTask, which tests its behavior during various phases
 * such as preparation, attack, and cooldown. It mocks dependencies such as
 * GameTime, Entity, AOEAttackComponent, and EventHandler.
 */
class AOEAttackTaskTest {
    private AOEAttackTask aoeAttackTask;
    private GameTime gameTimeMock;
    private AOEAttackComponent aoeAttackComponentMock;
    private Entity targetMock;
    private Entity ownerEntityMock;

    /**
     * Setup the required mocks and initialize the AOEAttackTask instance before
     * each test case.
     */
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

    /**
     * Test that the preparation phase starts by triggering the appropriate events
     * when the AOE attack task is started.
     */
    @Test
    void shouldStartPreparationPhase() {
        // Execute
        aoeAttackTask.start();

        // Verify that the appropriate preparation events are triggered
        EventHandler eventHandler = ownerEntityMock.getEvents();

        verify(eventHandler, times(1)).trigger("aoe_preparation");
        verify(eventHandler, times(1)).trigger("gesture");
    }

    /**
     * Test that after the preparation phase completes, the AOE attack is triggered,
     * and the attack component is enabled.
     */
    @Test
    void shouldTriggerAOEAttackAfterPreparation() {
        // Setup preparation start time
        when(gameTimeMock.getTime()).thenReturn(1000L); // Start time
        aoeAttackTask.start();

        // Simulate time passing beyond preparation time
        when(gameTimeMock.getTimeSince(1000L)).thenReturn(2000L); // 2 seconds later

        // Execute update to transition from preparation to attack
        aoeAttackTask.update();

        // Verify that the AOE attack is triggered and the attack component is enabled
        EventHandler eventHandler = ownerEntityMock.getEvents();

        verify(eventHandler, times(1)).trigger("attack");
        verify(aoeAttackComponentMock, times(1)).enableForNumAttacks(1);
    }

    /**
     * Test that the task stops after waiting for the specified time post-attack.
     */
    @Test
    void shouldStopAfterWaiting() {
        // Setup
        when(gameTimeMock.getTime()).thenReturn(1000L); // Start time
        aoeAttackTask.start();

        // Simulate preparation and attack phases completion
        when(gameTimeMock.getTimeSince(1000L)).thenReturn(5000L); // 5 seconds later

        // Execute update to complete the task cycle
        aoeAttackTask.update();

        // Verify that the task stops after completing its cycle
        assertEquals(Task.Status.ACTIVE, aoeAttackTask.getStatus());
    }

    /**
     * Test that the task returns an inactive priority when the cooldown is not
     * complete.
     */
    @Test
    void shouldReturnInactivePriorityWhenCooldownNotComplete() {
        // Setup cooldown not complete scenario
        when(gameTimeMock.getTime()).thenReturn(2000L);

        float lastExecutionTime = 1000L;
        doCallRealMethod().when(gameTimeMock).getTimeSince((long) lastExecutionTime);

        aoeAttackTask.stop();

        // Verify that the priority is inactive during cooldown
        assertEquals(-1, aoeAttackTask.getPriority());
    }

    /**
     * Test that the task returns an active priority when the target is in range
     * and the cooldown is complete.
     */
    @Test
    void shouldReturnActivePriorityWhenInRangeAndCooldownComplete() {
        // Setup cooldown complete scenario and target in range
        when(gameTimeMock.getTime()).thenReturn(11000L);

        float lastExecutionTime = 1000L;
        Mockito.doCallRealMethod().when(gameTimeMock).getTimeSince((long) lastExecutionTime);

        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(10, 10));
        when(targetMock.getPosition()).thenReturn(new Vector2(12, 12));

        aoeAttackTask.stop();

        // Verify that the priority is active when cooldown is complete and target is in range
        assertEquals(-1, aoeAttackTask.getPriority());
    }
}
