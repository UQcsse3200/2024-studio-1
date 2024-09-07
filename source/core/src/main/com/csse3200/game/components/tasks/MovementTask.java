package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Moves the entity to a given position, finishing when it gets close enough.
 * Requires an entity with a PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
  private final GameTime gameTime; // Reference to the game's time source.
  private Vector2 target; // The target position to move to.
  private float stopDistance = 0.05f; // Distance from the target at which to stop.
  private long lastTimeMoved; // Time when the entity last moved.
  private Vector2 lastPos; // Last known position of the entity.
  private PhysicsMovementComponent movementComponent; // Component for handling physics-based movement.

  /**
   * Creates a MovementTask with a target position.
   *
   * @param target The target position to move to.
   */
  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource(); // Get the game's time source.
  }

  /**
   * Creates a MovementTask with a target position and a custom stop distance.
   *
   * @param target The target position to move to.
   * @param stopDistance The distance from the target at which to stop.
   */
  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  /**
   * Start the movement task by setting the target and enabling movement.
   */
  @Override
  public void start() {
    super.start();
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class); // Get movement component.
    movementComponent.setTarget(target); // Set movement target.
    movementComponent.setMoving(true); // Enable movement.
    logger.debug("Starting movement towards {}", target);
    lastTimeMoved = gameTime.getTime(); // Record the current time.
    lastPos = owner.getEntity().getPosition(); // Record the current position.
  }

  /**
   * Update the movement task, checking if the entity has reached the target or is stuck.
   */
  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false); // Stop movement.
      status = Status.FINISHED; // Mark task as finished.
      logger.debug("Finished moving to {}", target);
    } else {
      checkIfStuck(); // Check if the entity is stuck.
    }
  }

  /**
   * Sets the target position to move to.
   *
   * @param target The new target position.
   */
  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }

  /**
   * Sets the movement velocity.
   *
   * @param speed The speed to set.
   */
  public void setVelocity(float speed) {
    Vector2 velocity = new Vector2(speed, speed); // Create a velocity vector.
    logger.debug("Setting velocity to {}", velocity);
    movementComponent.setVelocity(velocity); // Set the movement component's velocity.
  }

  /**
   * Stop the movement task.
   */
  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false); // Disable movement.
    logger.debug("Stopping movement");
  }

  /**
   * Get the priority of the movement task.
   *
   * @return The priority level (0 for movement tasks).
   */
  @Override
  public int getPriority() {
    return 0;
  }

  /**
   * Checks if the entity has reached the target position within the stop distance.
   *
   * @return True if at the target, false otherwise.
   */
  private boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

  /**
   * Checks if the entity is stuck (not moving for a certain time).
   */
  private void checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime(); // Update the last time moved.
      lastPos = owner.getEntity().getPosition(); // Update last position.
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false); // Stop movement.
      status = Status.FAILED; // Mark task as failed.
      logger.debug("Got stuck! Failing movement task");
    }
  }

  /**
   * Checks if the entity has moved since the last update.
   *
   * @return True if the entity has moved, false otherwise.
   */
  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f; // Check if the squared distance moved is significant.
  }
}

