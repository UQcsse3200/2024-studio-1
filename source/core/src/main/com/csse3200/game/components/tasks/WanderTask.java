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
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class WanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);

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
    return 1; // Low priority task
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition();

    waitTask = new WaitTask(waitTime, 0);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange());
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(wanderSpeed);
    currentTask = movementTask;

    this.owner.getEntity().getEvents().trigger("walk");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask) {
        startWaiting();
      } else {
        startMoving();
      }
    }
    currentTask.update();
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("idle");
    swapTask(waitTask);
  }

  private void startMoving() {
    logger.debug("Starting moving");
    movementTask.setTarget(getRandomPosInRange());
    this.owner.getEntity().getEvents().trigger("walk");
    swapTask(movementTask);
  }

  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  private Vector2 getRandomPosInRange() {
    Vector2 halfRange = wanderRange.cpy().scl(0.5f);
    Vector2 min = startPos.cpy().sub(halfRange);
    Vector2 max = startPos.cpy().add(halfRange);
    return RandomUtils.random(min, max);
  }
}