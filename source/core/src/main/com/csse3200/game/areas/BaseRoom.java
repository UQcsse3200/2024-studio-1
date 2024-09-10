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
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class representing a room in the game.
 * <p>
 * The {@code BaseRoom} class provides the foundation for creating and managing a room in the game. 
 * It includes functionality for spawning terrain, walls, enemies, and items, as well as managing 
 * connections to other rooms and handling room completion status.
 * </p>
 */
public abstract class BaseRoom implements Room {
    private static final Logger logger = LoggerFactory.getLogger(BaseRoom.class);
    private final NPCFactory npcFactory;
    private final CollectibleFactory collectibleFactory;
    private final TerrainFactory terrainFactory;
    private final List<String> roomConnections;
    /**
     * The list of door entities in the room.
     */
    protected List<Entity> doors;

    /**
     * The list of enemy entities in the room.
     */
    protected List<Entity> enemies;

    /**
     * The list of collectible item entities in the room.
     */
    protected List<Entity> items;

    /**
     * The specification for the room, including grid points and indices.
     */
    protected final String specification;

    /**
     * The minimum grid point of the room.
     */
    protected final GridPoint2 minGridPoint;

    /**
     * The maximum grid point of the room.
     */
    protected final GridPoint2 maxGridPoint;

    /**
     * The index representing the group of animals to be used in the room.
     */
    protected final int animalGroup;

    /**
     * The index representing the group of items to be used in the room.
     */
    protected final int itemGroup;

    /**
     * List of specifications for different animal groups.
     */
    protected List<List<String>> animalSpecifications;

    /**
     * List of specifications for different item groups.
     */
    protected List<List<String>> itemSpecifications;

    /**
     * Indicates whether the room is fresh and has not been populated yet.
     */
    public Boolean isRoomFresh = true;

    /**
     * Indicates whether the room is a boss room.
     */
    protected Boolean isBossRoom = false;
    
    private boolean isRoomCompleted = false;

    private static final float WALL_THICKNESS = 0.15f;

    // Constructor and other methods remain unchanged
    /**
     * Constructs a {@code BaseRoom} with the specified factories, room connections, and room specification.
     * 
     * @param npcFactory         the NPC factory used to create NPCs in the room
     * @param collectibleFactory the Collectible factory used to create collectible items in the room
     * @param terrainFactory     the Terrain factory used to create terrain in the room
     * @param roomConnections    the list of keys for all adjacent rooms
     * @param specification      the specification for the room, including grid points and indices
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
        this.items = new ArrayList<>();

        initializeSpecifications();

        List<String> split = Arrays.stream(specification.split(",")).toList();

        // Parse grid points and indices from the specification
        this.minGridPoint = new GridPoint2(
                Integer.parseInt(split.get(0)),
                Integer.parseInt(split.get(1))
        );
        this.maxGridPoint = new GridPoint2(
                Integer.parseInt(split.get(2)),
                Integer.parseInt(split.get(3))
        );
        this.animalGroup = Integer.parseInt(split.get(4));
        this.itemGroup = Integer.parseInt(split.get(5));

        this.specification = specification;
    }

    /**
     * Initializes the specifications for animals and items in the room.
     * <p>
     * This method must be implemented by subclasses to provide specific initialization logic.
     * </p>
     */
    protected abstract void initializeSpecifications();

    /**
     * Creates enemies for the room based on the provided animal specifications.
     * 
     * @param animals the list of specifications for the animals
     * @param player  the main player character for the room
     */
    protected void createEnemyEntities(List<String> animals, Entity player) {
        enemies = new ArrayList<>();
        for (String animal : animals) {
            enemies.add(npcFactory.create(animal, player));
        }
    }

    /**
     * Creates and spawns walls around the room.
     * 
     * @param area         the game area to spawn the walls in
     * @param thickness    the thickness of the walls
     * @param tileBounds   the bounds of the tile map
     * @param worldBounds  the world bounds of the room
     */
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

    /**
     * Creates and spawns a wall entity at the specified position.
     * 
     * @param area     the game area to spawn the wall in
     * @param width    the width of the wall
     * @param height   the height of the wall
     * @param position the grid position of the wall
     * @return the created wall entity
     */
    private Entity createAndSpawnWall(GameArea area, float width, float height, GridPoint2 position) {
        Entity wall = ObstacleFactory.createWall(width, height);
        area.spawnEntityAt(wall, position, false, false);
        return wall;
    }

    /**
     * Adjusts the position of a wall entity.
     * 
     * @param wall      the wall entity to adjust
     * @param offsetX   the offset in the X direction
     * @param offsetY   the offset in the Y direction
     */
    private void adjustWallPosition(Entity wall, float offsetX, float offsetY) {
        Vector2 wallPos = wall.getPosition();
        wall.setPosition(wallPos.x + offsetX, wallPos.y + offsetY);
    }

    /**
     * Marks all entities in the room for removal.
     * This includes doors and items, and clears the respective lists.
     */
    public void removeRoom() {
        for (Entity data : doors) {
            ServiceLocator.getEntityService().markEntityForRemoval(data);
        }

        for (Entity item : items) {
            ServiceLocator.getEntityService().markEntityForRemoval(item);
        }
        this.items.clear();
        this.enemies.clear();
    }

    /**
     * Spawns the terrain for the room, including walls and background.
     * 
     * @param area          the game area to spawn the terrain in
     * @param wallThickness the thickness of the walls
     * @param isBossRoom    whether the room is a boss room
     */
    protected void spawnTerrain(GameArea area, float wallThickness, boolean isBossRoom) {
        // Background terrain
        TerrainComponent terrain = terrainFactory.createTerrain(TerrainFactory.TerrainType.ROOM1, isBossRoom);
        area.setTerrain(terrain);
        area.spawnEntity(new Entity().addComponent(terrain));
        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
        createWalls(area, wallThickness, tileBounds, worldBounds);
    }

    /**
     * Spawns the room with terrain, doors, enemies, and items.
     * 
     * @param player the main player character for the room
     * @param area   the game area to spawn the room in
     */
    public void spawn(Entity player, MainGameArea area) {
        this.spawnTerrain(area, WALL_THICKNESS, isBossRoom);
        this.spawnDoors(area, player);
        if (!isRoomCompleted) {
            createEnemyEntities(this.animalSpecifications.get(this.animalGroup), player);
            this.spawnAnimals(area, player, this.minGridPoint, this.maxGridPoint);
        }
       // makeAllAnimalDead();
    }

    /**
     * Marks all animals in the room as dead.
     * This will set their health to zero and trigger their death event.
     */
    protected void makeAllAnimalDead() {
        for (Entity animal : enemies) {
            CombatStatsComponent combatStatsComponent = animal.getComponent(CombatStatsComponent.class);
            combatStatsComponent.setHealth(0);
            combatStatsComponent.hit(combatStatsComponent);
        }
    }

    /**
     * Checks if the room is complete.
     * 
     * @return {@code true} if the room is complete, {@code false} otherwise
     */
    public boolean getIsRoomComplete() {
        return this.isRoomCompleted;
    }

    /**
     * Checks if all animals in the room are dead.
     * If all animals are dead, the room is marked as complete.
     * 
     * @return {@code true} if all animals are dead, {@code false} otherwise
     */
    public boolean isAllAnimalDead() {
        if (enemies.isEmpty()) {
            return true;
        }
        for (Entity animal : enemies) {
            if (!animal.getComponent(CombatStatsComponent.class).isDead())
                return false;
        }
        this.isRoomCompleted = true;
        return true;
    }

    /**
     * Spawns a collectible item in the room at the specified location.
     * 
     * @param area          the game area to spawn the item in
     * @param specification the specification of the item to spawn
     * @param pos           the location to spawn the item at
     */
    protected void spawnItem(MainGameArea area, String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        item.getEvents().addListener("pickedUp", () -> {
            for (Entity curItem : this.items) {
                if (curItem != item) {
                    ServiceLocator.getEntityService().unregister(curItem);
                    ServiceLocator.getEntityService().markEntityForRemoval(curItem);
                }
            }
            this.items.clear();
        });
        this.items.add(item);
        area.spawnEntityAt(item, pos, true, true);
    }

    /**
     * Spawns collectible items in the room based on the item specifications.
     * If no items are already present, it spawns a few items at predefined positions.
     */
    public void spawnItems() {
        if (!this.items.isEmpty()) {
            return;
        }
        MainGameArea area = ServiceLocator.getGameAreaService().getGameArea();
        spawnItem(area, this.itemSpecifications.get(this.itemGroup).get(0), new GridPoint2(8, 8));
        spawnItem(area, this.itemSpecifications.get(this.itemGroup).get(1), new GridPoint2(6, 8));
    }

    /**
     * Spawns animals in the room based on the provided specifications.
     * 
     * @param area   the game area to spawn the animals in
     * @param player the player character for the animals to target
     * @param min    the minimum position for spawning animals
     * @param max    the maximum position for spawning animals
     */
    protected void spawnAnimals(MainGameArea area, Entity player, GridPoint2 min, GridPoint2 max) {
        createEnemyEntities(this.animalSpecifications.get(this.animalGroup), ServiceLocator.getGameAreaService().getGameArea().getPlayer());
        for (Entity enemy : this.enemies) {
            GridPoint2 randomPos = new GridPoint2(ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.x, max.x + 1),
                    ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.y, max.y + 1));
            area.spawnEntityAt(enemy, randomPos, true, true);
            enemy.getEvents().addListener("checkAnimalsDead", () -> {
                if (this.isAllAnimalDead())
                    this.isRoomCompleted = true;
                this.spawnItems();
            });
        }
    }

    /**
     * Spawns doors for the room based on the room connections.
     * Ensures that door connections are properly initialized and creates doors at appropriate positions.
     * 
     * @param area   the game area to spawn the doors in
     * @param player the main player character for the doors to interact with
     */
    protected void spawnDoors(GameArea area, Entity player) {
        // Ensure roomConnections is properly initialized
        this.doors.clear();
        if (this.roomConnections == null || this.roomConnections.size() < 4) {
            throw new IllegalStateException("Room connections are not properly initialized.");
        }

        // Define door connections
        List<String> connections = this.roomConnections;
        String connectN = connections.get(0);
        String connectE = connections.get(1);
        String connectW = connections.get(2);
        String connectS = connections.get(3);
        // Create doors and retrieve scales
        Entity[] doors = {
                new Door('v', player.getId(), connectW), // left
                new Door('v', player.getId(), connectE), // right
                new Door('h', player.getId(), connectN), // bottom
                new Door('h', player.getId(), connectS)  // top
        };

        Vector2 doorvScale = doors[0].getScale();
        Vector2 doorhScale = doors[2].getScale();

        // Define positions and offsets
        GridPoint2[] positions = {
                new GridPoint2(15, 5),  // For connectW
                new GridPoint2(0, 5),   // For connectE
                new GridPoint2(7, 0),   // For connectS
                new GridPoint2(7, 11)   // For connectN
        };

        Vector2[] offsets = {
                new Vector2(-2 * doorvScale.x, 0),  // For connectW
                new Vector2(-doorvScale.x, 0),      // For connectE
                new Vector2(0, -doorhScale.y),      // For connectS
                new Vector2(0, -2 * doorhScale.y)   // For connectN
        };

        // Spawn and adjust doors
        for (int i = 0; i < doors.length; i++) {
            String connection = connections.get(i);
            if (connection == null || connection.isEmpty()) {
                logger.info("Skipping door placement for connection: {}", connection);
                continue;
            }

            area.spawnEntityAt(doors[i], positions[i], true, true);
            Vector2 doorPos = doors[i].getPosition();
            doors[i].setPosition(doorPos.x + offsets[i].x, doorPos.y + offsets[i].y);
            this.doors.add(doors[i]);
        }
    }
}
