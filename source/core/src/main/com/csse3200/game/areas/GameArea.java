package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea extends LoadedFactory {
    private static final Logger log = LoggerFactory.getLogger(GameArea.class);
    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;
    private static final String BACKGROUND_MUSIC = "sounds/BGM_03_mp3.mp3";

    protected GameArea() {
        super();
        areaEntities = new ArrayList<>();
    }

    /**
     * Create the game area in the world.
     */
    public abstract void create();

    /**
     * Dispose of all internal entities in the area
     */
    public void dispose() {
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
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
                }
            }
        }
    }

    public void setTerrain(TerrainComponent terrain) {
        this.terrain = terrain;
    }

    /**
     * Get a list of all entities in the area. This list is a copy and can be modified.
     * @return list of entities
     */
    public CopyOnWriteArrayList<Entity> getListOfEntities() {
        CopyOnWriteArrayList<Entity> newAreaEntities = new CopyOnWriteArrayList<>(areaEntities);
        return newAreaEntities;
    }

    //stuff moved from maingamearea??

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

}
