package com.csse3200.game.components.npc.attack.attackeffects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.effects.KnockbackEffect;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class KnockbackEffectTest {

    private Entity sourceEntity;
    private Entity targetEntity;
    private PhysicsComponent targetPhysicsComponent;
    private Body targetBody;

    @BeforeEach
    void setUp() {
        // Creating mock objects
        sourceEntity = mock(Entity.class);
        targetEntity = mock(Entity.class);
        targetPhysicsComponent = mock(PhysicsComponent.class);
        targetBody = mock(Body.class);
    }

    @Test
    void shouldApplyKnockback() {
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(targetPhysicsComponent);
        when(targetPhysicsComponent.getBody()).thenReturn(targetBody);
        when(sourceEntity.getCenterPosition()).thenReturn(new Vector2(0, 0));
        when(targetEntity.getCenterPosition()).thenReturn(new Vector2(1, 1));
        when(targetBody.getWorldCenter()).thenReturn(new Vector2(0.5f, 0.5f));

        float force = 10f;
        KnockbackEffect knockbackEffect = new KnockbackEffect(sourceEntity, force);

        // Apply the knockback effect
        knockbackEffect.apply(targetEntity);

        // Create expected impulse vector
        Vector2 expectedImpulse = new Vector2(1, 1).sub(new Vector2(0, 0)).setLength(force);

        // Verify that the impulse was applied correctly
        verify(targetBody).applyLinearImpulse(eq(expectedImpulse), eq(new Vector2(0.5f, 0.5f)), eq(true));
    }

    @Test
    void shouldNotApplyKnockbackWhenNoPhysicsComponent() {
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(null);
        float force = 10f;
        KnockbackEffect knockbackEffect = new KnockbackEffect(sourceEntity, force);

        // Apply the knockback effect
        knockbackEffect.apply(targetEntity);

        // Verify that no impulse was applied
        verify(targetBody, never()).applyLinearImpulse(any(Vector2.class), any(Vector2.class), anyBoolean());
    }

    @Test
    void shouldNotApplyKnockbackWhenForceIsZero() {
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(targetPhysicsComponent);
        when(targetPhysicsComponent.getBody()).thenReturn(targetBody);

        float force = 0f;
        KnockbackEffect knockbackEffect = new KnockbackEffect(sourceEntity, force);

        // Apply the knockback effect
        knockbackEffect.apply(targetEntity);

        // Verify that no impulse was applied
        verify(targetBody, never()).applyLinearImpulse(any(Vector2.class), any(Vector2.class), anyBoolean());
    }
}