package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.configs.NPCConfigs;
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
  private static final int PRIORITY = 1;
  private final Vector2 wanderRange;
  private final float waitTime;
  private final float wanderSpeed;
  private Vector2 startPos;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;

  /**
   * @param config Configuration for the wander task.
   */
  public WanderTask(NPCConfigs.NPCConfig.TaskConfig.WanderTaskConfig config) {
    this.wanderRange = new Vector2(config.wanderRadius, config.wanderRadius);
    this.waitTime = config.waitTime;
    this.wanderSpeed = config.wanderSpeed;
  }

  @Override
  public int getPriority() {
    return PRIORITY; // Low priority task, can be overridden by higher priority tasks.
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition(); // Store the entity's starting position.

    // Initialize wait and movement tasks.
    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange()); // Move to a random position within the wander range.
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(wanderSpeed);
    currentTask = movementTask;
    this.owner.getEntity().getEvents().trigger("walk");
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
    swapTask(waitTask);
    this.owner.getEntity().getEvents().trigger("idle");
  }

  private void startMoving() {
    logger.debug("Starting moving");
    movementTask.setTarget(getRandomPosInRange());
    swapTask(movementTask);
    this.owner.getEntity().getEvents().trigger("walk");
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
