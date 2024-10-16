package com.csse3200.game.areas.Levels;

import com.csse3200.game.areas.Level;
import com.csse3200.game.areas.LevelMap;
import com.csse3200.game.areas.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Mock
    private LevelMap mockMap;

    @Mock
    private Room mockRoom1;

    @Mock
    private Room mockRoom2;

    private Level level;
    private Map<String, Room> mockRooms;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRooms = new HashMap<>();
        mockRooms.put("0_0", mockRoom1);
        mockRooms.put("1_1", mockRoom2);

        level = new Level(mockMap, 1, mockRooms);
    }

    @Test
    void testConstructor() {
        assertNotNull(level);
        assertEquals(1, level.getLevelNumber());
        assertEquals(mockMap, level.getMap());
    }

    @Test
    void testGetRoom() {
        Room room = level.getRoom("0_0");
        assertNotNull(room);
        assertEquals(mockRoom1, room);
    }

    @Test
    void testGetRoomNonExistent() {
        Room room = level.getRoom("nonexistent");
        assertNull(room);
    }

    @Test
    void testGetStartingRoomKey() {
        String startingRoomKey = level.getStartingRoomKey();
        assertEquals("0_0", startingRoomKey);
    }

    @Test
    void testGetLevelNumber() {
        int levelNumber = level.getLevelNumber();
        assertEquals(1, levelNumber);
    }

    @Test
    void testGetMap() {
        LevelMap returnedMap = level.getMap();
        assertEquals(mockMap, returnedMap);
    }
}



