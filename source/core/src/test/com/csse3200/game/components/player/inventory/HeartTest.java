package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.buffs.Heart;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HeartTest {

    @Mock
    private Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @InjectMocks
    private Heart heart;

    private static final int INITIAL_MAX_HEALTH = 100;
    private static final int MAX_HEALTH_INCREASE = 50;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the entity to return the CombatStatsComponent
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
    }

    @Test
    public void testHeartIncreasesMaxHealth() {
        // Set initial max health and expected new max health
        int currentMaxHealth = INITIAL_MAX_HEALTH;
        int expectedMaxHealth = INITIAL_MAX_HEALTH + MAX_HEALTH_INCREASE;

        when(combatStatsComponent.getMaxHealth()).thenReturn(currentMaxHealth);

        heart.effect(entity);

        // Verify that max health is set to the new expected value
        verify(combatStatsComponent).setMaxHealth(expectedMaxHealth);
    }

    @Test
    public void testGetName() {
        assertEquals("Heart", heart.getName());
    }

    @Test
    public void testGetBuffSpecification() {
        assertEquals("heart", heart.getBuffSpecification());
    }
}
