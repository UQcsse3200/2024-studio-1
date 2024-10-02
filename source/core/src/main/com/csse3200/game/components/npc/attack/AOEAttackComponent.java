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
 * This component extends the basic AttackComponent to provide AOE functionality.
 */
public class AOEAttackComponent extends AttackComponent {
    private final float aoeRadius;
    private Vector2 origin;
    private final float preparationTime = 2f;

    /**
     * Constructs a new AOEAttackComponent.
     *
     * @param target The target entity for the attack.
     * @param attackRange The maximum range at which the attack can be initiated.
     * @param attackRate The frequency of attacks (attacks per second).
     * @param effectConfigs An array of effect configurations to be applied with the attack.
     * @param aoeRadius The radius of the area of effect for the attack.
     */
    public AOEAttackComponent(Entity target, float attackRange, float attackRate,
                              NPCConfigs.NPCConfig.EffectConfig[] effectConfigs, float aoeRadius) {
        super(target, attackRange, attackRate, effectConfigs);
        this.aoeRadius = aoeRadius;
        this.origin = new Vector2();
    }

    /**
     * Initiates the AOE attack by triggering the preparation phase.
     * This method is called when the attack conditions are met.
     */
    @Override
    public void performAttack() {
        if (canAttack(entity, target)) {
            entity.getEvents().trigger("aoe_preparation");
            logger.info("{} is preparing an AOE attack at {}", entity, origin);

            Timer.schedule(new Task() {
                @Override
                public void run() {
                    executeAOEAttack();
                }
            }, preparationTime);
        }
    }

    /**
     * Executes the actual AOE attack after the preparation phase.
     * This method applies damage and effects to the target if within the AOE.
     */
    private void executeAOEAttack() {
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
     * @param target The entity being targeted.
     * @return true if the attack can be performed; false otherwise.
     */
    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        return target.getPosition().dst(entity.getCenterPosition()) <= aoeRadius;
    }
}