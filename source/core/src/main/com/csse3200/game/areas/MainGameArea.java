package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class MainGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(MainGameArea.class);
    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";

    private final Entity player;

    private final LevelFactory levelFactory;
    private Level currentLevel;
    private Room currentRoom;
    private boolean spawnRoom = true;
    private final List<Room> roomsVisited = new ArrayList<>();

    /**
     * Initialise this Game Area to use the provided levelFactory.
     *
     * @param levelFactory the provided levelFactory.
     */
    public MainGameArea(LevelFactory levelFactory, Entity player) {
        super();
        this.player = player;
        this.levelFactory = levelFactory;
        player.getEvents().addListener("teleportToBoss", () -> this.changeRooms("BOSS"));
        ServiceLocator.registerGameAreaService(new GameAreaService(this));
        create();
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        load(logger);
        logger.error("loaded all assets");

        displayUI();

        changeLevel(0);

        playMusic();
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

    private void selectRoom(String roomKey) {
        logger.info("Changing to room: {}", roomKey);
        Room newRoom = this.currentLevel.getRoom(roomKey);
        if (newRoom == null) {
            logger.error("Room \"{}\" not found!", roomKey);
            return;
        }
        this.currentRoom = newRoom;
        this.spawnRoom = true;
    }


    public void changeRooms(String roomKey) {
        this.currentRoom.removeRoom();
        selectRoom(roomKey);

        if (!this.currentRoom.getIsRoomComplete()) {
            this.currentLevel.roomTraversals++;
        }
    }

    public void spawnCurrentRoom() {
        if (!spawnRoom) {
            return;
        }
        var entityNames = ServiceLocator.getEntityService().getEntityNames();
        logger.info("Spawning new room, {} Entities\n{}", entityNames.size(), String.join("\n", entityNames));
        if (currentLevel.roomTraversals == 8 ) {
            this.currentRoom = currentLevel.getRoom("BOSS");
        }
        this.currentRoom.spawn(player, this);

        player.setPosition(7, 5);
        spawnEntity(player);

        entityNames = ServiceLocator.getEntityService().getEntityNames();
        logger.info("Spawned new room, {} Entities\n{}", entityNames.size(), String.join("\n", entityNames));
        spawnRoom = false;
    }

    public void changeLevel(int levelNumber) {
        logger.info("Changing to level: {}", levelNumber);

        // TODO: Save player progress or game state here, create a save manager

        // Create and load the new level
        this.currentLevel = this.levelFactory.create(levelNumber);
        selectRoom(this.currentLevel.getStartingRoomKey());
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
                "images/tile_1.png",
                "images/tile_2.png",
                "images/tile_3.png",
                "images/tile_4.png",
                "images/tile_5.png",
                "images/tile_6.png",
                "images/tile_7.png",
                "images/tile_8.png",
                "images/tile_middle.png",
                "images/tile_general.png",
                "images/tile_broken1.png",
                "images/tile_broken2.png",
                "images/tile_broken3.png",
                "images/tile_staircase.png",
                "images/tile_staircase_down.png",
                "images/tile_blood.png",
                "images/rounded_door_v.png",
                "images/rounded_door_h.png",
                "images/staircase.png"
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
