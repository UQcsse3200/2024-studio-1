package com.csse3200.game.areas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ForestGameAreaTest {

    private ForestGameArea forestGameArea;
    private TerrainFactory mockTerrainFactory;
    private ResourceService mockResourceService;
    private Music mockMusic;

    @BeforeEach
    void setUp() {
        mockTerrainFactory = mock(TerrainFactory.class);
        mockResourceService = mock(ResourceService.class);
        mockMusic = mock(Music.class);

        ServiceLocator.registerResourceService(mockResourceService);

        when(mockResourceService.getAsset(anyString(), eq(Music.class))).thenReturn(mockMusic);

        forestGameArea = new ForestGameArea(mockTerrainFactory);
    }

    @Test
    void testCreate() {
        TerrainComponent mockTerrain = mock(TerrainComponent.class);
        when(mockTerrainFactory.createTerrain(any())).thenReturn(mockTerrain);
        when(mockTerrain.getMapBounds(0)).thenReturn(new GridPoint2(10, 10));

        forestGameArea.create();

        verify(mockResourceService).loadTextures(any(String[].class));
        verify(mockResourceService).loadSounds(any(String[].class));
        verify(mockResourceService).loadMusic(any(String[].class));
        verify(mockMusic).setLooping(true);
        verify(mockMusic).setVolume(0.3f);
        verify(mockMusic).play();
    }

    @Test
    void testDispose() {
        forestGameArea.dispose();

        verify(mockMusic).stop();
        verify(mockResourceService).unloadAssets(any(String[].class));
    }

    @Test
    void testSpawnRooms() {
        // This method is private, so we need to test its effects indirectly
        // You might need to expose a way to check the spawned rooms or modify the method to be protected for testing
        forestGameArea.create(); // This should trigger spawnRooms() internally

        // Add assertions to check the effects of spawnRooms()
        // For example, you could add a method to get the number of rooms and check it's correct
        // assertEquals(5, forestGameArea.getRoomCount());
    }

    @Test
    void testPlayMusic() {
        forestGameArea.create(); // This should trigger playMusic() internally

        verify(mockMusic).setLooping(true);
        verify(mockMusic).setVolume(0.3f);
        verify(mockMusic).play();
    }

    @Test
    void testLoadAssets() {
        forestGameArea.create(); // This should trigger loadAssets() internally

        verify(mockResourceService).loadTextures(any(String[].class));
        verify(mockResourceService).loadSounds(any(String[].class));
        verify(mockResourceService).loadMusic(any(String[].class));
    }
}