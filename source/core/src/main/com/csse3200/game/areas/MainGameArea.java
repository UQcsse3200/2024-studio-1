package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.minimap.MinimapComponent;
import com.csse3200.game.areas.minimap.MinimapFactory;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.configs.PlayerLocationConfig;
import com.csse3200.game.components.player.inventory.*;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class MainGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(MainGameArea.class);
    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";
    public static final String PLAYER_SAVE_PATH = "saves/PlayerLocationSave.json";
    public static final String MAP_SAVE_PATH = "saves/MapSave.json";

    /** The main player entity */
    private final Entity player;

    /** Factory for creating game levels */
    private final LevelFactory levelFactory;

    /** Current level of the game */
    private Level currentLevel;

    /** Current level number */
    private int currentLevelNumber;

    /** Current room the player is in */
    private Room currentRoom;

    /** Flag to determine if a new room should be spawned */
    private boolean spawnRoom = true;

    /** Name of the current room */
    public String currentRoomName;

    /** Map to store the current position of the player */
    private Map <String, String> currentPosition = new HashMap<>();

    /** Flag to determine if the game should load a saved state */
    private final boolean shouldLoad;

    /** Factory for creating minimaps */
    private MinimapFactory minimapFactory;

    /**
     * Initialise this Game Area to use the provided levelFactory.
     *
     * @param levelFactory the provided levelFactory.
     * @param player the main player entity.
     * @param shouldLoad flag to determine if a saved state should be loaded.
     */
    public MainGameArea(LevelFactory levelFactory, Entity player, boolean shouldLoad) {
        super();
        this.player = player;
        this.levelFactory = levelFactory;
        this.shouldLoad = shouldLoad;
        player.getEvents().addListener("teleportToBoss", () -> this.changeRooms(getFlaggedRoom("Boss")));
        player.getEvents().addListener("saveMapLocation", this::saveMapLocation);
        player.getEvents().addListener("saveMapData", this::saveMapData);
        player.getEvents().addListener("checkAnimalsDead", () -> this.getCurrentRoom().checkIfRoomComplete());
        ServiceLocator.registerGameAreaService(new GameAreaService(this));
        create();
    }

    /**
     * Initialise this Game Area to use the provided levelFactory without loading a saved state.
     *
     * @param levelFactory the provided levelFactory.
     * @param player the main player entity.
     */
    public MainGameArea(LevelFactory levelFactory, Entity player) {
        this(levelFactory, player, false);
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    public void create() {
        load(logger);
        logger.error("loaded all assets");

        if (shouldLoad) {
            loadMapLocation();

        } else {
            changeLevel(0);
        }
        playMusic();
    }

    /**
    * Get the room key for a specified flagged room type.
    *
    * @param roomType The type of room to get the key for. Valid values are "Boss", "NPC", and "GameRoom".
    * @return The room key for the specified room type, or null if the room type is invalid.
    * @throws IllegalArgumentException if an invalid room type is provided.
    */
    public String getFlaggedRoom(String roomType) {
        if (!List.of("Boss", "NPC", "GameRoom").contains(roomType)) {
            throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
        return currentLevel.getMap().mapData.RoomKeys.get(roomType);
    }

    /**
     * Get the main player of this game area.
     *
     * @return the player entity.
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Get the current room the player is in.
     *
     * @return the current Room object.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Exports the current Level number and Room number of the player
     * as well as the complete map details of the current map generated into a JSON file
     * which can then be loaded and set as the starting position of the player when player
     * loads the game.
     */
    public void saveMapLocation() {
        String levelNum = "" + currentLevelNumber;
        currentPosition.put("LevelNum", levelNum);
        currentPosition.put("RoomNum", currentRoomName);
        //exports the current player location (room and level details into a json).
        System.out.println("roomComplete?:" + currentRoom.getIsRoomComplete());
        FileLoader.writeClass(currentPosition, PLAYER_SAVE_PATH, FileLoader.Location.EXTERNAL);
    }

    /**
     * Loads the saved Level number and Room number of the player
     */
    public void loadMapLocation() {
        PlayerLocationConfig playerLocationConfig = new PlayerLocationConfig();
        playerLocationConfig.savedLoc = FileLoader.readClass(HashMap.class, PLAYER_SAVE_PATH, FileLoader.Location.EXTERNAL);
        changeLevel(Integer.parseInt(playerLocationConfig.savedLoc.get("LevelNum")));
        changeRooms(playerLocationConfig.savedLoc.get("RoomNum"));
    }

    /**
     * Uses MainGameLevelFactory to save all the completed room numbers and the seed of the map as JSON file
     * which can be loaded when load button is pressed.
     */
    public void saveMapData() {
        //exports the rooms and map data into the filePath below after Save button is pressed
        levelFactory.exportToJson(MAP_SAVE_PATH);
    }

    /**
     * Selects a new room based on the given room key.
     *
     * @param roomKey the key of the room to select.
     */
    private void selectRoom(String roomKey) {
        logger.info("Changing to room: {}", roomKey);
        Room newRoom = this.currentLevel.getRoom(roomKey);
        if (newRoom == null) {
            logger.error("Room \"{}\" not found!", roomKey);
            return;
        }
        this.currentRoom = newRoom;
        this.currentRoomName = this.currentRoom.getRoomName();

        this.spawnRoom = true;
    }

    /**
     * Changes the current room to a new room specified by the room key.
     *
     * @param roomKey the key of the room to change to.
     */
    public void changeRooms(String roomKey) {
        this.currentRoom.removeRoom();
        selectRoom(roomKey);
        // update minimap
        minimapFactory.updateMinimap(roomKey);
    }

    /**
     * Generate the corresponding player position to the door they stepped in
     */
    private Vector2 getNewPlayerPosition(Entity player) {
        Map<Vector2, Vector2> doorPositions = new HashMap<>();
        doorPositions.put(new Vector2(0, 5), new Vector2(13, 5));
        doorPositions.put(new Vector2(14, 5), new Vector2(1, 5));
        doorPositions.put(new Vector2(7, 0), new Vector2(7, 9));
        doorPositions.put(new Vector2(7, 10), new Vector2(7, 1));
    
        Vector2 curPos = new Vector2(
            Math.round(player.getPosition().x),
            Math.round(player.getPosition().y)
        );
        
        return doorPositions.getOrDefault(curPos, new Vector2(7, 5));
    }

    /**
     * Spawns the current room if it hasn't been spawned yet.
     */
    public void spawnCurrentRoom() {
        if (!spawnRoom) {
            return;
        }

        if (ServiceLocator.getPhysicsService().getPhysics().getWorld().isLocked()) {
            logger.error("Physics is locked!");
            return;
        }

        logger.info("Spawning new room, {}", ServiceLocator.getEntityService());
        this.currentRoom.spawn(player, this);

        Vector2 nextRoomPos = this.getNewPlayerPosition(player);
        player.setPosition(nextRoomPos.x,nextRoomPos.y);
        spawnEntity(player);

        if(player.getComponent(InventoryComponent.class).getInventory().petsExist()){
            //do here
            if (ServiceLocator.getGameAreaService().getGameArea().getCurrentRoom() instanceof EnemyRoom room) {
                List<Entity> enemies = room.getEnemies();
                player.getComponent(InventoryComponent.class).getInventory().initialisePetAggro(enemies); 
            }
            else{
                //do nothing I guess and pray
            }
        } 

        logger.info("Spawned new room, {}", ServiceLocator.getEntityService());
        spawnRoom = false;
    }

    /**
     * Changes the current level to a new level specified by the level number.
     *
     * @param levelNumber the number of the level to change to.
     */
    public void changeLevel(int levelNumber) {
        logger.info("Changing to level: {}", levelNumber);
        currentLevelNumber = levelNumber;

        // Create and load the new level
        this.currentLevel = this.levelFactory.create(levelNumber);
        selectRoom(this.currentLevel.getStartingRoomKey());

        // initialize minimap
        this.minimapFactory = new MinimapFactory(getCurrentLevel(), 0.5f);
        MinimapComponent minimapComponent = minimapFactory.createMinimap();

        Entity minimap = new Entity();
        minimap.addComponent(minimapComponent);
        spawnEntity(minimap);
    }

    /**
     * Gets the current level.
     *
     * @return the current Level object.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }
}