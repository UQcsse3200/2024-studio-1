package com.csse3200.game.components.player.inventory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BandageTest {
    @Mock
    private Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @InjectMocks
    private Bandage bandage;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the entity to return the CombatStatsComponent when requested
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
    }

    @Test
    public void testGetName() {
        // Check if getName returns "Bandage"
        assertEquals("Bandage", bandage.getName());
    }

    @Test
    public void testGetItemSpecification() {
        assertEquals("bandage", bandage.getItemSpecification());
    }

    @Test
    public void testGetSpecification() {
        assertEquals("item:bandage", bandage.getSpecification());
    }

    @Test
    public void testApplyIncreasesHealth() {
        // Initialize initial health and expected health after applying Bandage
        int initialHealth = 140;
        int maxHealth = 150; // Assuming max health is 150
        int expectedHealth = Math.min(initialHealth + 20, maxHealth); //health should cap at 150

        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);
        when(combatStatsComponent.getMaxHealth()).thenReturn(maxHealth);

        bandage.apply(entity);

        verify(combatStatsComponent).setHealth(expectedHealth);
    }

    @Test
    public void testApplyIncreasesHealthFromZero() {
        // Initialize initial health as 0 and expected health after applying Bandage
        int initialHealth = 0;
        int maxHealth = 100; // Assuming max health is 100
        int expectedHealth = Math.min(initialHealth + 20, maxHealth);

        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);
        when(combatStatsComponent.getMaxHealth()).thenReturn(maxHealth);

        bandage.apply(entity);

        verify(combatStatsComponent).setHealth(expectedHealth);
    }
}
