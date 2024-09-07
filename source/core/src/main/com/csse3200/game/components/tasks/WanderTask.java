package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving to a random position within a specified range
 * from the starting position. After moving, wait for a defined amount of time.
 * Requires an entity with a PhysicsMovementComponent to function.
 */
public class WanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);

  private final Vector2 wanderRange; // The maximum distance the entity can move from its starting point.
  private final float waitTime; // Time to wait between movements.
  private final float wonderSpeed; // Speed of the entity's movement.
  private Vector2 startPos; // Starting position of the entity.
  private MovementTask movementTask; // Task for managing movement behavior.
  private WaitTask waitTask; // Task for managing waiting behavior.
  private Task currentTask; // Keeps track of the current active task (movement or wait).

  /**
   * Constructor for creating a WanderTask.
   *
   * @param wanderRange Distance in X and Y the entity can move from its starting position.
   * @param waitTime How long to wait between wandering movements.
   * @param wonderSpeed Speed at which the entity moves while wandering.
   */
  public WanderTask(Vector2 wanderRange, float waitTime, float wonderSpeed) {
    this.wanderRange = wanderRange;
    this.waitTime = waitTime;
    this.wonderSpeed = wonderSpeed;
  }

  @Override
  public int getPriority() {
    return 1; // Low priority task, can be overridden by higher priority tasks.
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition(); // Store the entity's starting position.

    // Initialize wait and movement tasks.
    waitTask = new WaitTask(waitTime, 0);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange()); // Move to a random position within the wander range.
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(wonderSpeed); // Set the wandering speed.
    currentTask = movementTask;

    this.owner.getEntity().getEvents().trigger("walk"); // Trigger walk event for animations or other effects.
  }

  @Override
  public void update() {
    // Check if the current task is active. If not, switch to the opposite task.
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask) {
        startWaiting(); // Finished moving, start waiting.
      } else {
        startMoving(); // Finished waiting, start moving.
      }
    }
    currentTask.update(); // Continue updating the active task.
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("idle"); // Trigger idle event (e.g., for animations).
    swapTask(waitTask); // Switch to the waiting task.
  }

  private void startMoving() {
    logger.debug("Starting moving");
    movementTask.setTarget(getRandomPosInRange()); // Set a new random target for the next movement.
    this.owner.getEntity().getEvents().trigger("walk");
    swapTask(movementTask); // Switch to the movement task.
  }

  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop(); // Stop the current task before switching.
    }
    currentTask = newTask;
    currentTask.start(); // Start the new task.
  }

  private Vector2 getRandomPosInRange() {
    // Calculate a random position within the wander range around the starting point.
    Vector2 halfRange = wanderRange.cpy().scl(0.5f);
    Vector2 min = startPos.cpy().sub(halfRange); // Minimum boundary of the range.
    Vector2 max = startPos.cpy().add(halfRange); // Maximum boundary of the range.
    return RandomUtils.random(min, max); // Generate a random position between min and max.
  }
}
