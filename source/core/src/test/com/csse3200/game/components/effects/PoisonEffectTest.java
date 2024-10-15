package com.csse3200.game.components.effects;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the PoisonEffect class.
 */
@ExtendWith(MockitoExtension.class)
public class PoisonEffectTest {
    @Mock
    private Entity target;
    @Mock
    private CombatStatsComponent combatStats;
    @Mock
    private EventHandler events;
    private PoisonEffect poisonEffect;

    @BeforeEach
    void setUp() {
        when(target.getEvents()).thenReturn(events);

        // Create and apply poison effect
        poisonEffect = new PoisonEffect(5, 10f);
        poisonEffect.apply(target);
    }

    @Test
    void testApply_ShouldTriggerPoisonedEvent() {
        // Verify
        verify(target.getEvents(), times(1)).trigger("poisoned");
    }

    @Test
    void testUpdate_ShouldApplyDamageEachSecond() {
        // Setup
        when(target.getComponent(CombatStatsComponent.class)).thenReturn(combatStats);

        // Simulate 3.5 seconds passing
        float[] deltaTimes = {1f, 1f, 1f, 0.5f};
        for (float deltaTime : deltaTimes) {
            poisonEffect.update(target, deltaTime);
        }

        // Verify
        verify(combatStats, times(3)).addHealth(-5); // applied 3 times
        assertFalse(poisonEffect.isExpired()); // not expired yet
    }

    @Test
    void testUpdate_ShouldExpireAfterDuration() {
        // Simulate 10 seconds passing
        for (int i = 0; i < 10; i++) {
            poisonEffect.update(target, 1f);
        }

        // Assert
        assertTrue(poisonEffect.isExpired());
    }

    @Test
    void testRefresh_ShouldExtendDuration() {
        // Simulate 5 seconds passing
        poisonEffect.update(target, 5f);

        // Refresh with a new PoisonEffect of duration 8 seconds
        PoisonEffect newPoison = new PoisonEffect(5, 8f);
        poisonEffect.refresh(newPoison);

        // Assert
        // Remaining duration should be max(5, 8) = 8 seconds
        for (int i = 0; i < 8; i++) {
            assertFalse(poisonEffect.isExpired());
            poisonEffect.update(target, 1f);
        }
        assertTrue(poisonEffect.isExpired());
    }

    @Test
    void testApply_WithZeroDuration_ShouldExpireImmediately() {
        // Setup
        PoisonEffect zeroDurationPoison = new PoisonEffect(5, 0f);
        poisonEffect.remove(target);

        // Execute
        zeroDurationPoison.apply(target);
        zeroDurationPoison.update(target, 0f);

        // Assert
        assertTrue(zeroDurationPoison.isExpired());
        verify(combatStats, never()).addHealth(-5); // No damage applied
        verify(events, times(2)).trigger("poisoned");
        verify(events, times(2)).trigger("poisonExpired");
    }

    @Test
    void testRemove_ShouldTriggerPoisonExpiredEvent() {
        // Remove effect
        poisonEffect.remove(target);

        // Verify
        verify(target.getEvents(), times(1)).trigger("poisonExpired");
    }
}