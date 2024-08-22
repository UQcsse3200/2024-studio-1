package com.csse3200.game.components.npc;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class NPCDamageHandlerComponentTest {
    private NPCDamageHandlerComponent damageHandler;
    @Mock private CombatStatsComponent combatStats;
    @Mock private EventHandler eventHandler;
    @Mock private Entity entity;

    @BeforeEach
    void setUp() {
        damageHandler = new NPCDamageHandlerComponent();
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStats);
        when(entity.getEvents()).thenReturn(eventHandler);
        when(entity.getId()).thenReturn(1);
        damageHandler.setEntity(entity);
    }

    @Test
    void shouldHandleDamage() {
        when(combatStats.getHealth()).thenReturn(50);
        damageHandler.create();
        damageHandler.onTakeDamage(10);

        verify(combatStats).takeDamage(10);
    }

    @Test
    void shouldTriggerDeathEvent() {
        when(combatStats.getHealth()).thenReturn(0);
        damageHandler.create();
        damageHandler.onTakeDamage(100);

        verify(eventHandler).trigger("death");
        assertTrue(NPCDamageHandlerComponent.deadAnimals.contains(1));
    }

    @Test
    void shouldNotTriggerDeathEventTwice() {
        when(combatStats.getHealth()).thenReturn(0);
        damageHandler.create();
        damageHandler.onTakeDamage(100);
        damageHandler.onTakeDamage(100);

        verify(eventHandler, times(1)).trigger("death");
    }
}