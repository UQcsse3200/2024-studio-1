package com.csse3200.game.areas.Levels;

import com.csse3200.game.areas.LevelMap;
import com.csse3200.game.areas.generation.MapGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelMapTest {

    @Mock
    private MapGenerator mockMapGenerator;

    private LevelMap levelMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        levelMap = new LevelMap("testSeed", 10);
        levelMap.mapData = mockMapGenerator;
    }

    @Test
    void testGetRoomConnections() {
        HashMap<String, List<String>> mockPositions = new HashMap<>();
        mockPositions.put("room1", Arrays.asList("room2", "room3", "", ""));
        when(mockMapGenerator.getPositions()).thenReturn(mockPositions);

        List<String> connections = levelMap.getRoomConnections("room1");
        assertEquals(Arrays.asList("room2", "room3", "", ""), connections);
    }

    @Test
    void testGetRoomConnection() {
        HashMap<String, List<String>> mockPositions = new HashMap<>();
        mockPositions.put("room1", Arrays.asList("room2", "room3", "", ""));
        when(mockMapGenerator.getPositions()).thenReturn(mockPositions);

        String connection = levelMap.getRoomConnection("room1", 1);
        assertEquals("room3", connection);
    }

    @Test
    void testGetAnimalIndex() {
        HashMap<String, HashMap<String, Integer>> mockRoomDetails = new HashMap<>();
        HashMap<String, Integer> roomInfo = new HashMap<>();
        roomInfo.put("animal_index", 2);
        mockRoomDetails.put("room1", roomInfo);
        when(mockMapGenerator.getRoomDetails()).thenReturn(mockRoomDetails);

        int animalIndex = levelMap.getAnimalIndex("room1");
        assertEquals(2, animalIndex);
    }

    @Test
    void testGetItemIndex() {
        HashMap<String, HashMap<String, Integer>> mockRoomDetails = new HashMap<>();
        HashMap<String, Integer> roomInfo = new HashMap<>();
        roomInfo.put("item_index", 3);
        mockRoomDetails.put("room1", roomInfo);
        when(mockMapGenerator.getRoomDetails()).thenReturn(mockRoomDetails);

        int itemIndex = levelMap.getItemIndex("room1");
        assertEquals(3, itemIndex);
    }

    @Test
    void testGetPlayerLocation() {
        when(mockMapGenerator.get_player_position()).thenReturn("0_0");
        String playerLocation = levelMap.getPlayerLocation();
        assertEquals("0_0", playerLocation);
    }

    @Test
    void testGetSeed() {
        when(mockMapGenerator.getMapSeed()).thenReturn("testSeed");
        String seed = levelMap.getSeed();
        assertEquals("testSeed", seed);
    }

    @Test
    void testGetMapSize() {
        when(mockMapGenerator.getMapSize()).thenReturn(10);
        int mapSize = levelMap.getMapSize();
        assertEquals(10, mapSize);
    }
}