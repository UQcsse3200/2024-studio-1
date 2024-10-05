package com.csse3200.game.components.player.inventory;

import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


public class BigRedButtonTest {
    private BigRedButton brb;
    private Entity player;
    private Entity enemy;
    @Mock
    private GameController mainGameArea;
    @Mock
    private GameAreaService gameAreaService;
    @Mock
    private EnemyRoom enemyRoom;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        brb = new BigRedButton();
        player = new Entity().addComponent(new CombatStatsComponent(100, 5, true, 0, 0));
        enemy = new Entity().addComponent(new CombatStatsComponent(100, 15));
        gameAreaService = mock(GameAreaService.class);
        mainGameArea = mock(GameController.class);
        enemyRoom = mock(EnemyRoom.class);
    }
    @Test
    public void testGetName() {assertEquals("BigRedButton", brb.getName());}

    @Test
    public void testGetItemSpecification() {assertEquals("BigRedButton", brb.getItemSpecification());}

//    @Test
//    public void testKillAllAnimals() {
//        brb.apply(player);
//        CombatStatsComponent victimStats = enemy.getComponent(CombatStatsComponent.class);
//        assertTrue(enemyRoom.isAllAnimalDead());
//    }

}
