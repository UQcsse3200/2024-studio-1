package com.csse3200.game.areas;


import com.csse3200.game.areas.generation.RandomMapGenerator;

import com.csse3200.game.areas.generation.RoomType;
import com.csse3200.game.services.RandomService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class MapGeneratorTest {
    private RandomMapGenerator randomMapGenerator;
    private static final String TEST_SEED = "testSeed";
    private String expectedSeed;
    private static final int TEST_MAP_SIZE = 100;

    @BeforeEach
    void setUp() {
        ServiceLocator.registerRandomService(new RandomService(TEST_SEED));
        this.expectedSeed = ServiceLocator.getRandomService()
                .getRandomNumberGenerator(RandomMapGenerator.class)
                .getSeed();
        randomMapGenerator = new RandomMapGenerator(TEST_MAP_SIZE);
    }

    @Test
    void testConstructor() {
        assertEquals(TEST_MAP_SIZE, randomMapGenerator.getMapSize(), "Map size should be set correctly");
        assertEquals(expectedSeed, randomMapGenerator.getMapSeed(), "Seed should be set correctly");
        assertEquals("0_0", randomMapGenerator.getPlayerPosition(), "Starting room should be '0_0'");
    }

    @Test
    void testCreateMap() {
        randomMapGenerator.createMap();
        Map<String, List<String>> positions = randomMapGenerator.getPositions();
        assertFalse(positions.isEmpty(), "Map should not be empty after creation");
        assertTrue(positions.containsKey("0_0"), "Map should contain the starting room");
    }

    @Test
    void testGetPositions() {
        randomMapGenerator.createMap();
        Map<String, List<String>> positions = randomMapGenerator.getPositions();
        assertNotNull(positions, "Positions should not be null");
        assertFalse(positions.isEmpty(), "Positions should not be empty after map creation");
    }

    @Test
    void testGetRoomDetails() {
        randomMapGenerator.createMap();
        Map<String, Map<String, Integer>> roomDetails = randomMapGenerator.getRoomDetails();
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
        RandomMapGenerator generator1 = new RandomMapGenerator(TEST_MAP_SIZE);
        generator1.createMap();
        var first = generator1.getPositions().entrySet();

        setUp();

        RandomMapGenerator generator2 = new RandomMapGenerator(TEST_MAP_SIZE);
        generator2.createMap();
        var second = generator2.getPositions().entrySet();

        assertEquals(first, second,
            "Maps generated with the same seed should be identical");
    }

    @Test
    void testDifferentSeedsProduceDifferentMaps() {
        RandomMapGenerator generator1 = new RandomMapGenerator(TEST_MAP_SIZE);
        RandomMapGenerator generator2 = new RandomMapGenerator(TEST_MAP_SIZE);

        generator1.createMap();
        generator2.createMap();

        assertNotEquals(generator1.getPositions(), generator2.getPositions(), 
            "Maps generated with different seeds should be different");
    }

    @Test
    void testMapSizeAffectsRoomCount() {
        RandomMapGenerator smallMap = new RandomMapGenerator(50);
        RandomMapGenerator largeMap = new RandomMapGenerator(200);

        smallMap.createMap();
        largeMap.createMap();

        assertTrue(smallMap.getPositions().size() < largeMap.getPositions().size(), 
            "Larger map size should result in more rooms");
    }

    @Test
    void testAllRoomsConnected() {
        randomMapGenerator.createMap();
        Map<String, List<String>> positions = randomMapGenerator.getPositions();
        
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
        randomMapGenerator.createMap();
        Map<String, Map<String, Integer>> roomDetails = randomMapGenerator.getRoomDetails();
        Map<String, List<String>> positions = randomMapGenerator.getPositions();

        assertEquals(positions.size(), roomDetails.size(), 
            "Number of rooms in positions and room details should match");

        for (String roomKey : positions.keySet()) {
            assertTrue(roomDetails.containsKey(roomKey), 
                "Each room in positions should have corresponding details");
        }
    }

    @Test
    void testSupplementaryRoomCreation() {
        randomMapGenerator.createMap();
        Map<String, Map<String, Integer>> roomDetails = randomMapGenerator.getRoomDetails();
        List<Integer> types = new ArrayList<>();
        List<String> keys = new ArrayList<>(roomDetails.keySet());
        for (String key : keys) {
            types.add(roomDetails.get(key).get("room_type"));
        }
        assertEquals(1, Collections.frequency(types, RoomType.BOSS_ROOM.num),
                "Should have only one boss room");
        assertEquals(1, Collections.frequency(types, RoomType.SHOP_ROOM.num),
                "Should have only one NPC room");
        assertEquals(1, Collections.frequency(types, RoomType.GAME_ROOM.num),
                "Should have only one Game room");
    }
}