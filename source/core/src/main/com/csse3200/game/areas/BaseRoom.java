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
import com.csse3200.game.services.ServiceLocator;
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
    private final List<String> roomConnections;
    List<Entity> doors;

    /**
     * Inject factories to be used for spawning here room object.
     *
     * @param npcFactory         the NPC factory to use
     * @param collectibleFactory the Collectible factory to use.
     * @param terrainFactory     the terrain factory to use.
     * @param roomConnections    the keys for all the adjacent rooms.
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
        this.doors = new ArrayList<>();

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
     * Mark all entities in the room for removal.
     */
    public void removeRoom() {
        for (Entity data : doors) {
            ServiceLocator.getEntityService().markEntityForRemoval(data);
        }
    }

    /**
     * Spawn the terrain of the room, including the walls and background of the map.
     *
     * @param area          the game area to spawn the terrain onto.
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
     *
     * @param area          the game area to spawn the item into.
     * @param specification the specification of the item to create.
     * @param pos           the location to spawn it to.
     */
    protected void spawnItem(GameArea area, String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        area.spawnEntityAt(item, pos, true, true);
    }

    /**
     * Spawn an NPC into the room
     *
     * @param area   the game area to spawn the NPC into.
     * @param player the player character for this npc to target.
     * @param animal the specification of the animal to create.
     * @param pos    the location to spawn it to.
     */
    protected void spawnAnimal(GameArea area, Entity player, String animal, GridPoint2 pos) {
        Entity spawn = npcFactory.create(animal, player);
        area.spawnEntityAt(spawn, pos, true, true);
    }

    /**
     * Spawn the doors for this room.
     *
     * @param area   the game area to spawn them into.
     * @param player The main player of the room.
     */
    protected void spawnDoors(GameArea area, Entity player) {
        List<String> connections = this.roomConnections;
        String connectN = connections.get(0);
        String connectE = connections.get(1);
        String connectW = connections.get(2);
        String connectS = connections.get(3);

        Entity westDoor = new Door('v', player.getId(), connectW); // left
        Entity eastDoor = new Door('v', player.getId(), connectE); // right
        Entity southDoor = new Door('h', player.getId(), connectS); // bottom
        Entity northDoor = new Door('h', player.getId(), connectN); // top

        Vector2 doorvScale = westDoor.getScale();
        Vector2 doorhScale = southDoor.getScale();


        List<DoorData> doorData = List.of(
                new DoorData(connectE, westDoor, new GridPoint2(0, 5), new Vector2(-doorvScale.x, 0)),
                new DoorData(connectW, eastDoor, new GridPoint2(15, 5), new Vector2(-2 * doorvScale.x, 0)),
                new DoorData(connectS, southDoor, new GridPoint2(7, 0), new Vector2(0, -doorhScale.y)),
                new DoorData(connectN, northDoor, new GridPoint2(7, 11), new Vector2(0, -2 * doorhScale.y))
        );

        // Loop through the door data and handle each door
        for (DoorData data : doorData) {
            if (!data.connection.isEmpty()) {
                area.spawnEntityAt(data.door, data.position, true, true);
                Vector2 doorPos = data.door.getPosition();
                data.door.setPosition(doorPos.x + data.offset.x, doorPos.y + data.offset.y);
                doors.add(data.door);
            }
        }
    }

    /**
     * Details of a door.
     */
    private record DoorData(
            String connection,
            Entity door,
            GridPoint2 position,
            Vector2 offset) {
    }
}
