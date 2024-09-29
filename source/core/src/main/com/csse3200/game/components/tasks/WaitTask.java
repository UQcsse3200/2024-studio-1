package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task that does nothing other than waiting for a given time.
 * The task's status changes to FINISHED after the specified duration has passed.
 */
public class WaitTask extends DefaultTask implements PriorityTask {
  private final GameTime timeSource; // Reference to the game's time source.
  private final float duration; // Duration to wait, in seconds.
  private long endTime; // The time at which the task should finish.
  private final int priority; // Priority level of the wait task.

  /**
   * Constructs a WaitTask.
   *
   * @param duration How long to wait for, in seconds.
   * @param priority The priority level of this task.
   */
  public WaitTask(float duration, int priority) {
    timeSource = ServiceLocator.getTimeSource(); // Get the game's time source.
    this.duration = duration;
    this.priority = priority;
  }

  /**
   * Start the wait task. Sets the endTime based on the current time and duration.
   */
  @Override
  public void start() {
    super.start();
    endTime = timeSource.getTime() + (int)(duration * 1000); // Convert duration to milliseconds.
    owner.getEntity().getEvents().trigger("idle");
  }

  /**
   * Update the task status. If the current time has reached or passed the endTime, finish the task.
   */
  @Override
  public void update() {
    if (timeSource.getTime() >= endTime) {
      status = Status.FINISHED; // Mark the task as finished.
    }
  }

  /**
   * Get the priority of the task.
   *
   * @return The priority level of this task.
   */
  @Override
  public int getPriority() {
    return priority;
  }
}
