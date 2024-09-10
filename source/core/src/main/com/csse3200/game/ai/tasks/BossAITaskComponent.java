package com.csse3200.game.ai.tasks;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Task-based AI component. Given a list of tasks with priorities, the AI component will run the
 * highest priority task each frame. Tasks can be made up of smaller sub-tasks. A negative priority
 * indicates that the task should not be run.
 *
 * <p>This is a simple implementation of Goal-Oriented Action Planning (GOAP), a common AI decision
 * algorithm in games that's more powerful than Finite State Machines (FSMs) (State pattern).
 */
public class BossAITaskComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

  private final List<PriorityTask> Tasks = new ArrayList<>(2);
  private PriorityTask currentTask;
  //need both values for charge task wait 
  private Boolean moved = true;
  private Boolean attacked;


  /**
   * Add a priority task to the list of tasks. 
   *
   * @param task Task to add
   * @return self
   */
  public BossAITaskComponent addTask(PriorityTask task) {
    logger.debug("{} Adding task {}", this, task);
    Tasks.add(task);
    task.create(this);
    return this;
  }

  private PriorityTask getRandomTask(){
    //for when attacks can be stored in sequence
    return getRandomMovement();
  }

  private PriorityTask getRandomMovement(){
    Random random = new Random();
    int randomIndex = random.nextInt((Tasks.size()-1)); 
    return Tasks.get(randomIndex); 
  }
  /**
   * On update, run random task. 
   */
  @Override
  public void update() {
    PriorityTask desiredtask = getRandomTask();
    if (desiredtask == null) {
      return;
    }

    if (desiredtask != currentTask) {
      changeTask(desiredtask);
    }
    currentTask.update();
  }

  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  private void changeTask(PriorityTask desiredTask) {
    logger.debug("{} Changing to task {}", this, desiredTask);
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = desiredTask;
    if (desiredTask != null) {
      desiredTask.start();
    }
  }
}
