package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.attack.BossRangeAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BulletStormTaskTest {
    private BulletStormTask bulletStormTask;
    private GameTime gameTimeMock;
    private CombatStatsComponent combatStatsMock;
    private BossRangeAttackComponent bossRangeAttackMock;

    @BeforeEach
    void setUp() {
        // Mock TaskConfig
        TaskConfig.BulletStormTaskConfig config = new TaskConfig.BulletStormTaskConfig();
        config.duration = 30f;

        // Mock GameTime
        gameTimeMock = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTimeMock);

        // Mock Entity and Components
        Entity ownerEntityMock = mock(Entity.class);
        combatStatsMock = mock(CombatStatsComponent.class);
        bossRangeAttackMock = mock(BossRangeAttackComponent.class);
        PhysicsMovementComponent physicsComponentMock = mock(PhysicsMovementComponent.class);

        when(ownerEntityMock.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsMock);
        when(ownerEntityMock.getComponent(BossRangeAttackComponent.class)).thenReturn(bossRangeAttackMock);
        when(ownerEntityMock.getComponent(PhysicsMovementComponent.class)).thenReturn(physicsComponentMock);
        when(ownerEntityMock.getPosition()).thenReturn(new Vector2(0, 0));

        // Initialize BulletStormTask
        AITaskComponent aiTaskComponentMock = mock(AITaskComponent.class);
        when(aiTaskComponentMock.getEntity()).thenReturn(ownerEntityMock);
        bulletStormTask = new BulletStormTask(config);
        aiTaskComponentMock.addTask(bulletStormTask);
        ownerEntityMock.addComponent(aiTaskComponentMock);
        bulletStormTask.create(aiTaskComponentMock);
    }

    @Test
    void shouldReturnNegativePriorityWhenHealthNotBelow50() {
        // Setup
        when(combatStatsMock.getHealth()).thenReturn(60);
        when(combatStatsMock.getMaxHealth()).thenReturn(100);

        // Execute
        int priority = bulletStormTask.getPriority();

        // Verify
        assertEquals(-1, priority);
    }

    @Test
    void shouldReturnTaskPriorityWhenHealthBelow50() {
        // Setup
        when(combatStatsMock.getHealth()).thenReturn(45);
        when(combatStatsMock.getMaxHealth()).thenReturn(100);

        // Execute
        int priority = bulletStormTask.getPriority();

        // Verify
        assertEquals(15, priority);
    }

    @Test
    void shouldMakeBossInvincibleOnStart() {
        // Execute
        bulletStormTask.start();

        // Verify
        verify(combatStatsMock, times(1)).makeInvincible(30f);
    }

    @Test
    void shouldEnableBossRangeAttackOnStart() {
        // Execute
        bulletStormTask.start();

        // Verify
        verify(bossRangeAttackMock, times(1)).setEnabled(true);
    }

    @Test
    void shouldStopTaskAfterDuration() {
        // Setup
        when(gameTimeMock.getTime()).thenReturn(1000L); // Start time
        bulletStormTask.start();
        when(gameTimeMock.getTime()).thenReturn(1000L + 31000L); // 31 seconds later

        // Execute
        bulletStormTask.update();

        // Verify
        verify(bossRangeAttackMock, times(1)).setEnabled(false);
        assertTrue(bulletStormTask.hasRun());
    }

    @Test
    void shouldDisableBossRangeAttackOnStop() {
        // Execute
        bulletStormTask.stop();

        // Verify
        verify(bossRangeAttackMock, times(1)).setEnabled(false);
        assertTrue(bulletStormTask.hasRun());
    }

    @Test
    void shouldReturnNegativePriorityAfterStop() {
        // Setup
        when(combatStatsMock.getHealth()).thenReturn(45);
        when(combatStatsMock.getMaxHealth()).thenReturn(100);
        bulletStormTask.start();
        bulletStormTask.stop();

        // Execute
        int priority = bulletStormTask.getPriority();

        // Verify
        assertEquals(-1, priority);
    }

    @Test
    void shouldNotRunAgainAfterStop() {
        // Setup
        when(combatStatsMock.getHealth()).thenReturn(45);
        when(combatStatsMock.getMaxHealth()).thenReturn(100);
        bulletStormTask.start();
        bulletStormTask.stop();

        // Reset interactions
        reset(combatStatsMock, bossRangeAttackMock);

        // Execute
        bulletStormTask.start();

        // Verify that methods are not called again
        verify(combatStatsMock, never()).makeInvincible(anyFloat());
        verify(bossRangeAttackMock, never()).setEnabled(anyBoolean());
    }
}