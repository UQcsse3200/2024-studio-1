package com.csse3200.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
    private final Array<Supplier<Entity>> entitiesToCreate = new Array<>();

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

    private String entityToString(Entity entity) {
        if (entity == null) {
            return "null";
        }

        NameComponent nameComponent = entity.getComponent(NameComponent.class);
        String name = (nameComponent == null) ? "Unknown Entity" : nameComponent.getName();
        Vector2 pos = entity.getPosition();

        return String.format("%02d\t(%.2f, %.2f)", entity.getId(), pos.x, pos.y) + "\t" + name;
    }

    public List<String> getEntityNames() {
        return Stream.of(getEntities()).map(this::entityToString).toList();
    }

    private void createQueuedEntities() {
        for (Supplier<Entity> entitySupplier : entitiesToCreate) {
            register(entitySupplier.get());
        }
        entitiesToCreate.clear();
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
}
