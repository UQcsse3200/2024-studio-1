package com.csse3200.game.areas.Rooms;

import com.csse3200.game.areas.*;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.RandomService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(GameExtension.class)
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
    private List<String> testItemsSpawned;

    @Mock
    private GameAreaService mockGameAreaService;
    @Mock
    private GameController mockGameController;

    @Mock
    private RandomService mockRandomService;

    @Mock
    private RandomNumberGenerator mockRNG;

    @Mock
    Entity entity;
    @Mock
    private PlayerConfig playerConfig;
    @Mock
    private PlayerConfigComponent playerConfigComponent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerConfig = mock(PlayerConfig.class);
        playerConfig.name = "bear";
        entity = mock(Entity.class);
        playerConfigComponent = mock(PlayerConfigComponent.class);

        ServiceLocator.registerGameAreaService(mockGameAreaService);
        when(mockGameAreaService.getGameController()).thenReturn(mockGameController);
        when(mockGameController.getPlayer()).thenReturn(entity);
        when(entity.getComponent(PlayerConfigComponent.class)).thenReturn(playerConfigComponent);
        when(playerConfigComponent.getPlayerConfig()).thenReturn(playerConfig);

        when(mockRandomService.getRandomNumberGenerator(any(Class.class))).thenReturn(mockRNG);
        ServiceLocator.registerRandomService(mockRandomService);

        roomFactory = new RoomFactory(mockNpcFactory, mockCollectibleFactory, mockTerrainFactory);
        testRoomConnections = Arrays.asList("North", "South", "East", "West");
        testSpecification = "0,0,14,10,1,1";
        testRoomName = "TestRoom";
        testItemsSpawned = Arrays.asList("item:targetdummy:buyable","buff:syringe:buyable","buff:armor:buyable",
                "item:medkit:buyable", "item:reroll:buyable","item:heart:buyable");
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
        Room room = roomFactory.createShopRoom(testRoomConnections, testSpecification, testRoomName, testItemsSpawned);
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

//    @Test
//    void testRoomInterfaceMethods() {
//        Room room = roomFactory.createRoom(testRoomConnections, testSpecification, testRoomName);
//
//        assertFalse(room.isComplete());
//        room.setComplete();
//        assertTrue(room.isComplete());
//
//        // TODO can't effectively test spawn, removeRoom, and checkIfRoomComplete
//        // without more complex setup or integration tests.
//    }
}