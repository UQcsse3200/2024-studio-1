/**package com.csse3200.game.components.npc;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class NPCDeathHandlerTest {

    private Entity entity;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        NPCDeathHandler npcDeathHandler = new NPCDeathHandler();
        CombatStatsComponent combatStats = mock(CombatStatsComponent.class);
        AnimationRenderComponent animationRender = mock(AnimationRenderComponent.class);
        AITaskComponent aiTaskComponent = mock(AITaskComponent.class);
        PhysicsMovementComponent physicsMovementComponent = mock(PhysicsMovementComponent.class);
        HitboxComponent hitboxComponent = mock(HitboxComponent.class);
        ColliderComponent colliderComponent = mock(ColliderComponent.class);

        entity.addComponent(npcDeathHandler)
                .addComponent(combatStats)
                .addComponent(animationRender)
                .addComponent(aiTaskComponent)
                .addComponent(physicsMovementComponent)
                .addComponent(hitboxComponent)
                .addComponent(colliderComponent);

        entity.create();

        // Mock ServiceLocator registrations
        ServiceLocator.registerEntityService(mock(EntityService.class));
    }

    @Test
    void shouldTriggerOnDeathWhenDiedEvent() {
        entity.getEvents().trigger("died");

        // Fast-forward the Timer to ensure scheduled tasks execute
        Timer.instance().clear();

        // Verify that relevant components were disabled
        verify(entity.getComponent(AITaskComponent.class)).setEnabled(false);
        verify(entity.getComponent(PhysicsMovementComponent.class)).setEnabled(false);
        verify(entity.getComponent(HitboxComponent.class)).setEnabled(false);
        verify(entity.getComponent(ColliderComponent.class)).setEnabled(false);

        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }

    @Test
    void shouldNotTriggerDeathAgainIfAlreadyDead() {
        // Trigger death twice
        entity.getEvents().trigger("died");
        entity.getEvents().trigger("died");

        // Verify that the components' disabling and death process is only called once
        verify(entity.getComponent(AITaskComponent.class), times(1)).setEnabled(false);
        verify(entity.getComponent(PhysicsMovementComponent.class), times(1)).setEnabled(false);
        verify(entity.getComponent(HitboxComponent.class), times(1)).setEnabled(false);
        verify(entity.getComponent(ColliderComponent.class), times(1)).setEnabled(false);
        assertTrue(NPCDeathHandler.deadEntities.contains(entity.getId()));
    }
}*/