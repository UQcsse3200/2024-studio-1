package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
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
    private final Entity target;
    private final float jumpHeight;
    private final float jumpDuration;
    private Vector2 startPos;
    private Vector2 targetPos;
    private PhysicsComponent physicsComponent;
    private GameTime gameTime;
    private long startTime;
    private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

    /**
     * Creates a JumpTask towards the target with a specified jump height and duration.
     *
     * @param target Entity to jump towards.
     * @param jumpHeight Maximum height of the jump arc.
     * @param jumpDuration Duration for the entire jump (in milliseconds).
     */
    public JumpTask(Entity target, float jumpHeight, float jumpDuration) {
        this.target = target;
        this.jumpHeight = jumpHeight;
        this.jumpDuration = jumpDuration * 1000;
    }

    @Override
    public void start() {
        logger.info("Starting jump towards {}", target);
        super.start();
        this.physicsComponent = owner.getEntity().getComponent(PhysicsComponent.class);
        this.gameTime = ServiceLocator.getTimeSource();

        startPos = owner.getEntity().getPosition();
        targetPos = target.getPosition();

        startTime = gameTime.getTime();
        this.owner.getEntity().getEvents().trigger("jump");
    }

    @Override
    public void update() {
        long elapsedTime = gameTime.getTimeSince(startTime);
        float t = Math.min((float)elapsedTime / jumpDuration, 1);  // Time ratio from 0 to 1

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
    }

    @Override
    public int getPriority() {
        return 10;
    }
}