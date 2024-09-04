package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Room;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.areas.GameAreaService;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class MainGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(MainGameArea.class);
    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";

    private Entity player;

    private final LevelFactory levelFactory;
    private Level currentLevel;
    private Room currentRoom;

    /**
     * Initialise this Game Area to use the provided levelFactory.
     *
     * @param levelFactory the provided levelFactory.
     */
    public MainGameArea(LevelFactory levelFactory) {
        super();
        this.levelFactory = levelFactory;
        ServiceLocator.registerGameAreaService(new GameAreaService(this));
        
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create(Entity player) {
        this.player = player;

        load(logger);
        logger.error("loaded all assets");

        displayUI();

        changeLevel(0);

        playMusic();
    }

    public void changeRooms(String roomKey){
        this.remove_room();
        //ServiceLocator.getPhysicsService().getPhysics().destroyAllBodies();
        this.currentRoom = this.currentLevel.getRoom(roomKey);    
        this.currentRoom.spawn(player, this);
        spawnEntityAt(player, new GridPoint2(10, 10), true, true);
    }

    public void changeLevel(int levelNumber){
        this.currentLevel = this.levelFactory.create(levelNumber);
        this.currentRoom = this.currentLevel.getRoom(this.currentLevel.getStartingRoomKey());    
        this.currentRoom.spawn(player, this);
        spawnEntityAt(player, new GridPoint2(10, 10), true, true);
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("BEAST BREAKOUT FACILITY"));
        spawnEntity(ui);
    }

    private void playMusic() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (!resourceService.containsAsset(BACKGROUND_MUSIC, Music.class)){
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
        return new String[]{
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
                "images/rounded_door_h.png"
        };
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
