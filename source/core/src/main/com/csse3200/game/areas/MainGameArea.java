package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
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

    public boolean isRoomFresh(Room currentRoom) {
        if (roomsVisited.contains(currentRoom)) {
            return false;
        }
        roomsVisited.add(currentRoom);
        return true;
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
        logger.info("spawning: new room");
        if (currentLevel.roomTraversals == 8) {
            this.currentRoom = currentLevel.getRoom("BOSS");
        }
        this.currentRoom.spawn(player, this);
        logger.info("spawned: new room");
        logger.info("spawning: player");

        // int player_x = (int) (15 - player.getPosition().x);
        // int player_y = (int) (9 - player.getPosition().y);


        int player_x = 7;
        int player_y = 5;

        player.setPosition(player_x, player_y);
        spawnEntity(player);
        logger.info("spawned: player");
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
                "images/ghost.atlas",
                "images/ghostKing.atlas"
        };
    }

    @Override
    protected String[] getTextureFilepaths() {

        List<String> filepaths = new ArrayList<>();
        String[] commonTextures = {
                "images/box_boy_leaf.png",
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
