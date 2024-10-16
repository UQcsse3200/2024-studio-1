package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.attack.BossRangeAttackComponent;
import com.csse3200.game.components.npc.attack.RangeAttackComponent;
import com.csse3200.game.entities.configs.TaskConfig;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Task for the boss to perform a Bullet Storm:
 * - Move to the center of the room at (7,5).
 * - Enable BossRangeAttackComponent.
 * - Become immune to damage.
 * - Runs once when boss health is under 50%.
 * - Deactivates after a given amount of time.
 */
public class BulletStormTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(BulletStormTask.class);
    private static final int TASK_PRIORITY = 15; // Highest priority
    private static final float ACTIVATION_HEALTH_PERCENTAGE = 50f;
    private final Vector2 centerPosition = new Vector2(7, 5); // Centre of room
    private final float duration; // In seconds
    private final GameTime gameTime;
    private long startTime;
    private boolean hasRun = false;
    private MovementTask movementTask;

    /**
     * Creates a BulletStormTask.
     */
    public BulletStormTask(TaskConfig.BulletStormTaskConfig config) {
        duration = config.duration;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        if (hasRun) {
            return;
        }
        super.start();
        logger.info("Starting BulletStormTask.");
        startTime = gameTime.getTime();

        // Make the boss immune to damage
        CombatStatsComponent combatStats = owner.getEntity().getComponent(CombatStatsComponent.class);
        if (combatStats != null) {
            combatStats.makeInvincible(duration);
            logger.debug("Boss is now immune to damage.");
        }

        // Move the boss to the center of the room
        movementTask = new MovementTask(centerPosition);
        movementTask.create(owner);
        movementTask.start();
        movementTask.setVelocity(2f);
        logger.debug("Boss is moving to the center of the room at {}", centerPosition);

        // Enable the BossRangeAttackComponent
        BossRangeAttackComponent bossRangeAttack = owner.getEntity().getComponent(BossRangeAttackComponent.class);
        RangeAttackComponent rangeAttack = owner.getEntity().getComponent(RangeAttackComponent.class);
        int numShot = 4;
        if (bossRangeAttack != null) {
            setShootPattern(bossRangeAttack, numShot);
            bossRangeAttack.setSpeedCoefficient(0.4f);
            bossRangeAttack.setAttackCooldown(bossRangeAttack.getAttackCooldown() * 1.8f);
            if (rangeAttack != null) {
                rangeAttack.setSpeedCoefficient(0.4f);
                rangeAttack.setAttackCooldown(rangeAttack.getAttackCooldown() * 1.8f);
            }
            bossRangeAttack.setEnabled(true);
            logger.debug("BossRangeAttackComponent enabled.");
        } else {
            logger.warn("BossRangeAttackComponent not found on the boss entity.");
        }
    }

    private void setShootPattern(BossRangeAttackComponent rangeAttack, int numShot) {
        rangeAttack.setSpreadAngle(0.25f);
        List<Vector2> spawnLocations = new ArrayList<>();
        List<Vector2> movingDirections = new ArrayList<>();
        for (float i = 0.3f; i < 8.7; i += (float) ((8.4) / numShot)) {
            Vector2 v = new Vector2(1, i);
            spawnLocations.add(v);
            movingDirections.add(new Vector2(1,0));
        }

        rangeAttack.loadShootPattern(spawnLocations.toArray(new Vector2[0]), movingDirections.toArray(new Vector2[0]));
    }

    @Override
    public void update() {
        long currentTime = gameTime.getTime();
        float elapsedTime = (currentTime - startTime) / 1000f; // Convert to seconds

        // Update movement task
        if (movementTask != null && movementTask.getStatus() != Status.FINISHED) {
            movementTask.update();
            // Check if boss has reached the center
            if (movementTask.getStatus() == Task.Status.FAILED) {
                logger.debug("Boss got stuck moving to the center. Restarting.");
                start();
                return;
            }
        }

        // Check if duration has passed
        if (elapsedTime >= duration) {
            stop();
            logger.info("BulletStormTask duration completed. Deactivating.");
        }
    }

    @Override
    public void stop() {
        super.stop();
        logger.debug("Stopping BulletStormTask.");

        // Disable the BossRangeAttackComponent
        BossRangeAttackComponent bossRangeAttack = owner.getEntity().getComponent(BossRangeAttackComponent.class);
        if (bossRangeAttack != null) {
            bossRangeAttack.setEnabled(false);
            // Convert the attack speed back to normal
            RangeAttackComponent rangeAttack = owner.getEntity().getComponent(RangeAttackComponent.class);
            if (rangeAttack != null) {
                rangeAttack.setSpeedCoefficient(1f);
                rangeAttack.setAttackCooldown(rangeAttack.getAttackCooldown() * 0.55f);
            }

            logger.debug("BossRangeAttackComponent disabled.");
        }

        hasRun = true;
    }

    @Override
    public int getPriority() {
        if (hasRun) {
            return -1;
        }
        CombatStatsComponent combatStats = owner.getEntity().getComponent(CombatStatsComponent.class);
        if (combatStats == null) {
            return -1;
        }

        float healthPercentage = ((float) combatStats.getHealth() / combatStats.getMaxHealth()) * 100f;
        if (!hasRun && healthPercentage <= ACTIVATION_HEALTH_PERCENTAGE) {
            return TASK_PRIORITY;
        }

        return -1;
    }

    /**
     * Returns whether the task has run.
     *
     * @return true if the task has run, false otherwise
     */
    public boolean hasRun() {
        return hasRun;
    }
}
