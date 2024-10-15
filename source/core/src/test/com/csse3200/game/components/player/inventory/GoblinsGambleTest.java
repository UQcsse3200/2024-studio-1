package com.csse3200.game.components.player.inventory;

import static org.mockito.Mockito.*;

import com.csse3200.game.components.player.inventory.buffs.GoblinsGamble;


import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.RandomService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

public class GoblinsGambleTest {

    private GoblinsGamble goblinsGamble;
    private Entity entity;
    private CoinsComponent coinsComponent;
    @Mock
    private RandomService randomService;
    @Mock
    private RandomNumberGenerator randomNumberGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize the GoblinsGamble object
        goblinsGamble = new GoblinsGamble();

        // Create mocks for Entity, CoinsComponent, and RandomService
        entity = Mockito.mock(Entity.class);
        coinsComponent = Mockito.mock(CoinsComponent.class);
        randomService = Mockito.mock(RandomService.class);
        randomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);

        // Register the random service mock
        ServiceLocator.registerRandomService(randomService);

        // Mock the entity having a CoinsComponent
        when(entity.getComponent(CoinsComponent.class)).thenReturn(coinsComponent);

        // Mock the random number generator being returned from the random service
        when(randomService.getRandomNumberGenerator(GoblinsGamble.class)).thenReturn(randomNumberGenerator);
    }

    @Test
    public void testGambleWins() {
        // Set up the random outcome for a win (40% chance, i.e., roll <= 0.4)
        when(randomNumberGenerator.getRandomDouble(0.0, 1.0)).thenReturn(0.3);

        // Simulate a gamble with 10 coins
        when(coinsComponent.getCoins()).thenReturn(10);

        goblinsGamble.gamblePlayersCoins(entity);

        // Verify that 8 coins were added to the player's total
        verify(coinsComponent).addCoins(8);
    }

    @Test
    public void testGambleLoses() {
        // Set up the random outcome for a loss (60% chance, i.e., roll > 0.4)
        when(randomNumberGenerator.getRandomDouble(0.0, 1.0)).thenReturn(0.6);

        // Simulate a gamble with 10 coins
        when(coinsComponent.getCoins()).thenReturn(10);

        goblinsGamble.gamblePlayersCoins(entity);

        // Verify that 5 coins were spent
        verify(coinsComponent).spend(5);
    }

    @Test
    public void testGambleLosesOnAvailableCoins() {
        // Set up the random outcome for a loss
        when(randomNumberGenerator.getRandomDouble(0.0, 1.0)).thenReturn(0.6);

        // Simulate a gamble with 3 coins (less than LOSE_AMOUNT)
        when(coinsComponent.getCoins()).thenReturn(2);

        goblinsGamble.gamblePlayersCoins(entity);

        // Verify that the exact number of available coins (3) was spent
        verify(coinsComponent).spend(2);
    }
}
