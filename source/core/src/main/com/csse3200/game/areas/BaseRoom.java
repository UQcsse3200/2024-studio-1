package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.utils.math.GridPoint2Utils;


public abstract class BaseRoom implements Room {
    private final NPCFactory npcFactory;
    private final CollectibleFactory collectibleFactory;
    private final TerrainFactory terrainFactory;

    public BaseRoom(
            NPCFactory npcFactory,
            CollectibleFactory collectibleFactory,
            TerrainFactory terrainFactory) {
        this.npcFactory = npcFactory;
        this.collectibleFactory = collectibleFactory;
        this.terrainFactory = terrainFactory;
    }

    private void createWalls(GameArea area, float thickness, GridPoint2 tileBounds, Vector2 worldBounds) {
        // Left
        Entity leftWall = ObstacleFactory.createWall(thickness, tileBounds.y);
        area.spawnEntityAt(
                leftWall,
                GridPoint2Utils.ZERO,
                false,
                false);
        // Right
        Entity rightWall = ObstacleFactory.createWall((thickness), worldBounds.y);
        area.spawnEntityAt(
                rightWall,
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        Vector2 rightWallPos = rightWall.getPosition();
        rightWall.setPosition(rightWallPos.x - thickness, rightWallPos.y);
        // Top
        Entity topWall = ObstacleFactory.createWall(worldBounds.x, thickness);
        area.spawnEntityAt(
                topWall,
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        Vector2 topWallPos = topWall.getPosition();
        topWall.setPosition(topWallPos.x, topWallPos.y - thickness);
        // Bottom
        Entity bottomWall = ObstacleFactory.createWall(worldBounds.x, thickness);
        area.spawnEntityAt(
                bottomWall,
                GridPoint2Utils.ZERO,
                false, false);
    }

    protected void spawnTerrain(GameArea area, float wallThickness) {
        // Background terrain
        TerrainComponent terrain = terrainFactory.createTerrain(TerrainFactory.TerrainType.ROOM1);
        area.setTerrain(terrain);
        area.spawnEntity(new Entity().addComponent(terrain));
        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
        createWalls(area, wallThickness, tileBounds, worldBounds);
    }

    protected void spawnItem(GameArea area, String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        area.spawnEntityAt(item, pos, true, true);
    }

    protected void spawnAnimal(GameArea area, Entity player, String animal, GridPoint2 pos) {
        Entity spawn = npcFactory.create(animal, player);
        area.spawnEntityAt(spawn, pos, true, true);
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