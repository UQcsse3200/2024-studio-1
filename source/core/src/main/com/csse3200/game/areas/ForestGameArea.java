package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.Generation.MapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class ForestGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
    private static final GridPoint2 ITEM_SPAWN = new GridPoint2(10, 5);

    private static final float WALL_THICKNESS = 0.15f;
    private static final int NUM_PICKAXES = 4;
    private static final int NUM_SHOTGUNS = 4;
    private static final String[] tileTextures = {
            "images/box_boy_leaf.png",
            "images/rounded_door_v.png",
            "images/rounded_door_h.png",
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
            "images/tile_blood.png",
            "images/Weapons/Shotgun.png",
            "images/Weapons/pickaxe.png",
    };

    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas"
    };
    private static final String[] forestSounds = {"sounds/Impact4.ogg"};
    private static final String[] weaponSounds = {"sounds/shotgun1_f.ogg", "sounds/shotgun1_r.ogg"};
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private static final String[] forestMusic = {backgroundMusic};
    private final TerrainFactory terrainFactory;

    private final MapGenerator mapGenerator;

    private Entity player;
    private final List<Room> roomList;

    private String currentRoom;



    /**
     * Initialise this ForestGameArea to use the provided TerrainFactory.
     *
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    public ForestGameArea(TerrainFactory terrainFactory) {
        this(terrainFactory, 1, "1234", "0_0");

    }

    public ForestGameArea(TerrainFactory terrainFactory, int difficulty, String seed, String startingRoom) {
        this.terrainFactory = terrainFactory;
        this.roomList = new ArrayList<>();
        this.mapGenerator = new MapGenerator(difficulty * 12, seed);
        this.mapGenerator.createMap();
        this.currentRoom = startingRoom;
        //this.mapGenerator.getRoomDetails().get("0_0");
        EntitySpawner.setGameArea(this);
        //IDK NOTHING IS RETURNED!!
        //logger.info(String.join(",",MapFactory.loadMap(null).roomConnections.get("0_0")));

        logger.info(this.mapGenerator.printRelativePosition());
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        player = spawnPlayer();

        createDoors();
        spawnEntities();

        //playMusic();
        //spawnCollectibleTest();
        //spawnBandage();
        //spawnMedkit();
        //spawnShieldPotion();
        //spawnPickaxes();
        //spawnShotgun();

        playMusic();

        //spawnCollectibleTest(); Test must not be in the source code
        spawnBandage();
        spawnMedkit();
        spawnShieldPotion();
        spawnPickaxes();
        spawnShotgun();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("BEAST BREAKOUT FACILITY"));
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
        createWalls(tileBounds, worldBounds);
    }

    private void spawnEntities() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = new GridPoint2(14,10);
        //should use animal index in MapGenerator
        //AnimalSpawner.spawnAnimalGroup(player,index,minPos,maxPos);
        EntitySpawner.addAnimalGroups(Arrays.asList("Rat","Dog","Minotaur","Dino","Bear", "Snake", "Bat"));
        EntitySpawner.addAnimalGroups(Arrays.asList("Bear","Snake","Dino"));
        EntitySpawner.addAnimalGroups(Arrays.asList("Bear","Bear","Minotaur"));
        EntitySpawner.addAnimalGroups(Arrays.asList("Snake","Bat","Minotaur"));
        EntitySpawner.addAnimalGroups(Arrays.asList("Bat","Bat","Bat"));
        EntitySpawner.addAnimalGroups(Arrays.asList("Minotaur","Minotaur","Minotaur"));
        EntitySpawner.addAnimalGroups(Arrays.asList("Rat","Bat","Bear"));
        EntitySpawner.addItemGroups(Arrays.asList("buff:energydrink", "item:medkit", "melee:knife", "ranged:shotgun", "item:shieldpotion"));
        EntitySpawner.addItemGroups(Arrays.asList("item:bandage", "melee:knife", "ranged:shotgun", "buff:energydrink", "item:shieldpotion"));
        EntitySpawner.addItemGroups(Arrays.asList("ranged:shotgun", "item:medkit", "melee:knife", "item:bandage", "buff:energydrink"));
        EntitySpawner.addItemGroups(Arrays.asList("item:shieldpotion", "ranged:shotgun", "melee:knife", "item:medkit", "buff:energydrink"));
        EntitySpawner.addItemGroups(Arrays.asList("melee:knife", "item:bandage", "ranged:shotgun", "item:shieldpotion", "item:medkit"));
        EntitySpawner.addItemGroups(Arrays.asList("buff:energydrink", "item:shieldpotion", "ranged:shotgun", "melee:knife", "item:bandage"));
        EntitySpawner.addItemGroups(Arrays.asList("item:medkit", "melee:knife", "buff:energydrink", "ranged:shotgun", "item:shieldpotion"));

        EntitySpawner.spawnAnimalGroup(player, 0, minPos, maxPos);
        EntitySpawner.spawnItemGroup(0,minPos, maxPos);
    }

    private void createWalls(GridPoint2 tileBounds, Vector2 worldBounds) {

        // Left
        Entity leftWall = ObstacleFactory.createWall(WALL_THICKNESS, tileBounds.y);
        spawnEntityAt(
                leftWall,
                GridPoint2Utils.ZERO,
                false,
                false);
        // Right
        Entity rightWall = ObstacleFactory.createWall((WALL_THICKNESS), worldBounds.y);
        spawnEntityAt(
                rightWall,
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        Vector2 rightWallPos = rightWall.getPosition();
        rightWall.setPosition(rightWallPos.x - WALL_THICKNESS, rightWallPos.y);
        // Top
        Entity topWall = ObstacleFactory.createWall(worldBounds.x, WALL_THICKNESS);
        spawnEntityAt(
                topWall,
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        Vector2 topWallPos = topWall.getPosition();
        topWall.setPosition(topWallPos.x, topWallPos.y - WALL_THICKNESS);
        // Bottom
        Entity bottomWall = ObstacleFactory.createWall(worldBounds.x, WALL_THICKNESS);
        spawnEntityAt(
                bottomWall,
                GridPoint2Utils.ZERO,
                false, false);
    }

    private void createDoors() {
        List<Entity> doors = new ArrayList<>();
        List<String> connections = this.mapGenerator.getPositions().get("0_0"); // N E W S
        String connectN = connections.get(0);
        String connectE = connections.get(1);
        String connectW = connections.get(2);
        String connectS = connections.get(3);
        DoorCallBack callBackEast = () -> {
            logger.info(connectE);
            this.currentRoom = connectE;
        };

        DoorCallBack callBackNorth = () -> {
            logger.info(connectN);
            this.currentRoom = connectN;
        };

        DoorCallBack callBackWest = () -> {
            logger.info(connectW);
            this.currentRoom = connectW;
        };

        DoorCallBack callBackSouth = () -> {
            logger.info(connectS);
            this.currentRoom = connectS;
        };
        Entity door = DoorFactory.createDoor('v', callBackEast, player.getId()); // left
        Entity door2 = DoorFactory.createDoor('v', callBackWest, player.getId()); // right
        Entity door3 = DoorFactory.createDoor('h', callBackSouth,player.getId()); // bottom
        Entity door4 = DoorFactory.createDoor('h', callBackNorth, player.getId()); // top

        Vector2 doorvScale = door.getScale();
        Vector2 doorhScale = door3.getScale();
        // Left Door
        if(!connectE.isEmpty()) {
            spawnEntityAt(
                    door,
                    new GridPoint2(0, 5),
                    true,
                    true);
            Vector2 doorPos = door.getPosition();
            door.setPosition(doorPos.x - doorvScale.x, doorPos.y);
            doors.add(door);
        }
        // Right Door
        if(!connectW.isEmpty()) {
            spawnEntityAt(door2,
                    new GridPoint2(15, 5),
                    true,
                    true);
            Vector2 door2Pos = door2.getPosition();
            door2.setPosition(door2Pos.x - 2 * doorvScale.x, door2Pos.y);
            doors.add(door2);
        }
        // Bottom Door
        if(!connectS.isEmpty()) {
            spawnEntityAt(door3,
                    new GridPoint2(7, 0),
                    true,
                    true);
            Vector2 door3Pos = door3.getPosition();
            door3.setPosition(door3Pos.x, door3Pos.y - doorhScale.y);
            doors.add(door3);
        }
        // Top Door
        if(!connectN.isEmpty()) {
            spawnEntityAt(door4,
                    new GridPoint2(7, 11),
                    true,
                    true);
            Vector2 door4Pos = door4.getPosition();
            door4.setPosition(door4Pos.x, door4Pos.y - 2 * doorhScale.y);
            doors.add(door4);
        }


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


    private Entity spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        return newPlayer;
    }

    private Entity spawnCollectibleTest() {
        Entity collectibleEntity = CollectibleFactory.createCollectibleEntity("buff:energydrink");
        spawnEntityAt(collectibleEntity, ITEM_SPAWN, true, true);
        return collectibleEntity;
    }

    private void spawnBandage() {
        Entity collectibleEntity = CollectibleFactory.createCollectibleEntity("item:bandage");
        spawnEntityAt(collectibleEntity, new GridPoint2(9, 9), true, true);
    }

    private void spawnMedkit() {
        Entity collectibleEntity = CollectibleFactory.createCollectibleEntity("item:medkit");
        spawnEntityAt(collectibleEntity, new GridPoint2(8, 8), true, true);
    }

    private void spawnShieldPotion() {
        Entity collectibleEntity = CollectibleFactory.createCollectibleEntity("item:shieldpotion");
        spawnEntityAt(collectibleEntity, new GridPoint2(5, 5), true, true);
    }

    private void spawnPickaxes() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_PICKAXES; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity pickaxe = CollectibleFactory.createCollectibleEntity("melee:knife");
            spawnEntityAt(pickaxe, randomPos, true, false); // Spawning the knife at a random position
        }
    }

    private void spawnShotgun() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < NUM_SHOTGUNS; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity shotgun = CollectibleFactory.createCollectibleEntity("ranged:shotgun");
            spawnEntityAt(shotgun, randomPos, true, false); // Spawning the Shotgun at a random position
        }
    }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    if(!UserSettings.get().mute)
    {
      music.setVolume(UserSettings.get().musicVolume);
    }
    else {
      music.setVolume(0);
    }
    music.play();
  }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(tileTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadSounds(weaponSounds);
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
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(forestMusic);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }
}
