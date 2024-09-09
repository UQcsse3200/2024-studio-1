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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class NPCDeathHandlerTest {

    private Entity entity;
    private AnimationRenderComponent animationRender;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        NPCDeathHandler npcDeathHandler = new NPCDeathHandler();
        CombatStatsComponent combatStats = mock(CombatStatsComponent.class);
        animationRender = mock(AnimationRenderComponent.class);
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        AITaskComponent aiTaskComponent = mock(AITaskComponent.class);

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
        when(animationRender.hasAnimation("death")).thenReturn(true);

        entity.getEvents().trigger("died");

        // Fast-forward the Timer to ensure scheduled tasks execute
        Timer.instance().clear();

        // Verify that death animation started and components were disabled
        verify(animationRender).startAnimation("death");
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
        when(animationRender.hasAnimation("death")).thenReturn(false);

        entity.getEvents().trigger("died");

        // Fast-forward the Timer to ensure scheduled tasks execute
        Timer.instance().clear();

        verify(animationRender, never()).startAnimation("death");
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }
}
