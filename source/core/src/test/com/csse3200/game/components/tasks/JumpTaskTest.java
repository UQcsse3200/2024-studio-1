// JumpTaskTest.java
package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JumpTaskTest {
    private GameTime gameTime;
    private Entity owner;
    private Entity target;
    private JumpTask jumpTask;
    private PhysicsComponent physicsComponent;
    private ColliderComponent colliderComponent;
    private DirectionalNPCComponent directionalComponent;
    private AOEAttackComponent aoeAttackComponent;
    private AITaskComponent aiComponent;

    @Before
    public void setUp() {
        // Mock the GameTime service
        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);

        // Create owner and target entities
        owner = new Entity();
        target = new Entity();

        // Add necessary components to the owner
        physicsComponent = mock(PhysicsComponent.class);
        when(physicsComponent.getBody()).thenReturn(mock(com.badlogic.gdx.physics.box2d.Body.class));

        colliderComponent = mock(ColliderComponent.class);
        directionalComponent = new DirectionalNPCComponent(true);
        aoeAttackComponent = mock(AOEAttackComponent.class);

        owner.addComponent(physicsComponent);
        owner.addComponent(colliderComponent);
        owner.addComponent(directionalComponent);
        owner.addComponent(aoeAttackComponent);

        // Set positions
        owner.setPosition(0, 0);
        target.setPosition(10, 0);

        // Create a simple config for JumpTask
        NPCConfigs.NPCConfig.TaskConfig.JumpTaskConfig config = new NPCConfigs.NPCConfig.TaskConfig.JumpTaskConfig();
        config.activationMinRange = 5f;
        config.activationMaxRange = 15f;
        config.jumpDuration = 1f;
        config.waitTime = 0.5f;
        config.cooldownTime = 2f;

        // Create the JumpTask
        jumpTask = new JumpTask(target, config);
        aiComponent = new AITaskComponent().addTask(jumpTask);
        owner.addComponent(aiComponent);
        owner.create();
    }

    @After
    public void tearDown() {
        ServiceLocator.clear();
    }

    @Test
    public void testPriorityWhenInactiveAndInRange() {
        // Set time to simulate cooldown has passed
        when(gameTime.getTime()).thenReturn(0L);

        // Distance between owner and target is 10 units (within 5 and 15)
        int priority = jumpTask.getPriority();
        assertEquals("Priority should be INACTIVE_PRIORITY", 7, priority);
    }

    @Test
    public void testPriorityWhenInactiveAndOutOfRange() {
        // Move target out of range
        target.setPosition(20, 0);

        int priority = jumpTask.getPriority();
        assertEquals("Priority should be -1 when out of range", -1, priority);
    }

    @Test
    public void testPriorityWhenOnCooldown() {
        when(gameTime.getTime()).thenReturn(100L);
        jumpTask.stop(); // Sets lastExecutionTime to 0.1

        // Advance time to 1 second (cooldownTime is 2 seconds)
        when(gameTime.getTime()).thenReturn(1000L);

        int priority = jumpTask.getPriority();
        assertEquals("Priority should be -1 when on cooldown", -1, priority);
    }

    @Test
    public void testStart() {
        jumpTask.start();

        // Verify components are retrieved
        assertNotNull("PhysicsComponent should be initialised", physicsComponent);
        assertNotNull("ColliderComponent should be initialised", colliderComponent);
        assertNotNull("DirectionalNPCComponent should be initialised", directionalComponent);

        // Verify collider is set to sensor
        verify(colliderComponent).setSensor(true);
    }

    @Test
    public void testAttackTriggeredOnLanding() {
        when(gameTime.getTimeSince(anyLong())).thenReturn(1000L); // Jump complete
        when(gameTime.getTime()).thenReturn(1000L);

        jumpTask.start();
        jumpTask.update();

        // Verify AOE attack is enabled
        verify(aoeAttackComponent).enableForNumAttacks(1);
    }

    @Test
    public void testColliderResetAfterJump() {
        when(gameTime.getTimeSince(anyLong())).thenReturn(1000L); // Jump complete
        when(gameTime.getTime()).thenReturn(1000L);

        jumpTask.start();
        jumpTask.update();

        // Collider should be reset after landing
        verify(colliderComponent).setSensor(false);
    }

    @Test
    public void testIsCooldownComplete() {
        when(gameTime.getTime()).thenReturn(0L);

        jumpTask.stop(); // Sets lastExecutionTime to 0
        when(gameTime.getTime()).thenReturn(3000L); // Advance time by 3 seconds (cooldownTime is 2 seconds)

        boolean cooldownComplete = jumpTask.isCooldownComplete();
        assertTrue("Cooldown should be complete after cooldownTime has passed", cooldownComplete);
    }
}