package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.LoadedFactory;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public class GameArea extends LoadedFactory {
    private static final Logger log = LoggerFactory.getLogger(GameArea.class);
    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;
    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";
    private Music normalMusic;
    private Music bossMusic;

    public GameArea() {
        super();
        areaEntities = new ArrayList<>();
    }

    /**
     * Dispose of all internal entities in the area
     */
    @Override
    public void dispose() {
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
        super.dispose();
    }

    /**
     * Displays the user interface for the game area.
     */
    protected void displayUI(String areaName) {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay(areaName));
        ui.addComponent(new NameComponent(areaName + " UI"));
        spawnEntity(ui);
    }

    /**
     * Spawn entity at its current position
     *
     * @param entity Entity (not yet registered)
     */
    public void spawnEntity(Entity entity) {
        areaEntities.add(entity);
        ServiceLocator.getEntityService().register(entity);
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity  Entity (not yet registered)
     * @param tilePos tile position to spawn at
     * @param centerX true to center entity X on the tile, false to align the bottom left corner
     * @param centerY true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
        float tileSize = terrain.getTileSize();

        if (centerX) {
            worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(worldPos);
        spawnEntity(entity);
    }

    public void disposeEntity(Entity entity) {
        if (areaEntities != null && !areaEntities.isEmpty()) {
            for (int i = 0; i < areaEntities.size(); i++) {
                if (areaEntities.get(i).equals(entity)) {
                    ServiceLocator.getEntityService().markEntityForRemoval(entity);
                    areaEntities.remove(i);
                    break;
                }}}}

    public void setTerrain(TerrainComponent terrain) {
        this.terrain = terrain;
    }

    public enum MusicType {
        NORMAL("bgm_new.wav"), BOSS("boss_room_music.mp3");

        private final String path;
        private static final String BASE = "sounds/music/";

        MusicType(String filename) {
            this.path = BASE + filename;
        }
    }

    /**
     * Plays the specified type of music.
     *
     * @param musicType type of music to play (normal/boss).
     */
    public void playMusic(MusicType musicType) {
        Music newMusic = ServiceLocator.getResourceService().playMusic(musicType.path, true);
        if (musicType == MusicType.BOSS) {
            bossMusic = newMusic;
        } else {
            normalMusic = newMusic;
        }
    }

    /**
     * Stops the currently playing music.
     */
    private void stopCurrentMusic() {
        if (normalMusic != null) {
            normalMusic.stop();
        }
        if (bossMusic != null) {
            bossMusic.stop();
        }
    }

    /**
     * Get a list of all entities in the area. This list is a copy and can be modified.
     * @return list of entities
     */
    public CopyOnWriteArrayList<Entity> getListOfEntities() {
        CopyOnWriteArrayList<Entity> newAreaEntities = new CopyOnWriteArrayList<>(areaEntities);
        return newAreaEntities;
    }

    /**
     * Plays the background music for the game area.
     */
    public void playMusic() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (!resourceService.containsAsset(BACKGROUND_MUSIC, Music.class)) {
            log.error("Music not loaded");
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


    protected void playBackgroundMusic() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        if (!resourceService.containsAsset(BACKGROUND_MUSIC, Music.class)) {
            log.error("Music not loaded");
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

    protected void stopBackgroundMusic() {
        ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    }

    // Asset-related methods moved from MainGameArea

    /**
     * Gets the file paths for all sound effects used in the game area.
     *
     * @return An array of String paths for sound effects.
     */
    @Override
    public String[] getSoundFilepaths() {
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
    public String[] getTextureAtlasFilepaths() {
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
    public String[] getTextureFilepaths() {
        List<String> filepaths = new ArrayList<>(Arrays.asList(
            "images/box_boy_leaf.png",
            "images/rounded_door_v.png",
            "images/rounded_door_h.png",
            "images/staircase.png",
            "skins/levels/level1/level1_skin.png",
            "skins/levels/level2/level2_skin.png",
            "skins/levels/level3/level3_skin.png",
            "skins/minimap/minimap.png"
        ));
    
        List<String> tileTypes = Arrays.asList(
            "tile_1_level", "tile_2_level", "tile_3_level", "tile_4_level",
            "tile_5_level", "tile_6_level", "tile_7_level", "tile_8_level",
            "tile_middle_level", "tile_broken1_level", "tile_broken2_level",
            "tile_broken3_level", "tile_blood_level", "tile_staircase_level"
        );
    
        for (int level = 1; level <= 3; level++) {
            for (String tileType : tileTypes) {
                filepaths.add("images/" + tileType + level + ".png");
            }
        }
    
        return filepaths.toArray(new String[0]);
    }

    /**
     * Gets the file paths for all music files used in the game area.
     *
     * @return An array of String paths for music files.
     */
    @Override
    public String[] getMusicFilepaths() {
        return new String[]{BACKGROUND_MUSIC};
    }

}
