package com.csse3200.game.components.npc;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.areas.BossRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BossHealthDialogueComponentTest {

    @Mock
    private Entity mockEntity;
    @Mock
    private CombatStatsComponent mockCombatStats;
    @Mock
    private AlertBoxService mockAlertBoxService;
    @Mock
    private GameAreaService mockGameAreaService;
    @Mock
    private MainGameArea mockMainGameArea;
    @Mock
    private BossRoom mockBossRoom;

    private BossHealthDialogueComponent component;

    @BeforeEach
    void setUp() {
        // Set up mocks
        when(mockEntity.getComponent(CombatStatsComponent.class)).thenReturn(mockCombatStats);
        when(mockCombatStats.getMaxHealth()).thenReturn(100);

        ServiceLocator.registerAlertBoxService(mockAlertBoxService);
        ServiceLocator.registerGameAreaService(mockGameAreaService);
        when(mockGameAreaService.getGameArea()).thenReturn(mockMainGameArea);
        when(mockMainGameArea.getCurrentRoom()).thenReturn(mockBossRoom);

        // Create component
        component = new BossHealthDialogueComponent();
        component.setEntity(mockEntity);
        component.create();
    }

    @Test
    void testDialogueTriggers() {
        // Test 75% health threshold
        when(mockCombatStats.getHealth()).thenReturn(74);
        component.update();
        verify(mockAlertBoxService).confirmDialogBox(eq(mockEntity), eq("You've managed to hurt me. But this battle is far from over!"), any());

        // Test 50% health threshold
        when(mockCombatStats.getHealth()).thenReturn(49);
        component.update();
        verify(mockAlertBoxService).confirmDialogBox(eq(mockEntity), eq("Half of my strength is gone, but my resolve remains unbroken!"), any());
        verify(mockBossRoom).spawnOtherAnimals(mockEntity);

        // Test 25% health threshold
        when(mockCombatStats.getHealth()).thenReturn(24);
        component.update();
        verify(mockAlertBoxService).confirmDialogBox(eq(mockEntity), eq("I'm on my last legs, but I won't go down without a fight!"), any());
    }
}