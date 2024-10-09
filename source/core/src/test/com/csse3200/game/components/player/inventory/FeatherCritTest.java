package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.buffs.Feather;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.RandomService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class FeatherCritTest {

    private Entity player;
    private CombatStatsComponent combatStats;

    @Mock
    private RandomService randomService;

    @Mock
    private RandomNumberGenerator randomNumberGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        //Initialise player and CombatStatsComponent
        player = new Entity();
        combatStats = new CombatStatsComponent(100, 20, true, 0, 0);
        player.addComponent(combatStats);

        //Mock the random number generator in the ServiceLocator
        ServiceLocator.registerRandomService(randomService);
        when(randomService.getRandomNumberGenerator(CombatStatsComponent.class)).thenReturn(randomNumberGenerator);
    }

    @Test
    public void testApplyCritChance() {
        Feather feather = new Feather();

        assertFalse(combatStats.getCanCrit());
        assertEquals(0.0, combatStats.getCritChance());

        feather.effect(player);

        assertTrue(combatStats.getCanCrit());
        assertEquals(0.2, combatStats.getCritChance());
    }

    @Test
    public void testCriticalHitTriggered() {
        player.getComponent(CombatStatsComponent.class).updateCritAbility();
        player.getComponent(CombatStatsComponent.class).updateCritChance(0.2);

        when(randomNumberGenerator.getRandomDouble(0.0, 1.0)).thenReturn(0.1);

        int baseDamage = 20;
        int expectedDamage = baseDamage * 2;

        int actualDamage = combatStats.applyCrit(baseDamage, combatStats.getCritChance());

        assertEquals(expectedDamage, actualDamage);
    }

    @Test
    public void testNoCriticalHit() {
        player.getComponent(CombatStatsComponent.class).updateCritAbility();
        player.getComponent(CombatStatsComponent.class).updateCritChance(0.2);

        when(randomNumberGenerator.getRandomDouble(0.0, 1.0)).thenReturn(0.3);

        int baseDamage = 20;
        int actualDamage = combatStats.applyCrit(baseDamage, 0.2);

        assertEquals(baseDamage, actualDamage);
    }
}
