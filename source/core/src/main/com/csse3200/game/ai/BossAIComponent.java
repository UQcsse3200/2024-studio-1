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
import com.csse3200.game.entities.configs.BossConfig;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AI component for boss entities. This will generate tasks and attack components for the boss entity.
 * The boss will switch between different states based on the distance to the target entity, and it's last state.
 * In each state, the boss will perform different actions using tasks and attack components.
 *
 * <p>This is a simple implementation of Goal-Oriented Action Planning (GOAP), a common AI decision
 * algorithm in games that's more powerful than Finite State Machines (FSMs) (State pattern).
 */
public class BossAIComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

  private enum State {
    IDLE, WANDER, CHARGE, JUMP, CHASE, RETREAT, AOE_ATTACK, RANGE_ATTACK, RANGE_ATTACK_2, WAIT
  }
  private State currentState = State.IDLE;
  private State previousState = State.IDLE;
  private final Entity target;
  private final BossConfig config;
  private GameTime timeSource;
  private long endTime;


  // Components for attack and movement
  private MeleeAttackComponent meleeAttackComponent;
  private RangeAttackComponent rangeAttackComponent;
  // private AOEAttackComponent aoeAttackComponent;

  // Movement tasks
  private ChargeTask chargeTask;
  private ChaseTask chaseTask;
  private RunAwayTask runAwayTask;
  private JumpTask jumpTask;
  private WaitTask waitTask;
  private WanderTask wanderTask;
  private PriorityTask currentTask;

  public BossAIComponent(Entity target, BossConfig config) {
    this.target = target;
    this.config = config;
  }

  @Override
  public void create() {
    super.create();
    timeSource = ServiceLocator.getTimeSource();
    // Initialize tasks with configurations
    chargeTask = new ChargeTask(target, config.tasks.charge);
    chaseTask = new ChaseTask(target, config.tasks.chase);
    runAwayTask = new RunAwayTask(target, config.tasks.runAway);
    jumpTask = new JumpTask(target);
    waitTask = new WaitTask(config.waitTime,1);
    wanderTask = new WanderTask(config.tasks.wander);
    chargeTask.create(this);
    chaseTask.create(this);
    runAwayTask.create(this);
    jumpTask.create(this);
    waitTask.create(this);
    wanderTask.create(this);

    // Initialize attack components
    if (config.attacks.melee == null) {
      logger.error("Melee attack component is required for boss entities");
    }
    meleeAttackComponent = getEntity().getComponent(MeleeAttackComponent.class);
    meleeAttackComponent.setEnabled(false);
    if (config.attacks.ranged != null) {
      rangeAttackComponent = getEntity().getComponent(RangeAttackComponent.class);
      rangeAttackComponent.setEnabled(false);
    }
//    if (config.attacks.aoe != null) {
//      aoeAttackComponent = getEntity().getComponent(AOEAttackComponent.class);
//      aoeAttackComponent.setEnabled(false);
//    }

    setState(State.IDLE);
  }

  /**
   * On update, update current state.
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
      case WANDER:
        handleWanderState(distanceToTarget);
        break;
      case CHARGE:
        handleChargeState();
        break;
      case JUMP:
        handleJumpState();
        break;
      case CHASE:
        handleChaseState(distanceToTarget);
        break;
      case RETREAT:
        handleRetreatState();
        break;
      case AOE_ATTACK:
        handleAOEAttackState();
        break;
      case RANGE_ATTACK, RANGE_ATTACK_2:
        handleRangeAttackState();
        break;
      case WAIT:
        handleWaitState();
        break;
    }
    currentTask.update();
  }

  private void handleIdleState(float distanceToTarget) {
    if (distanceToTarget > config.wanderMinRange) {
      setState(State.WANDER);
      previousState = State.WANDER;
    } else if (distanceToTarget > config.chargeMinRange && distanceToTarget < config.chargeMaxRange
            && previousState != State.CHARGE) {
      setState(State.CHARGE);
      previousState = State.CHARGE;
    } else if (distanceToTarget > config.chaseMinRange && distanceToTarget < config.chaseMaxRange
            && previousState != State.CHASE) {
      setState(State.CHASE);
      previousState = State.CHASE;
    } else if (distanceToTarget > config.jumpMinRange && distanceToTarget < config.jumpMaxRange
            && previousState != State.JUMP) {
      setState(State.JUMP);
      previousState = State.JUMP;
    } else if (distanceToTarget > config.retreatMinRange && distanceToTarget < config.retreatMaxRange
            && previousState != State.RETREAT) {
      setState(State.RETREAT);
      previousState = State.RETREAT;
    } else if (distanceToTarget > config.rangedMinRange && distanceToTarget < config.rangedMaxRange
            && previousState != State.RANGE_ATTACK) {
      setState(State.RANGE_ATTACK);
      previousState = State.RANGE_ATTACK;
    } else if (distanceToTarget > config.ranged2MinRange && distanceToTarget < config.ranged2MaxRange
            && previousState != State.RANGE_ATTACK_2) {
      setState(State.RANGE_ATTACK_2);
      previousState = State.RANGE_ATTACK;
    } else if (distanceToTarget > config.aoeMinRange && distanceToTarget < config.aoeMaxRange) {
      setState(State.AOE_ATTACK);
      previousState = State.AOE_ATTACK;
    } else {
      State fallback = getFallbackState(config.fallbackState);
      setState(fallback);
      previousState = fallback;
    }
  }

  private void handleWanderState(float distanceToTarget) {
    if (distanceToTarget < 10) {
      setState(State.IDLE);
    }
  }

  private void handleChargeState() {
    if (chargeTask.getStatus() != PriorityTask.Status.ACTIVE) {
      setState(State.WAIT);
    }
  }

  private void handleJumpState() {
    if (jumpTask.getStatus() != PriorityTask.Status.ACTIVE) {
      setState(State.AOE_ATTACK);
    }
  }

  private void handleChaseState(float distanceToTarget) {
    if (timeSource.getTime() >= endTime) {
      if (distanceToTarget < 2) {
        setState(State.AOE_ATTACK);
      } else {
        setState(State.RANGE_ATTACK);
      }
    }
  }

  private void handleRetreatState() {
    if (runAwayTask.getStatus() != PriorityTask.Status.ACTIVE) {
      setState(State.RANGE_ATTACK);
    }
  }

  private void handleAOEAttackState() {
    setState(State.WAIT);
  }

  private void handleRangeAttackState() {
    if (!rangeAttackComponent.isEnabled()) {
      setState(State.WAIT);
    }
  }

  private void handleWaitState() {
    if (waitTask.getStatus() != PriorityTask.Status.ACTIVE) {
      setState(State.IDLE);
    }
  }

  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  private void setState(State newState) {
    currentState = newState;
    // Disable attacks
    meleeAttackComponent.setEnabled(false);

    logger.info("{} Changing state to {}", this, newState);

    // Set up the new state
    switch (newState) {
      case WANDER:
        changeTask(wanderTask);
        break;
      case CHARGE:
        changeTask(chargeTask);
        chargeTask.triggerCharge(1);
        meleeAttackComponent.setEnabled(true);
        break;
      case CHASE:
        changeTask(chaseTask);
        endTime = timeSource.getTime() + (int)(config.chaseTime * 1000);
        meleeAttackComponent.setEnabled(true);
        break;
      case JUMP:
        changeTask(jumpTask);
        //aoeAttackComponent.enableForNumAttacks(1);
        break;
      case RETREAT:
        changeTask(runAwayTask);
        runAwayTask.triggerCharge(1);
        break;
      case WAIT:
        changeTask(waitTask);
        break;
      case AOE_ATTACK:
        //changeTask(waitTask);
        //aoeAttackComponent.enableForNumAttacks(1);
        break;
      case RANGE_ATTACK:
        if (rangeAttackComponent == null) {
          setState(State.WAIT);
        } else {
          changeTask(waitTask);
          rangeAttackComponent.setType("single");
          rangeAttackComponent.enableForNumAttacks(config.rangedAttackNum);
        }
        break;
      case RANGE_ATTACK_2:
        if (rangeAttackComponent == null) {
          setState(State.WAIT);
        } else {
          changeTask(waitTask);
          rangeAttackComponent.setType("spread");
          rangeAttackComponent.enableForNumAttacks(config.ranged2AttackNum);
        }
        break;
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

  private State getFallbackState(String fallbackStateStr) {
      return switch (fallbackStateStr.toUpperCase()) {
          case "WANDER" -> State.WANDER;
          case "CHARGE" -> State.CHARGE;
          case "CHASE" -> State.CHASE;
          case "JUMP" -> State.JUMP;
          case "RETREAT" -> State.RETREAT;
          case "AOE_ATTACK" -> State.AOE_ATTACK;
          case "RANGE_ATTACK" -> State.RANGE_ATTACK;
          case "WAIT" -> State.WAIT;
          default -> State.IDLE;
      };
  }
}
