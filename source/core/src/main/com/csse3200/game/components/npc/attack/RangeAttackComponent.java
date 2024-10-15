package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * This attack component supports shooting projectiles from animals
 */
public class RangeAttackComponent extends AttackComponent {

    float spreadAngle = 0.1f;
    private ShootType type;
    private Entity latestProjectile;
    String[] projectileNames;
    String[] attackTriggers;

    int animationID = 0;

    private final ProjectileFactory projectileFactory = new ProjectileFactory();

    /**
     * Enum type that control different shooting methods
     */
    enum ShootType {
        SINGLE, // Single shot
        SPREAD, // Spread shot
    }

    /**
     * Makes a ranged attack component
     *
     * @param target the player
     * @param config the attack configuration
     */
    public RangeAttackComponent(Entity target, AttackConfig.RangeAttack config) {
        super(target, config.range, config.rate, config.effects);
        if (config.type == 0) {
            type = ShootType.SINGLE;
        }
        else {
            type = ShootType.SPREAD;
        }
        projectileNames = new String[]{"projectile"};
        this.setEnabled(false);
    }

    /**
     * Change the animation ID for controlling animations
     *  new id must be within number of animations available
     * @param i new animationID
     */
    public void setAnimationID(int i) {
        animationID = i;
        if (attackTriggers != null) {
            animationID = animationID % attackTriggers.length;
        }
    }

//    /**
//     * return animationID
//     * @return animationID
//     */
//    public int getAnimationID() {
//        return animationID;
//    }

    public void setSpreadAngle(float spreadAngle) {
        this.spreadAngle = spreadAngle;
    }

    /**
     * Base create function to overwrite projectileNames and attackTriggers for different projectile animations
     */
    @Override
    public void create() {
        Entity baseEntity = this.getEntity();
        String baseName = "Dragon";
        if (baseEntity.getComponent(NameComponent.class) == null) {
            // Use for test entity
            projectileNames = new String[]{"dragonProjectile"};
            attackTriggers = new String[]{"fire_attack"};
        } else {
            baseName = baseEntity.getComponent(NameComponent.class).getName();
        }

        System.out.println("Ranged animals shooting:");
        System.out.println(baseName);
        if (baseName.equals("dragon")) {
            projectileNames = new String[]{"dragonProjectile"};
            attackTriggers = new String[]{"fire_attack"};
        } else if (baseName.equals("kitsune")) {
            projectileNames = new String[]{"kitsuneProjectile", "kitsuneProjectile1", "kitsuneProjectile2"};
            attackTriggers = new String[]{"kitsune_bullet", "fire1", "fire2"};
        } else if (baseName.equals("cthulu")) {
            projectileNames = new String[]{"cthuluProjectile"};
            attackTriggers = new String[]{"cthulu_bullet"};
        }
        else {
            projectileNames = new String[]{"dragonProjectile"};
            attackTriggers = new String[]{"fire_attack"};
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
        String baseName = this.getEntity().getComponent(NameComponent.class).getName();
        // Uncomment this for testing change projectile animation
        // Primarily for Kitsune
        shoot(direction);
        // Attack effects
        applyEffects(target);
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
    Vector2 getDirection(Entity target) {
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
        Entity projectile = projectileFactory.create(projectileNames[animationID], direction, entity.getPosition());
        projectile.getComponent(com.csse3200.game.components.projectile.ProjectileAttackComponent.class).create();
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9,9),
                true, true);
        updateDirection(projectile, direction);
        projectile.getEvents().trigger(attackTriggers[animationID]);
        latestProjectile = projectile;
    }

    /**
     * Update the directional component to be either left or right with respect to target
     * @param projectile projectile entity
     * @param direction direction that projectile is flying toward
     */
    void updateDirection(Entity projectile, Vector2 direction) {
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
    void spreadShoot(Vector2 direction, int numShot) {
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
    Vector2 rotate(Vector2 mainRay, float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float newX = mainRay.x * cos - mainRay.y * sin;
        float newY = mainRay.x * sin + mainRay.y * cos;

        return new Vector2(newX, newY);
    }

    /**
     * Set the type of shooting
     * @param type The type of shooting
     */
    public void setType(String type) {
        this.type = switch (type.toUpperCase()) {
            case "SINGLE" -> ShootType.SINGLE;
            case "SPREAD" -> ShootType.SPREAD;
            default -> ShootType.SINGLE;
        };
    }
}


