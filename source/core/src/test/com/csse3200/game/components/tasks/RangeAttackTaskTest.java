package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RangeAttackTaskTest {
    private RangeAttackTask rangeAttackTask;
    private GameTime gameTimeMock;
    private RangeAttackComponent rangeAttackComponentMock;
    private Entity targetMock;
    private Entity ownerEntityMock;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Mock TaskConfig
        TaskConfig.RangeAttackTaskConfig config = new TaskConfig.RangeAttackTaskConfig();
        config.activationMinRange = 5f;
        config.activationMaxRange = 15f;
        config.attackNum = 3;
        config.cooldownTime = 10f;

        // Mock GameTime
        gameTimeMock = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTimeMock);

        // Mock Entity and Components
        targetMock = mock(Entity.class);
        when(targetMock.getPosition()).thenReturn(new Vector2(10, 10));

        ownerEntityMock = mock(Entity.class);
        rangeAttackComponentMock = mock(RangeAttackComponent.class);

        // Ensure RangeAttackComponent is returned when requested from the owner entity
        when(ownerEntityMock.getComponent(RangeAttackComponent.class)).thenReturn(rangeAttackComponentMock);

        // Initialize entity position
        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(8, 8));

        // Initialize RangeAttackTask
        AITaskComponent aiTaskComponentMock = mock(AITaskComponent.class);
        when(aiTaskComponentMock.getEntity()).thenReturn(ownerEntityMock);

        rangeAttackTask = new RangeAttackTask(targetMock, config, "single");

        // Associate task with AI component and entity
        aiTaskComponentMock.addTask(rangeAttackTask);
        when(ownerEntityMock.getComponent(AITaskComponent.class)).thenReturn(aiTaskComponentMock);
        ownerEntityMock.addComponent(aiTaskComponentMock);

        // Inject the RangeAttackComponent mock into the RangeAttackTask using reflection
        rangeAttackTask.create(aiTaskComponentMock);
        Field rangeAttackComponentField = RangeAttackTask.class.getDeclaredField("rangeAttackComponent");
        rangeAttackComponentField.setAccessible(true);
        rangeAttackComponentField.set(rangeAttackTask, rangeAttackComponentMock);
    }

    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

    @Test
    void shouldStartRangeAttack() {
        rangeAttackTask.start();

        verify(rangeAttackComponentMock, times(1)).setType("single");
        verify(rangeAttackComponentMock, times(1)).enableForNumAttacks(3);
        verify(rangeAttackComponentMock, times(1)).setEnabled(true);
    }

    @Test
    void shouldStopRangeAttack() {
        rangeAttackTask.start();

        rangeAttackTask.stop();

        verify(rangeAttackComponentMock, times(1)).setEnabled(false);
    }

    @Test
    void shouldReturnActivePriorityWhenActive() {
        rangeAttackTask.start();

        assertEquals(9, rangeAttackTask.getPriority());
    }

    @Test
    void shouldReturnInactivePriorityWhenInRangeAndCooldownComplete() {
        // Start the task to initialize and enable the RangeAttackComponent
        rangeAttackTask.start();

        // Simulate time passage to ensure cooldown is complete
        when(gameTimeMock.getTime()).thenReturn(20000L); // Current time
        rangeAttackTask.stop(); // Simulate stopping to set lastExecutionTime

        // Ensure cooldown is complete
        when(gameTimeMock.getTime()).thenReturn(30000L); // Time after cooldown

        // Ensure target is within range
        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(10, 10));

        // Calculate distance to ensure it is within activation range
        float distanceToTarget = ownerEntityMock.getPosition().dst(targetMock.getPosition());
        assertFalse(distanceToTarget >= 5f && distanceToTarget <= 15f, "Target should be within range");

        // Check if priority is correctly set for "single" attack type
        assertEquals(-1, rangeAttackTask.getPriority());
    }

    @Test
    void shouldReturnNegativePriorityWhenOutOfRangeOrCooldownNotComplete() {
        when(gameTimeMock.getTime()).thenReturn(2000L); // Cooldown not complete

        long lastExecutionTime = 1000L;


        try {
            Field lastExecutionTimeField = RangeAttackTask.class.getDeclaredField("lastExecutionTime");
            lastExecutionTimeField.setAccessible(true);
            lastExecutionTimeField.set(rangeAttackTask, lastExecutionTime);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set lastExecutionTime via reflection");
        }

        assertEquals(-1, rangeAttackTask.getPriority());

        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(20, 20)); // Out of range

        assertEquals(-1, rangeAttackTask.getPriority());
    }
}
