package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
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

        entity = mock(Entity.class);
        playerActions = mock(PlayerActions.class);

        // Mock the entity to return the necessary components
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        when(entity.getComponent(PlayerActions.class)).thenReturn(playerActions);

        // Mock getting the PlayerActions component
        when(entity.getComponent(PlayerActions.class)).thenReturn(playerActions);

        // Mock event handling system
        EventHandler eventHandler = mock(EventHandler.class);
        when(entity.getEvents()).thenReturn(eventHandler);
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
    public void testSpeedBoostWithMaxSpeedLimit() {
        // Setup initial conditions
        Vector2 initialSpeed = new Vector2(3f, 3f);
        float initialSpeedPercentage = 0.75f;  // 75% of max speed
        float maxSpeed = 1.0f;  // Maximum allowed speed percentage

        // Mock the initial state of player actions
        when(playerActions.getCurrSpeed()).thenReturn(initialSpeed);
        when(playerActions.getCurrSpeedPercentage()).thenReturn(initialSpeedPercentage);
        when(playerActions.getMaxSpeed()).thenReturn(maxSpeed);

        // Create an instance of your class
        DivinePotion divinePotion = new DivinePotion();

        // Call the speed method
        divinePotion.speed(entity);

        // Verify that speed is capped at the max allowed speed
        verify(playerActions).setSpeed(new Vector2(6f, 6f)); // Expected capped speed
        verify(playerActions).setSpeedPercentage(maxSpeed); // Speed percentage capped to max

        // Verify that the event to update the UI is triggered with the correct value
        verify(entity.getEvents()).trigger(eq("updateSpeedUI"), eq(maxSpeed));
    }

    @Test
    public void testSpeedBoostBelowMaxSpeed() {
        // Setup initial conditions
        Vector2 initialSpeed = new Vector2(3f, 3f);
        float initialSpeedPercentage = 0.5f;  // 50% of max speed
        float maxSpeed = 1.0f;  // Maximum allowed speed percentage

        // Mock the initial state of player actions
        when(playerActions.getCurrSpeed()).thenReturn(initialSpeed);
        when(playerActions.getCurrSpeedPercentage()).thenReturn(initialSpeedPercentage);
        when(playerActions.getMaxSpeed()).thenReturn(maxSpeed);

        // Create an instance of your class
        DivinePotion divinePotion = new DivinePotion();

        // Call the speed method
        divinePotion.speed(entity);

        // Expected updated speed after applying boost
        Vector2 expectedUpdatedSpeed = new Vector2(3.25f, 3.25f);
        float expectedNewSpeedPercentage = 0.75f; // Speed boosted by 25%

        // Verify the updated speed and percentage
        verify(playerActions).setSpeed(expectedUpdatedSpeed);
        verify(playerActions).setSpeedPercentage(expectedNewSpeedPercentage);

        // Verify that the event to update the UI is triggered with the correct value
        verify(entity.getEvents()).trigger(eq("updateSpeedUI"), eq(expectedNewSpeedPercentage));
    }
}




