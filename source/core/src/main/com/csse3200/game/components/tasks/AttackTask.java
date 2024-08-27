package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task that used to simulate the attack movement of an entity.
 * Should have priority higher than other movement task (recommend 10).
 */
public class AttackTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(AttackTask.class);
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float chaseSpeed;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask currentTask;

    /**
     * Constructor for AttackTask
     * @param target enemy's entity
     * @param priority priority value for the task
     * @param viewDistance the distance to allow the entity to attack
     * @param chaseSpeed approaching speed of attack movement
     */
    public AttackTask(Entity target, int priority, float viewDistance, float chaseSpeed) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.chaseSpeed = chaseSpeed;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * When started, set as movement task while triggering `attack`
     */
    @Override
    public void start() {
        super.start();
        currentTask = new MovementTask(target.getPosition());
        currentTask.create(owner);
        currentTask.start();
        currentTask.setVelocity(chaseSpeed);
        this.owner.getEntity().getEvents().trigger("attack");
    }

    /**
     * Get the priority of the AttackTask
     * @return the -1 of out of visible range, otherwise `priority`
     */
    public int getPriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    /**
     * Update the active status of the task
     * Out of range = INACTIVE, otherwise = ACTIVE
     */
    @Override
    public void update() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            super.start();
            if (currentTask != null) {
                currentTask.start();
            }
        } else {
            super.stop();
            if (currentTask != null) {
                currentTask.stop();
            }
        }
        assert currentTask != null;
        currentTask.update();
    }

    /**
     * Calculate the distance between the target and current entity
     * @return distance in float
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    /**
     * Check whether the straight path between the target and current entity
     * is blocked.
     * @return True of nothing is in-between, otherwise False
     */
    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}