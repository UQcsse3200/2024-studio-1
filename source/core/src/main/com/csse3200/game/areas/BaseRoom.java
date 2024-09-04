package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.utils.math.GridPoint2Utils;

/**
 * This is the foundation of a room,
 * it is able to use other factories to build the complex structures needed for a room in the game.
 * e.g. walls, terrain, items, and enemies.
 */
public abstract class BaseRoom implements Room {
    private final NPCFactory npcFactory;
    private final CollectibleFactory collectibleFactory;
    private final TerrainFactory terrainFactory;
    private List<String> roomConnections;

    /**
     * Inject factories to be used for spawning here room object.
     * @param npcFactory the NPC factory to use
     * @param collectibleFactory the Collectible factory to use.
     * @param terrainFactory the terrain factory to use.
     */
    public BaseRoom(
            NPCFactory npcFactory,
            CollectibleFactory collectibleFactory,
            TerrainFactory terrainFactory,
            List<String> roomConnections) {
        this.npcFactory = npcFactory;
        this.collectibleFactory = collectibleFactory;
        this.terrainFactory = terrainFactory;
        this.roomConnections = roomConnections;
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



    /**
     * Spawn the terrain of the room, including the walls and background of the map.
     *
     * @param area the game area to spawn the terrain onto.
     * @param wallThickness the thickness of the walls around the room.
     */
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

    /**
     * Spawn a collectible item into the room.
     * @param area the game area to spawn the item into.
     * @param specification the specification of the item to create.
     * @param pos the location to spawn it to.
     */
    protected void spawnItem(GameArea area, String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        area.spawnEntityAt(item, pos, true, true);
    }

    /**
     * Spawn an NPC into the room
     * @param area the game area to spawn the NPC into.
     * @param player the player character for this npc to target.
     * @param animal the specification of the animal to create.
     * @param pos the location to spawn it to.
     */
    protected void spawnAnimal(GameArea area, Entity player, String animal, GridPoint2 pos) {
        Entity spawn = npcFactory.create(animal, player);
        area.spawnEntityAt(spawn, pos, true, true);
    }

    protected void spawnDoors(GameArea area, Entity player) {
        List<Entity> doors = new ArrayList<>();
        List<String> connections = this.roomConnections; 
        String connectN = connections.get(0);
        String connectE = connections.get(1);
        String connectW = connections.get(2);
        String connectS = connections.get(3);


        Entity door = DoorFactory.createDoor('v', player.getId(), connectW); // left
        Entity door2 = DoorFactory.createDoor('v',  player.getId(), connectE ); // right
        Entity door3 = DoorFactory.createDoor('h', player.getId(), connectS); // bottom
        Entity door4 = DoorFactory.createDoor('h',  player.getId(), connectN); // top

        Vector2 doorvScale = door.getScale();
        Vector2 doorhScale = door3.getScale();

        
        List<DoorData> doorData = List.of(
        new DoorData(connectE, door, new GridPoint2(0, 5), new Vector2(-doorvScale.x, 0)),
        new DoorData(connectW, door2, new GridPoint2(15, 5), new Vector2(-2 * doorvScale.x, 0)),
        new DoorData(connectS, door3, new GridPoint2(7, 0), new Vector2(0, -doorhScale.y)),
        new DoorData(connectN, door4, new GridPoint2(7, 11), new Vector2(0, -2 * doorhScale.y))
        );
            
    

        // Loop through the door data and handle each door
        for (DoorData data : doorData) {
            if (!data.connection.isEmpty()) {
                area.spawnEntityAt(data.door, data.position, true, true);
                Vector2 doorPos = data.door.getPosition();
                data.door.setPosition(doorPos.x + data.offset.x, doorPos.y + data.offset.y);
                doors.add(data.door);
            }
        }}

        private static class DoorData {
            String connection;
            Entity door;
            GridPoint2 position;
            Vector2 offset;
    
            DoorData(String connection, Entity door, GridPoint2 position, Vector2 offset) {
                this.connection = connection;
                this.door = door;
                this.position = position;
                this.offset = offset;
            }
    }

    }
