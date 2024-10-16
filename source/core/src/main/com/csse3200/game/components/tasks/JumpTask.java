package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.math.MathUtils.lerp;

/**
 * Task for an NPC to "jump" towards a target, bypassing obstacles and simulating a jump arc in the y direction.
 * The task allows the NPC to leap towards a target entity, dealing an AOE attack upon landing,
 * while taking care of obstacle collisions and directional updates during the jump.
 */
public class JumpTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(JumpTask.class);
    private static final int ACTIVE_PRIORITY = 10;
    private static final int INACTIVE_PRIORITY = 7;
    private static final float JUMP_HEIGHT_SCALAR = 0.3f;
    private final Entity target;
    private final float activationMinRange;
    private final float activationMaxRange;
    private final float jumpDuration;
    private final float waitTime;
    private final float cooldownTime;
    private float jumpHeight;
    private Vector2 startPos;
    private Vector2 targetPos;
    private PhysicsComponent physicsComponent;
    private ColliderComponent colliderComponent;
    private AOEAttackComponent aoeAttackComponent;
    private final GameTime gameTime;
    private long startTime;
    private long lastExecutionTime;
    private WaitTask waitTask;
    private boolean hasAttacked;
    private Filter originalFilter;

    /**
     * Creates a JumpTask towards the target with a specified jump height and duration.
     *
     * @param target Entity to jump towards.
     * @param config Configuration for the jump task, including activation ranges, jump duration, wait time, and cooldown.
     */
    public JumpTask(Entity target, TaskConfig.JumpTaskConfig config) {
        this.target = target;
        this.activationMinRange = config.activationMinRange;
        this.activationMaxRange = config.activationMaxRange;
        this.jumpDuration = config.jumpDuration;
        this.waitTime = config.waitTime;
        this.cooldownTime = config.cooldownTime;
        gameTime = ServiceLocator.getTimeSource();
    }

    /**
     * Starts the jump task. This sets up the jump by calculating the jump height and preparing
     * the physics and collider components of the entity for the leap.
     */
    @Override
    public void start() {
        logger.debug("Starting jump towards {}", target);
        super.start();
        this.physicsComponent = owner.getEntity().getComponent(PhysicsComponent.class);
        this.colliderComponent = owner.getEntity().getComponent(ColliderComponent.class);
        DirectionalNPCComponent directionalComponent = owner.getEntity().getComponent(DirectionalNPCComponent.class);
        this.aoeAttackComponent = owner.getEntity().getComponent(AOEAttackComponent.class);
        startTime = gameTime.getTime();
        hasAttacked = false;

        // Set the jump height based on the distance between the entity and the target
        startPos = owner.getEntity().getPosition();
        targetPos = target.getPosition();
        jumpHeight = JUMP_HEIGHT_SCALAR * startPos.dst(targetPos);

        // Update direction to face the target
        if (directionalComponent != null) {
            if (startPos.x < targetPos.x) {
                directionalComponent.setDirection("right");
            } else {
                directionalComponent.setDirection("left");
            }
        }

        // Modify collision filter to ignore PLAYER and NPC layers
        modifyCollisionFilter();

        this.owner.getEntity().getEvents().trigger("jump");
    }

    /**
     * Updates the jump task, gradually moving the entity towards the target while calculating the arc of the jump.
     * When the jump finishes, the AOE attack is triggered.
     */
    @Override
    public void update() {
        long elapsedTime = gameTime.getTimeSince(startTime);
        float t = Math.min((float)elapsedTime / (jumpDuration * 1000), 1);  // Time ratio from 0 to 1

        // Calculate the new position and jump height
        float newX = lerp(startPos.x, targetPos.x, t);
        float newY = lerp(startPos.y, targetPos.y, t);
        float jumpArc = calculateJumpArc(t);
        Vector2 newPosition = new Vector2(newX, newY + jumpArc);
        Vector2 currentPosition = owner.getEntity().getPosition();

        // Calculate and apply impulse to entity
        Vector2 impulse = newPosition.sub(currentPosition).scl(physicsComponent.getBody().getMass());
        physicsComponent.getBody().applyLinearImpulse(impulse, physicsComponent.getBody().getWorldCenter(), true);

        // Check if the jump is finished
        if (t >= 1 && !hasAttacked) {
            physicsComponent.getBody().setLinearVelocity(Vector2.Zero);

            // Restore the original collision filter
            colliderComponent.getFixture().setFilterData(originalFilter);
            colliderComponent.getFixture().refilter();

            // Perform the AOE attack immediately upon landing
            aoeAttackComponent.enableForNumAttacks(1);
            owner.getEntity().getEvents().trigger("attack"); // Trigger attack animation
            hasAttacked = true;

            // Start wait task for cooldown
            waitTask = new WaitTask(waitTime);
            waitTask.create(owner);
            waitTask.start();
        } else if (hasAttacked && waitTask.getStatus() == Status.ACTIVE) {
            waitTask.update();
        } else if (hasAttacked && waitTask.getStatus() == Status.FINISHED) {
            this.stop();
        }
    }

    /**
     * Calculates the parabolic jump arc (y direction) based on the time ratio.
     *
     * @param t Time ratio of the jump (0 to 1)
     * @return Height of the jump arc at the current time.
     */
    private float calculateJumpArc(float t) {
        return 4 * jumpHeight * t * (1 - t);  // Parabolic motion equation for arc
    }

    /**
     * Stops the jump task, marking the last execution time.
     */
    @Override
    public void stop() {
        super.stop();
        lastExecutionTime = gameTime.getTime();
    }

    /**
     * Returns the priority of the task. The task will have a high priority when active and a lower priority
     * if the target is within the defined range and the cooldown has completed.
     *
     * @return the priority of the task, or -1 if it should not be executed.
     */
    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return ACTIVE_PRIORITY;
        }
        float dst = owner.getEntity().getPosition().dst(target.getPosition());
        if (isCooldownComplete() && dst >= activationMinRange && dst <= activationMaxRange) {
            return INACTIVE_PRIORITY;
        }
        return -1;
    }

    /**
     * Checks if the cooldown time since the last execution has completed, allowing a new jump.
     *
     * @return true if the cooldown is complete, false otherwise.
     */
    boolean isCooldownComplete() {
        if (lastExecutionTime == 0) {
            return true;
        }
        long currentTime = gameTime.getTime();
        return (currentTime - lastExecutionTime) >= (cooldownTime * 1000);
    }

    /**
     * Modifies the collision filter of the entity to ignore all layers during the jump besides OBSTACLE.
     */
    private void modifyCollisionFilter() {
        originalFilter = new Filter();
        Filter currentFilter = colliderComponent.getFixture().getFilterData();
        originalFilter.categoryBits = currentFilter.categoryBits;
        originalFilter.maskBits = currentFilter.maskBits;
        originalFilter.groupIndex = currentFilter.groupIndex;

        // Include only obstacle layer
        short newMaskBits = PhysicsLayer.OBSTACLE;

        // Modify the collision filter
        Filter jumpFilter = new Filter();
        jumpFilter.categoryBits = currentFilter.categoryBits;
        jumpFilter.maskBits = newMaskBits;
        jumpFilter.groupIndex = 0;

        colliderComponent.getFixture().setFilterData(jumpFilter);
        colliderComponent.getFixture().refilter();
    }
}
