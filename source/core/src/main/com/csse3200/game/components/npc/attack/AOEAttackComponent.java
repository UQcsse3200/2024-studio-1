package com.csse3200.game.components.npc.attack;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * AOEAttackComponent manages Area of Effect attacks for NPCs, including a preparation phase.
 */
public class AOEAttackComponent extends AttackComponent {
    private final float aoeRadius;
    private Vector2 origin;
    private final float preparationTime = 2f; // 2-second delay

    public AOEAttackComponent(Entity target, float attackRange, float attackRate,
                              NPCConfigs.NPCConfig.EffectConfig[] effectConfigs, float aoeRadius) {

        super(target, attackRange, attackRate, effectConfigs);
        this.target = target;
        this.aoeRadius = aoeRadius;
        this.origin = new Vector2();
    }

    /**
     * Initiates the AOE attack by triggering the preparation phase.
     */
    @Override
    public void performAttack() {
        if (canAttack(entity, target)) {
            // Trigger the AOE preparation event
            entity.getEvents().trigger("aoe_preparation");
            logger.info("{} is preparing an AOE attack at {}", entity, origin);

            // Schedule the actual AOE attack after the preparation time
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    executeAOEAttack();
                }
            }, preparationTime); // Delay in seconds
        }
    }

    /**
     * Executes the actual AOE attack: triggers the attack event and applies damage.
     */
    void executeAOEAttack() {
        // Trigger the actual AOE attack event
        entity.getEvents().trigger("aoe_attack");
        logger.info("{} executes AOE attack at {}", entity, origin);
        Circle aoeCircle = new Circle(entity.getCenterPosition(), aoeRadius);

        if (target.getComponent(CombatStatsComponent.class) != null
                && aoeCircle.contains(target.getPosition())) {
            logger.info("AOE attack hits {} for {} damage", target, combatStats.getBaseAttack());
            target.getComponent(CombatStatsComponent.class).hit(combatStats);
            applyEffects(target);
        }
    }

    /**
     * Sets the origin point of the AOE attack.
     *
     * @param newOrigin The new origin position for the AOE attack.
     */
    public void setOrigin(Vector2 newOrigin) {
        this.origin.set(newOrigin);
    }

    /**
     * Determines if the attacker can perform an attack on the target.
     *
     * @param attacker The entity performing the attack.
     * @param target   The entity being targeted.
     * @return True if the attack can be performed; otherwise, false.
     */
    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return target.getPosition().dst(entity.getCenterPosition()) <= aoeRadius;
    }
}