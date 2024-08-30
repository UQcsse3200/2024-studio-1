package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.Generation.MapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class MainGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(MainGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);

    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";
    private final MainRoomSpawner entitySpawner;

    /**
     * Initialise this ForestGameArea to use the provided TerrainFactory.
     *
     * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
     * @requires terrainFactory != null
     */
    public MainGameArea(TerrainFactory terrainFactory,
                        PlayerFactory playerFactory,
                        NPCFactory npcFactory,
                        CollectibleFactory collectibleFactory) {
        this(terrainFactory, playerFactory, npcFactory, collectibleFactory, 1, "1234");
    }

    public MainGameArea(
            TerrainFactory terrainFactory,
            PlayerFactory playerFactory,
            NPCFactory npcFactory,
            CollectibleFactory collectibleFactory,
            int difficulty,
            String seed) {
        this.entitySpawner = new MainRoomSpawner(this, npcFactory, collectibleFactory, playerFactory, terrainFactory);
        MapGenerator mapGenerator = new MapGenerator(difficulty * 12, seed);
        mapGenerator.createMap();

        logger.info(mapGenerator.printRelativePosition());
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        load(logger);
        logger.error("loaded all assets");
        displayUI();
        Entity player = this.entitySpawner.spawnPlayer(PLAYER_SPAWN);
        this.entitySpawner.spawnRoom(player, "0,0,14,10,0,0");

        playMusic();
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
                "images/rounded_door_v.png",
                "images/rounded_door_h.png",
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
                "images/Weapons/Shotgun.png",
                "images/Weapons/pickaxe.png",
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
