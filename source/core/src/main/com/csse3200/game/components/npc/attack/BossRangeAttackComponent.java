package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;


/**
 * This attack component supports shooting projectiles for bosses
 * Mainly for bullet hell (spawn projectile at specific location with given moving vector)
 * Add spiral shot
 */
public class BossRangeAttackComponent extends RangeAttackComponent {

    private Vector2[] spawnLocations;
    private Vector2[] movingDirections;
    private final Random rand = new Random();

    private final ProjectileFactory projectileFactory = new ProjectileFactory();

    /**
     * Makes a ranged attack component
     *
     * @param target the player
     * @param config the attack configuration
     */
    public BossRangeAttackComponent(Entity target, AttackConfig.RangeAttack config) {
        super(target, config);
    }

    public void setSpawnLocations(Vector2[] spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public void setMovingDirections(Vector2[] movingDirections) {
        this.movingDirections = movingDirections;
    }

    /**
     * Set shoot patterns for each projectile (each projectile must have location and direction)
     * The corner of the room are (0,0) - (9,0) - (9,14) - (0,14)
     */
    public void loadShootPattern(Vector2[] spawnLocations, Vector2[] movingDirections) {
        if (spawnLocations.length != movingDirections.length) {
            return;
        }
        setSpawnLocations(spawnLocations);
        setMovingDirections(movingDirections);
    }

    /**
     * Perform attack for superclass `update` call
     */
    @Override
    public void performAttack() {
        // when nothing is added, acted as a normal shooting one
        if (spawnLocations == null || spawnLocations.length != movingDirections.length ) {
            spawnLocations = new Vector2[]{entity.getPosition()};
            movingDirections = new Vector2[]{getDirection(target)};
        }
        logger.info("{} shoots {}", entity, target);
        // Shoot target
        Vector2 direction = getDirection(target);
        setAnimationID(0);
        entity.getEvents().trigger("attack");
        String baseName = "Dragon";
        if (entity.getComponent(NameComponent.class) == null) {
            // Use for test entity
            projectileNames = new String[]{"dragonProjectile"};
            attackTriggers = new String[]{"fire_attack"};
        } else {
            baseName = entity.getComponent(NameComponent.class).getName();
        }
        int numShot = 1;
        if (baseName.equals("kitsune")) {
            numShot = 4;
        } else if (baseName.equals("cthulu")) {
            numShot = 5;
            setSpreadAngle(0.4f);
        }
        spreadShoot(direction, numShot);
        shoot();
        // Uncomment this for testing change projectile animation

        // Attack effects
//        if (effects != null) {
//            applyEffects(target);
//        }
    }

    private void shoot() {
        for (int i = 0; i < spawnLocations.length; i++) {
            int ran = rand.nextInt(2);
            if (ran == 0) {
                spreadShoot(spawnLocations[i], movingDirections[i], 2);
            } else {
                singleShoot(spawnLocations[i], movingDirections[i]);
            }
        }
    }

    private void singleShoot(Vector2 spawnLocation, Vector2 movingDirection) {
        Entity projectile = projectileFactory.create(projectileNames[animationID], movingDirection, spawnLocation);
        projectile.getComponent(com.csse3200.game.components.projectile.ProjectileAttackComponent.class).create();
        ServiceLocator.getGameAreaService().getGameArea().spawnEntityAt(projectile, new GridPoint2(9,9),
                true, true);
        updateDirection(projectile, movingDirection);
        projectile.getEvents().trigger(attackTriggers[animationID]);
    }

    private void spreadShoot(Vector2 spawnLocation, Vector2 movingDirection, int numShot) {
        if (numShot == 1) {
            singleShoot(spawnLocation, movingDirection);
            return;
        }
        for (int i = 0; i < numShot; i++) {
            singleShoot(spawnLocation, rotate(movingDirection, spreadAngle * ((numShot - 1) - 2 * i)));
        }
    }
}
