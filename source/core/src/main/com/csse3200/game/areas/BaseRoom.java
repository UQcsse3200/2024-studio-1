package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.GridPoint2Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRoom implements Room {
    private static final Logger logger = LoggerFactory.getLogger(BaseRoom.class);
    protected final DoorFactory doorFactory = new DoorFactory();
    protected final TerrainFactory terrainFactory;
    protected final CollectibleFactory collectibleFactory;
    protected final List<String> roomConnections;
    protected List<Entity> entities;  // Unified list for all entities

    protected final String specification;
    protected final GridPoint2 minGridPoint;
    protected final GridPoint2 maxGridPoint;
    protected final int itemGroup;
    protected List<List<String>> itemSpecifications;
    protected boolean isRoomCompleted = false;
    protected static final float WALL_THICKNESS = 0.15f;
    protected final String roomName;

    public BaseRoom(
            TerrainFactory terrainFactory,
            CollectibleFactory collectibleFactory,
            List<String> roomConnections,
            String specification,
            String roomName) {
        this.terrainFactory = terrainFactory;
        this.collectibleFactory = collectibleFactory;
        this.roomConnections = roomConnections;
        this.entities = new ArrayList<>();
        this.itemSpecifications = getItemSpecifications();

        this.roomName = roomName;

        List<String> split = Arrays.stream(specification.split(",")).toList();

        this.minGridPoint = new GridPoint2(
                Integer.parseInt(split.get(0)),
                Integer.parseInt(split.get(1))
        );
        this.maxGridPoint = new GridPoint2(
                Integer.parseInt(split.get(2)),
                Integer.parseInt(split.get(3))
        );
        this.itemGroup = Integer.parseInt(split.get(5));
        this.specification = specification;
    }

    public String getRoomName() {
        return this.roomName;
    }

    protected abstract List<List<String>> getItemSpecifications();


    public abstract void checkIfRoomComplete();
  /**
     * Spawns the terrain for the room, including walls and background.
     *
     * @param area          the game area to spawn the terrain in
     * @param wallThickness the thickness of the walls
     * @param isBossRoom    whether the room is a boss room
     */

    protected void spawnTerrain(GameArea area, float wallThickness, boolean isBossRoom) {
        TerrainComponent terrain = terrainFactory.createTerrain(TerrainFactory.TerrainType.ROOM1, isBossRoom);
        area.setTerrain(terrain);
        Entity terrainEntity = new Entity()
                .addComponent(terrain)
                .addComponent(new NameComponent("terrain"));
                
        area.spawnEntity(terrainEntity);
        entities.add(terrainEntity);
        
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
        createWalls(area, wallThickness, tileBounds, worldBounds);
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
        Entity leftWall = createAndSpawnWall(area, thickness, tileBounds.y, GridPoint2Utils.ZERO);
        Entity rightWall = createAndSpawnWall(area, thickness, worldBounds.y, new GridPoint2(tileBounds.x, 0));
        Entity topWall = createAndSpawnWall(area, worldBounds.x, thickness, new GridPoint2(0, tileBounds.y));
        Entity bottomWall = createAndSpawnWall(area, worldBounds.x, thickness, GridPoint2Utils.ZERO);

        entities.addAll(List.of(leftWall, rightWall, topWall, bottomWall));
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

    protected void spawnDoors(GameArea area, Entity player) {
        if (this.roomConnections == null || this.roomConnections.size() < 4) {
            throw new IllegalStateException("Room connections are not properly initialized.");
        }

        List<String> connections = this.roomConnections;
        String connectS = connections.get(0);
        String connectW = connections.get(1);
        String connectE = connections.get(2);
        String connectN = connections.get(3);
        logger.info("[{}, {}, {}, {}]", connectN, connectE, connectW, connectS);
        Entity[] doors = {
                doorFactory.create('h', player.getId(), connectS),
                doorFactory.create('v', player.getId(), connectW),
                doorFactory.create('v', player.getId(), connectE),
                doorFactory.create('h', player.getId(), connectN)
        };
        logger.info("doors created");

        Vector2 doorvScale = doors[1].getScale();
        Vector2 doorhScale = doors[0].getScale();

        GridPoint2[] positions = {
                new GridPoint2(7, 0),
                new GridPoint2(15, 5),
                new GridPoint2(0, 5),
                new GridPoint2(7, 11)
        };

        Vector2[] offsets = {
                new Vector2(0, -doorhScale.y),
                new Vector2(-2 * doorvScale.x, 0),
                new Vector2(-doorvScale.x, 0),
                new Vector2(0, -2 * doorhScale.y)
        };

        for (int i = 0; i < doors.length; i++) {
            String connection = connections.get(i);
            if (connection == null || connection.isEmpty()) {
                logger.info("Skipping door placement for connection: {}", connection);
                continue;
            }

            area.spawnEntityAt(doors[i], positions[i], true, true);
            Vector2 doorPos = doors[i].getPosition();
            doors[i].setPosition(doorPos.x + offsets[i].x, doorPos.y + offsets[i].y);
            entities.add(doors[i]);
        }
    }

    protected void spawnAnimalEntity(MainGameArea area, Entity enemy, GridPoint2 position) {
        area.spawnEntityAt(enemy, position, true, true);
    }

    protected void spawnItem(MainGameArea area, String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        entities.add(item);
        area.spawnEntityAt(item, pos, true, true);
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
     * Sets the room as completed when loading the map
     */
    public void setIsRoomComplete() {
        this.isRoomCompleted = true;
    }

    public void spawn(Entity player, MainGameArea area) {
        ServiceLocator.getPhysicsService().getPhysics().update();
        logger.info("spawning terrain");
        this.spawnTerrain(area, WALL_THICKNESS, false);

        logger.info("spawning doors");
        this.spawnDoors(area, player);
    }

    public void removeRoom() {
        List<String> entityNames = ServiceLocator.getEntityService().getEntityNames();
        logger.info("Removing room, {} Entities\n{}", entityNames.size(), String.join("\n", entityNames));

        for (Entity entity : entities) {
            ServiceLocator.getEntityService().markEntityForRemoval(entity);
        }
        entities.clear();
    }
}
