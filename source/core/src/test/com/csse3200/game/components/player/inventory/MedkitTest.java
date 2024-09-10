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

//    @Test
//    public void testGetIcon() {
//        // Test getIcon method
//        Texture icon = medkit.getIcon();
//        assertNotNull(icon,"icon should not be null");
//
//        // Ensure the correct path is used
//        assertEquals("images/items/med_kit.png", icon.toString());
//    }

    @Test
    public void testApplyIncreasesHealth() {
        // Initialize initial health and expected health
        int initialHealth = 0;
        int expectedHealth = initialHealth + 100;

        // Setup the getHealth method to return initial health
        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);

        // Apply the Medkit
        medkit.apply(entity);

        // Verify that addHealth was called with the correct amount
        verify(combatStatsComponent).setHealth(expectedHealth);
    }
}
