package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.math.MathUtils.lerp;

/**
 * Task for an NPC to "jump" toward a target, bypassing obstacles and simulating a jump arc in the y direction.
 */
public class JumpTask extends DefaultTask implements PriorityTask {
    private static final int ACTIVE_PRIORITY = 10;
    private static final int INACTIVE_PRIORITY = 7;
    private static final float JUMP_DURATION = 1.5f * 1000;
    private static final float JUMP_HEIGHT_SCALAR = 0.3f;
    private final Entity target;
    private final float activationMinRange;
    private final float activationMaxRange;
    private float jumpHeight;
    private Vector2 startPos;
    private Vector2 targetPos;
    private PhysicsComponent physicsComponent;
    private ColliderComponent colliderComponent;
    private GameTime gameTime;
    private long startTime;
    private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

    /**
     * Creates a JumpTask towards the target with a specified jump height and duration.
     *
     * @param target Entity to jump towards.
     */
    public JumpTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.JumpTaskConfig config) {
        this.target = target;
        this.activationMinRange = config.activationMinRange;
        this.activationMaxRange = config.activationMaxRange;
    }

    @Override
    public void start() {
        logger.debug("Starting jump towards {}", target);
        super.start();
        this.physicsComponent = owner.getEntity().getComponent(PhysicsComponent.class);
        colliderComponent = owner.getEntity().getComponent(ColliderComponent.class);
        this.gameTime = ServiceLocator.getTimeSource();

        startPos = owner.getEntity().getPosition();
        targetPos = target.getPosition();
        jumpHeight = JUMP_HEIGHT_SCALAR * startPos.dst(targetPos);

        startTime = gameTime.getTime();
        this.owner.getEntity().getEvents().trigger("jump");
        colliderComponent.setSensor(true);
    }

    @Override
    public void update() {
        long elapsedTime = gameTime.getTimeSince(startTime);
        float t = Math.min((float)elapsedTime / JUMP_DURATION, 1);  // Time ratio from 0 to 1

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
        if (t >= 1) {
            status = Status.FINISHED;
            this.stop();
        }
    }

    /**
     * Calculate the jump arc (parabolic motion).
     */
    private float calculateJumpArc(float t) {
        return 4 * jumpHeight * t * (1 - t);
    }

    @Override
    public void stop() {
        super.stop();
        physicsComponent.getBody().setLinearVelocity(Vector2.Zero);
        colliderComponent.setSensor(false);
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return ACTIVE_PRIORITY;
        }
        float dst = owner.getEntity().getPosition().dst(target.getPosition());
        if (dst >= activationMinRange && dst <= activationMaxRange) {
            return INACTIVE_PRIORITY;
        }
        return -1;
    }
}