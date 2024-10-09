package com.csse3200.game.areas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.GameController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameAreaServiceTest {

    private GameController mockGameArea;
    private GameAreaService gameAreaService;

    @BeforeEach
    void setUp() {
        mockGameArea = mock(GameController.class);
        gameAreaService = new GameAreaService(mockGameArea);
    }

    @Test
    void testConstructor() {
        assertNotNull(gameAreaService, "GameAreaService should be created successfully");
    }

    @Test
    void testGetGameArea() {
        GameController result = gameAreaService.getGameController();
        assertEquals(mockGameArea, result, "getGameArea should return the game area passed in the constructor");
    }

    @Test
    void testUpdate() {
        gameAreaService.update();
        verify(mockGameArea, times(1)).spawnCurrentRoom();
    }
}