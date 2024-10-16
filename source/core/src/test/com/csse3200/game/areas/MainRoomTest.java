// package com.csse3200.game.areas;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.util.ArrayList;
// import java.util.List;

// import com.badlogic.gdx.math.GridPoint2;
// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.utils.Timer;
// import com.csse3200.game.areas.GameArea;
// import com.csse3200.game.areas.terrain.TerrainFactory;
// import com.csse3200.game.areas.terrain.TerrainComponent;
// import com.csse3200.game.entities.Entity;
// import com.csse3200.game.entities.EntityService;
// import com.csse3200.game.entities.factories.CollectibleFactory;
// import com.csse3200.game.entities.factories.NPCFactory;
// import com.csse3200.game.events.EventHandler;
// import com.csse3200.game.areas.GameAreaService;
// import com.csse3200.game.components.CameraComponent;
// import com.csse3200.game.services.ResourceService;
// import com.csse3200.game.components.player.PlayerConfigComponent;
// import com.csse3200.game.entities.configs.PlayerConfig;
// import com.csse3200.game.services.ServiceLocator;
// import com.csse3200.game.components.CombatStatsComponent;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class MainRoomTest {

//     private MainRoom mainRoom;

//     private NPCFactory mockNpcFactory;
//     private CollectibleFactory mockCollectibleFactory;
//     private TerrainFactory spyTerrainFactory;
//     private GameArea mockGameArea;
//     private GameAreaService mockGameAreaService;
//     private Entity mockPlayer;
//     private EntityService mockEntityService;
//     private ResourceService mockResourceService;
//     private GameController mockGameController;
//     private PlayerConfigComponent mockComponent;
//     private PlayerConfig mockConfig;

//     @BeforeEach
//     void setUp() {
//         // Initialize mocks
//         mockNpcFactory = mock(NPCFactory.class);
//         mockCollectibleFactory = mock(CollectibleFactory.class);
//         spyTerrainFactory = spy(new TerrainFactory(mock(CameraComponent.class)));
//         mockGameArea = mock(GameArea.class);
//         mockGameAreaService = mock(GameAreaService.class);
//         mockPlayer = mock(Entity.class);
//         mockEntityService = mock(EntityService.class);
//         mockResourceService = mock(ResourceService.class);
//         mockGameController = mock(GameController.class);
//         mockComponent = mock(PlayerConfigComponent.class);
//         mockConfig = mock(PlayerConfig.class);
//         mockConfig.name = "bear";

//         // Set up ServiceLocator mocks
//         ServiceLocator.registerGameAreaService(mockGameAreaService);
//         ServiceLocator.registerEntityService(mockEntityService);
//         ServiceLocator.registerResourceService(mockResourceService);

//         when(mockGameAreaService.getGameArea()).thenReturn(mockGameArea);
//         when(mockGameAreaService.getGameController()).thenReturn(mockGameController);
//         when(mockGameController.getPlayer()).thenReturn(mockPlayer);
//         when(mockPlayer.getComponent(PlayerConfigComponent.class)).thenReturn(mockComponent);
//         when(mockComponent.getPlayerConfig()).thenReturn(mockConfig);
//         //when(mockConfig.name).thenReturn("bear");

//         List<String> roomConnections = new ArrayList<>();
//         mainRoom = new MainRoom(mockNpcFactory, mockCollectibleFactory, spyTerrainFactory,
//                 roomConnections, "0,0,14,10,0,0","TestMain");
//     }

//     @Test
//     void testSpawnDeployable() {
//         Entity mockDeployable = mock(Entity.class);
//         GridPoint2 tilePos = new GridPoint2(1, 1);

//         mainRoom.spawnDeployable(mockDeployable, tilePos, true, true);

//         verify(mockGameArea).spawnEntityAt(mockDeployable, tilePos, true, true);
//         assertTrue(mainRoom.entities.contains(mockDeployable));
//     }

//     @Test
//     void testCheckComplete_AllEnemiesDead() {
//         assertEquals(mainRoom.getEnemies().size(),0);

//         mainRoom.makeAllAnimalDead();
//         mainRoom.checkComplete();

//         Timer.schedule(new Timer.Task() {
//             @Override
//             public void run() {
//                 assertTrue(mainRoom.isComplete());
//             }
//         }, 1.1f);

//     }
//     @Test
//     void testCheckComplete_NotAllEnemiesDead() {
//         Entity mockEnemy = new Entity();
//         mockEnemy.addComponent(new CombatStatsComponent(10,1));

//         mainRoom.getEnemies().add(mockEnemy);

//         mainRoom.checkComplete();

//         Timer.schedule(new Timer.Task() {
//             @Override
//             public void run() {
//                 assertTrue(mainRoom.isComplete());
//             }
//         }, 1.1f);

//     }


//     @Test
//     void testIsAllAnimalDead() {
//         Entity mockEnemy = new Entity();
//         CombatStatsComponent mockCombat = mock(CombatStatsComponent.class);
//         mockEnemy.addComponent(mockCombat);
//         when(mockCombat.isDead()).thenReturn(false);

//         mainRoom.getEnemies().add(mockEnemy);

//         assertFalse(mainRoom.isAllAnimalDead());

//         when(mockCombat.isDead()).thenReturn(true);

//         assertTrue(mainRoom.isAllAnimalDead());
//     }


//     @Test
//     void testRemoveRoom() {
//         Entity mockEnemy = mock(Entity.class);
//         mainRoom.getEnemies().add(mockEnemy);

//         mainRoom.removeRoom();

//         assertTrue(mainRoom.getEnemies().isEmpty());
//     }
// }