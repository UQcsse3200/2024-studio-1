package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class StraightWanderTaskTest {

    private Entity entity;
    private StraightWanderTask straightWanderTask;

    @BeforeEach
    void setUp() {
        // Initialize necessary services
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f); // 20 ms per frame
        ServiceLocator.registerTimeSource(gameTime);
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Set up entity and task components
        entity = new Entity();
        entity.addComponent(new PhysicsComponent());
        entity.addComponent(new PhysicsMovementComponent());
        AITaskComponent aiTaskComponent = new AITaskComponent();
        straightWanderTask = new StraightWanderTask(5f);
        aiTaskComponent.addTask(straightWanderTask);
        entity.addComponent(aiTaskComponent);
        entity.create();
        entity.setPosition(0f, 0f);
    }

    @Test
    void shouldStartMovingOnStart() {
        Vector2 initialPosition = entity.getPosition().cpy();
        straightWanderTask.start();

        // Simulate updates to check movement
        for (int i = 0; i < 5; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        Vector2 newPosition = entity.getPosition();
        float distanceMoved = initialPosition.dst(newPosition);

        assertNotEquals(initialPosition, newPosition, "Entity should have moved from its initial position.");
        assertTrue(distanceMoved > 0, "Entity should have moved a non-zero distance.");
    }

    @Test
    void shouldChangeDirectionOnCollision() {
        Vector2 initialPosition = entity.getPosition().cpy();
        straightWanderTask.start();

        // Simulate movement before collision
        for (int i = 0; i < 5; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        Vector2 positionBeforeCollision = entity.getPosition().cpy();

        // Calculate initial movement direction
        Vector2 initialDirection = positionBeforeCollision.cpy().sub(initialPosition).nor();

        // Simulate a collision
        entity.getEvents().trigger("collisionStart", null, null);

        // Simulate updates after collision
        for (int i = 0; i < 5; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        Vector2 positionAfterCollision = entity.getPosition();

        // Calculate direction after collision
        Vector2 newDirection = positionAfterCollision.cpy().sub(positionBeforeCollision).nor();

        // Ensure directions are not the same
        float angleBetween = initialDirection.angleDeg(newDirection);
        assertTrue(angleBetween > 5f, "Entity should have moved in a different direction after collision.");
    }

    @Test
    void shouldStopTaskProperly() {
        straightWanderTask.start();
        Vector2 positionBeforeStop = entity.getPosition().cpy();

        straightWanderTask.stop();

        // Simulate updates to check if movement has stopped
        for (int i = 0; i < 5; i++) {
            straightWanderTask.update();
            entity.earlyUpdate();
            entity.update();
        }

        Vector2 positionAfterStop = entity.getPosition();
        assertEquals(positionBeforeStop, positionAfterStop, "Entity should not move after task is stopped.");
    }
}