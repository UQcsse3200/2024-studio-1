package com.csse3200.game.areas.Levels;

import com.csse3200.game.areas.*;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.components.player.inventory.ItemPickupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.RandomService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(GameExtension.class)
class MainGameLevelFactoryTest {

    private LevelFactory levelFactory;
    @TempDir
    Path tempDir;

    @Mock
    private RenderService mockRenderService;

    @Mock
    private RandomService mockRandomService;

    @Mock
    private RandomNumberGenerator mockRNG;

    @Mock
    private GameAreaService mockGameAreaService;

    @Mock
    private CameraComponent mockCamera;

    @Mock
    private GameController mockGameController;

    @Mock
    Entity entity;
    @Mock
    private PlayerConfig playerConfig;
    @Mock
    private PlayerConfigComponent playerConfigComponent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        playerConfig = mock(PlayerConfig.class);
        entity = mock(Entity.class);
        playerConfigComponent = mock(PlayerConfigComponent.class);

        // Set up mock RenderService
        when(mockRenderService.getCamera()).thenReturn(mockCamera);
        ServiceLocator.registerRenderService(mockRenderService);

        // Mock the RandomService and RandomNumberGenerator
        when(mockRandomService.getRandomNumberGenerator(any(Class.class))).thenReturn(mockRNG);
        ServiceLocator.registerRandomService(mockRandomService);

        ServiceLocator.registerGameAreaService(mockGameAreaService);
        when(mockGameAreaService.getGameController()).thenReturn(mockGameController);
        playerConfig.name = "bear";
        when(mockGameController.getPlayer()).thenReturn(entity);
        when(entity.getComponent(PlayerConfigComponent.class)).thenReturn(playerConfigComponent);
        when(playerConfigComponent.getPlayerConfig()).thenReturn(playerConfig);


        // Mock behavior for RandomNumberGenerator if needed
        when(mockRNG.getRandomInt(anyInt(), anyInt())).thenReturn(0); // Adjust return value as necessary

        // Set up ResourceService (or mock it if necessary)
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRandomService(new RandomService(""));

        levelFactory = new MainGameLevelFactory(false, null);
    }

    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

   @Test
   void testCreate() {
       Level level = levelFactory.create(1);
       assertNotNull(level);
       assertEquals(1, level.getLevelNumber());
       assertNotNull(level.getMap());
       assertNotNull(level.getStartingRoomKey());
   }

    @Test
    void testCorrectType() {
        Level level = levelFactory.create(1);
        assertInstanceOf(MainGameLevel.class, level);
    }

    @Test
    void testCreateMultipleLevels() {
        Level level1 = levelFactory.create(1);
        Level level2 = levelFactory.create(2);
        Level level3 = levelFactory.create(3);

        assertNotNull(level1);
        assertNotNull(level2);
        assertNotNull(level3);

        assertEquals(1, level1.getLevelNumber());
        assertEquals(2, level2.getLevelNumber());
        assertEquals(3, level3.getLevelNumber());

        // Check that the levels are indeed different
        assertNotSame(level1, level2);
        assertNotSame(level2, level3);
        assertNotSame(level1, level3);

        // Check that the maps are different
        assertNotSame(level1.getMap(), level2.getMap());
        assertNotSame(level2.getMap(), level3.getMap());
        assertNotSame(level1.getMap(), level3.getMap());

        // Get room layouts
        Set<String> level1Rooms = new HashSet<>(level1.getMap().mapData.getPositions().keySet());
        Set<String> level2Rooms = new HashSet<>(level2.getMap().mapData.getPositions().keySet());
        Set<String> level3Rooms = new HashSet<>(level3.getMap().mapData.getPositions().keySet());

        // Print room layouts for debugging
        System.out.println("Level 1 rooms: " + level1Rooms);
        System.out.println("Level 2 rooms: " + level2Rooms);
        System.out.println("Level 3 rooms: " + level3Rooms);

        // Check if all layouts are identical
        boolean allIdentical = level1Rooms.equals(level2Rooms) && level2Rooms.equals(level3Rooms);

        // Detailed assertion message
        String message = String.format(
                "Levels should have different layouts.%nLevel 1: %s%nLevel 2: %s%nLevel 3: %s",
                level1Rooms, level2Rooms, level3Rooms
        );

        assertFalse(allIdentical, message);

        // Check that the number of rooms is as expected
        assertTrue(level1Rooms.size() > 1, "Level 1 should have multiple rooms");
        assertTrue(level2Rooms.size() > 1, "Level 2 should have multiple rooms");
        assertTrue(level3Rooms.size() > 1, "Level 3 should have multiple rooms");
    }
}