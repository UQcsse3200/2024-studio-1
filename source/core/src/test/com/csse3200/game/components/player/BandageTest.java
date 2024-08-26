package com.csse3200.game.components.player;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.Bandage;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
//    @Test
//    public void testGetIcon() {
//        // Test getIcon method
//        Texture icon = bandage.getIcon();
//        assertNotNull(icon,"icon should not be null");
//
//        // Ensure the correct path is used
//        assertEquals("images/items/bandage.png", icon.toString());
//    }

    @Test
    public void testApplyIncreasesHealth() {
        // Initialize initial health and expected health
        int initialHealth = 50;
        int expectedHealth = initialHealth + 20;

        // Setup the getHealth method to return initial health
        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);

        // Apply the bandage
        bandage.apply(entity);

        // Verify that addHealth was called with the correct amount
        verify(combatStatsComponent).addHealth(20);
    }
}
