package com.csse3200.game.ai;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.components.tasks.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task-based AI component. Given a list of tasks with priorities, the AI component will run the
 * highest priority task each frame. Tasks can be made up of smaller sub-tasks. A negative priority
 * indicates that the task should not be run.
 *
 * <p>This is a simple implementation of Goal-Oriented Action Planning (GOAP), a common AI decision
 * algorithm in games that's more powerful than Finite State Machines (FSMs) (State pattern).
 */
public class BossAIComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

  private enum State {
    IDLE, CHARGE, JUMP, CHASE, RETREAT, AOE_ATTACK, RANGE_ATTACK, WAIT
  }

  private State currentState = State.IDLE;
  private Entity target;
  private NPCConfigs.NPCConfig config;

  // Components for attack and movement
  private MeleeAttackComponent meleeAttackComponent;
  private RangeAttackComponent rangeAttackComponent;
  //private AOEAttackComponent aoeAttackComponent;

  // Movement tasks
  private ChargeTask chargeTask;
  private ChaseTask chaseTask;
  private RunAwayTask runAwayTask;
  private WaitTask waitTask;
  private WanderTask wanderTask;
  private PriorityTask currentTask;

  public BossAIComponent(Entity target, NPCConfigs.NPCConfig config) {
    this.target = target;
    this.config = config;
  }

  @Override
  public void create() {
    super.create();

    // Initialize tasks with configurations
    chargeTask = new ChargeTask(target, config.tasks.charge);
    chaseTask = new ChaseTask(target, config.tasks.chase);
    runAwayTask = new RunAwayTask(target, config.tasks.runAway);
    waitTask = new WaitTask(3,1);
    wanderTask = new WanderTask(config.tasks.wander);

    // Initialize attack components
    meleeAttackComponent = getEntity().getComponent(MeleeAttackComponent.class);
    rangeAttackComponent = getEntity().getComponent(RangeAttackComponent.class);
    //aoeAttackComponent = getEntity().getComponent(AOEAttackComponent.class);

    setState(State.IDLE);
  }

  /**
   * On update, run random task. 
   */
  @Override
  public void update() {
    super.update();
    Vector2 targetPosition = target.getPosition();
    Vector2 currentPosition = getEntity().getPosition();
    float distanceToTarget = currentPosition.dst(targetPosition);

    switch (currentState) {
      case IDLE:
        handleIdleState(distanceToTarget);
        break;
      case CHARGE:
        handleChargeState(distanceToTarget);
        break;
      case JUMP:
        handleJumpState(distanceToTarget);
        break;
      case CHASE:
        handleChaseState(distanceToTarget);
        break;
      case RETREAT:
        handleRetreatState(distanceToTarget);
        break;
      case AOE_ATTACK:
        handleAOEAttackState(distanceToTarget);
        break;
      case RANGE_ATTACK:
        handleRangeAttackState(distanceToTarget);
        break;
      case WAIT:
        handleWaitState();
        break;
    }
  }

  private void handleIdleState(float distanceToTarget) {
    if (distanceToTarget > 5 && distanceToTarget < 10) {
      setState(State.CHARGE);
    } else if (distanceToTarget > 2 && distanceToTarget < 5) {
      setState(State.CHASE);
    } else if (distanceToTarget < 2) {
      setState(State.RETREAT);
    } else {
      setState(State.RANGE_ATTACK);
    }
  }

  private void handleChargeState(float distanceToTarget) {
    if (distanceToTarget < 2) {
      setState(State.WAIT);
    }
  }

  private void handleJumpState(float distanceToTarget) {
    setState(State.AOE_ATTACK);
  }

  private void handleChaseState(float distanceToTarget) {
    if (distanceToTarget < 2) {
      setState(State.AOE_ATTACK);
    } else {
      setState(State.RANGE_ATTACK);
    }
  }

  private void handleRetreatState(float distanceToTarget) {
    if (distanceToTarget > 2) {
      setState(State.WAIT);
    } else {
      setState(State.AOE_ATTACK);
    }
  }

  private void handleAOEAttackState(float distanceToTarget) {
    setState(State.WAIT);
  }

  private void handleRangeAttackState(float distanceToTarget) {
    setState(State.WAIT);
  }

  private void handleWaitState() {
    setState(State.IDLE);
  }

  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  private void setState(State newState) {
    currentState = newState;
    // Clear all tasks and disable attacks

    // Set up the new state
    switch (newState) {
      case CHARGE:
        changeTask(chargeTask);
        break;
      case CHASE:
        changeTask(chaseTask);
        break;
      case RETREAT:
        changeTask(runAwayTask);
        break;
      case WAIT:
        changeTask(waitTask);
        break;
      case AOE_ATTACK:
        //changeTask(waitTask);
        //aoeAttackComponent.triggerAttack();
        break;
      case RANGE_ATTACK:
        changeTask(waitTask);
        rangeAttackComponent.enableForNumAttacks(10);
        break;
      // Add other cases as needed
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
