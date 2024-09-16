package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class NPCDeathHandlerTest {

    private Entity entity;
    private PhysicsMovementComponent physicsMovement;
    private AITaskComponent aiTaskComponent;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        NPCDeathHandler npcDeathHandler = new NPCDeathHandler();
        CombatStatsComponent combatStats = mock(CombatStatsComponent.class);
        AnimationRenderComponent animationRender = mock(AnimationRenderComponent.class);
        physicsMovement = mock(PhysicsMovementComponent.class);
        aiTaskComponent = mock(AITaskComponent.class);

        entity.addComponent(npcDeathHandler)
                .addComponent(combatStats)
                .addComponent(animationRender)
                .addComponent(physicsMovement)
                .addComponent(aiTaskComponent);

        entity.create();

        // Mock ServiceLocator registrations
        ServiceLocator.registerEntityService(mock(EntityService.class));
    }

    @Test
    void shouldTriggerOnDeathWhenDiedEvent() {
        entity.getEvents().trigger("died");

        // Verify that components were disabled and entity added to deadEntities
        verify(aiTaskComponent).setEnabled(false);
        verify(physicsMovement).setEnabled(false);
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }

    @Test
    void shouldNotTriggerDeathAgainIfAlreadyDead() {
        // Trigger death twice
        entity.getEvents().trigger("died");
        entity.getEvents().trigger("died");

        // Verify that the death process is only called once
        verify(aiTaskComponent, times(1)).setEnabled(false);
        verify(physicsMovement, times(1)).setEnabled(false);
        assertEquals(1, NPCDeathHandler.deadEntities.size());
    }
}