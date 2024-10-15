package com.csse3200.game.components.npc.attack;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.effects.Effect;
import com.csse3200.game.components.effects.EffectComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EffectConfig;
import com.csse3200.game.entities.factories.EffectFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class that handles attack logic for an NPC, applying damage and effects to a target within range.
 */
public abstract class AttackComponent extends Component implements AttackBehaviour{
    protected Entity target;
    protected float attackRange;
    protected float attackCooldown;
    protected float timeSinceLastAttack;
    protected CombatStatsComponent combatStats;
    protected EffectConfig[] effectConfigs;
    protected int remainingAttacks = -1;
    protected static final Logger logger = LoggerFactory.getLogger(AttackComponent.class);


    /**
     * Makes an attack component 
     *
     * @param target        The target entity for the attack.
     * @param attackRange   The range of the attack.
     * @param attackRate    The rate of the attack.
     * @param effectConfigs The effect configurations to apply on the target.
     */
    public AttackComponent(Entity target, float attackRange, float attackRate,
                           EffectConfig[] effectConfigs) {
        this.target = target;
        this.attackRange = attackRange;
        this.attackCooldown = 1 / attackRate;
        this.effectConfigs = effectConfigs;
        this.timeSinceLastAttack = attackCooldown;
    }

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    @Override
    public void update() {
        super.update();
        if (target == null || entity == null || !canAttack(entity, target)) {
            return;
        }

        timeSinceLastAttack += ServiceLocator.getTimeSource().getDeltaTime();
        if (timeSinceLastAttack >= attackCooldown) {
            performAttack();
            timeSinceLastAttack = 0; // Reset cooldown timer

            // Disable the attack component if it has a limited number of attacks
            if (remainingAttacks > 0) {
                remainingAttacks--;
                if (remainingAttacks == 0) {
                    this.setEnabled(false);
                }
            }
        }
    }

    /**
     * Applies effects to the target entity by adding them to the target's EffectComponent.
     *
     * @param target The entity being attacked.
     */
    protected void applyEffects(Entity target) {
        EffectComponent effectComponent = target.getComponent(EffectComponent.class);
        if (effectComponent == null) {
            logger.warn("Target entity {} does not have an EffectComponent.", target);
            return;
        }
        if (effectConfigs != null) {
            for (EffectConfig config : effectConfigs) {
                try {
                    Effect effect = EffectFactory.createEffect(config, entity);
                    effectComponent.addEffect(effect);
                } catch (IllegalArgumentException e) {
                    logger.error("Failed to create effect: {}", e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean canAttack(Entity attacker, Entity target) {
        float distanceToTarget = attacker.getPosition().dst(target.getPosition());
        return distanceToTarget <= attackRange;
    }

    /**
     * Enable the attack component for a given number of attacks.
     *
     * @param numberOfAttacks Number of attacks to enable for.
     */
    public void enableForNumAttacks(int numberOfAttacks) {
        this.remainingAttacks = numberOfAttacks;
        this.setEnabled(true);
    }
}
