package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
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
  private static final int PRIORITY = 4;
  private final Entity target;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final float chaseSpeed;
  private String direction;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;

  /**
   * Constructs a ChaseTask.
   *
   * @param target The entity to chase.
   * @param config Configuration for the chase task.
   */
  public ChaseTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.ChaseTaskConfig config) {
    this.target = target;
    this.viewDistance = config.viewDistance;
    this.maxChaseDistance = config.chaseDistance;
    this.chaseSpeed = config.chaseSpeed;
    this.physics = ServiceLocator.getPhysicsService().getPhysics();
    this.debugRenderer = ServiceLocator.getRenderService().getDebug();
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
    movementTask.setVelocity(chaseSpeed);
    direction = owner.getEntity().getComponent(DirectionalNPCComponent.class).getDirection();
    this.owner.getEntity().getEvents().trigger("walk");
  }

  /**
   * Update the chase behavior by continually moving towards the target's current position.
   */
  @Override
  public void update() {
    movementTask.setTarget(target.getPosition()); // Update target position in case it has moved.
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    } else if (!direction.equals(owner.getEntity().getComponent(DirectionalNPCComponent.class).getDirection())) {
      direction = owner.getEntity().getComponent(DirectionalNPCComponent.class).getDirection();
      this.owner.getEntity().getEvents().trigger("walk");
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
    return PRIORITY;
  }

  /**
   * Determine the priority when the task is inactive.
   *
   * @return The priority if the target is within view distance and visible, otherwise -1.
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return PRIORITY;
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

  /**

    Calculates the desired vector position to move away from the target*
    @return The vector position.*/
    private Vector2 getVectorTo(){
      float currentX = owner.getEntity().getPosition().x;
      float currentY = owner.getEntity().getPosition().y;

        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;

        float newX = currentX + (currentX - targetX);
        float newY = currentY + (currentY - targetY);

        Vector2 newPos = new Vector2(newX, newY);
        return newPos;
      }

    /**
    * Gets the current target
    * @return the current target
    */
    public Entity getTarget() {
      return target;
    }

}
