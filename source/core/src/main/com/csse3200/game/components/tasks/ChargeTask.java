package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
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
  protected final Entity target; // The entity to charge towards.
  protected final int priority; // Priority of this task.
  private final float viewDistance; // Distance within which charging can start.
  protected final float maxChaseDistance; // Maximum distance to keep charging before giving up.
  private final float chaseSpeed; // Speed of the charge.
  private final float waitTime; // Time to wait after charging.
  private final PhysicsEngine physics; // Physics engine for raycasting.
  private final DebugRenderer debugRenderer; // Renderer for debugging visuals.
  private final RaycastHit hit = new RaycastHit(); // Stores raycast hit information.
  protected MovementTask movementTask; // Task for handling movement towards the target.
  private WaitTask waitTask; // Task for waiting after charging.
  private Task currentTask; // The current active task.

  /**
   * Creates a ChargeTask.
   *
   * @param target Entity to charge towards.
   * @param priority Task priority while charging.
   * @param viewDistance Maximum distance from the entity at which charging can start.
   * @param maxChaseDistance Maximum distance from the entity while charging before giving up.
   * @param chaseSpeed The speed at which the entity charges.
   * @param waitTime How long to wait after charging.
   */
  public ChargeTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float chaseSpeed,
                    float waitTime) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.chaseSpeed = chaseSpeed;
    this.waitTime = waitTime;
    physics = ServiceLocator.getPhysicsService().getPhysics(); // Get physics engine.
    debugRenderer = ServiceLocator.getRenderService().getDebug(); // Get debug renderer.
  }

  /**
   * Start the charge task. Initializes tasks if not already done.
   */
  @Override
  public void start() {
    super.start();
    if (movementTask == null) {
      initialiseTasks();
    }
    startMoving(); // Begin charging towards the target.
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
        startMoving(); // Start moving again.
      }
    }
    currentTask.update(); // Continue updating the active task.
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
    if (status == Task.Status.ACTIVE) {
      return getActivePriority();
    }
    return getInactivePriority();
  }

  /**
   * Initializes the movement and wait tasks for charging behavior.
   */
  protected void initialiseTasks() {
    waitTask = new WaitTask(waitTime, priority + 1);
    waitTask.create(owner);
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(chaseSpeed); // Set charge speed.
  }

  /**
   * Start moving towards the target.
   */
  protected void startMoving() {
    logger.debug("Starting moving towards {}", target.getPosition());
    movementTask.setTarget(target.getPosition()); // Update target position.
    movementTask.setVelocity(chaseSpeed);
    swapTask(movementTask);
    this.owner.getEntity().getEvents().trigger("run");
  }

  /**
   * Start waiting after charging.
   */
  protected void startWaiting() {
    logger.debug("Starting waiting");
    if (movementTask != null) {
      movementTask.stop();
    }
    swapTask(waitTask);
    this.owner.getEntity().getEvents().trigger("gesture");
  }

  /**
   * Swap the current task to a new task.
   *
   * @param newTask The new task to switch to.
   */
  protected void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  /**
   * Calculate the distance to the target entity.
   *
   * @return The distance to the target.
   */
  protected float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * Determine the priority when the task is active.
   *
   * @return The priority or -1 if the target is too far or not visible.
   */
  protected int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far or not visible, stop charging.
    }
    return priority;
  }

  /**
   * Determine the priority when the task is inactive.
   *
   * @return The priority if the target is within view distance and visible, otherwise -1.
   */
  protected int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  /**
   * Checks if the target is visible using a raycast to detect obstacles.
   *
   * @return True if the target is visible, false otherwise.
   */
  protected boolean isTargetVisible() {
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
}
