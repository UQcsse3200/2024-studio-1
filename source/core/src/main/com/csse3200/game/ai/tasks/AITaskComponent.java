package com.csse3200.game.ai.tasks;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.attack.AOEAttackComponent;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.components.tasks.ChargeTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.entities.Entity;
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
public class AITaskComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

  private final List<PriorityTask> priorityTasks = new ArrayList<>(2);
  private PriorityTask currentTask;

  private Entity currentTarget;

  /**
   * Add a priority task to the list of tasks. This task will be run only when it has the highest
   * priority, and can be stopped to run a higher priority task.
   *
   * @param task Task to add
   * @return self
   */
  public AITaskComponent addTask(PriorityTask task) {
    logger.debug("{} Adding task {}", this, task);
    priorityTasks.add(task);
    task.create(this);

    return this;
  }

  /**
   * On update, run the current highest priority task. If it's a different one, stop the old one and
   * start the new one. If the highest priority task has negative priority, no task will be run.
   */
  @Override
  public void update() {
    PriorityTask desiredtask = getHighestPriorityTask();
    if (desiredtask == null || desiredtask.getPriority() < 0) {
      return;
    }

    if (desiredtask != currentTask || currentTask.getStatus() == Task.Status.INACTIVE) {
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

  private PriorityTask getHighestPriorityTask() {
    try {
      return Collections.max(priorityTasks, Comparator.comparingInt(PriorityTask::getPriority));
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  private void changeTask(PriorityTask desiredTask) {
    logger.debug("{} Changing to task {}", this, desiredTask);
    if (currentTask != null) {
      currentTask.stop();
    }

    updateTarget(desiredTask);

    currentTask = desiredTask;
    if (desiredTask != null) {
      desiredTask.start();
    }
  }

  /**
   * If the desiredTask has a new target, update the target for MeleeAttackComponent/RangeAttackComponent
   * @param desiredTask the desired task for AI entity to perform
   */
  public void updateTarget(PriorityTask desiredTask) {
    if (desiredTask instanceof ChaseTask chaseTask) {
      Entity newTarget = ((ChaseTask) desiredTask).getTarget();
      if (newTarget != currentTarget) {
        currentTarget = newTarget;
        updateAttackTarget();
      }
    } else if (desiredTask instanceof ChargeTask chargeTask) {
      Entity newTarget = ((ChargeTask) desiredTask).getTarget();
      if (newTarget != currentTarget) {
        currentTarget = newTarget;
        updateAttackTarget();
      }
    }
  }

  /**
   * Updates the AttackComponentTarget for the AI entity
   */
  private void updateAttackTarget() {
    if (entity.getComponent(MeleeAttackComponent.class) != null) {
      entity.getComponent(MeleeAttackComponent.class).updateTarget(currentTarget);
    }
    if (entity.getComponent(RangeAttackComponent.class) != null) {
      entity.getComponent(RangeAttackComponent.class).updateTarget(currentTarget);
    }
    if (entity.getComponent(AOEAttackComponent.class) != null) {
      entity.getComponent(AOEAttackComponent.class).setOrigin(entity.getPosition());

    }

  }

}
