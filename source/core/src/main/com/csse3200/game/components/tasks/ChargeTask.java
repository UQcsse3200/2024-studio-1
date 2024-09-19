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
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for an entity to charge towards a target entity.
 * The entity will move towards the target's current position at high speed if within a certain view distance.
 * After arriving or if the target is out of range, the entity may wait before moving again.
 */
public class ChargeTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(ChargeTask.class);
  protected final Entity target;
  protected int priority;
  private final int basePriority;
  private final int triggerPriority = 15;
  private final float viewDistance;
  protected final float maxChaseDistance;
  private final float chaseSpeed;
  private final float waitTime;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  protected MovementTask movementTask;
  WaitTask waitTask;
  private Task currentTask;
  private AOEAttackComponent aoeAttackComponent;

  /**
   * Creates a ChargeTask.
   *
   * @param target Entity to charge towards.
   * @param config Configuration for the charge task.
   */
  public ChargeTask(Entity target, NPCConfigs.NPCConfig.TaskConfig.ChargeTaskConfig config) {
    this.target = target;
    this.priority = config.priority;
    this.basePriority = config.priority;
    this.viewDistance = config.viewDistance;
    this.maxChaseDistance = config.chaseDistance;
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
    super.start();
    if (movementTask == null) {
      initialiseTasks();
    }
    aoeAttackComponent = owner.getEntity().getComponent(AOEAttackComponent.class);
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
        if (priority == triggerPriority) {
          resetPriority();
        } else {
          startMoving();
        }
      }
    }
    currentTask.update(); // Continue updating the active task.

    // Perform AOE attack if close to target
    if (canPerformAOEAttack()) {
      aoeAttackComponent.performAttack();
    }
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

    // Set the AOE attack origin to the current position
    if (aoeAttackComponent != null) {
      aoeAttackComponent.setOrigin(owner.getEntity().getPosition());
    }
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
    if (canPerformAOEAttack()) {
      return priority + 1; // Increase priority if can perform AOE attack
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

  private void resetPriority() {
    this.priority = basePriority;
  }

  /**
   * Triggers the charge task to start charging towards the target once.
   */
  public void triggerCharge() {
    this.priority = triggerPriority;
  }

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

  private boolean canPerformAOEAttack() {
    return aoeAttackComponent != null && getDistanceToTarget() <= aoeAttackComponent.getRadius();
  }
}