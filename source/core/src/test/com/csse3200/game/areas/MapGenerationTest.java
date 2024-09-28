package com.csse3200.game.areas;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.csse3200.game.areas.Generation.MapGenerator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class MapGeneratorTest {
    private MapGenerator mapGenerator;
    private static final String TEST_SEED = "testSeed";
    private static final int TEST_MAP_SIZE = 100;

    @BeforeEach
    void setUp() {
        mapGenerator = new MapGenerator(TEST_MAP_SIZE, TEST_SEED);
    }

    @Test
    void testConstructor() {
        assertEquals(TEST_MAP_SIZE, mapGenerator.getMapSize(), "Map size should be set correctly");
        assertEquals(TEST_SEED, mapGenerator.getMapSeed(), "Seed should be set correctly");
        assertEquals("0_0", mapGenerator.get_player_position(), "Starting room should be '0_0'");
    }

    @Test
    void testCreateMap() {
        mapGenerator.createMap();
        HashMap<String, List<String>> positions = mapGenerator.getPositions();
        assertFalse(positions.isEmpty(), "Map should not be empty after creation");
        assertTrue(positions.containsKey("0_0"), "Map should contain the starting room");
    }

    @Test
    void testGetPositions() {
        mapGenerator.createMap();
        HashMap<String, List<String>> positions = mapGenerator.getPositions();
        assertNotNull(positions, "Positions should not be null");
        assertFalse(positions.isEmpty(), "Positions should not be empty after map creation");
    }

    @Test
    void testGetRoomDetails() {
        mapGenerator.createMap();
        HashMap<String, HashMap<String, Integer>> roomDetails = mapGenerator.getRoomDetails();
        assertNotNull(roomDetails, "Room details should not be null");
        assertFalse(roomDetails.isEmpty(), "Room details should not be empty after map creation");
    }

    // @Test
    // void testParseIntRelativeLocation() {
    //     int[] coordinates = mapGenerator.parseIntRelativeLocation("1_2");
    //     assertArrayEquals(new int[]{2, 1}, coordinates, "Should correctly parse string to coordinates");
    // }

    @Test
    void testConsistentMapGeneration() {
        MapGenerator generator1 = new MapGenerator(TEST_MAP_SIZE, TEST_SEED);
        MapGenerator generator2 = new MapGenerator(TEST_MAP_SIZE, TEST_SEED);

        generator1.createMap();
        generator2.createMap();

        assertEquals(generator1.getPositions(), generator2.getPositions(), 
            "Maps generated with the same seed should be identical");
    }

    @Test
    void testDifferentSeedsProduceDifferentMaps() {
        MapGenerator generator1 = new MapGenerator(TEST_MAP_SIZE, TEST_SEED);
        MapGenerator generator2 = new MapGenerator(TEST_MAP_SIZE, "differentSeed");

        generator1.createMap();
        generator2.createMap();

        assertNotEquals(generator1.getPositions(), generator2.getPositions(), 
            "Maps generated with different seeds should be different");
    }

    @Test
    void testMapSizeAffectsRoomCount() {
        MapGenerator smallMap = new MapGenerator(50, TEST_SEED);
        MapGenerator largeMap = new MapGenerator(200, TEST_SEED);

        smallMap.createMap();
        largeMap.createMap();

        assertTrue(smallMap.getPositions().size() < largeMap.getPositions().size(), 
            "Larger map size should result in more rooms");
    }

    @Test
    void testAllRoomsConnected() {
        mapGenerator.createMap();
        HashMap<String, List<String>> positions = mapGenerator.getPositions();
        
        for (List<String> connections : positions.values()) {
            assertTrue(connections.stream().anyMatch(c -> !c.isEmpty()), 
                "Each room should have at least one connection");
        }
    }

    // @Test
    // void testQuickMapExport() {
    //     // Create a MapGenerator instance with a small map size

    //     int mapSize = 50;
    //     String seed = "testSeed";
    //     MapFactory test_factory = new MapFactory(seed ,mapSize);

    //     test_factory.exportMapGenerator("configs/test_json.json");

    //     assertTrue(true);

    //     // Export the map to JSON
    // }

    @Test
    void testRoomDetailsConsistency() {
        mapGenerator.createMap();
        HashMap<String, HashMap<String, Integer>> roomDetails = mapGenerator.getRoomDetails();
        HashMap<String, List<String>> positions = mapGenerator.getPositions();

        assertEquals(positions.size(), roomDetails.size(), 
            "Number of rooms in positions and room details should match");

        for (String roomKey : positions.keySet()) {
            assertTrue(roomDetails.containsKey(roomKey), 
                "Each room in positions should have corresponding details");
        }
    }

    @Test
    void testFurthestRoomFind() {
        mapGenerator.createMap();
        // 4_-7 is the biggest in this list
        assertEquals(mapGenerator.findFurthestRoom(), "4_-7",
                "Furthest room should be '4_-7' with this seed");
        assertEquals(mapGenerator.calculateDistance(mapGenerator.findFurthestRoom()), 11,
                "Calculate distance should calculate distance of furthest room");
    }

    @Test
    void testSupplementaryRoomCreation() {
        mapGenerator.createMap();
        HashMap<String, HashMap<String, Integer>> roomDetails = mapGenerator.getRoomDetails();
        List<Integer> types = new ArrayList<>();
        List<String> keys = new ArrayList<>(roomDetails.keySet());
        for (String key : keys) {
            types.add(roomDetails.get(key).get("room_type"));
        }
        assertEquals(Collections.frequency(types, MapGenerator.BOSSROOM), 1,
                "Should have only one boss room");
        assertEquals(Collections.frequency(types, MapGenerator.NPCROOM), 1,
                "Should have only one NPC room");
        assertEquals(Collections.frequency(types, MapGenerator.GAMEROOM), 1,
                "Should have only one Game room");
    }
}