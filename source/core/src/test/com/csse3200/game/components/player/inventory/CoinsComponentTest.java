package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CoinsComponentTest {

    @Mock
    private Entity player;

    @Mock
    private EventHandler eventHandler;

    private CoinsComponent coinsComponent;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the entity to return the event handler when requested
        when(player.getEvents()).thenReturn(eventHandler);

        InventoryComponent inventoryComponent = new InventoryComponent();
        inventoryComponent.setEntity(player);
        player.addComponent(inventoryComponent);

        // Instantiate the CoinsComponent with the player's inventory
        coinsComponent = new CoinsComponent();
        coinsComponent.setEntity(player);
        player.addComponent(coinsComponent);
        player.addComponent(new CombatStatsComponent(100, 10));

        // Create the player entity to initialize components
        player.create();

    }

    /**
     * Ensures that a new game starts with zero coins
     */
    @Test
    void testInitialCoinCount() {
        assertEquals(0, coinsComponent.getCoins(),
                "Initial coin count should be 0");
    }

    /**
     * Ensures that spending coins reduces the amount of coin player owns
     */
    @Test
    void testSpendCoins() {
        coinsComponent.setCoins(10);
        coinsComponent.spend(4);
        assertEquals(6, coinsComponent.getCoins(),
                "Coin count should be 6 after spending 4 coins");
    }

    /**
     * Ensures after spending all the money, the amount is reduced to 0
     */
    @Test
    void testSpendEverything() {
        coinsComponent.setCoins(1000000000);
        coinsComponent.spend(1000000000);
        assertEquals(0,coinsComponent.getCoins(),
                "Coin should 0 after spedning everything" );
    }


    /**
     * Ensures that calling addCoins() method adds coins to player's coins
     */
    @Test
    void testAddCoins() {
        coinsComponent.addCoins(5);
        assertEquals(5, coinsComponent.getCoins(),
                "Coin count should be 5 after adding 5 coins");
    }


    /**
     * Esnures that negative coins cannot be added
     */
    @Test
    void testAddNegativeCoins() {
        coinsComponent.addCoins(-2);
        assertEquals(0, coinsComponent.getCoins(),
                "Negetive amount cannot be added");
    }

    /**
     * Ensures that call setCoins changes the stored amount of coins
     */
    @Test
    void testSetCoins() {
        coinsComponent.setCoins(10);
        assertEquals(10, coinsComponent.getCoins(),
                "Coin count should be 10 after setting to 10");
    }

    /**
     * Ensures that coins cannot be set to a negative number
     */
    @Test
    void testSetNegativeCoins() {
        coinsComponent.setCoins(-5);
        assertEquals(0, coinsComponent.getCoins(),
                "Coin count should be 0 after setting to -5");
    }

    /**
     * Ensures edge case: after setting the amount to zero, the amount of coins are reduced to zero
     */
    @Test
    void testSet0Coin() {
        coinsComponent.setCoins(0);
        assertEquals(0, coinsComponent.getCoins(),
                "The amount should 0 after setting to 0");
    }

    /**
     * Ensures that hasCoins() returns the accurate amount of coins
     */
    @Test
    void testHasCoins() {
        coinsComponent.setCoins(10);
        assertTrue(coinsComponent.hasCoins(5), "Should have at least 5 coins");
        assertFalse(coinsComponent.hasCoins(20), "Should not have 20 coins");
    }

    /**
     * Ensures that the player is not able to spend more money than it has
     */
    @Test
    void testSpendMoreThanAvailableCoins() {
        coinsComponent.setCoins(5);
        coinsComponent.spend(10);
        assertEquals(5, coinsComponent.getCoins(),
                "Coin count should be 0 after trying to spend more than available");
    }
}
