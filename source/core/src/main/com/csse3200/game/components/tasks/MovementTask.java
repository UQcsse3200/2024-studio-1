package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.components.DirectionalNPCComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
  private final GameTime gameTime;
  private Vector2 target;
  private float stopDistance = 0.05f;
  private long lastTimeMoved;
  private Vector2 lastPos;
  private PhysicsMovementComponent movementComponent;
  private DirectionalNPCComponent directionalComponent;

  /**
   * Creates a MovementTask with a target position.
   *
   * @param target the target position to move to
   */
  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
  }

  /**
   * Creates a MovementTask with a target position and a custom stop distance.
   *
   * @param target the target position to move to
   * @param stopDistance the distance from the target at which to stop
   */
  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  @Override
  public void start() {
    super.start();
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
    this.directionalComponent = owner.getEntity().getComponent(DirectionalNPCComponent.class);
    movementComponent.setTarget(target);
    movementComponent.setMoving(true);
    logger.debug("Starting movement towards {}", target);
    lastTimeMoved = gameTime.getTime();
    lastPos = owner.getEntity().getPosition();
  }

  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false);
      status = Status.FINISHED;
      logger.debug("Finished moving to {}", target);
    } else {
      checkIfStuck();
    }
  }

  /**
   * Sets the target position to move to.
   *
   * @param target the new target position
   */
  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
    updateDirection();
  }

  /**
   * Sets the movement velocity.
   *
   * @param speed the speed to set
   */
  public void setVelocity(float speed) {
    Vector2 velocity = new Vector2(speed,speed);
    logger.debug("Setting velocity to {}", velocity);
    movementComponent.setVelocity(velocity);
  }

  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
    logger.debug("Stopping movement");
  }

  @Override
  public int getPriority() {
    return 0;
  }

  private boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

  private void checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime();
      lastPos = owner.getEntity().getPosition();
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false);
      status = Status.FAILED;
      logger.debug("Got stuck! Failing movement task");
    }
  }

  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
  }

  private void updateDirection() {
    if (directionalComponent != null) {
      Vector2 currentPosition = owner.getEntity().getPosition();
      if (currentPosition.x < target.x) {
        directionalComponent.setDirection("right");
      } else {
        directionalComponent.setDirection("left");
      }
    }
  }
}
