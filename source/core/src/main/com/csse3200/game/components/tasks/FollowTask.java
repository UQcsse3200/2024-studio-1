package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Chases a target entity until it gets too far away or the line of sight is lost.
 * Requires an entity with a PhysicsMovementComponent to function.
 */
public class FollowTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(FollowTask.class);
  private final float followSpeed;
  private String direction;
  private final float waitTime;
  private Vector2 followRange;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;

  /**
   * Constructs a FollowTask.
   *
   * @param config Configuration for the chase task.
   */
  public FollowTask(TaskConfig.FollowTaskConfig config) {
    this.followRange = new Vector2(config.followRadius, config.followRadius);
    this.waitTime = config.waitTime;
    this.followSpeed = config.followSpeed;
  }

  /**
   * Start the follow task by initializing the movement task towards the target.
   */
  @Override
  public void start() {
    super.start();
    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange()); // Move to a random position within the range 
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(followSpeed);
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
  /**
   * Stop the chase task and associated movement task.
   */
  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  /**
   * Get the priority of the task 
   *
   * @return 1 as this is lowest priority task 
   */
  @Override
  public int getPriority() {
    return 1; // Low priority task, can be overridden by higher priority tasks.
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
    Vector2 playerPosition = ServiceLocator.getGameAreaService().getGameArea().getPlayer().getPosition();
    Vector2 halfRange = followRange.cpy().scl(0.5f);
    Vector2 min = playerPosition.cpy().sub(halfRange); // Minimum boundary of the range.
    Vector2 max = playerPosition.cpy().add(halfRange); // Maximum boundary of the range.
    return RandomUtils.random(min, max); // Generate a random position between min and max.
  }
}
