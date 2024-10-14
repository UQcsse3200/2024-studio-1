package com.csse3200.game.components.npc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class NPCDeathHandlerTest {

    private Entity entity;
    private Entity target;
    private NPCDeathHandler npcDeathHandler;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        target = new Entity();
        npcDeathHandler = new NPCDeathHandler(target, 10);
        entity.addComponent(npcDeathHandler);
    }

    @Test
    void testCreate() {
        npcDeathHandler.create();
        // Just verify that create() doesn't throw any exceptions
        assertTrue(true);
    }

    @Test
    void testOnDeath() {
        npcDeathHandler.create();
        entity.getEvents().trigger("died");

        // Verify that the entity is added to deadEntities
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }
}