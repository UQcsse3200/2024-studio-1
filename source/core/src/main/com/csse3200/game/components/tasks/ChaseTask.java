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

/**
 * Chases a target entity until it gets too far away or the line of sight is lost.
 * Requires an entity with a PhysicsMovementComponent to function.
 */
public class ChaseTask extends DefaultTask implements PriorityTask {
  private final Entity target; // The entity to chase.
  private final int priority; // Priority when the task is active.
  private final float viewDistance; // Distance within which chasing can start.
  private final float maxChaseDistance; // Maximum distance to keep chasing before giving up.
  private final float chaseSpeed; // Speed at which the entity chases.
  private final PhysicsEngine physics; // Physics engine for raycasting.
  private final DebugRenderer debugRenderer; // Renderer for debugging visuals.
  private final RaycastHit hit = new RaycastHit(); // Stores raycast hit information.
  private MovementTask movementTask; // Task for handling movement towards the target.

  /**
   * Constructs a ChaseTask.
   *
   * @param target The entity to chase.
   * @param priority Task priority when chasing (negative when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   * @param chaseSpeed The speed at which the entity chases.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float chaseSpeed) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.chaseSpeed = chaseSpeed;
    physics = ServiceLocator.getPhysicsService().getPhysics(); // Get physics engine.
    debugRenderer = ServiceLocator.getRenderService().getDebug(); // Get debug renderer.
  }

  /**
   * Start the chase task by initializing the movement task towards the target.
   */
  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition()); // Create a movement task towards the target.
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(chaseSpeed); // Set the chase speed.

    this.owner.getEntity().getEvents().trigger("walk"); // Trigger walk event.
  }

  /**
   * Update the chase behavior by continually moving towards the target's current position.
   */
  @Override
  public void update() {
    movementTask.setTarget(target.getPosition()); // Update target position in case it has moved.
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start(); // Restart the movement task if it has stopped.
    }
  }

  /**
   * Stop the chase task and associated movement task.
   */
  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  /**
   * Get the priority of the chase task based on whether the entity is actively chasing or can start chasing.
   *
   * @return The priority level or -1 if not active.
   */
  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }
    return getInactivePriority();
  }

  /**
   * Calculate the distance to the target entity.
   *
   * @return The distance to the target.
   */
  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * Determine the priority when the task is active.
   *
   * @return The priority or -1 if the target is too far or not visible.
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far or not visible, stop chasing.
    }
    return priority;
  }

  /**
   * Determine the priority when the task is inactive.
   *
   * @return The priority if the target is within view distance and visible, otherwise -1.
   */
  private int getInactivePriority() {
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
}
