package com.csse3200.game.components.player.inventory;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.inventory.buffs.DamageBuff;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DamageBuffTest {

    private Entity player;

    private Entity enemy;

    private DamageBuff damageBuff;

    @Mock
    private MainGameArea mainGameArea;

    @Mock
    private GameAreaService gameAreaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        player = new Entity().addComponent(new CombatStatsComponent(100, 5, true, 0, 0));
        enemy = new Entity().addComponent(new CombatStatsComponent(100, 15));
        damageBuff = new DamageBuff();

        // Mock GameAreaService and register it using ServiceLocator
        gameAreaService = mock(GameAreaService.class);
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock the behavior of gameAreaService.getGameArea()
        when(gameAreaService.getGameArea()).thenReturn(mainGameArea);

        // Make sure getPlayer() returns the mocked player entity
        when(mainGameArea.getPlayer()).thenReturn(player);
    }

    @Test
    public void testGetName() {assertEquals("Damage Buff", damageBuff.getName());}

    @Test
    public void testGetItemSpecification() {assertEquals("damagebuff", damageBuff.getBuffSpecification());}

    @Test
    public void testGetBuff() {assertEquals(15, damageBuff.getBuff());}

    @Test
    public void testBaseDamageBuff() {assertEquals(0, player.getComponent(CombatStatsComponent.class).getDamageBuff());}

    @Test
    public void testDamageBuff()
    {
        damageBuff.effect(player);
        assertEquals(15, player.getComponent(CombatStatsComponent.class).getDamageBuff());
    }

    @Test
    public void testAdditionalDamage() {
        damageBuff.effect(player);
        CombatStatsComponent playerStats = player.getComponent(CombatStatsComponent.class);
        CombatStatsComponent victimStats = enemy.getComponent(CombatStatsComponent.class);
        victimStats.hit(playerStats);
        assertEquals(80, victimStats.getHealth());
    }
}
