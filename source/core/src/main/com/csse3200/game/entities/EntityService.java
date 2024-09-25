package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 * <p>
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
    private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
    private static final int INITIAL_CAPACITY = 16;

    private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);

    private final Array<Entity> entitiesToRemove = new Array<>();

    /**
     * Register a new entity with the entity service. The entity will be created and start updating.
     *
     * @param entity new entity.
     */
    public void register(Entity entity) {
        if (!entities.contains(entity, false)){
            logger.debug("Registering {} in entity service", entity);
            entities.add(entity);
            entity.create();
        }
    }

    /**
     * Unregister an entity with the entity service. The entity will be removed and stop updating.
     *
     * @param entity entity to be removed.
     */
    public void unregister(Entity entity) {
        logger.debug("Unregistering {} in entity service", entity);
        entities.removeValue(entity, true);
    }

    /**
     * Marks the specified entity to be removed on next update. If entity is already marked,
     * then it will not be marked for removal again
     *
     * @param entity the entity to be marked for removal
     */
    public void markEntityForRemoval(Entity entity) {
        if (entity == null){
            return;
        }

        if (!entities.contains(entity, true)){
            return;
        }

        if (!entitiesToRemove.contains(entity, true)) {
            entitiesToRemove.add(entity);
        }
    }

    /**
     * Update all registered entities. Should only be called from the main game loop.
     */
    public void update() {
        for (Entity entity : entities) {
            // If entity is in the entitiesToRemove list then don't update the entity
            if (!entitiesToRemove.contains(entity, true)) {
                entity.earlyUpdate();
                entity.update();
            }
        }
        disposeMarkedEntities();
    }

    /**
     * Dispose all entities marked for removal
     */
    private void disposeMarkedEntities() {
        Array<Entity> removed = new Array<>();
        for (Entity entity : entitiesToRemove) {
            if (!ServiceLocator.getPhysicsService().getPhysics().getWorld().isLocked()){
                entity.dispose();
                removed.add(entity);
            }
        }
        entitiesToRemove.removeAll(removed, true);
    }

    /**
     * Dispose all entities.
     */
    public void dispose() {
        for (Entity entity : entities) {
            entity.dispose();
        }
    }

    /**
     * Get all the registered entities
     * @return the array of all the entities.
     */
    public Entity[] getEntities() {
        return entities.toArray(Entity.class);
    }

    @Override
    public String toString() {
        List<String> entries = Arrays.stream(this.getEntities())
                .sorted(Comparator.comparingInt(Entity::getId))
                .map(Entity::toString)
                .toList();
        return getEntities().length + " Entities\n" +
                "ID\tPosition\t\tName\n" +
                String.join("\n", entries);
    }
}
