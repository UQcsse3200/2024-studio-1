package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.RoomDirection;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.RoomFactory;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final String[] tileTextures = {
          "images/tile_1.png",
          "images/tile_2.png",
          "images/tile_3.png",
          "images/tile_4.png",
          "images/tile_5.png",
          "images/tile_6.png",
          "images/tile_7.png",
          "images/tile_8.png",
          "images/tile_middle.png",
          "images/tile_general.png",
          "images/tile_broken1.png",
          "images/tile_broken2.png",
          "images/tile_broken3.png",
          "images/tile_staircase.png",
          "images/tile_staircase_down.png",
          "images/tile_blood.png"
  };

  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};
  private final TerrainFactory terrainFactory;

  private Entity player;
  private List<Room> roomList;
  private static final float WALL_WIDTH = 0.1f;


  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    this.roomList = new ArrayList<>();
  }


  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();


    playMusic();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("BEAST BREAKOUT FACILITY"));
    spawnTerrain();
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.ROOM1);
    spawnEntity(new Entity().addComponent(terrain));
    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),
            false,
            false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),
            false,
            false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
  }


  /**
   * TODO: testing with rooms in all directions, inside the main room
   * TODO: adding entry and exit at main door and also inside
   * TODO: adding items inside, which are collectable
   */
  private void spawnRooms() {
    // Room with inner rooms in all cardinal directions
    Room mainRoom = RoomFactory.createRoom(true);

    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    mainRoom.setRoomPosition(randomPos);

    // inner rooms don't need positions, they will be drawn relative to the main room
    Room north = RoomFactory.createRoom(false);
    Room south = RoomFactory.createRoom(false);
    Room east = RoomFactory.createRoom(false);
    Room west = RoomFactory.createRoom(false);

    mainRoom = RoomFactory.createConnections(mainRoom, north, south, east, west);

    // adding rooms to the list for reference
    roomList.add(mainRoom);
    roomList.add(north);
    roomList.add(south);
    roomList.add(east);
    roomList.add(west);

    // TODO: Add rooms to the game area
    for (Room room : roomList) {
      if (room.isInside()) {
        // TODO: spawn related to position of the room
        spawnEntityAt(room, room.getRoomPos(), true, true);
      }
    }
  }




  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(tileTextures);

    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(tileTextures);

    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
