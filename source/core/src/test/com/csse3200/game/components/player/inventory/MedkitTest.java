package com.csse3200.game.components.player.inventory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MedkitTest {
    @Mock
    private Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @InjectMocks
    private MedKit medkit;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the entity to return the CombatStatsComponent
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
    }

    @Test
    public void testGetName() {
        // Check if getName returns "Medkit"
        assertEquals("Medkit", medkit.getName());
    }

    @Test
    public void testGetItemSpecification() {
        assertEquals("medkit", medkit.getItemSpecification());
    }

    @Test
    public void testGetSpecification() {
        assertEquals("item:medkit", medkit.getSpecification());
    }


    @Test
    public void testApplyIncreasesHealth() {
        // Initialize initial health and expected health
        int initialHealth = 50;
        int expectedHealth = 100; // Health should cap at 100

        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);

        medkit.apply(entity);

        verify(combatStatsComponent).setHealth(expectedHealth);
    }

    @Test
    public void testApplyIncreasesHealthFromZero() {
        // Initialize initial health as 0 and expected health after applying Medkit
        int initialHealth = 0;
        int expectedHealth = 100;

        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);

        medkit.apply(entity);

        verify(combatStatsComponent).setHealth(expectedHealth);
    }
}
