package com.csse3200.game.areas;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

@ExtendWith(GameExtension.class)
class GameAreaTest {
    private TerrainComponent mockTerrain;
    private Entity mockEntity;

    private GameArea gameArea;

    @BeforeEach
    void setUp() {
        mockTerrain = mock(TerrainComponent.class);
        mockEntity = mock(Entity.class);
        ServiceLocator.clear();
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());

        gameArea = new GameArea();


    }
//    @Test
//    void testDispose() {
//        ServiceLocator.getResourceService().loadMusic(new String[]{"sounds/BGM_03_mp3.mp3"});
//
//        // Spawn some entities
//        gameArea.spawnEntity(mockEntity);
//        gameArea.spawnEntity(mock(Entity.class));
//        gameArea.spawnEntity(mock(Entity.class));
//        gameArea.spawnEntity(mock(Entity.class));
//
//        gameArea.dispose();
//
//        // Verify all entities are disposed
//        verify(mockEntity).dispose();
//        assertEquals(0, gameArea.getListOfEntities().size());
//    }

    @Test
    void testSpawnEntity() {
        gameArea.spawnEntity(mockEntity);
        List<Entity> entities = gameArea.getListOfEntities();
        assertTrue(entities.contains(mockEntity));
    }

    @Test
    void testSpawnEntityAt() {
        gameArea.setTerrain(mockTerrain);
        GridPoint2 tilePos = new GridPoint2(2, 2);
        Vector2 worldPos = new Vector2(2, 2);
        when(mockTerrain.getTileSize()).thenReturn(1f);
        when(mockTerrain.tileToWorldPosition(tilePos)).thenReturn(worldPos);
        when(mockEntity.getCenterPosition()).thenReturn(new Vector2(0.5f,0.5f));

        gameArea.spawnEntityAt(mockEntity, tilePos, true, true);

        //check if entity is spawn in the correct position
        verify(mockEntity).setPosition(new Vector2(2,2));

        List<Entity> entities = gameArea.getListOfEntities();
        //check if entity is spawn correctly
        assertTrue(entities.contains(mockEntity));
    }
//
//    @Test
//    void testDisposeEntity() {
//        gameArea.spawnEntity(mockEntity);
//        gameArea.disposeEntity(mockEntity);
//        List<Entity> entities = gameArea.getListOfEntities();
//        assertFalse(entities.contains(mockEntity));
//    }

//    @Test
//    void testPlayMusic() {
//
//        gameArea.playMusic(GameArea.MusicType.NORMAL);
//        assertTrue("sounds/music/bgm_new.wav", true));
//
//        gameArea.playMusic(GameArea.MusicType.BOSS);
//        verify(ServiceLocator.getResourceService().playMusic("sounds/music/boss_room_music.mp3", true));
//    }
//
//    @Test
//    void testStopCurrentMusic() {
//        when(mockResourceService.playMusic(anyString(), eq(true))).thenReturn(mockMusic);
//
//        gameArea.playMusic(GameArea.MusicType.NORMAL);
//        gameArea.playMusic(GameArea.MusicType.BOSS);
//        gameArea.stopCurrentMusic();
//
//        verify(mockMusic, times(2)).stop();
//    }
//
    @Test
    void testGetSoundFilepaths() {
        String[] soundPaths = gameArea.getSoundFilepaths();
        assertArrayEquals(new String[]{"sounds/Impact4.ogg"}, soundPaths);
    }

    @Test
    void testGetTextureAtlasFilepaths() {
        String[] atlasPaths = gameArea.getTextureAtlasFilepaths();
        assertTrue(atlasPaths.length > 0);
        assertTrue(Arrays.asList(atlasPaths).contains("images/terrain_iso_grass.atlas"));
    }

    @Test
    void testGetTextureFilepaths() {
        String[] texturePaths = gameArea.getTextureFilepaths();
        assertTrue(texturePaths.length > 0);
        assertTrue(Arrays.asList(texturePaths).contains("images/box_boy_leaf.png"));
    }

    @Test
    void testGetMusicFilepaths() {
        String[] musicPaths = gameArea.getMusicFilepaths();
        assertArrayEquals(new String[]{
                "sounds/music/bgm_new.wav",
                "sounds/music/boss_room_music.mp3"
        }, musicPaths);
    }
}
