package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.projectile.ProjectileAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AnimalProjectileConfig;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShootTask extends DefaultTask implements PriorityTask {

    private static final Logger logger = LoggerFactory.getLogger(ShootTask.class);
    protected final Entity target;
    private final float attackRange;
    private final int priority;
    private final float waitTime;
    private final float spreadAngle = 0.08f;
    private ProjectileConfig bulletConfig;
    private final ShootType type = ShootType.SINGLE;

    private final GameTime timeSource;
    private long endTime;

    private final ProjectileFactory projectileFactory = new ProjectileFactory();
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();

    public ShootTask(Entity target, float attackRange, float waitTime, int priority) {
        this.target = target;
        this.attackRange = attackRange;
        this.waitTime = waitTime;
        this.priority = priority;

        timeSource = ServiceLocator.getTimeSource();
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        bulletConfig = new AnimalProjectileConfig();
    }

    @Override
    public int getPriority() {
        if (status == Task.Status.ACTIVE) {
            return getActivePriority();
        }
        return getInactivePriority();
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > attackRange || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < attackRange && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    @Override
    public void update() {
        if (status == Status.ACTIVE) {
            // Shoot when status is active
            Vector2 shootDirection = getDirection(target);
            if (shoot(shootDirection)) {
                status = Status.INACTIVE;
                System.out.println(timeSource.getTime());
                logger.info("NPC {} shoot target.", owner.getEntity().getId());
                endTime = timeSource.getTime() + (int)(waitTime * 300);
            }
        } else {
            if (timeSource.getTime() >= endTime) {
                status = Status.ACTIVE;
            }
        }
    }

    private Vector2 getDirection(Entity target) {
        if (target == null) {
            return new Vector2(0, 0);
        }
        Vector2 from = this.owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();
        return to.cpy().sub(from).nor();
    }

    /**
     * Check whether the target is visible
     * @return True if visible (no obstacle in-between), otherwise False
     */
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

    /**
     * Return distance from NPC to target
     * @return distance in float
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    /**
     * Shoot at the specific direction from NPC
     * @param direction The direction to shoot at
     * @return whether the action is executable or not
     * (currently won't shoot if obstacle is in-between)
     */
    private boolean shoot(Vector2 direction) {
        if (this.type == ShootType.SPREAD) {
            // return spreadShoot(direction, 5);
        }
        return singleShoot(direction);
    }

    /**
     * Shoot at the specific direction from NPC
     * @param direction The direction to shoot at
     * @return whether the action is executable or not
     * (currently won't shoot if obstacle is in-between)
     */
    private boolean singleShoot(Vector2 direction) {
        if (isTargetVisible() && getDistanceToTarget() < attackRange) {
            Entity projectile = projectileFactory.createProjectile(this.bulletConfig, direction, owner.getEntity().getPosition());
            projectile.getComponent(ProjectileAttackComponent.class).create();
            ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9,9), true, true);
            logger.info("Ranged weapon shoot");
            return true;
        }
        return false;
    }

    private boolean spreadShoot(Vector2 direction, int numShot) {
        if (numShot == 1) {
            return shoot(direction);
        }
        if (isTargetVisible() && getDistanceToTarget() < attackRange) {
            for (int i = 0; i < numShot; i++) {
                singleShoot(rotate(direction, spreadAngle * ((numShot - 1) - 2 * i)));
            }
            return true;
        }
        return false;
    }

    private Vector2 rotate(Vector2 mainRay, float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float newX = mainRay.x * cos - mainRay.y * sin;
        float newY = mainRay.x * sin + mainRay.y * cos;

        return new Vector2(newX, newY);
    }
}

enum ShootType {
    SINGLE, // Single shot
    SPREAD, // Spread shot
}