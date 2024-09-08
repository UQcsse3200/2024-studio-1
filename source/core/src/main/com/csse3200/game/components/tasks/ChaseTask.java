package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.components.DirectionalNPCComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.components.DirectionalNPCComponent;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final float chaseSpeed;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   * @param chaseSpeed The speed at which an entity chases at.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float chaseSpeed) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.chaseSpeed = chaseSpeed;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    movementTask.setVelocity(chaseSpeed);
    char dir = getDirection(getVectorTo());
    DirectionalNPCComponent getDirection = owner.getComponent(DirectionalNPCComponent.class);
    Boolean directable = getDirection.isDirectable();
    if(directable){
        //right movement 
        PhysicsMovementComponent direction = owner.getComponent(PhysicsMovementComponent.class);
        if(dir == '<'){
            this.owner.getEntity().getEvents().trigger("walk_right");
        }
        else{
            this.owner.getEntity().getEvents().trigger("walk_left");
        }
    }
    else{
        this.owner.getEntity().getEvents().trigger("walk");
    }
  }

  @Override
  public void update() {
    movementTask.setTarget(target.getPosition());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  private boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point);
      return false;
    }
    debugRenderer.drawLine(from, to);
    return true;
  }
private char getDirection(Vector2 destination) {
    if (owner.getEntity().getPosition().x - destination.x < 0) {
      return '>';
    }
    if (owner.getEntity().getPosition().x - destination.x > 0) {
      return '<';
    }
    return '=';
  }

  /**
   
    Calculates the desired vector position to move away from the target*
    @return The vector position.*/
    private Vector2 getVectorTo(){
      float currentX = owner.getEntity().getPosition().x;
      float currentY = owner.getEntity().getPosition().y;

        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;

        float newX = currentX + (currentX - targetX);
        float newY = currentY + (currentY - targetY);

        Vector2 newPos = new Vector2(newX, newY);
        return newPos;
      }
}
