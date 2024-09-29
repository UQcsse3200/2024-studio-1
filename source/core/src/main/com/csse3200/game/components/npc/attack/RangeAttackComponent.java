package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * This attack component supports shooting projectiles from animals
 */
public class RangeAttackComponent extends AttackComponent {

    private final float spreadAngle = 0.1f;
    private final ShootType type;
    private Entity latestProjectile;
    private String projectileName;
    private String attackTrigger;

    private final ProjectileFactory projectileFactory = new ProjectileFactory();

    public RangeAttackComponent(Entity target, float attackRange, float attackRate, int shootType,
                                NPCConfigs.NPCConfig.EffectConfig[] effectConfigs) {
        super(target, attackRange, attackRate, effectConfigs);
        if (shootType == 0) {
            type = ShootType.SINGLE;
        }
        else {
            type = ShootType.SPREAD;
        }
        projectileName = "projectile";
    }

    @Override
    public void create() {
        Entity baseEntity = this.getEntity();
        String baseName = "Dragon";
        if (baseEntity.getComponent(NameComponent.class) == null) {
            // Use for test entity
            projectileName = "dragonProjectile";
            attackTrigger = "fire_attack";
        } else {
            baseName = baseEntity.getComponent(NameComponent.class).getName();
        }
        System.out.println("Ranged animals shooting:");
        System.out.println(baseName);
        if (baseName.equals("Dragon")) {
            projectileName = "dragonProjectile";
            attackTrigger = "fire_attack";
        } else if (baseName.equals("Kitsune")) {
            projectileName = "kitsuneProjectile";
            attackTrigger = "fire1";
        } else {
            projectileName = "projectile";
            attackTrigger = "shoot";
        }
    }

    /**
     * Perform attack for superclass `update` call
     */
    @Override
    public void performAttack() {
        logger.info("{} shoots {}", entity, target);
        // Shoot target
        Vector2 direction = getDirection(target);
        entity.getEvents().trigger("attack");
        shoot(direction);
        // Attack effects
        //applyEffects(target);
    }

    /**
     * Updates the attack target
     * @param newTarget the new target
     */
    public void updateTarget(Entity newTarget) {
        this.target = newTarget;
    }

    /**
     * Return the direction from this entity towards its target
     * @param target The target entity
     * @return direction from this entity towards its target
     */
    private Vector2 getDirection(Entity target) {
        if (target == null) {
            return new Vector2(0, 0);
        }
        Vector2 from = entity.getCenterPosition();
        Vector2 to = target.getCenterPosition();
        return to.cpy().sub(from).nor();
    }

    /**
     * Return the latest projectile -- only available for singleShoot
     * Primarily used for testing
     * @return the latest projectile being created
     */
    public Entity getLatestProjectile() {
        return latestProjectile;
    }

    /**
     * Shoot at the specific direction from NPC
     * @param direction The direction to shoot at
     * @return whether the action is executable or not
     * (currently won't shoot if obstacle is in-between)
     */
    private void shoot(Vector2 direction) {
        switch (this.type) {
            case ShootType.SPREAD:
                spreadShoot(direction, 3);
                break;
            case ShootType.SINGLE:
                singleShoot(direction);
                break;
            default:
                throw new IllegalArgumentException("Unknown shooting type");
        }
    }

    /**
     * Shoot at the specific direction from NPC
     * @param direction The direction to shoot at
     */
    private void singleShoot(Vector2 direction) {
        Entity projectile = projectileFactory.create(projectileName, direction, entity.getPosition());
        projectile.getComponent(com.csse3200.game.components.projectile.ProjectileAttackComponent.class).create();
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9,9),
                true, true);
        updateDirection(projectile, direction);
        projectile.getEvents().trigger(attackTrigger);
        latestProjectile = projectile;
    }

    private void updateDirection(Entity projectile, Vector2 direction) {
        if (direction.x >= 0) {
            projectile.getComponent(DirectionalNPCComponent.class).setDirection("right");
        } else {
            projectile.getComponent(DirectionalNPCComponent.class).setDirection("left");
        }
    }

    /**
     * Shoot multiple projectiles at different angles using singleShoot
     * Not yet available for usage
     * @param direction The direction to shoot at
     * @param numShot number of spreads
     */
    private void spreadShoot(Vector2 direction, int numShot) {
        if (numShot == 1) {
            shoot(direction);
            return;
        }
        for (int i = 0; i < numShot; i++) {
            singleShoot(rotate(direction, spreadAngle * ((numShot - 1) - 2 * i)));
        }
    }

    /**
     * Rotate a vector2 with an angle in radian
     * @param mainRay The vector to be rotated
     * @param angle The rotated angle
     * @return The rotated vector
     */
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
