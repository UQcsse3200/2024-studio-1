package com.csse3200.game.areas;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.TaskRunner;
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
 * This is the foundation of a room,
 * it is able to use other factories to build the complex structures needed for a room in the game.
 * e.g. walls, terrain, items, and enemies.
 */
public abstract class BaseRoom implements Room {
    private static final Logger logger = LoggerFactory.getLogger(BaseRoom.class);
    private final NPCFactory npcFactory;
    private final CollectibleFactory collectibleFactory;
    private final TerrainFactory terrainFactory;
    private List<String> roomConnections;
    protected List<Entity> doors;
    protected List<Entity> enemies;
    protected List<Entity> items;

    protected final String specification;
    protected final GridPoint2 minGridPoint;
    protected final GridPoint2 maxGridPoint;
    protected final int animalGroup;
    protected final int itemGroup;

    List<List<String>> animalSpecifications;
    List<List<String>> itemSpecifications;
    public Boolean isRoomFresh = true;
    protected Boolean isBossRoom = false;
    private boolean isRoomCompleted = false;

    private static final float WALL_THICKNESS = 0.15f;

    /**
     * Inject factories to be used for spawning here room object.
     *
     * @param npcFactory the NPC factory to use
     * @param collectibleFactory the Collectible factory to use.
     * @param terrainFactory  the terrain factory to use.
     * @param roomConnections  the keys for all the adjacent rooms.
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

    @SuppressWarnings("unused")
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

    /**
     * Mark all entities in the room for removal.
     */
    public void removeRoom() {
        for (Entity data : doors) {
            ServiceLocator.getEntityService().markEntityForRemoval(data);
        } 
        
        // for (Entity data : enemies) {
        //     ServiceLocator.getEntityService().markEntityForRemoval(data);
        // } 
        this.enemies.clear();
    }


    /**
     * Spawn the terrain of the room, including the walls and background of the map.
     *
     * @param area the game area to spawn the terrain onto.
     * @param wallThickness the thickness of the walls around the room.
     */
    protected void spawnTerrain(GameArea area, float wallThickness, boolean isBossRoom) {
        // Background terrain
        TerrainComponent terrain = terrainFactory.createTerrain(TerrainFactory.TerrainType.ROOM1 ,isBossRoom);
        area.setTerrain(terrain);
        area.spawnEntity(new Entity().addComponent(terrain));
        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
        createWalls(area, wallThickness, tileBounds, worldBounds);
    }

    public void spawn(Entity player, MainGameArea area) {
        
        this.spawnTerrain(area, WALL_THICKNESS, isBossRoom);
        this.spawnDoors(area, player);
        if (!isRoomCompleted) {
            createEnemyEntities(this.animalSpecifications.get(this.animalGroup), player);
            this.spawnAnimals(area, player, this.minGridPoint, this.maxGridPoint);
        }
           
        // FIXME
        // logger.info("Spawning items:");
        // int itemGroup = Integer.parseInt(split.get(5));
        // for (String s : itemSpecifications.get(itemGroup)){
        //     GridPoint2 randomPos = RandomUtils.random(min, max);
        //     this.spawnItem(area, s, randomPos);
        // }
    }
    protected void makeAllAnimalDead(){
        for(Entity animal : enemies){
            CombatStatsComponent combatStatsComponent = animal.getComponent(CombatStatsComponent.class);
            combatStatsComponent.setHealth(0);
            combatStatsComponent.hit(combatStatsComponent);
        }
    }


    public boolean getIsRoomComplete() {
        return this.isRoomCompleted;
    }

    
    public boolean isAllAnimalDead(){
        if (enemies.isEmpty()) {
            return true;
        }
        for(Entity animal : enemies){
            if (!animal.getComponent(CombatStatsComponent.class).isDead())
                return false;
        }
        this.isRoomCompleted = true;
        return true;
    }

    /**
     * Spawn a collectible item into the room.
     * @param area the game area to spawn the item into.
     * @param specification the specification of the item to tete.
     * @param pos the location to spawn it to.
     */
    protected void spawnItem(MainGameArea area, String specification, GridPoint2 pos) {
        Entity item = collectibleFactory.createCollectibleEntity(specification);
        item.getEvents().addListener("pickedUp",()->{
            for (Entity curItem : this.items){
                if(curItem != item) {
                    ServiceLocator.getEntityService().unregister(curItem);
                    ServiceLocator.getEntityService().markEntityForRemoval(curItem);
                }
            }
        });
        this.items.add(item);
        area.spawnEntityAt(item, pos, true, true);
    }

    public void spawnItems() {
        MainGameArea area = ServiceLocator.getGameAreaService().getGameArea();
        spawnItem(area,this.itemSpecifications.get(this.itemGroup).get(0),new GridPoint2(8,8));
        spawnItem(area,this.itemSpecifications.get(this.itemGroup).get(1),new GridPoint2(6,8));
    }

    /**
     * Spawn an NPC into the room
     * @param area the game area to spawn the NPC into.
     * @param player the player character for this npc to target.
     * @param animal the specification of the animal to create.
     * @param pos    the location to spawn it to.
     * @param min the specification of the animal to create.
     * @param max the location to spawn it to.
     */
    protected void spawnAnimals(MainGameArea area, Entity player, GridPoint2 min, GridPoint2 max) {
        if(area.isRoomFresh(this)) {
            createEnemyEntities(this.animalSpecifications.get(this.animalGroup), ServiceLocator.getGameAreaService().getGameArea().player);
            for (Entity enemy : this.enemies) {


                GridPoint2 randomPos = new GridPoint2(ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.x, max.x + 1),
                        ServiceLocator.getRandomService().getRandomNumberGenerator(this.getClass()).getRandomInt(min.y, max.y + 1));


            area.spawnEntityAt(enemy, randomPos, true, true);
            enemy.getEvents().addListener("died",()->{
               if(this.isAllAnimalDead())
                   this.isRoomCompleted = true;
                   this.spawnItems();
            });
        }
        //this will make all animals commit suicide 
       makeAllAnimalDead();
    }}

    /**
     * Spawn the doors for this room.
     *
     * @param area   the game area to spawn them into.
     * @param player The main player of the room.
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
            System.out.println(connections);
            if (connection != "" && !connection.isEmpty() && connection != null) {
                area.spawnEntityAt(doors[i], positions[i], true, true);
                Vector2 doorPos = doors[i].getPosition();
                doors[i].setPosition(doorPos.x + offsets[i].x, doorPos.y + offsets[i].y);
                this.doors.add(doors[i]);
            } else {
                System.out.println("Skipping door placement for connection: " + connection);
            }
        }
    }
}
