package com.csse3200.game.components.npc.attack;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.AttackConfig;


/**
 * Component that handles melee attacks for an NPC, applying damage and effects to a target within range.
 */
public class MeleeAttackComponent extends AttackComponent {

    /**
     * Constructs a new MeleeAttackComponent.
     *
     * @param target The target entity for the attack.
     * @param config The melee attack configuration.
     */
    public MeleeAttackComponent(Entity target, AttackConfig.MeleeAttack config) {
        super(target, config.range, config.rate, config.effects);
        this.setEnabled(false);
    }

    @Override
    public void performAttack() {
        if (target != null) {
            CombatStatsComponent combatComponent = target.getComponent(CombatStatsComponent.class);
            if (combatComponent != null) {
                // Apply damage to the target
                combatComponent.hit(combatStats);
                logger.info("{} attacks {} for {} damage", entity, target, combatStats.getBaseAttack());

                // Trigger attack animation
                entity.getEvents().trigger("attack");

                // Attack effects
                applyEffects(target);
            }
        }
    }

    /**
     * Updates the attack target
     *
     * @param newTarget the new target
     */
    public void updateTarget(Entity newTarget) {
        this.target = newTarget;
        logger.debug("Updated attack target to {}", newTarget);
    }

}
