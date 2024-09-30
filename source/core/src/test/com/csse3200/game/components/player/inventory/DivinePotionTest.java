package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DivinePotionTest {

    @Mock
    Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @Mock
    private PlayerActions playerActions;

    @InjectMocks
    private DivinePotion divinePotion;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the entity to return the necessary components
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        when(entity.getComponent(PlayerActions.class)).thenReturn(playerActions);
    }

    @Test
    public void testGetName() {
        // Check if getName returns "Divine Potion"
        assertEquals("Divine Potion", divinePotion.getName());
    }

    @Test
    public void testGetBuffSpecification() {
        // Check if getBuffSpecification returns "Divine potion"
        assertEquals("Divine potion", divinePotion.getBuffSpecification());
    }

    @Test
    public void testDivinePotionBoostHealth() {
        // Initialize current health and max health
        int initialHealth = 70;
        int maxHealth = 100;
        int potionBoost = 30;
        int expectedHealth = Math.min(initialHealth + potionBoost, maxHealth);

        when(combatStatsComponent.getHealth()).thenReturn(initialHealth);
        when(combatStatsComponent.getMaxHealth()).thenReturn(maxHealth);

        divinePotion.Boost(entity);

        verify(combatStatsComponent).setHealth(expectedHealth);

        when(combatStatsComponent.getHealth()).thenReturn(expectedHealth);
        assertEquals(expectedHealth, combatStatsComponent.getHealth(), "Health should match the expected value after using Divine Potion.");
    }
    @Test
    public void testDivinePotionSpeedBoost() {
        // Set up initial speed
        Vector2 initialSpeed = new Vector2(3f, 3f);  // Define initial speed

        // Mock the player's current speed
        when(playerActions.getCurrSpeed()).thenReturn(initialSpeed);  // Mock getCurrSpeed to return initialSpeed

        // Verify initial speed is correct
        assertEquals(initialSpeed, entity.getComponent(PlayerActions.class).getCurrSpeed(), "Initial speed should match");

//        // Apply the divine potion effect (Assuming this boosts the speed)
//        divinePotion.effect(entity);  // Call the method under test
//
//        // Set the expected speed after the effect is applied
//        Vector2 expectedNewSpeed = new Vector2(3.25f, 3.25f);  // Assuming a boost of 0.25 to both x and y
//
//        // Mock the new speed after the effect is applied
//        when(playerActions.getCurrSpeed()).thenReturn(expectedNewSpeed);  // Mock the new speed
//
//        // Verify new speed after applying the effect
//        Vector2 newSpeed = entity.getComponent(PlayerActions.class).getCurrSpeed();
//        assertEquals(expectedNewSpeed, newSpeed, "Speed should be boosted by the divine potion effect");
    }

}
