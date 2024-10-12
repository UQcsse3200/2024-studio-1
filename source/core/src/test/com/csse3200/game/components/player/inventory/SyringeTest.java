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

public class SyringeTest {

    @Mock
    private Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @InjectMocks
    private Syringe syringe;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the entity to return the CombatStatsComponent
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
    }

    @Test
    public void testGetName() {
        // Check if getName returns "Syringe"
        assertEquals("Syringe", syringe.getName());
    }

    @Test
    public void testGetBuffSpecification() {
        // Check if getBuffSpecification returns "Syringe"
        assertEquals("syringe", syringe.getBuffSpecification());
    }


    @Test
    public void testSyringeIncreaseHealth() {
        // Initialize current health and max health
        int initialHealth = 80;
        int syringeBoost = 50;
        int expectedHealth = initialHealth+syringeBoost; // Assuming 100 is the max health

        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);

        syringe.effect(entity);

        verify(combatStatsComponent).addHealth(syringeBoost);

        when(combatStatsComponent.getHealth()).thenReturn(expectedHealth);

        int finalHealth = combatStatsComponent.getHealth();
        assertEquals(expectedHealth, finalHealth, "Health should match the expected value after using the Syringe.");
    }
}
