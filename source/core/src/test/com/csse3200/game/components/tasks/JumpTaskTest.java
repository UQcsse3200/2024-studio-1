// JumpTaskTest.java
package com.csse3200.game.components.tasks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class JumpTaskTest {
    private GameTime gameTime;
    private Fixture fixture;
    private AOEAttackComponent aoeAttackComponent;
    private Entity target;
    private JumpTask jumpTask;
    private Filter originalFilter;

    @Before
    public void setUp() {
        // Initialize mocks
        gameTime = mock(GameTime.class);
        PhysicsComponent physicsComponent = mock(PhysicsComponent.class);
        ColliderComponent colliderComponent = mock(ColliderComponent.class);
        Body body = mock(Body.class);
        fixture = mock(Fixture.class);
        aoeAttackComponent = mock(AOEAttackComponent.class);

        // Set up mocks
        when(physicsComponent.getBody()).thenReturn(body);
        when(colliderComponent.getFixture()).thenReturn(fixture);

        // Set up the fixture to return a default filter initially
        originalFilter = new Filter();
        originalFilter.categoryBits = PhysicsLayer.NPC;
        originalFilter.maskBits = PhysicsLayer.OBSTACLE | PhysicsLayer.PLAYER | PhysicsLayer.NPC;
        originalFilter.groupIndex = 0;

        when(fixture.getFilterData()).thenReturn(originalFilter);
        ServiceLocator.registerTimeSource(gameTime);

        // Create owner and target entities
        Entity owner = new Entity();
        target = new Entity();

        // Add necessary components to the owner
        when(physicsComponent.getBody()).thenReturn(mock(com.badlogic.gdx.physics.box2d.Body.class));

        DirectionalNPCComponent directionalComponent = new DirectionalNPCComponent(true);
        owner.addComponent(physicsComponent);
        owner.addComponent(colliderComponent);
        owner.addComponent(directionalComponent);
        owner.addComponent(aoeAttackComponent);

        // Set positions
        owner.setPosition(0, 0);
        target.setPosition(10, 0);

        // Create a simple config for JumpTask
        TaskConfig.JumpTaskConfig config = new TaskConfig.JumpTaskConfig();
        config.activationMinRange = 5f;
        config.activationMaxRange = 15f;
        config.jumpDuration = 1f;
        config.waitTime = 0.5f;
        config.cooldownTime = 2f;

        // Create the JumpTask
        jumpTask = new JumpTask(target, config);
        AITaskComponent aiComponent = new AITaskComponent().addTask(jumpTask);
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

        // Verify that modifyCollisionFilter was called by checking setFilterData with correct parameters
        ArgumentCaptor<Filter> filterCaptor = ArgumentCaptor.forClass(Filter.class);
        verify(fixture).setFilterData(filterCaptor.capture());
        verify(fixture).refilter();

        Filter appliedFilter = filterCaptor.getValue();

        // Assert that maskBits are set to only OBSTACLE and groupIndex is 0
        assertEquals("CategoryBits should remain unchanged", originalFilter.categoryBits, appliedFilter.categoryBits);
        assertEquals("MaskBits should only include OBSTACLE", PhysicsLayer.OBSTACLE, appliedFilter.maskBits);
        assertEquals("GroupIndex should be 0", 0, appliedFilter.groupIndex);
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

        // Verify that the original filter is restored
        ArgumentCaptor<Filter> filterCaptor = ArgumentCaptor.forClass(Filter.class);
        verify(fixture, times(2)).setFilterData(filterCaptor.capture()); // Once in start(), once in restore
        verify(fixture, times(2)).refilter();

        // The second filter set should be the original filter
        Filter restoredFilter = filterCaptor.getAllValues().get(1);
        assertEquals("Original CategoryBits should be restored", originalFilter.categoryBits, restoredFilter.categoryBits);
        assertEquals("Original MaskBits should be restored", originalFilter.maskBits, restoredFilter.maskBits);
        assertEquals("Original GroupIndex should be restored", originalFilter.groupIndex, restoredFilter.groupIndex);
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