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
import com.csse3200.game.entities.configs.MapLoadConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class MainGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(MainGameArea.class);
//    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";
    private static final String BACKGROUND_MUSIC = "sounds/bgm_beast_breakout.wav";
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

    private MapLoadConfig config;

    /**
     * Initialise this Game Area to use the provided levelFactory.
     *
     * @param levelFactory the provided levelFactory.
     * @param player the main player entity.
     * @param shouldLoad flag to determine if a saved state should be loaded.
     */
    public MainGameArea(LevelFactory levelFactory, Entity player, boolean shouldLoad, MapLoadConfig config) {
        super();
        if(config != null){
            this.config = config;
        }
        this.player = player;
        this.levelFactory = levelFactory;
        this.shouldLoad = shouldLoad;
        player.getEvents().addListener("teleportToBoss", () -> this.changeRooms(getBossRoom()));
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
        this(levelFactory, player, false, new MapLoadConfig());
    }

    /**
     * Initialise this Game Area to use the provided levelFactory without loading a saved state.
     *
     * @param levelFactory the provided levelFactory.
     * @param shouldLoad if the game should be loaded
     * @param player the main player entity.
     */
    public MainGameArea(LevelFactory levelFactory, Entity player, Boolean shouldLoad) {
        this(levelFactory, player, false, new MapLoadConfig());
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        load(logger);
        logger.error("loaded all assets");

        displayUI();
        if (shouldLoad) {
            changeLevel(Integer.parseInt(config.currentLevel));
            changeRooms(config.currentRoom);

        } else {
            changeLevel(0);
        }
        playMusic();
    }

    /**
     * Get the room key for the boss room.
     *
     * @return the room key for the boss room.
     */
    public String getBossRoom() {
        return currentLevel.getMap().mapData.RoomKeys.get("Boss");
    }

    /**
     * Get the room key for the NPC room.
     *
     * @return the room key for the NPC room.
     */
    public String getNpcRoom() {
        return currentLevel.getMap().mapData.RoomKeys.get("NPC");
    }

    /**
     * Get the room key for the game room.
     *
     * @return the room key for the game room.
     */
    public String getGameRoom() {
        return currentLevel.getMap().mapData.RoomKeys.get("GameRoom");
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
     * Uses MainGameLevelFactory to save all the completed room numbers and the seed of the map as JSON file
     * which can be loaded when load button is pressed.
     */
    public void saveMapData() {
        //exports the rooms and map data into the filePath below after Save button is pressed
        String levelNum = "" + currentLevelNumber;
        levelFactory.saveMapData(MAP_SAVE_PATH, levelNum);
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
    private Vector2 getNewPlayerPosition(Entity player){
        Vector2 curPos = player.getPosition();
        curPos.x = Math.round(curPos.x);
        curPos.y = Math.round(curPos.y);
        if(curPos.x == 0.0f && curPos.y == 5.0f){
            return new Vector2(13,5);
        }
        if(curPos.x == 14.0f && curPos.y == 5.0f){
            return new Vector2(1,5);
        }
        if(curPos.x == 7.0f && curPos.y == 0.0f){
            return new Vector2(7,9);
        }
        if(curPos.x == 7.0f && curPos.y == 10.0f){
            return new Vector2(7,1);
        }
        return new Vector2(7,5);
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
     * Displays the user interface for the game area.
     */
    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("BEAST BREAKOUT FACILITY"));
        ui.addComponent(new NameComponent("Main Game Area UI"));
        spawnEntity(ui);
    }

    /**
     * Plays the background music for the game area.
     */
    private void playMusic() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (!resourceService.containsAsset(BACKGROUND_MUSIC, Music.class)) {
            logger.error("Music not loaded");
            return;
        }
        Music music = resourceService.getAsset(BACKGROUND_MUSIC, Music.class);
        music.setLooping(true);
        if (!UserSettings.get().mute) {
            music.setVolume(UserSettings.get().musicVolume);
        } else {
            music.setVolume(0);
        }
        music.play();
    }

    /**
     * Gets the current level.
     *
     * @return the current Level object.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets the file paths for all sound effects used in the game area.
     *
     * @return An array of String paths for sound effects.
     */
    @Override
    protected String[] getSoundFilepaths() {
        return new String[]{
                "sounds/Impact4.ogg"
        };
    }

    /**
     * Gets the file paths for all texture atlases used in the game area.
     *
     * @return An array of String paths for texture atlases.
     */
    @Override
    protected String[] getTextureAtlasFilepaths() {
        return new String[]{
                "images/terrain_iso_grass.atlas",
                "skins/levels/level1/level1_skin.atlas",
                "skins/levels/level2/level2_skin.atlas",
                "skins/levels/level3/level3_skin.atlas",
                "skins/minimap/minimap.atlas"
        };
    }

    /**
     * Gets the file paths for all textures used in the game area.
     *
     * @return An array of String paths for textures, including:
     * - Common textures (player, doors, etc.)
     * - Tile textures for levels 1-3
     */
    @Override
    protected String[] getTextureFilepaths() {
        List<String> filepaths = new ArrayList<>();
        String[] commonTextures = {
                "images/box_boy_leaf.png",
                "images/rounded_door_v.png",
                "images/rounded_door_h.png",
                "images/staircase.png",
                "skins/levels/level1/level1_skin.png",
                "skins/levels/level2/level2_skin.png",
                "skins/levels/level3/level3_skin.png",
                "skins/minimap/minimap.png"
        };
        Collections.addAll(filepaths, commonTextures);

        for (int level = 1; level <= 3; level++) {
            filepaths.add("images/tile_1_level" + level + ".png");
            filepaths.add("images/tile_2_level" + level + ".png");
            filepaths.add("images/tile_3_level" + level + ".png");
            filepaths.add("images/tile_4_level" + level + ".png");
            filepaths.add("images/tile_5_level" + level + ".png");
            filepaths.add("images/tile_6_level" + level + ".png");
            filepaths.add("images/tile_7_level" + level + ".png");
            filepaths.add("images/tile_8_level" + level + ".png");
            filepaths.add("images/tile_middle_level" + level + ".png");
            filepaths.add("images/tile_broken1_level" + level + ".png");
            filepaths.add("images/tile_broken2_level" + level + ".png");
            filepaths.add("images/tile_broken3_level" + level + ".png");
            filepaths.add("images/tile_blood_level" + level + ".png");
            filepaths.add("images/tile_staircase_level" + level + ".png");
        }

        // Convert the list to an array and return
        return filepaths.toArray(new String[0]);
    }

    /**
     * Gets the file paths for all music files used in the game area.
     *
     * @return An array of String paths for music files.
     */
    @Override
    protected String[] getMusicFilepaths() {
        return new String[]{BACKGROUND_MUSIC};
    }

    /**
     * Disposes of the game area resources, including stopping the background music.
     */
    @Override
    public void dispose() {
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
        super.dispose();
    }
}
