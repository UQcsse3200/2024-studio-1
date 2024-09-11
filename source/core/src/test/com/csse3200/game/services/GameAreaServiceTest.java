package com.csse3200.game.areas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameAreaServiceTest {

    private MainGameArea mockGameArea;
    private GameAreaService gameAreaService;

    @BeforeEach
    void setUp() {
        mockGameArea = mock(MainGameArea.class);
        gameAreaService = new GameAreaService(mockGameArea);
    }

    @Test
    void testConstructor() {
        assertNotNull(gameAreaService, "GameAreaService should be created successfully");
    }

    @Test
    void testGetGameArea() {
        MainGameArea result = gameAreaService.getGameArea();
        assertEquals(mockGameArea, result, "getGameArea should return the game area passed in the constructor");
    }

    @Test
    void testUpdate() {
        gameAreaService.update();
        verify(mockGameArea, times(1)).spawnCurrentRoom();
    }
}