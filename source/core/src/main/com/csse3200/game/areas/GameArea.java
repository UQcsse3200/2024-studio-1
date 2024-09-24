package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.NPCConfigComponent;
import com.csse3200.game.components.npc.attack.AttackComponent;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.components.tasks.BossAttackTask;
import com.csse3200.game.components.tasks.ChargeTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.LoadedFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList; // this list is thread safe
import java.util.List;

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

    /**
     * Updates the enemy entities in the current area by adding tasks for the newly specified target.
     * The tasks are determined based on the NPC configuration of each entity.
     * @param target the target entity that enemies will attack
     */
    public void updateEnemyTargets(Entity target) {
        for (Entity entity : areaEntities) {
            if (entity.getComponent(AITaskComponent.class) != null) {
                NPCConfigs.NPCConfig config = entity.getComponent(NPCConfigComponent.class).config;
                NPCConfigs.NPCConfig.TaskConfig tasks = config.tasks;

                // Add chase task
                if (tasks.chase != null) {
                    entity.getComponent(AITaskComponent.class).addTask(new ChaseTask(target, tasks.chase));
                }
                // Add charge task
                if (tasks.charge != null) {
                    entity.getComponent(AITaskComponent.class).addTask(new ChargeTask(target, tasks.charge));
                }

                entity.getComponent(AITaskComponent.class).update();
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
}
