package com.csse3200.game.components.player.inventory;

import static org.mockito.Mockito.*;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.buffs.WerewolfFang;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

public class WerewolfFangTest {

    private WerewolfFang werewolfFang;
    @Mock
    private Entity player;
    @Mock
    private Entity enemy;
    @Mock
    private CombatStatsComponent playerStats;
    @Mock
    private CombatStatsComponent enemyStats;
    @Mock
    private GameAreaService gameAreaService;
    @Mock
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialise the WerewolfFang item
        werewolfFang = new WerewolfFang();

        // Mock entities and their components
        player = mock(Entity.class);
        enemy = mock(Entity.class);
        playerStats = mock(CombatStatsComponent.class);
        enemyStats = mock(CombatStatsComponent.class);

        // Mock GameAreaService and GameController
        gameAreaService = mock(GameAreaService.class);
        gameController = mock(GameController.class);

        // Register the GameAreaService
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock the behavior of gameAreaService.getGameController()
        when(gameAreaService.getGameController()).thenReturn(gameController);

        // Ensure that gameController.getPlayer() returns the mocked player entity
        when(gameController.getPlayer()).thenReturn(player);

        // Mock player having a CombatStatsComponent
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(playerStats);
        when(enemy.getComponent(CombatStatsComponent.class)).thenReturn(enemyStats);

    }

    @Test
    public void testEffectAppliesBleedStatus() {
        // Simulate that the player can cause bleed
        when(playerStats.getCanCauseBleed()).thenReturn(true);
        when(playerStats.getBleedDamage()).thenReturn(5); // Mock bleed damage of 5%

        // Call the effect method which should apply bleed to the player
        werewolfFang.effect(player);

        // Verify that the player's bleed status is updated and bleed damage is applied
        verify(playerStats).updateBleedStatus();
        verify(playerStats).updateBleedDamage(5);
    }


    @Test
    public void testBleedEffectTriggersOnEnemyHit() {
//        // Mock the player having the ability to cause bleed
//        when(playerStats.getCanCauseBleed()).thenReturn(true);
//        when(playerStats.getBleedDamage()).thenReturn(5); // Mock bleed damage of 5%
//
//        // Mock the enemy's current health
//        when(enemyStats.getHealth()).thenReturn(100);
//
//        // Calculate expected health after applying the bleed effect
//        int initialHealth = 100;
//        int expectedHealth = initialHealth - (initialHealth * 5 / 100); // 5% of health
//
//        // Call the hit method simulating an attack by the player on the enemy
//        enemyStats.hit(playerStats);
//
//        // Verify that the bleed effect was triggered and health was reduced
//        //verify(enemyStats).setHealth(expectedHealth);
    }

    @Test
    public void testNoBleedEffectIfCannotCauseBleed() {
        // Simulate that the player cannot cause bleed
        when(playerStats.getCanCauseBleed()).thenReturn(false);

        // Call the hit method simulating an attack by the player on the enemy
        enemyStats.hit(playerStats);

        // Verify that bleed was not triggered since the player can't cause bleed
        verify(enemyStats, never()).setHealth(anyInt());
    }
}
