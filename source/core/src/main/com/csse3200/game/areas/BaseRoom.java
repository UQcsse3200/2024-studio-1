package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;

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
    protected List<Entity> doors;
    protected List<Entity> enemies;

    protected final String specification;
    protected final GridPoint2 minGridPoint;
    protected final GridPoint2 maxGridPoint; 
    protected final int animalGroup;
    protected final int itemGroup;

    List<List<String>> animalSpecifications;
    List<List<String>> itemSpecifications;

    private static final float WALL_THICKNESS = 0.15f;

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
            List<String> roomConnections,
            String specification) {
        this.npcFactory = npcFactory;
        this.collectibleFactory = collectibleFactory;
        this.terrainFactory = terrainFactory;
        this.roomConnections = roomConnections;
        this.doors = new ArrayList<>();
        this.enemies = new ArrayList<>();

        initializeSpecifications();
        


        List<String> split = Arrays.stream(specification.split(",")).toList();

        // first 4 indexes of specification is the Grid points
        this.minGridPoint = new GridPoint2(
                Integer.parseInt(split.get(0)),
                Integer.parseInt(split.get(1))
        );
        this.maxGridPoint = new GridPoint2(
                Integer.parseInt(split.get(2)),
                Integer.parseInt(split.get(3))
        );

        // animal index in specificaiton 4
        this.animalGroup = Integer.parseInt(split.get(4));

        // item Group index specifcaiton 5
        this.itemGroup = Integer.parseInt(split.get(5));
        

        this.specification = specification;
    }

    // overide method 
    protected void initializeSpecifications() {}


    protected List<Entity> createEnemyEntities(List<String> animals, Entity player) {
        enemies = new ArrayList<>();
        for (String animal : animals) {
            enemies.add(npcFactory.create(animal, player));
        }
        return enemies;
    } 

    private void createWalls(GameArea area, float thickness, GridPoint2 tileBounds, Vector2 worldBounds) {
        // Create and spawn walls
        Entity leftWall = createAndSpawnWall(area, thickness, tileBounds.y, GridPoint2Utils.ZERO);
        Entity rightWall = createAndSpawnWall(area, thickness, worldBounds.y, new GridPoint2(tileBounds.x, 0));
        Entity topWall = createAndSpawnWall(area, worldBounds.x, thickness, new GridPoint2(0, tileBounds.y));
        Entity bottomWall = createAndSpawnWall(area, worldBounds.x, thickness, GridPoint2Utils.ZERO);
    
        // Adjust wall positions
        adjustWallPosition(rightWall, -thickness, 0);
        adjustWallPosition(topWall, 0, -thickness);
    }
    
    // Method to create and spawn a wall
    private Entity createAndSpawnWall(GameArea area, float width, float height, GridPoint2 position) {
        Entity wall = ObstacleFactory.createWall(width, height);
        area.spawnEntityAt(wall, position, false, false);
        return wall;
    }
    
    // Method to adjust the position of a wall
    private void adjustWallPosition(Entity wall, float offsetX, float offsetY) {
        Vector2 wallPos = wall.getPosition();
        wall.setPosition(wallPos.x + offsetX, wallPos.y + offsetY);
    }

    public void remove_room() {
        for (Entity data : doors) {
            ServiceLocator.getEntityService().markEntityForRemoval(data);
        } 
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

    public void spawn(Entity player, MainGameArea area) {
        this.spawnTerrain(area, WALL_THICKNESS);
        this.spawnDoors(area, player);
        createEnemyEntities(this.animalSpecifications.get(this.animalGroup), player);
        this.spawnAnimals(area, player, this.minGridPoint, this.minGridPoint);
        
        // FIXME
        // logger.info("Spawning items:");
        // int itemGroup = Integer.parseInt(split.get(5));
        // for (String s : itemSpecifications.get(itemGroup)){
        //     GridPoint2 randomPos = RandomUtils.random(min, max);
        //     this.spawnItem(area, s, randomPos);
        // }
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
    protected void spawnAnimals(GameArea area, Entity player, GridPoint2 min, GridPoint2 max) {

        for (Entity enemy : this.enemies) {
            GridPoint2 randomPos = RandomUtils.random(min, max);
            area.spawnEntityAt(enemy, randomPos, true, true);
        }
    }

    protected void spawnDoors(GameArea area, Entity player) {
        // Define door connections
        List<String> connections = this.roomConnections;
        String connectN = connections.get(0);
        String connectE = connections.get(1);
        String connectW = connections.get(2);
        String connectS = connections.get(3);
    
        // Create doors
        Entity[] doors = new Entity[4];
        doors[0] = new Door('v', player.getId(), connectW); // left
        doors[1] = new Door('v', player.getId(), connectE); // right
        doors[2] = new Door('h', player.getId(), connectS); // bottom
        doors[3] = new Door('h', player.getId(), connectN); // top
    
        // Retrieve scales
        Vector2 doorvScale = doors[0].getScale();
        Vector2 doorhScale = doors[2].getScale();
    
        // Define door data
        DoorData[] doorData = {
            new DoorData(connectE, doors[1], new GridPoint2(0, 5), new Vector2(-doorvScale.x, 0)),
            new DoorData(connectW, doors[0], new GridPoint2(15, 5), new Vector2(-2 * doorvScale.x, 0)),
            new DoorData(connectS, doors[2], new GridPoint2(7, 0), new Vector2(0, -doorhScale.y)),
            new DoorData(connectN, doors[3], new GridPoint2(7, 11), new Vector2(0, -2 * doorhScale.y))
        };
    
        // Spawn and adjust doors
        for (DoorData data : doorData) {
            if (!data.connection.isEmpty()) {
                area.spawnEntityAt(data.door, data.position, true, true);
                Vector2 doorPos = data.door.getPosition();
                data.door.setPosition(doorPos.x + data.offset.x, doorPos.y + data.offset.y);
                this.doors.add(data.door);
            }
        }
    }
    
    // Inner class for storing door data
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
