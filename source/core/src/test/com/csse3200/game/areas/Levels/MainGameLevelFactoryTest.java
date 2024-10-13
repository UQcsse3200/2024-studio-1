package com.csse3200.game.areas.Levels;

import com.csse3200.game.areas.Level;
import com.csse3200.game.areas.LevelFactory;
import com.csse3200.game.areas.MainGameLevelFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.options.GameOptions.Difficulty;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class MainGameLevelFactoryTest {

    private LevelFactory levelFactory;
    @TempDir
    Path tempDir;

    @Mock
    private RenderService mockRenderService;
    @Mock
    private CameraComponent mockCamera;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock RenderService
        when(mockRenderService.getCamera()).thenReturn(mockCamera);
        ServiceLocator.registerRenderService(mockRenderService);

        // Set up ResourceService (or mock it if necessary)
        ServiceLocator.registerResourceService(new ResourceService());

        levelFactory = new MainGameLevelFactory(false, new MapLoadConfig());
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

    @Test
    void harderDifficultyGivesBiggerLevel() {
        LevelFactory easyFactory = new MainGameLevelFactory(
                false, new MapLoadConfig().setSizeFromDifficulty(Difficulty.EASY));
        LevelFactory mediumFactory = new MainGameLevelFactory(
                false, new MapLoadConfig().setSizeFromDifficulty(Difficulty.MEDIUM));
        LevelFactory hardFactory = new MainGameLevelFactory(
                false, new MapLoadConfig().setSizeFromDifficulty(Difficulty.HARD));
        assertTrue(easyFactory.create(0).getMap().getMapSize()
                < mediumFactory.create(0).getMap().getMapSize(),
                "Easy level should be smaller than medium");
        assertTrue(mediumFactory.create(0).getMap().getMapSize()
                        < hardFactory.create(0).getMap().getMapSize(),
                "Medium level should be smaller than hard");
    }
}