// package com.csse3200.game.areas;

// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;

// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.utils.Array;
// import com.badlogic.gdx.scenes.scene2d.Stage;
// import com.csse3200.game.areas.*;
// import com.csse3200.game.areas.minimap.MinimapComponent;
// import com.csse3200.game.areas.minimap.MinimapFactory;
// import com.csse3200.game.areas.generation.MapGenerator;
// import com.csse3200.game.components.player.inventory.InventoryComponent;
// import com.csse3200.game.entities.Entity;
// import com.csse3200.game.entities.configs.MapLoadConfig;
// import com.csse3200.game.services.ServiceLocator;
// import com.csse3200.game.areas.GameAreaService;
// import com.csse3200.game.entities.EntityService;
// import com.csse3200.game.rendering.RenderService;
// import com.csse3200.game.services.ResourceService;
// import com.csse3200.game.services.RandomService;
// import com.csse3200.game.physics.PhysicsService;
// import com.csse3200.game.physics.PhysicsEngine;
// import com.badlogic.gdx.physics.box2d.World;
// import com.csse3200.game.components.player.inventory.Pet;
// import com.csse3200.game.events.EventHandler;
// import com.csse3200.game.components.CameraComponent;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.util.*;

// class GameControllerTest {

//     private GameArea spyGameArea;
//     private LevelFactory mockLevelFactory;
//     private Entity mockPlayer;
//     private Level mockLevel;
//     private Room mockRoom;
//     private MinimapFactory mockMinimapFactory;
//     private InventoryComponent mockInventoryComponent;

//     private GameController gameController;
//     private GameController spy;

//     @BeforeEach
//     void setUp() {
//         // Setup ServiceLocator with real instances
//         ServiceLocator.clear();
//         ResourceService resourceServices= new ResourceService();
//         ResourceService spyResourceService = spy(resourceServices);
//         doNothing().when(spyResourceService).loadTextures(any(String[].class));

//         RenderService renderService= new RenderService();
//         RenderService spyRenderService = spy(renderService);
//         when(spyRenderService.getStage()).thenReturn(mock(Stage.class));

//         PhysicsService physicsService = new PhysicsService();
//         PhysicsService spyPhysicsService = spy(physicsService);
//         when(spyPhysicsService.getPhysics()).thenReturn(mock(PhysicsEngine.class));
//         when(spyPhysicsService.getPhysics().getWorld()).thenReturn(mock(World.class));
//         when(spyPhysicsService.getPhysics().getWorld().isLocked()).thenReturn(false);

//         ServiceLocator.registerEntityService(new EntityService());
//         ServiceLocator.registerRandomService(new RandomService(""));
//         ServiceLocator.registerPhysicsService(spyPhysicsService);
//         ServiceLocator.registerRenderService(spyRenderService);
//         ServiceLocator.registerResourceService(spyResourceService);


//         GameArea gameArea = new GameArea();
//         spyGameArea = spy(gameArea);
//         doNothing().when(spyGameArea).updateMinimap(anyString());
//         doNothing().when(spyGameArea).generateMinimap(any(Level.class));
//         mockLevelFactory = mock(LevelFactory.class);
//         mockPlayer = mock(Entity.class);
//         mockLevel = mock(Level.class);
//         mockRoom = mock(Room.class);
//         mockInventoryComponent = mock(InventoryComponent.class);


//         // Setup mocks
//         when(mockPlayer.getEvents()).thenReturn(mock(EventHandler.class));
//         when(mockPlayer.getPosition()).thenReturn(new Vector2(7, 5));
//         when(mockPlayer.getComponent(InventoryComponent.class)).thenReturn(mockInventoryComponent);
//         when(mockLevelFactory.create(anyInt())).thenReturn(mockLevel);
//         when(mockLevel.getStartingRoomKey()).thenReturn("0_0");
//         when(mockLevel.getRoom(anyString())).thenReturn(mockRoom);
//         when(mockInventoryComponent.getPets()).thenReturn(new Array<>());

//         ServiceLocator.getRenderService().setSecondaryCamera(mock(CameraComponent.class));

//         gameController = new GameController(spyGameArea, mockLevelFactory, mockPlayer, false, new MapLoadConfig());
//     }

//     @Test
//     void testChangeRooms() {
//         gameController.changeRooms("newRoom");
//         verify(mockRoom).removeRoom();
//         verify(spyGameArea, times(2)).playMusic(0);
//     }

//     @Test
//     void testUpdateLevel() {
//         gameController.changeLevel(1);
//         gameController.updateLevel();
//         verify(mockLevelFactory).create(1);
//         verify(spyGameArea).spawnEntity(any(Entity.class));
//     }

//     @Test
//     void testSpawnCurrentRoom() {
//         gameController.spawnCurrentRoom();
//         verify(mockRoom).spawn(eq(mockPlayer), eq(spyGameArea));
//         verify(spyGameArea).spawnEntity(mockPlayer);
//     }

//     @Test
//     void testSaveMapData() {
//         gameController.saveMapData();
//         verify(mockLevelFactory).saveMapData(eq(GameController.MAP_SAVE_PATH), anyString());
//     }
// }