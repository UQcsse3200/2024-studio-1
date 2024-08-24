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

public class ChargeTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(ChargeTask.class);
  final Entity target;
  final int priority;
  final float viewDistance;
  final float maxChaseDistance;
  final float chaseSpeed;
  final float stunTime = 2f;
  final PhysicsEngine physics;
  final DebugRenderer debugRenderer;
  final RaycastHit hit = new RaycastHit();
  MovementTask movementTask;
  WaitTask waitTask;
  Task currentTask;

  public ChargeTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float chaseSpeed) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.chaseSpeed = chaseSpeed;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    initialiseTasks();
    startMoving();
    this.owner.getEntity().getEvents().trigger("chaseStart");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask) {
        this.owner.getEntity().getEvents().trigger("chaseEnd");
        startWaiting();
      } else {
        this.owner.getEntity().getEvents().trigger("chaseStart");
        startMoving();
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

  void initialiseTasks() {
    waitTask = new WaitTask(stunTime);
    waitTask.create(owner);
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(chaseSpeed);
  }

  private void startMoving() {
    logger.debug("Starting moving towards {}", target.getPosition());
    movementTask.setTarget(target.getPosition());
    swapTask(movementTask);
  }

  void startWaiting() {
    logger.debug("Starting waiting");
    if (movementTask != null) {
        movementTask.stop();
    }
    swapTask(waitTask);
  }

  void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  boolean isTargetVisible() {
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