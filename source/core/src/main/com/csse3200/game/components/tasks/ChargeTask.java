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
 * Task for an entity to charge towards a target entity. The task will move the entity towards the
 * target's current position in a straight line if it is within a certain view distance. After
 * arriving, the entity will wait for a short time before starting to move again.
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
    this.priority = config.priority;
    this.basePriority = config.priority;
    this.viewDistance = config.viewDistance;
    this.maxChaseDistance = config.chaseDistance;
    this.chaseSpeed = config.chaseSpeed;
    this.waitTime = config.waitTime;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    if (movementTask == null) {
      initialiseTasks();
    }
    startMoving();
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask && waitTime > 0) {
        startWaiting();
      } else {
        if (priority == triggerPriority) {
          resetPriority();
        } else {
          startMoving();
        }
      }
    }
    currentTask.update();
  }

  @Override
  public void stop() {
    super.stop();
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  @Override
  public int getPriority() {
    if (status == Task.Status.ACTIVE) {
      return getActivePriority();
    }
    return getInactivePriority();
  }

  private void initialiseTasks() {
    waitTask = new WaitTask(waitTime, priority + 1);
    waitTask.create(owner);
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(chaseSpeed);
  }

  protected void startMoving() {
    logger.debug("Starting moving towards {}", target.getPosition());
    movementTask.setTarget(target.getPosition());
    movementTask.setVelocity(chaseSpeed);
    swapTask(movementTask);
    this.owner.getEntity().getEvents().trigger("run");
  }


  private void startWaiting() {
    logger.debug("Starting waiting");
    swapTask(waitTask);
    this.owner.getEntity().getEvents().trigger("gesture");
  }

  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  protected float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  protected int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

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