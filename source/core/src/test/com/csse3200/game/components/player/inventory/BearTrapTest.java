package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.components.player.inventory.usables.BearTrap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DeployableItemFactory;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BearTrapTest {

    @Mock
    private Entity entity;

    @Mock
    private DeployableItemFactory deployableItemFactory;

    @Mock
    private GameAreaService gameAreaService;

    @Mock
    private Entity bearTrap;

    @InjectMocks
    private BearTrap bearTrapItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up the ServiceLocator for the game area
        ServiceLocator.registerGameAreaService(gameAreaService);

        // Mock the factory to return a bear trap entity when called
        when(deployableItemFactory.createBearTrap()).thenReturn(bearTrap);

        // Set up a mock position for the entity (where the trap will be placed)
        when(entity.getPosition()).thenReturn(new Vector2(5, 5));
    }

//    @Test
//    public void testApplySpawnsBearTrap() {
//        // Apply the bear trap
//        bearTrapItem.apply(entity);
//
//        // Convert the entity's position (Vector2) to a GridPoint2
//        GridPoint2 expectedPosition = new GridPoint2(5, 5);
//
//        // Verify that the bear trap was created and spawned at the entity's position
//        verify(gameAreaService.getGameArea()).spawnEntityAt(eq(bearTrap), eq(expectedPosition), eq(true), eq(true));
//    }

    @Test
    public void testGetName() {
        BearTrap bearTrapItem = new BearTrap();
        assertEquals("Bear Trap", bearTrapItem.getName());
    }

    @Test
    public void testGetItemSpecification() {
        BearTrap bearTrapItem = new BearTrap();
        assertEquals("beartrap", bearTrapItem.getItemSpecification());
    }
}
