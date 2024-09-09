package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class NPCDeathHandlerTest {

    private Entity entity;
    private AnimationRenderComponent animationRender;
    private PhysicsComponent physicsComponent;
    private AITaskComponent aiTaskComponent;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        NPCDeathHandler npcDeathHandler = new NPCDeathHandler();
        CombatStatsComponent combatStats = mock(CombatStatsComponent.class);
        animationRender = mock(AnimationRenderComponent.class);
        physicsComponent = mock(PhysicsComponent.class);
        aiTaskComponent = mock(AITaskComponent.class);

        entity.addComponent(npcDeathHandler)
                .addComponent(combatStats)
                .addComponent(animationRender)
                .addComponent(physicsComponent)
                .addComponent(aiTaskComponent);

        entity.create();

        // Mock ServiceLocator registrations
        ServiceLocator.registerEntityService(mock(EntityService.class));
    }

    @Test
    void shouldTriggerOnDeathWhenDiedEvent() {
        // Set up the mock animation to have a death animation
        when(animationRender.hasAnimation("death")).thenReturn(true);

        // Trigger the death event
        entity.getEvents().trigger("died");

        // Verify that death animation started and components were disabled
        verify(animationRender).startAnimation("death");
        verify(physicsComponent).setEnabled(false);
        verify(aiTaskComponent).setEnabled(false);
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }

    @Test
    void shouldNotTriggerDeathAgainIfAlreadyDead() {
        when(animationRender.hasAnimation("death")).thenReturn(true);

        // Trigger death twice
        entity.getEvents().trigger("died");
        entity.getEvents().trigger("died");

        // Verify that the death process is only called once
        verify(animationRender, times(1)).startAnimation("death");
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }

    @Test
    void shouldNotPlayDeathAnimationIfNotAvailable() {
        // Set up the mock animation to NOT have a death animation
        when(animationRender.hasAnimation("death")).thenReturn(false);

        // Trigger the death event
        entity.getEvents().trigger("died");

        // Verify that no animation was played but components were still disabled
        verify(animationRender, never()).startAnimation("death");
        verify(physicsComponent).setEnabled(false);
        verify(aiTaskComponent).setEnabled(false);
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }
}
