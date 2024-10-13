package com.csse3200.game.components.npc;

import static org.mockito.Mockito.*;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.areas.BossRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BossHealthDialogueComponentTest {

    private BossHealthDialogueComponent component;
    private Entity mockEntity;
    private CombatStatsComponent mockCombatStats;
    private AlertBoxService mockAlertBoxService;

    @BeforeEach
    void setUp() {
        component = new BossHealthDialogueComponent();
        mockEntity = mock(Entity.class);
        mockCombatStats = mock(CombatStatsComponent.class);
        mockAlertBoxService = mock(AlertBoxService.class);
        GameAreaService mockGameAreaService = mock(GameAreaService.class);
        GameController mockMainGameArea = mock(GameController.class);
        BossRoom mockBossRoom = mock(BossRoom.class);

        when(mockEntity.getComponent(CombatStatsComponent.class)).thenReturn(mockCombatStats);
        when(mockCombatStats.getMaxHealth()).thenReturn(100);

        when(mockGameAreaService.getGameController()).thenReturn(mockMainGameArea);
        when(mockMainGameArea.getCurrentRoom()).thenReturn(mockBossRoom);

        ServiceLocator.registerAlertBoxService(mockAlertBoxService);
        ServiceLocator.registerGameAreaService(mockGameAreaService);

        component.setEntity(mockEntity);
        component.create();
    }

    @Test
    void testDialogueTriggeredAt75PercentHealth() {
        when(mockCombatStats.getHealth()).thenReturn(74);  // Just below 75% threshold
        component.update();

        verify(mockAlertBoxService, times(1)).confirmDialogBox(
                eq(mockEntity),
                anyString(),
                any(AlertBoxService.ConfirmationListener.class)
        );
    }

    @Test
    void testNoDialogueTriggeredAbove75PercentHealth() {
        when(mockCombatStats.getHealth()).thenReturn(76);  // Just above 75% threshold
        component.update();

        verify(mockAlertBoxService, never()).confirmDialogBox(
                any(Entity.class),
                anyString(),
                any(AlertBoxService.ConfirmationListener.class)
        );
    }

    @Test
    void testDialogueTriggeredAt50PercentHealth() {
        when(mockCombatStats.getHealth()).thenReturn(49);  // Just below 50% threshold
        component.update();

        verify(mockAlertBoxService, times(1)).confirmDialogBox(
                eq(mockEntity),
                anyString(),
                any(AlertBoxService.ConfirmationListener.class)
        );
    }

    @Test
    void testDialogueTriggeredAt25PercentHealth() {
        when(mockCombatStats.getHealth()).thenReturn(24);  // Just below 25% threshold
        component.update();

        verify(mockAlertBoxService, times(1)).confirmDialogBox(
                eq(mockEntity),
                anyString(),
                any(AlertBoxService.ConfirmationListener.class)
        );
    }
}
