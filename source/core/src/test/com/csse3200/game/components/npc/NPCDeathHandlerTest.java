package com.csse3200.game.components.npc;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class NPCDeathHandlerTest {
    private NPCDeathHandler deathHandler;

    @Mock private AnimationRenderComponent animator;
    @Mock private PhysicsComponent physicsComponent;
    @Mock private AITaskComponent aiTaskComponent;
    @Mock private EntityService entityService;
    @Mock private Entity entity;
    @Mock private EventHandler eventHandler;

    @BeforeEach
    void setUp() {
        deathHandler = new NPCDeathHandler();
        when(entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);
        when(entity.getComponent(PhysicsComponent.class)).thenReturn(physicsComponent);
        when(entity.getComponent(AITaskComponent.class)).thenReturn(aiTaskComponent);
        when(entity.getEvents()).thenReturn(eventHandler);
        when(entity.getId()).thenReturn(1);

        ServiceLocator.registerEntityService(entityService);

        deathHandler.setEntity(entity);
    }

    /**
     * Tests if the NPCDeathHandler correctly handles the death of an entity.
     */
    @Test
    void shouldHandleDeath() {
        when(animator.hasAnimation("death")).thenReturn(true);
        deathHandler.create();
        deathHandler.onDeath();

        verify(animator).startAnimation("death");
        verify(physicsComponent).setEnabled(false);
        verify(aiTaskComponent).setEnabled(false);
    }

    /**
     * Tests if the NPCDeathHandler correctly handles the death of an entity without a death animation.
     */
    @Test
    void shouldHandleDeathWithoutAnimation() {
        when(animator.hasAnimation("death")).thenReturn(false);
        deathHandler.create();
        deathHandler.onDeath();

        verify(animator, never()).startAnimation("death");
        verify(physicsComponent).setEnabled(false);
        verify(aiTaskComponent).setEnabled(false);
    }
}