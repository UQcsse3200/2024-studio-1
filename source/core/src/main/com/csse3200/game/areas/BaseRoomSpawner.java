package com.csse3200.game.areas;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.RandomNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseRoomSpawner implements RoomSpawner{
    private static final Logger logger = LoggerFactory.getLogger(BaseRoomSpawner.class);
    private final RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator("HiCameron");
    private final GameArea gameArea;

    private final List<List<String>> animals = new ArrayList<>();

    private final List<List<String>> items = new ArrayList<>();
    private static final float WALL_THICKNESS = 0.15f;

    private final NPCFactory npcFactory;
    private final CollectibleFactory collectibleFactory;
    private final PlayerFactory playerFactory;
    private final TerrainFactory terrainFactory;

    public BaseRoomSpawner(
            GameArea gameArea,
            NPCFactory npcFactory,
            CollectibleFactory collectibleFactory,
            PlayerFactory playerFactory,
            TerrainFactory terrainFactory) {
        this.gameArea = gameArea;
        this.npcFactory = npcFactory;
        this.collectibleFactory = collectibleFactory;
        this.playerFactory = playerFactory;
        this.terrainFactory = terrainFactory;
    }

    private void createWalls(GridPoint2 tileBounds, Vector2 worldBounds) {
        // Left
        Entity leftWall = ObstacleFactory.createWall(WALL_THICKNESS, tileBounds.y);
        gameArea.spawnEntityAt(
                leftWall,
                GridPoint2Utils.ZERO,
                false,
                false);
        // Right
        Entity rightWall = ObstacleFactory.createWall((WALL_THICKNESS), worldBounds.y);
        gameArea.spawnEntityAt(
                rightWall,
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        Vector2 rightWallPos = rightWall.getPosition();
        rightWall.setPosition(rightWallPos.x - WALL_THICKNESS, rightWallPos.y);
        // Top
        Entity topWall = ObstacleFactory.createWall(worldBounds.x, WALL_THICKNESS);
        gameArea.spawnEntityAt(
                topWall,
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        Vector2 topWallPos = topWall.getPosition();
        topWall.setPosition(topWallPos.x, topWallPos.y - WALL_THICKNESS);
        // Bottom
        Entity bottomWall = ObstacleFactory.createWall(worldBounds.x, WALL_THICKNESS);
        gameArea.spawnEntityAt(
                bottomWall,
                GridPoint2Utils.ZERO,
                false, false);
    }

    protected void spawnTerrain() {
        // Background terrain
        TerrainComponent terrain = terrainFactory.createTerrain(TerrainFactory.TerrainType.ROOM1);
        gameArea.spawnEntity(new Entity().addComponent(terrain));
        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
        createWalls(tileBounds, worldBounds);
    }

    protected Entity spawnPlayer(GridPoint2 position) {
        Entity newPlayer = playerFactory.createPlayer();
        gameArea.spawnEntityAt(newPlayer, position, true, true);
        return newPlayer;
    }

    protected void spawnItem(String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        gameArea.spawnEntityAt(item, pos, true, true);
    }

    protected void spawnAnimal(Entity player, String animal, GridPoint2 pos) {
        Entity spawn = npcFactory.create(animal, player);
        gameArea.spawnEntityAt(spawn, pos, true, true);
    }

    /*
    private void spawnDoors() {
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
        Entity door3 = DoorFactory.createDoor('h', callBackSouth, player.getId()); // bottom
        Entity door4 = DoorFactory.createDoor('h', callBackNorth, player.getId()); // top

        Vector2 doorvScale = door.getScale();
        Vector2 doorhScale = door3.getScale();
        // Left Door
        if (!connectE.isEmpty()) {
            gameArea.spawnEntityAt(
                    door,
                    new GridPoint2(0, 5),
                    true,
                    true);
            Vector2 doorPos = door.getPosition();
            door.setPosition(doorPos.x - doorvScale.x, doorPos.y);
            doors.add(door);
        }
        // Right Door
        if (!connectW.isEmpty()) {
            gameArea.spawnEntityAt(door2,
                    new GridPoint2(15, 5),
                    true,
                    true);
            Vector2 door2Pos = door2.getPosition();
            door2.setPosition(door2Pos.x - 2 * doorvScale.x, door2Pos.y);
            doors.add(door2);
        }
        // Bottom Door
        if (!connectS.isEmpty()) {
            gameArea.spawnEntityAt(door3,
                    new GridPoint2(7, 0),
                    true,
                    true);
            Vector2 door3Pos = door3.getPosition();
            door3.setPosition(door3Pos.x, door3Pos.y - doorhScale.y);
            doors.add(door3);
        }
        // Top Door
        if (!connectN.isEmpty()) {
            gameArea.spawnEntityAt(door4,
                    new GridPoint2(7, 11),
                    true,
                    true);
            Vector2 door4Pos = door4.getPosition();
            door4.setPosition(door4Pos.x, door4Pos.y - 2 * doorhScale.y);
            doors.add(door4);
        }


    }
     */
}