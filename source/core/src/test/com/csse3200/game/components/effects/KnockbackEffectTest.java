package com.csse3200.game.components.effects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the KnockbackEffect class.
 */
@ExtendWith(MockitoExtension.class)
class KnockbackEffectTest {
    @Mock
    private Entity sourceEntity;
    @Mock
    private Entity targetEntity;
    @Mock
    private PhysicsComponent targetPhysicsComponent;
    @Mock
    private Body targetBody;
    private KnockbackEffect knockbackEffect;

    @Test
    void testApply_ShouldApplyCorrectImpulse() {
        // Setup the target entity to return the mocked PhysicsComponent
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(targetPhysicsComponent);
        when(targetPhysicsComponent.getBody()).thenReturn(targetBody);
        knockbackEffect = new KnockbackEffect(sourceEntity, 10f);

        // Arrange
        // Define the positions of source and target entities
        Vector2 sourcePosition = new Vector2(0, 0);
        Vector2 targetPosition = new Vector2(1, 1);
        Vector2 targetCenterPosition = new Vector2(0.5f, 0.5f);

        // Stub the position methods
        when(sourceEntity.getCenterPosition()).thenReturn(sourcePosition);
        when(targetEntity.getCenterPosition()).thenReturn(targetPosition);
        when(targetBody.getWorldCenter()).thenReturn(targetCenterPosition);

        // Apply the knockback effect
        knockbackEffect.apply(targetEntity);

        // Calculate the expected impulse vector
        Vector2 direction = new Vector2(targetPosition).sub(sourcePosition).nor().scl(10f);

        // Assert
        // Verify that applyLinearImpulse was called with the correct parameters
        verify(targetBody, times(1)).applyLinearImpulse(eq(direction), eq(targetCenterPosition), eq(true));

        // Ensure no other interactions occurred
        verifyNoMoreInteractions(targetBody);
    }

    @Test
    void testApply_ShouldNotApplyImpulseWhenNoPhysicsComponent() {
        // Setup the target entity to return null for PhysicsComponent
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(null);

        // Initialise and apply knockback effect
        knockbackEffect = new KnockbackEffect(sourceEntity, 10f);
        knockbackEffect.apply(targetEntity);

        // Verify that applyLinearImpulse was never called
        verify(targetBody, never()).applyLinearImpulse(any(Vector2.class), any(Vector2.class), anyBoolean());

        // Ensure no interactions occurred with the PhysicsComponent
        verifyNoInteractions(targetPhysicsComponent);
        verifyNoMoreInteractions(targetBody);
    }

    @Test
    void testApply_ShouldNotApplyImpulseWhenForceIsZero() {
        // Setup the target entity to return the mocked PhysicsComponent
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(targetPhysicsComponent);

        // Initialize and apply KnockbackEffect with zero force
        KnockbackEffect zeroForceKnockback = new KnockbackEffect(sourceEntity, 0f);
        zeroForceKnockback.apply(targetEntity);

        // Verify that applyLinearImpulse was never called
        verify(targetBody, never()).applyLinearImpulse(any(Vector2.class), any(Vector2.class), anyBoolean());

        // Ensure no interactions occurred with the PhysicsComponent
        verifyNoInteractions(targetPhysicsComponent);
        verifyNoMoreInteractions(targetBody);
    }

    @Test
    void testApply_ShouldNotApplyImpulseWhenForceIsNegative() {
        // Setup the target entity to return the mocked PhysicsComponent
        when(targetEntity.getComponent(PhysicsComponent.class)).thenReturn(targetPhysicsComponent);

        // Initialize KnockbackEffect with negative force
        KnockbackEffect negativeForceKnockback = new KnockbackEffect(sourceEntity, -5f);

        // Apply the negative force knockback effect
        negativeForceKnockback.apply(targetEntity);

        // Verify no impulse was applied
        verify(targetBody, never()).applyLinearImpulse(any(Vector2.class), any(Vector2.class), anyBoolean());

        // Ensure no interactions occurred with the PhysicsComponent
        verifyNoInteractions(targetPhysicsComponent);
        verifyNoMoreInteractions(targetBody);
    }
}