package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for an entity to charge towards a target entity.
 * The entity will move towards the target's current position at high speed if within a certain view distance.
 * After arriving or if the target is out of range, the entity may wait before moving again.
 */
public class ChargeTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(ChargeTask.class);
  private static final int ACTIVE_PRIORITY = 10;
    private static final int INACTIVE_PRIORITY = 6;
  private final Entity target;
  private final float activationMinRange;
  private final float activationMaxRange;
  private final float chaseSpeed;
  private final float waitTime;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;

  /**
   * Creates a ChargeTask.
   *
   * @param target Entity to charge towards.
   * @param config Configuration for the charge task.
   */
  public ChargeTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig config) {
    this.target = target;
    this.activationMinRange = config.activationMinRange;
    this.activationMaxRange = config.activationMaxRange;
    this.chaseSpeed = config.chaseSpeed;
    this.waitTime = config.waitTime;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  /**
   * Start the charge task. Initializes tasks if not already done.
   */
  @Override
  public void start() {
    logger.debug("Starting to charge towards {}", target);
    super.start();
    Vector2 targetPos = target.getPosition();

    // Initialise tasks
    if (waitTask == null) {
      waitTask = new WaitTask(waitTime, 1);
      waitTask.create(owner);
    }
    if (movementTask == null) {
      movementTask = new MovementTask(targetPos);
      movementTask.create(owner);
    }
    movementTask.start();
    movementTask.setTarget(targetPos);
    movementTask.setVelocity(chaseSpeed);
    currentTask = movementTask;

    // Trigger the run animation
    owner.getEntity().getEvents().trigger("run");
  }

  /**
   * Update the charge behavior. Switch between moving and waiting as needed.
   */
  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask && waitTime > 0) {
        startWaiting(); // After moving, start waiting if waitTime is set.
      } else {
        this.stop();
      }
    }
    currentTask.update();
  }

  /**
   * Start waiting after charging.
   */
  private void startWaiting() {
    logger.debug("Starting waiting");
    if (movementTask != null) {
      movementTask.stop();
    }
    currentTask = waitTask;
    currentTask.start();
    this.owner.getEntity().getEvents().trigger("gesture");
  }

  /**
   * Stop the charge task and any current sub-task.
   */
  @Override
  public void stop() {
    super.stop();
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  /**
   * Get the priority of the charge task based on whether the entity is actively charging or can start charging.
   *
   * @return The priority level or -1 if not active.
   */
  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return ACTIVE_PRIORITY;
    }
    float dst = owner.getEntity().getPosition().dst(target.getPosition());
    if (dst >= activationMinRange && dst <= activationMaxRange && isTargetVisible()) {
      return INACTIVE_PRIORITY;
    }
    return -1;
  }

  private boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // Perform a raycast to check for obstacles between the entity and the target.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point); // Draw line to the hit point for debugging.
      return false; // Obstacle detected, target not visible.
    }
    debugRenderer.drawLine(from, to); // Draw line directly to the target for debugging.
    return true; // No obstacle, target is visible.
  }

  /**
   * Gets the current target
   * @return the current target
   */
  public Entity getTarget() {
    return target;
  }
}
