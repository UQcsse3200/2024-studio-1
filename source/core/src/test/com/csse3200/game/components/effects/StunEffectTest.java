package com.csse3200.game.components.effects;

import com.csse3200.game.components.player.PlayerActions;
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
 * Unit tests for the StunEffect class.
 */
@ExtendWith(MockitoExtension.class)
public class StunEffectTest {
    @Mock
    private Entity target;
    @Mock
    private PlayerActions playerActions;
    @Mock
    private EventHandler events;
    private StunEffect stunEffect;

    @BeforeEach
    void setUp() {
        when(target.getComponent(PlayerActions.class)).thenReturn(playerActions);
        when(target.getEvents()).thenReturn(events);

        // Create and apply stun effect
        stunEffect = new StunEffect(5f); // 5 seconds duration
        stunEffect.apply(target);
    }

    @Test
    void testApply_ShouldTriggerStunnedEventAndDisablePlayerActions() {
        // Verify
        verify(target.getEvents(), times(1)).trigger("stunned");
        verify(playerActions, times(1)).setEnabled(false);
        verifyNoMoreInteractions(events, playerActions);
    }

    @Test
    void testUpdate_ShouldNotExpireBeforeDuration() {
        // Simulate 3 seconds passing
        stunEffect.update(target, 3f);

        // Assert
        assertFalse(stunEffect.isExpired());
        verify(playerActions, never()).setEnabled(true);
    }

    @Test
    void testUpdate_ShouldExpireAfterDuration() {
        // Simulate 5 seconds passing
        for (int i = 0; i < 5; i++) {
            stunEffect.update(target, 1f);
        }

        // Assert
        assertTrue(stunEffect.isExpired());
        verify(target.getEvents(), times(1)).trigger("stunExpired");
        verify(playerActions, times(1)).setEnabled(true);
    }

    @Test
    void testRefresh_ShouldExtendDuration() {
        // Simulate 3 seconds passing
        stunEffect.update(target, 3f); // 2 seconds left

        // Refresh with a new StunEffect of duration 4 seconds
        StunEffect newStun = new StunEffect(4f);
        stunEffect.refresh(newStun);

        // Simulate 3 second passing
        stunEffect.update(target, 3f);

        // Assert that the duration is not finished yet
        assertFalse(stunEffect.isExpired());

        // Simulate final second passing
        stunEffect.update(target, 1f);

        // Assert
        assertTrue(stunEffect.isExpired());
        verify(target.getEvents(), times(1)).trigger("stunExpired");
        verify(playerActions, times(1)).setEnabled(true);
    }

    @Test
    void testRemove_ShouldTriggerStunExpiredEventAndEnablePlayerActions() {
        // Remove the stun effect
        stunEffect.remove(target);

        // Verify
        verify(target.getEvents(), times(1)).trigger("stunExpired");
        verify(playerActions, times(1)).setEnabled(true);
    }

    @Test
    void testApply_WithZeroDuration_ShouldExpireImmediately() {
        // Setup
        StunEffect zeroDurationStun = new StunEffect(0f);
        stunEffect.remove(target);

        // Apply the zero duration stun effect
        zeroDurationStun.apply(target);
        zeroDurationStun.update(target, 0f);

        // Assert
        assertTrue(zeroDurationStun.isExpired());
        verify(events, times(2)).trigger("stunned");
        verify(playerActions, times(2)).setEnabled(false);
        verify(playerActions, times(2)).setEnabled(true);
    }
}