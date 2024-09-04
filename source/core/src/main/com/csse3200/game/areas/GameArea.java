package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.LoadedFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea extends LoadedFactory {
    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;

    protected GameArea() {
        super();
        areaEntities = new ArrayList<>();
        ServiceLocator.registerGameAreaService(new GameAreaService(this));
    }

    /**
     * Create the game area in the world.
     */
    public abstract void create(Entity player);

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

    public void remove_room() {
        if (areaEntities != null && !areaEntities.isEmpty()) {
            for (Entity entity : areaEntities) {
                ServiceLocator.getEntityService().unregister(entity);
                ServiceLocator.getEntityService().markEntityForRemoval(entity);
            }
        }
    }

    public void disposeEntity(Entity entity) {
        if (areaEntities != null && !areaEntities.isEmpty()) {
            for (int i = 0; i < areaEntities.size(); i++) {
                if (areaEntities.get(i).equals(entity)) {
                    ServiceLocator.getEntityService().unregister(entity);
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
}
