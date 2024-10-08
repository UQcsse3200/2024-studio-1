package com.csse3200.game.areas.Rooms;

import com.csse3200.game.areas.BossRoom;
import com.csse3200.game.areas.MainRoom;
import com.csse3200.game.areas.ShopRoom;
import com.csse3200.game.areas.GambleRoom;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomFactoryTest {

    @Mock
    private NPCFactory mockNpcFactory;
    @Mock
    private CollectibleFactory mockCollectibleFactory;
    @Mock
    private TerrainFactory mockTerrainFactory;

    private RoomFactory roomFactory;
    private List<String> testRoomConnections;
    private String testSpecification;
    private String testRoomName;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomFactory = new RoomFactory(mockNpcFactory, mockCollectibleFactory, mockTerrainFactory);
        testRoomConnections = Arrays.asList("North", "South", "East", "West");
        testSpecification = "0,0,14,10,1,1";
        testRoomName = "TestRoom";
    }

    @Test
    void testCreateRoom() {
        Room room = roomFactory.createRoom(testRoomConnections, testSpecification, testRoomName);
        
        assertNotNull(room);
        assertTrue(room instanceof MainRoom);
        assertEquals(testRoomName, room.getRoomName());
    }

    @Test
    void testCreateBossRoom() {
        Room room = roomFactory.createBossRoom(testRoomConnections, testSpecification, testRoomName);
        
        assertNotNull(room);
        assertTrue(room instanceof BossRoom);
        assertEquals(testRoomName, room.getRoomName());
    }

    @Test
    void testCreateShopRoom() {
        Room room = roomFactory.createShopRoom(testRoomConnections, testSpecification, testRoomName);
        
        assertNotNull(room);
        assertTrue(room instanceof ShopRoom);
        assertEquals(testRoomName, room.getRoomName());
    }

    @Test
    void testCreateGambleRoom() {
        Room room = roomFactory.createGambleRoom(testRoomConnections, testSpecification, testRoomName);
        
        assertNotNull(room);
        assertTrue(room instanceof GambleRoom);
        assertEquals(testRoomName, room.getRoomName());
    }

    @Test
    void testRoomInterfaceMethods() {
        Room room = roomFactory.createRoom(testRoomConnections, testSpecification, testRoomName);
        
        assertFalse(room.getIsRoomComplete());
        room.setIsRoomComplete();
        assertTrue(room.getIsRoomComplete());
        
        // TODO can't effectively test spawn, removeRoom, and checkIfRoomComplete
        // without more complex setup or integration tests.
    }
}