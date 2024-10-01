package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.minimap.MinimapComponent;
import com.csse3200.game.areas.minimap.MinimapFactory;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.configs.PlayerLocationConfig;
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

    private final Entity player;

    private final LevelFactory levelFactory;
    private Level currentLevel;
    private int currentLevelNumber;
    private Room currentRoom;
    private boolean spawnRoom = true;
    public String currentRoomName;
    private Map <String, String> currentPosition = new HashMap<>();
    private final boolean shouldLoad;
    private MinimapFactory minimapFactory;
    

    /**
     * Initialise this Game Area to use the provided levelFactory.
     *
     * @param levelFactory the provided levelFactory.
     *
     */
    public MainGameArea(LevelFactory levelFactory, Entity player, boolean shouldLoad) {
        super();
        this.player = player;
        this.levelFactory = levelFactory;
        this.shouldLoad = shouldLoad;
        player.getEvents().addListener("teleportToBoss", () -> this.changeRooms(getBossRoom()));
        player.getEvents().addListener("saveMapLocation", this::saveMapLocation);
        player.getEvents().addListener("saveMapData", this::saveMapData);

        player.getEvents().addListener("checkAnimalsDead", () -> this.getCurrentRoom().checkIfRoomComplete());
        ServiceLocator.registerGameAreaService(new GameAreaService(this));
        create();
    }

    public MainGameArea(LevelFactory levelFactory, Entity player) {
        this(levelFactory, player, false);
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
            loadMapLocation();

        } else {
            changeLevel(0);
        }
        playMusic();
    }

    public String getBossRoom() {
        return currentLevel.getMap().mapData.RoomKeys.get("Boss");
    }

    public String getNpcRoom() {
        return currentLevel.getMap().mapData.RoomKeys.get("NPC");
    }

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
        FileLoader.writeClass(currentPosition, PLAYER_SAVE_PATH, FileLoader.Location.LOCAL);
    }

    public void loadMapLocation() {
        PlayerLocationConfig playerLocationConfig = new PlayerLocationConfig();
        playerLocationConfig.savedLoc = FileLoader.readClass(HashMap.class, PLAYER_SAVE_PATH, FileLoader.Location.LOCAL);
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


    public void changeRooms(String roomKey) {
        this.currentRoom.removeRoom();
        selectRoom(roomKey);
        // update minimap
        minimapFactory.updateMinimap(roomKey);
    }

    public void spawnCurrentRoom() {
        if (!spawnRoom) {
            return;
        }

        if (ServiceLocator.getPhysicsService().getPhysics().getWorld().isLocked()){
            logger.error("Physics is locked!");
            return;
        }

        logger.info("Spawning new room, {}", ServiceLocator.getEntityService());
        this.currentRoom.spawn(player, this);

        player.setPosition(7, 5);
        spawnEntity(player);

        logger.info("Spawned new room, {}", ServiceLocator.getEntityService());
        spawnRoom = false;
    }

    public void changeLevel(int levelNumber) {
        logger.info("Changing to level: {}", levelNumber);
        currentLevelNumber = levelNumber;

        // TODO: Save player progress or game state here, create a save manager

        // Create and load the new level
        this.currentLevel = this.levelFactory.create(levelNumber);
        selectRoom(this.currentLevel.getStartingRoomKey());

        // // initialize minimap

        this.minimapFactory = new MinimapFactory(getCurrentLevel(), 0.5f);
        MinimapComponent minimapComponent = minimapFactory.createMinimap();

        Entity minimap = new Entity();
        minimap.addComponent(minimapComponent);
        spawnEntity(minimap);

    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("BEAST BREAKOUT FACILITY"));
        ui.addComponent(new NameComponent("Main Game Area UI"));
        spawnEntity(ui);
    }

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

    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    protected String[] getSoundFilepaths() {
        return new String[]{
                "sounds/Impact4.ogg"
        };
    }

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
     *         - Common textures (player, doors, etc.)
     *         - Tile textures for levels 1-3
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

    @Override
    protected String[] getMusicFilepaths() {
        return new String[]{BACKGROUND_MUSIC};
    }

    @Override
    public void dispose() {
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
        super.dispose();
    }
}
